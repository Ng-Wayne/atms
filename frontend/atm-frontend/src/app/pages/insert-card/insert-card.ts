import { Component, ElementRef, ViewChild } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-insert-card',
  standalone: true,
  imports: [FormsModule, CommonModule],
  templateUrl: './insert-card.html',
  styleUrl: './insert-card.css'
})
export class InsertCardComponent {

  isFocused: boolean = false;
  cardNumber: string = '';
  boxes = Array(16);

  @ViewChild('hiddenInput') hiddenInput!: ElementRef;

  focusInput() {
    this.hiddenInput.nativeElement.focus();
  }

  onInputChange() {
    this.cardNumber = this.cardNumber.replace(/\D/g, '');

    if (this.cardNumber.length > 16) {
      this.cardNumber = this.cardNumber.slice(0, 16);
    }
  }

  submit() {
    console.log(this.cardNumber);
  }
}
