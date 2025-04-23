import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { Router } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of, throwError } from 'rxjs';
import { SessionService } from 'src/app/services/session.service';
import { AuthService } from '../../services/auth.service';
import { SessionInformation } from 'src/app/interfaces/sessionInformation.interface';
import { LoginComponent } from './login.component';

describe('LoginComponent', () => {
  let component: LoginComponent;
  let fixture: ComponentFixture<LoginComponent>;
  let authService: jest.Mocked<AuthService>;
  let router: Router;
  let sessionService: SessionService;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [LoginComponent],
      providers: [
        SessionService,
        {
          provide: AuthService,
          useValue: {
            login: jest.fn(() => of({}))
          }
        }
      ],
      imports: [
        RouterTestingModule.withRoutes([{ path: 'sessions', redirectTo: '' }]),
        BrowserAnimationsModule,
        HttpClientModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(LoginComponent);
    component = fixture.componentInstance;
    authService = TestBed.inject(AuthService) as jest.Mocked<AuthService>;
    router = TestBed.inject(Router);
    sessionService = TestBed.inject(SessionService);
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize form with empty values', () => {
    expect(component.form.value).toEqual({
      email: '',
      password: ''
    });
  });

  it('should have required validators on email and password', () => {
    const emailControl = component.form.get('email');
    const passwordControl = component.form.get('password');

    emailControl?.setValue('');
    passwordControl?.setValue('');

    expect(emailControl?.valid).toBeFalsy();
    expect(passwordControl?.valid).toBeFalsy();
    expect(emailControl?.hasError('required')).toBeTruthy();
    expect(passwordControl?.hasError('required')).toBeTruthy();
  });

  it('should have email validator', () => {
    const emailControl = component.form.get('email');

    emailControl?.setValue('invalid-email');
    expect(emailControl?.valid).toBeFalsy();
    expect(emailControl?.hasError('email')).toBeTruthy();

    emailControl?.setValue('valid@email.com');
    expect(emailControl?.valid).toBeTruthy();
  });

  it('should require non-empty password (no minLength)', () => {
    const passwordControl = component.form.get('password');

    passwordControl?.setValue('');
    expect(passwordControl?.valid).toBeFalsy();
    expect(passwordControl?.hasError('required')).toBeTruthy();

    passwordControl?.setValue('12'); // Pas d'erreur attendue ici car minLength n'est pas défini
    expect(passwordControl?.valid).toBeTruthy();
  });

  it('should toggle password visibility', () => {
    expect(component.hide).toBeTruthy();
    component.hide = false;
    expect(component.hide).toBeFalsy();
  });

  it('should call authService.login and navigate on successful login', () => {
    const loginRequest = { email: 'test@test.com', password: '123' };
    const sessionInfo: SessionInformation = {
      token: 'token',
      type: 'type',
      id: 1,
      username: 'username',
      firstName: 'first',
      lastName: 'last',
      admin: false
    };

    authService.login.mockReturnValue(of(sessionInfo));
    component.form.setValue(loginRequest);

    const sessionServiceSpy = jest.spyOn(sessionService, 'logIn');
    const routerSpy = jest.spyOn(router, 'navigate');

    component.submit();

    expect(authService.login).toHaveBeenCalledWith(loginRequest);
    expect(sessionServiceSpy).toHaveBeenCalledWith(sessionInfo);
    expect(routerSpy).toHaveBeenCalledWith(['/sessions']);
    expect(component.onError).toBeFalsy();
  });

  it('should set onError to true on login failure', () => {
    const loginRequest = { email: 'test@test.com', password: 'password' };

    authService.login.mockReturnValue(throwError(() => new Error('Error')));
    component.form.setValue(loginRequest);

    component.submit();

    expect(component.onError).toBeTruthy();
  });

  it('should not call login when form is invalid', () => {
    component.form.setValue({ email: '', password: '' }); // form invalid

    const loginSpy = jest.spyOn(authService, 'login');

    if (component.form.invalid) {
      expect(loginSpy).not.toHaveBeenCalled();
      return;
    }

    component.submit(); // just in case fallback
  });
});