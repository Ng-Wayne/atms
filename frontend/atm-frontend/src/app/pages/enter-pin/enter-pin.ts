import { Component, ViewChild, ElementRef } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { SessionService } from '../../services/session.service';
import { MatSnackBar } from '@angular/material/snack-bar';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { ConfirmDialogComponent } from '../../shared/confirm-dialog/confirm-dialog.component';

@Component({
  selector: 'app-enter-pin',
  standalone: true,
  imports: [FormsModule, CommonModule, MatDialogModule],
  templateUrl: './enter-pin.html'
})
export class EnterPinComponent {

  isPinFocused = false;
  pin: string = '';
  boxes = Array(6);

  @ViewChild('pinInput') pinInput!: ElementRef;

  constructor(
    private http: HttpClient,
    private sessionService: SessionService,
    private snackBar: MatSnackBar,
    private dialog: MatDialog,
    private router: Router
  ) {}

  focusPinInput() {
    this.pinInput.nativeElement.focus();
  }

  onPinChange() {
    this.pin = this.sanitizeNumericInput(this.pin, 6);
  }

  sanitizeNumericInput(value: string, maxLength: number): string {

    value = value.replace(/\D/g, '');

    if (value.length > maxLength) {
      value = value.slice(0, maxLength);
    }

    return value;
  }

  submit() {

    const sessionId = this.sessionService.getSessionId();

    this.http.post(
      `http://localhost:8080/api/session/${sessionId}/authenticate`,
      {},
      {
        params: {
          pin: this.pin
        }
      }
    ).subscribe({
      next: (response) => {
        console.log('Session authenticated:', response);
        this.router.navigate(['/home']);
      },
      error: (err) => {
        console.error('Error:', err);

        if (err.error?.message === 'Invalid PIN') {
          this.snackBar.open(
            'Invalid Pin',
            'Close',
            {
              duration: 5000
            }
          );
        }

        if (err.error?.message === 'SESSION_MAX_PIN_ATTEMPTS') {
          this.router.navigate(['/']);

          this.snackBar.open(
            'Session Ended: Max Pin Attempts Exceeded',
            'Close',
            {
              duration: 10000
            }
          );
        }

        if (err.error?.message === 'CARD_MAX_PIN_ATTEMPTS') {
          this.router.navigate(['/']);

          this.snackBar.open(
            'Card Blocked: Max Pin Attempts Exceeded',
            'Close',
            {
              duration: 10000
            }
          );
        }

      }
    });
  }

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
}
