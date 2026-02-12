import { Component, OnInit, ChangeDetectorRef } from '@angular/core';
import { CommonModule } from '@angular/common';
import { HttpClient } from '@angular/common/http';
import { RouterModule } from '@angular/router';
import { API } from '../../core/api';

@Component({
  selector: 'app-admin-transactions',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './admin-transactions.html'
})
export class AdminTransactions implements OnInit {

  transactions: any[] = [];

  constructor(
    private http: HttpClient,
    private cdr: ChangeDetectorRef
  ) {}

  ngOnInit(): void {
    this.loadTransactions();
  }

  loadTransactions(): void {
    this.http.get<any[]>(API.ADMIN.TRANSACTIONS)
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

  formatAmount(amount: number): string {
    return Number(amount).toFixed(2);
  }
}