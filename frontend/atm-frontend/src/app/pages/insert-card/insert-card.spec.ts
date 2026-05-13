import { ComponentFixture, TestBed } from '@angular/core/testing';

import { InsertCard } from './insert-card';

describe('InsertCard', () => {
  let component: InsertCard;
  let fixture: ComponentFixture<InsertCard>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [InsertCard],
    }).compileComponents();

    fixture = TestBed.createComponent(InsertCard);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
