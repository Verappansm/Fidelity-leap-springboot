import { ChangeDetectorRef, Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { HttpClient } from '@angular/common/http';
import { API } from '../../core/api';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-deposit',
  standalone: true,
  imports: [RouterModule, CommonModule, FormsModule],
  templateUrl: './deposit.html'
})
export class Deposit {

  accountId = 0;
  amount = 0;
  message = '';
  messageType = '';
  loading = false;

  constructor(
    private http: HttpClient,
    private cdr: ChangeDetectorRef
  ) {}

  deposit(): void {

    if (this.loading) return;

    this.loading = true;

    this.http.post(API.ADMIN.DEPOSIT, {
      accountId: this.accountId,
      amount: this.amount
    }).subscribe({
      next: () => {
        this.message = 'Deposit successful';
        this.messageType = 'success';

        // reset inputs
        this.accountId = 0;
        this.amount = 0;

        this.loading = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        this.message =
          err?.error?.message || 'Deposit failed';
        this.messageType = 'error';

        this.loading = false;
        this.cdr.detectChanges();
      }
    });
  }
}