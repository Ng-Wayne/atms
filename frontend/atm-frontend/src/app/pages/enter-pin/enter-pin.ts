import { Component, ViewChild, ElementRef } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

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

  constructor(private http: HttpClient) {}

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

    console.log(this.pin);
  }
}
