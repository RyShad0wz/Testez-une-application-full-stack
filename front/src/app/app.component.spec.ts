import { HttpClientModule } from '@angular/common/http';
import { TestBed } from '@angular/core/testing';
import { MatToolbarModule } from '@angular/material/toolbar';
import { Router } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { of } from 'rxjs';

import { AppComponent } from './app.component';
import { SessionService } from './services/session.service';

describe('AppComponent', () => {
  let component: AppComponent;
  let fixture: any;
  let sessionService: SessionService;
  let router: Router;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        HttpClientModule,
        MatToolbarModule
      ],
      declarations: [
        AppComponent
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(AppComponent);
    component = fixture.componentInstance;
    sessionService = TestBed.inject(SessionService);
    router = TestBed.inject(Router);
  });

  it('should create the app', () => {
    expect(component).toBeTruthy();
  });

  describe('when user is logged in', () => {
    beforeEach(() => {
      jest.spyOn(sessionService, '$isLogged').mockReturnValue(of(true));
      fixture.detectChanges();
    });

    it('should display sessions and account links', () => {
      const compiled = fixture.nativeElement;
      expect(compiled.querySelector('span[routerLink="sessions"]')).toBeTruthy();
      expect(compiled.querySelector('span[routerLink="me"]')).toBeTruthy();
      expect(compiled.querySelector('span[routerLink="login"]')).toBeFalsy();
    });

    it('should call logout and navigate on logout click', () => {
      const logoutSpy = jest.spyOn(sessionService, 'logOut');
      const navigateSpy = jest.spyOn(router, 'navigate');
      
      component.logout();
      
      expect(logoutSpy).toHaveBeenCalled();
      expect(navigateSpy).toHaveBeenCalledWith(['']);
    });
  });

  describe('when user is not logged in', () => {
    beforeEach(() => {
      jest.spyOn(sessionService, '$isLogged').mockReturnValue(of(false));
      fixture.detectChanges();
    });

    it('should display login and register links', () => {
      const compiled = fixture.nativeElement;
      expect(compiled.querySelector('span[routerLink="login"]')).toBeTruthy();
      expect(compiled.querySelector('span[routerLink="register"]')).toBeTruthy();
      expect(compiled.querySelector('span[routerLink="sessions"]')).toBeFalsy();
    });
  });

  it('should call sessionService.$isLogged when $isLogged is called', () => {
    const isLoggedSpy = jest.spyOn(sessionService, '$isLogged').mockReturnValue(of(true));
    component.$isLogged().subscribe((logged) => {
      expect(logged).toBe(true);
    });
    expect(isLoggedSpy).toHaveBeenCalled();
  });
});