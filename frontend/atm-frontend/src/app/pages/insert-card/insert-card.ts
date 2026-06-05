import { Component, ElementRef, ViewChild } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { SessionService } from '../../services/session.service';
import { MatSnackBar } from '@angular/material/snack-bar';

@Component({
  selector: 'app-insert-card',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './insert-card.html'
})
export class InsertCardComponent {

  isCardFocused = false;
  isAtmFocused = false;
  cardNumber: string = '';
  atmCode: string = '';
  cnBoxes = Array(16);
  acBoxes = Array(6);

  @ViewChild('cardInput') cardInput!: ElementRef;
  @ViewChild('atmInput') atmInput!: ElementRef;

  constructor(
    private http: HttpClient,
    private sessionService: SessionService,
    private snackBar: MatSnackBar
  ) {}

  focusCardInput() {
    this.cardInput.nativeElement.focus();
  }

  focusAtmInput() {
    this.atmInput.nativeElement.focus();
  }

  onCardNumberChange() {
    this.cardNumber = this.sanitizeNumericInput(this.cardNumber, 16);
  }

  onAtmCodeChange() {
    this.atmCode = this.sanitizeAlphaNumericInput(this.atmCode, 6);
  }

  sanitizeNumericInput(value: string, maxLength: number): string {

    value = value.replace(/\D/g, '');

    if (value.length > maxLength) {
      value = value.slice(0, maxLength);
    }

    return value;
  }

  sanitizeAlphaNumericInput(value: string, maxLength: number): string {

    value = value.replace(/[^a-zA-Z0-9]/g, '');

    if (value.length > maxLength) {
      value = value.slice(0, maxLength);
    }

    return value;
  }

  submit() {

    this.http.post<any>(
      'http://localhost:8080/api/session/start',
      {},
      {
        params: {
          cardNumber: this.cardNumber,
          atmCode: this.atmCode
        }
      }
    ).subscribe({
      next: (response) => {
        this.sessionService.setSessionId(response.sessionId);
        console.log('Stored Session ID:', response.sessionId);
        console.log('Session started:', response);
      },
      error: (err) => {
        console.error('Error:', err);

        this.snackBar.open(
          err.error?.message || 'Unable to start session.',
          'Close',
          {
            duration: 3000
          }
        );
      }
    });
  }
}
