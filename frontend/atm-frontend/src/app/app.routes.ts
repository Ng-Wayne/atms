import { Routes } from '@angular/router';
import { InsertCardComponent } from './pages/insert-card/insert-card';
import { EnterPinComponent } from './pages/enter-pin/enter-pin';
import { HomeComponent } from './pages/home/home';
import { BalanceInquiryComponent } from './pages/balance-inquiry/balance-inquiry';
import { WithdrawComponent } from './pages/withdraw/withdraw';
import { DepositComponent } from './pages/deposit/deposit';

export const routes: Routes = [
  {
    path: '',
    component: InsertCardComponent
  },
  {
    path: 'enter-pin',
    component: EnterPinComponent
  },
  {
    path: 'home',
    component: HomeComponent
  },
  {
    path: 'balance-inquiry',
    component: BalanceInquiryComponent
  },
  {
    path: 'withdraw',
    component: WithdrawComponent
  },
  {
    path: 'deposit',
    component: DepositComponent
  }
];
