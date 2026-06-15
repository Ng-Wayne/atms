import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { SessionService } from '../../services/session.service';

@Component({
  selector: 'app-home',
  imports: [],
  templateUrl: './home.html'
})
export class HomeComponent {

  constructor(
    private http: HttpClient,
    private sessionService: SessionService,
    private router: Router
  ) {}

  cancel() {

    const sessionId = this.sessionService.getSessionId();

    this.http.post<any>(
      `http://localhost:8080/api/session/${sessionId}/end`,
      {},
      {
        params: {
          reason: "USER_CANCELLED"
        }
      }
      ).subscribe({
        next: () => {
          console.log('Session ended by user');
        },
        error: (err) => {
          console.error('Error:', err);
        }
      });

    this.router.navigate(['']);
  }

  balanceInquiry() {
    this.router.navigate(['/balance-inquiry']);
  }

  withdraw() {
    this.router.navigate(['/withdraw']);
  }

}
