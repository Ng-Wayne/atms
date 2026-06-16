import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { HttpClient } from '@angular/common/http';
import { SessionService } from '../../services/session.service';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { ConfirmDialogComponent } from '../../shared/confirm-dialog/confirm-dialog.component';

@Component({
  selector: 'app-home',
  standalone: true,
  imports: [
    MatDialogModule
  ],
  templateUrl: './home.html'
})
export class HomeComponent {

  constructor(
    private http: HttpClient,
    private sessionService: SessionService,
    private router: Router,
    private dialog: MatDialog
  ) {}

  confirmCancel() {

    const dialogRef = this.dialog.open(ConfirmDialogComponent, {
      width: '400px',
      data: {
        title: 'Cancel Session',
        message: 'Are you sure you want to cancel this session?'
      }
    });

    dialogRef.afterClosed().subscribe(confirmed => {
      if (confirmed) {
        this.cancel();
      }
    });
  }

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
          this.router.navigate(['']);
        },
        error: (err) => {
          console.error('Error:', err);
        }
      });
  }

  balanceInquiry() {
    this.router.navigate(['/balance-inquiry']);
  }

  withdraw() {
    this.router.navigate(['/withdraw']);
  }

}
