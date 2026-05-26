import { Routes } from '@angular/router';
import { InsertCardComponent } from './pages/insert-card/insert-card';
import { EnterPinComponent } from './pages/enter-pin/enter-pin';

export const routes: Routes = [
  {
    path: '',
    component: InsertCardComponent
  },
  {
    path: 'enter-pin',
    component: EnterPinComponent
  }
];
