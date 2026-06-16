import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { SessionService } from '../../services/session.service';
import { FormsModule } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  standalone: true,
  selector: 'app-withdraw',
  imports: [
    FormsModule
  ],
  templateUrl: './withdraw.html'
})
export class WithdrawComponent {

  amount: number = 50;

  constructor(
    private http: HttpClient,
    private sessionService: SessionService,
    private snackBar: MatSnackBar,
    private router: Router
  ) {}

  withdraw(){

    const sessionId = this.sessionService.getSessionId();

    this.http.post(
      'http://localhost:8080/api/withdraw',
      {},
      {
        params: {
          sessionId,
          amount: this.amount
        },
        responseType: 'text'
      }
    ).subscribe({
      next: (response) => {

      },
      error: (err) => {
        console.error('Error:', err);

        this.snackBar.open(
          err.error?.message || 'Unable to withdraw.',
          'Close',
          {
            duration: 3000
          }
        );
      }
    });
  }
}
