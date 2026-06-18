import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { SessionService } from '../../services/session.service';
import { FormsModule } from '@angular/forms';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  standalone: true,
  selector: 'app-deposit',
  imports: [
    FormsModule
  ],
  templateUrl: './deposit.html'
})
export class DepositComponent {

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
      'http://localhost:8080/api/deposit',
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
        this.router.navigate(['/home']);

        this.snackBar.open(
          'Deposit Successful',
          'Close',
          {
            duration: 5000
          }
        );
      },
      error: (err) => {
        console.error('Error:', err);

        this.snackBar.open(
          err.error?.message || 'Unable to deposit.',
          'Close',
          {
            duration: 3000
          }
        );
      }
    });
  }

  back() {
    this.router.navigate(['/home']);
  }
}
