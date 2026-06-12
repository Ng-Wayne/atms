import { Component } from '@angular/core';
import { Router } from '@angular/router';

@Component({
  selector: 'app-home',
  imports: [],
  templateUrl: './home.html'
})
export class HomeComponent {

  constructor(
      private router: Router
    ) {}

  balanceInquiry() {
    this.router.navigate(['/balance-inquiry']);
  }

}
