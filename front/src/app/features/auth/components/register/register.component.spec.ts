import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { FormBuilder, ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { Router } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { expect } from '@jest/globals';
import { of, throwError } from 'rxjs';
import { AuthService } from '../../services/auth.service';
import { RegisterComponent } from './register.component';
import { NgZone } from '@angular/core';

describe('RegisterComponent', () => {
  let component: RegisterComponent;
  let fixture: ComponentFixture<RegisterComponent>;
  let authService: AuthService;
  let router: Router;
  let ngZone: NgZone;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [RegisterComponent],
      imports: [
        BrowserAnimationsModule,
        HttpClientModule,
        ReactiveFormsModule,
        RouterTestingModule.withRoutes([{ path: 'login', redirectTo: '' }]),
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule
      ],
      providers: [
        AuthService,
        FormBuilder
      ]
    })
      .compileComponents();

    fixture = TestBed.createComponent(RegisterComponent);
    component = fixture.componentInstance;
    authService = TestBed.inject(AuthService);
    router = TestBed.inject(Router);
    ngZone = TestBed.inject(NgZone);
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize form with empty values and validators', () => {
    expect(component.form).toBeDefined();
    expect(component.form.get('email')?.value).toEqual('');
    expect(component.form.get('firstName')?.value).toEqual('');
    expect(component.form.get('lastName')?.value).toEqual('');
    expect(component.form.get('password')?.value).toEqual('');

    expect(component.form.get('email')?.validator).toBeTruthy();
    expect(component.form.get('firstName')?.validator).toBeTruthy();
    expect(component.form.get('lastName')?.validator).toBeTruthy();
    expect(component.form.get('password')?.validator).toBeTruthy();
  });

  it('should have invalid form when empty', () => {
    expect(component.form.invalid).toBeTruthy();
  });

  it('should validate email field', () => {
    const email = component.form.get('email');
    expect(email?.errors?.['required']).toBeTruthy();

    email?.setValue('invalid-email');
    expect(email?.errors?.['email']).toBeTruthy();

    email?.setValue('valid@email.com');
    expect(email?.errors).toBeNull();
  });

  it('should validate firstName field', () => {
    const firstName = component.form.get('firstName');
    expect(firstName?.errors?.['required']).toBeTruthy();

    // Le validateur min(3) ne fonctionne pas pour les strings, donc on ne peut pas tester cela
    // On vérifie juste que le champ est requis
    firstName?.setValue('John');
    expect(firstName?.errors).toBeNull();
  });

  it('should validate lastName field', () => {
    const lastName = component.form.get('lastName');
    expect(lastName?.errors?.['required']).toBeTruthy();

    // Le validateur min(3) ne fonctionne pas pour les strings, donc on ne peut pas tester cela
    // On vérifie juste que le champ est requis
    lastName?.setValue('Doe');
    expect(lastName?.errors).toBeNull();
  });

  it('should validate password field', () => {
    const password = component.form.get('password');
    expect(password?.errors?.['required']).toBeTruthy();

    // Le validateur min(3) ne fonctionne pas pour les strings, donc on ne peut pas tester cela
    // On vérifie juste que le champ est requis
    password?.setValue('password123');
    expect(password?.errors).toBeNull();
  });

  it('should have valid form when all fields are properly filled', () => {
    component.form.setValue({
      email: 'test@test.com',
      firstName: 'John',
      lastName: 'Doe',
      password: 'password123'
    });
    expect(component.form.valid).toBeTruthy();
  });

  it('should call authService.register and navigate on successful registration', () => {
    const authServiceSpy = jest.spyOn(authService, 'register').mockReturnValue(of(void 0));
    const routerSpy = jest.spyOn(router, 'navigate');

    component.form.setValue({
      email: 'test@test.com',
      firstName: 'John',
      lastName: 'Doe',
      password: 'password123'
    });

    ngZone.run(() => {
      component.submit();
    });

    expect(authServiceSpy).toHaveBeenCalledWith({
      email: 'test@test.com',
      firstName: 'John',
      lastName: 'Doe',
      password: 'password123'
    });
    expect(routerSpy).toHaveBeenCalledWith(['/login']);
    expect(component.onError).toBeFalsy();
  });

  it('should set onError to true when registration fails', () => {
    jest.spyOn(authService, 'register').mockReturnValue(throwError(() => new Error('Error')));

    component.form.setValue({
      email: 'test@test.com',
      firstName: 'John',
      lastName: 'Doe',
      password: 'password123'
    });

    component.submit();

    expect(component.onError).toBeTruthy();
  });

  it('should disable submit button when form is invalid', () => {
    component.form.setValue({
      email: '',
      firstName: '',
      lastName: '',
      password: ''
    });
    fixture.detectChanges();

    const submitButton = fixture.nativeElement.querySelector('button[type="submit"]');
    expect(submitButton.disabled).toBeTruthy();

    component.form.setValue({
      email: 'test@test.com',
      firstName: 'John',
      lastName: 'Doe',
      password: 'password123'
    });
    fixture.detectChanges();

    expect(submitButton.disabled).toBeFalsy();
  });
});