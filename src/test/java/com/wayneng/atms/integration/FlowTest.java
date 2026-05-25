package com.wayneng.atms.integration;

import com.wayneng.atms.model.*;
import com.wayneng.atms.repository.*;
import com.wayneng.atms.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.math.BigDecimal;
import java.time.LocalDate;
import static org.assertj.core.api.Assertions.*;

@SpringBootTest
class FlowTest {

    @Autowired private AccountRepository accountRepository;
    @Autowired private CardRepository cardRepository;
    @Autowired private ATMRepository atmRepository;
    @Autowired private SessionRepository sessionRepository;
    @Autowired private TransactionRepository transactionRepository;
    @Autowired private BankRepository bankRepository;
    @Autowired private CustomerRepository customerRepository;

    @Autowired private SessionService sessionService;
    @Autowired private DepositService depositService;
    @Autowired private WithdrawalService withdrawalService;
    @Autowired private BalanceInquiryService balanceInquiryService;
    @Autowired private TransactionService transactionService;

    private static final String ATM_CODE = "ATM001";
    private static final String CARD_NUMBER = "1234567890123456";
    private static final String ACCOUNT_NUMBER = "ACC001";
    private static final String PIN = "1234";

    @BeforeEach
    void setUp() {
        transactionRepository.deleteAll();
        sessionRepository.deleteAll();
        cardRepository.deleteAll();
        accountRepository.deleteAll();
        customerRepository.deleteAll();
        atmRepository.deleteAll();
        bankRepository.deleteAll();

        Bank bank = new Bank();
        bank.setBankCode("SDCT");
        bank.setName("Standard Chartered");
        bankRepository.save(bank);

        Customer customer = new Customer();
        customer.setFullName("Wayne Ng");
        customer.setEmail("ngzuwayne@gmail.com");
        customer.setStatus("ACTIVE");
        customer.setBank(bank);
        customerRepository.save(customer);

        Account account = new Account();
        account.setAccountNumber(ACCOUNT_NUMBER);
        account.setAccountStatus("ACTIVE");
        account.setAccountType("SAVINGS");
        account.setCurrency("MYR");
        account.setAvailableBalance(new BigDecimal("1000"));
        account.setLedgerBalance(new BigDecimal("1000"));
        account.setMinimumBalance(new BigDecimal("100"));
        account.setDailyWithdrawalLimit(new BigDecimal("5000"));
        account.setCustomer(customer);
        account.setBank(bank);
        accountRepository.save(account);

        Card card = new Card();
        card.setCardNumber(CARD_NUMBER);
        card.setCardType("VISA");
        card.setCardStatus("ACTIVE");
        card.setAccount(account);
        card.setPinHash(new org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder().encode(PIN));
        card.setExpiryDate(LocalDate.now().plusYears(3));
        card.setFailedPinAttempts(0);
        card.setCustomer(customer);
        cardRepository.save(card);

        ATM atm = new ATM();
        atm.setAtmCode(ATM_CODE);
        atm.setLocationName("Happy Garden, KL");
        atm.setAtmStatus("ACTIVE");
        atm.setCashAvailable(new BigDecimal("10000"));
        atm.setCurrency("MYR");
        atm.setPerTransactionLimit(new BigDecimal("2000"));
        atm.setBank(bank);
        atmRepository.save(atm);
    }

    // SUCCESS CASE (ONE WITHDRAWAL)
    @Test
    void shouldWithdrawSuccessfully() {

        Session session = sessionService.startSession(CARD_NUMBER, ATM_CODE);
        sessionService.authenticateSession(session.getSessionId(), PIN);

        BigDecimal withdrawAmount = new BigDecimal("200");
        withdrawalService.withdraw(session.getSessionId(), withdrawAmount);

        Transaction tx = transactionService.getTransactionBySessionId(session.getSessionId());
        assertThat(tx.getTransactionType()).isEqualTo("WITHDRAWAL");
        assertThat(tx.getTransactionStatus()).isEqualTo("SUCCESS");

        Account updatedAccount = accountRepository.findById(ACCOUNT_NUMBER).orElseThrow();
        assertThat(updatedAccount.getAvailableBalance())
                .isEqualByComparingTo("800");

        ATM updatedATM = atmRepository.findById(ATM_CODE).orElseThrow();
        assertThat(updatedATM.getCashAvailable())
                .isEqualByComparingTo("9800");

        sessionService.endSession(session.getSessionId(), "COMPLETED");
        Session ended = sessionRepository.findById(session.getSessionId()).orElseThrow();
        assertThat(ended.getSessionStatus()).isEqualTo("ENDED");
    }

    // SUCCESS CASE (ONE DEPOSIT)
    @Test
    void shouldDepositSuccessfully() {
        Session session = sessionService.startSession(CARD_NUMBER, ATM_CODE);
        sessionService.authenticateSession(session.getSessionId(), PIN);

        BigDecimal depositAmount = new BigDecimal("500");
        depositService.deposit(session.getSessionId(), depositAmount);

        Transaction tx = transactionService.getTransactionBySessionId(session.getSessionId());
        assertThat(tx.getTransactionType()).isEqualTo("DEPOSIT");
        assertThat(tx.getTransactionStatus()).isEqualTo("SUCCESS");

        Account updatedAccount = accountRepository.findById(ACCOUNT_NUMBER).orElseThrow();
        assertThat(updatedAccount.getAvailableBalance())
                .isEqualByComparingTo("1500");

        ATM updatedATM = atmRepository.findById(ATM_CODE).orElseThrow();
        assertThat(updatedATM.getCashAvailable())
                .isEqualByComparingTo("10500");

        sessionService.endSession(session.getSessionId(), "COMPLETED");
        Session ended = sessionRepository.findById(session.getSessionId()).orElseThrow();
        assertThat(ended.getSessionStatus()).isEqualTo("ENDED");
    }

    // SUCCESS CASE (BALANCE INQUIRY)
    @Test
    void shouldInquireSuccessfully() {

        Session session = sessionService.startSession(CARD_NUMBER, ATM_CODE);
        sessionService.authenticateSession(session.getSessionId(), PIN);

        BigDecimal balance = balanceInquiryService.inquire(session.getSessionId());
        assertThat(balance).isEqualByComparingTo("1000");

        sessionService.endSession(session.getSessionId(), "COMPLETED");
        Session ended = sessionRepository.findById(session.getSessionId()).orElseThrow();
        assertThat(ended.getSessionStatus()).isEqualTo("ENDED");
    }

    // SUCCESS CASE (ONE DEPOSIT, ONE WITHDRAWAL, BALANCE INQUIRY)
    @Test
    void shouldDepositWithdrawInquireSuccessfully() {

        Session session = sessionService.startSession(CARD_NUMBER, ATM_CODE);
        sessionService.authenticateSession(session.getSessionId(), PIN);

        BigDecimal depositAmount = new BigDecimal("500");
        depositService.deposit(session.getSessionId(), depositAmount);

        Transaction tx = transactionService.getTransactionBySessionId(session.getSessionId());
        assertThat(tx.getTransactionType()).isEqualTo("DEPOSIT");
        assertThat(tx.getTransactionStatus()).isEqualTo("SUCCESS");

        Account updatedAccount = accountRepository.findById(ACCOUNT_NUMBER).orElseThrow();
        assertThat(updatedAccount.getAvailableBalance())
                .isEqualByComparingTo("1500");

        ATM updatedATM = atmRepository.findById(ATM_CODE).orElseThrow();
        assertThat(updatedATM.getCashAvailable())
                .isEqualByComparingTo("10500");

        BigDecimal withdrawAmount = new BigDecimal("200");
        withdrawalService.withdraw(session.getSessionId(), withdrawAmount);

        Transaction tx2 = transactionService.getTransactionBySessionId(session.getSessionId());
        assertThat(tx2.getTransactionType()).isEqualTo("WITHDRAWAL");
        assertThat(tx2.getTransactionStatus()).isEqualTo("SUCCESS");

        Account updatedAccount2 = accountRepository.findById(ACCOUNT_NUMBER).orElseThrow();
        assertThat(updatedAccount2.getAvailableBalance())
                .isEqualByComparingTo("1300");

        ATM updatedATM2 = atmRepository.findById(ATM_CODE).orElseThrow();
        assertThat(updatedATM2.getCashAvailable())
                .isEqualByComparingTo("10300");

        BigDecimal balance = balanceInquiryService.inquire(session.getSessionId());
        assertThat(balance).isEqualByComparingTo("1300");

        sessionService.endSession(session.getSessionId(), "COMPLETED");
        Session ended = sessionRepository.findById(session.getSessionId()).orElseThrow();
        assertThat(ended.getSessionStatus()).isEqualTo("ENDED");
    }

    // FAILURE CASE - INSUFFICIENT BALANCE (ONE WITHDRAWAL)
    @Test
    void shouldFailWithdrawal_dueToInsufficientBalance() {

        Session session = sessionService.startSession(CARD_NUMBER, ATM_CODE);
        sessionService.authenticateSession(session.getSessionId(), PIN);
        BigDecimal withdrawAmount = new BigDecimal("2000");

        assertThatThrownBy(() ->
                withdrawalService.withdraw(session.getSessionId(), withdrawAmount)
        ).isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Insufficient balance");

        Transaction tx = transactionService.getTransactionBySessionId(session.getSessionId());
        assertThat(tx.getTransactionType()).isEqualTo("WITHDRAWAL");
        assertThat(tx.getTransactionStatus()).isEqualTo("FAILED");

        Account updatedAccount = accountRepository.findById(ACCOUNT_NUMBER).orElseThrow();
        assertThat(updatedAccount.getAvailableBalance())
                .isEqualByComparingTo("1000");

        ATM updatedATM = atmRepository.findById(ATM_CODE).orElseThrow();
        assertThat(updatedATM.getCashAvailable())
                .isEqualByComparingTo("10000");

        sessionService.endSession(session.getSessionId(), "FAILED");
        Session ended = sessionRepository.findById(session.getSessionId()).orElseThrow();
        assertThat(ended.getSessionStatus()).isEqualTo("ENDED");
    }

    // FAILURE CASE - MINIMUM BALANCE VIOLATED (TWO WITHDRAWALS)
    @Test
    void shouldFailWithdrawal_dueToMinimumBalance() {

        Session session = sessionService.startSession(CARD_NUMBER, ATM_CODE);
        sessionService.authenticateSession(session.getSessionId(), PIN);
        BigDecimal withdrawAmount = new BigDecimal("900.01");

        assertThatThrownBy(() ->
                withdrawalService.withdraw(session.getSessionId(), withdrawAmount)
        ).isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Minimum balance violated");

        Transaction tx = transactionService.getTransactionBySessionId(session.getSessionId());
        assertThat(tx.getTransactionType()).isEqualTo("WITHDRAWAL");
        assertThat(tx.getTransactionStatus()).isEqualTo("FAILED");

        Account updatedAccount = accountRepository.findById(ACCOUNT_NUMBER).orElseThrow();
        assertThat(updatedAccount.getAvailableBalance())
                .isEqualByComparingTo("1000");

        ATM updatedATM = atmRepository.findById(ATM_CODE).orElseThrow();
        assertThat(updatedATM.getCashAvailable())
                .isEqualByComparingTo("10000");

        BigDecimal withdrawAmount2 = new BigDecimal("900");
        withdrawalService.withdraw(session.getSessionId(), withdrawAmount2);

        Transaction tx2 = transactionService.getTransactionBySessionId(session.getSessionId());
        assertThat(tx2.getTransactionType()).isEqualTo("WITHDRAWAL");
        assertThat(tx2.getTransactionStatus()).isEqualTo("SUCCESS");

        Account updatedAccount2 = accountRepository.findById(ACCOUNT_NUMBER).orElseThrow();
        assertThat(updatedAccount2.getAvailableBalance())
                .isEqualByComparingTo("100");

        ATM updatedATM2 = atmRepository.findById(ATM_CODE).orElseThrow();
        assertThat(updatedATM2.getCashAvailable())
                .isEqualByComparingTo("9100");

        sessionService.endSession(session.getSessionId(), "COMPLETED");
        Session ended = sessionRepository.findById(session.getSessionId()).orElseThrow();
        assertThat(ended.getSessionStatus()).isEqualTo("ENDED");
    }
}