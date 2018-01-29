import { Injectable } from '@angular/core';
import { ContractsService } from './services/contracts/contracts.service';

@Injectable()
export class AppFactory {
  constructor(private _cs: ContractsService) {
  }

  public load(): Promise<boolean> {
    return this._cs.init().then(() => true).catch(() =>  false);
  }
}