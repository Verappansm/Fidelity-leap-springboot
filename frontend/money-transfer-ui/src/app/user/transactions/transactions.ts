import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { API } from '../../core/api';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-transactions',
  standalone: true,
  imports: [RouterModule, CommonModule],
  templateUrl: './transactions.html'
})
export class Transactions implements OnInit {

  transactions: any[] = [];

  constructor(
    private http: HttpClient,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
  this.http.get<any[]>(API.TRANSFERS.HISTORY)
    .subscribe({
      next: (data) => {
        this.transactions = data;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('Failed to load transactions:', err);
      }
    });
}


  formatDate(date: string): string {
    return new Date(date).toLocaleString();
  }

  getOtherAccount(tx: any): string {
    const accountId = Number(localStorage.getItem('accountId'));
    return tx.transactionType === 'DEBIT'
      ? tx.toAccountId
      : tx.fromAccountId;
  }
}