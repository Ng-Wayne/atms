import { Routes } from '@angular/router';
import { InsertCardComponent } from './pages/insert-card/insert-card';
import { EnterPinComponent } from './pages/enter-pin/enter-pin';
import { HomeComponent } from './pages/home/home';

export const routes: Routes = [
  {
    path: '',
    component: InsertCardComponent
  },
  {
    path: 'enter-pin',
    component: EnterPinComponent
  },
  {
    path: 'home',
    component: HomeComponent
  }
];
