import { Component } from '@angular/core';
import { SessionService } from '../../services/session.service';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { AfterViewInit } from '@angular/core';
import { ChangeDetectorRef } from '@angular/core';
import { DecimalPipe } from '@angular/common';

@Component({
  selector: 'app-balance-inquiry',
  imports: [DecimalPipe],
  templateUrl: './balance-inquiry.html'
})
export class BalanceInquiryComponent {

  balance: number = 0;

  constructor(
    private http: HttpClient,
    private sessionService: SessionService,
    private router: Router,
    private cdr: ChangeDetectorRef
  ) {}

  ngAfterViewInit(): void {
    setTimeout(() => {
      this.loadBalance();
    });
  }

  loadBalance() {
    const sessionId = this.sessionService.getSessionId();

    this.http.get<any>(
      `http://localhost:8080/api/balance/${sessionId}`
      ).subscribe(response => {
        this.balance = response;
        this.cdr.detectChanges();
      });
  }

  back() {
    this.router.navigate(['/home']);
  }

}
