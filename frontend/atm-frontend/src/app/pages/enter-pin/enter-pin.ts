import { Component, ViewChild, ElementRef } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { SessionService } from '../../services/session.service';

@Component({
  selector: 'app-enter-pin',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './enter-pin.html'
})
export class EnterPinComponent {

  isPinFocused = false;
  pin: string = '';
  boxes = Array(6);

  @ViewChild('pinInput') pinInput!: ElementRef;

  constructor(
    private http: HttpClient,
    private sessionService: SessionService
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
      },
      error: (err) => {
        console.error('Error:', err);
      }
    });

  }
}
