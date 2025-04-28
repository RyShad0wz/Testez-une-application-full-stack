import { HttpClientModule } from '@angular/common/http';
import { DatePipe } from '@angular/common';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { of } from 'rxjs';
import { SessionService } from 'src/app/services/session.service';
import { UserService } from 'src/app/services/user.service';
import { User } from '../../interfaces/user.interface';
import { MeComponent } from './me.component';
import { SessionInformation } from '../../interfaces/sessionInformation.interface';

describe('MeComponent', () => {
  let component: MeComponent;
  let fixture: ComponentFixture<MeComponent>;
  let mockSessionService: Partial<SessionService>;
  let mockUserService: Partial<UserService>;
  let mockSnackBar: Partial<MatSnackBar>;
  let mockRouter: Partial<Router>;

  // Correction de l'interface User
  const baseUser: User = {
    id: 1,
    firstName: 'John',
    lastName: 'Doe',
    email: 'john@doe.com',
    admin: true,
    password: 'fakePassword', // Ajout de la propriété manquante
    createdAt: new Date(),
    updatedAt: new Date(),
  };

  // Correction de l'interface SessionInformation
  const mockSessionInfo: SessionInformation = {
    token: 'fakeToken',
    type: 'user',
    id: 1,
    username: 'john@doe.com',
    firstName: 'John',
    lastName: 'Doe',
    admin: true
  };

  beforeEach(async () => {
    mockSessionService = {
      sessionInformation: mockSessionInfo, // Utilisation de l'objet complet
      logOut: jest.fn(),
    };

    mockUserService = {
      getById: jest.fn().mockReturnValue(of(baseUser)),
      delete: jest.fn().mockReturnValue(of(null)),
    };

    mockSnackBar = { open: jest.fn() };
    mockRouter = { navigate: jest.fn() };

    await TestBed.configureTestingModule({
      declarations: [MeComponent],
      imports: [
        MatSnackBarModule,
        HttpClientModule,
        MatCardModule,
        MatFormFieldModule,
        MatIconModule,
        MatInputModule,
        MatButtonModule,
      ],
      providers: [
        { provide: SessionService, useValue: mockSessionService },
        { provide: UserService, useValue: mockUserService },
        { provide: MatSnackBar, useValue: mockSnackBar },
        { provide: Router, useValue: mockRouter },
      ],
    }).compileComponents();

    fixture = TestBed.createComponent(MeComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should fetch user on init', () => {
    expect(mockUserService.getById).toHaveBeenCalledWith('1');
    expect(component.user).toEqual(baseUser);
  });

  describe('Template rendering', () => {
    it('should display user info', () => {
      const compiled = fixture.nativeElement;
      expect(compiled.querySelector('h1').textContent).toContain('User information');
      expect(compiled.querySelector('p').textContent).toContain('John DOE');
      expect(compiled.querySelector('p:nth-of-type(2)').textContent).toContain(baseUser.email);
    });

    it('should call history.back() on back button click', () => {
      const spy = jest.spyOn(window.history, 'back');
      fixture.nativeElement.querySelector('button[mat-icon-button]').click();
      expect(spy).toHaveBeenCalled();
    });

    it('should show admin status for admin user', () => {
      expect(fixture.nativeElement.querySelector('.my2').textContent).toContain('You are admin');
    });
  });

  describe('Non-admin user', () => {
    beforeEach(() => {
      const nonAdminUser = { ...baseUser, admin: false };
      mockUserService.getById = jest.fn().mockReturnValue(of(nonAdminUser));
      
      // Recréation du composant avec les nouvelles données
      fixture = TestBed.createComponent(MeComponent);
      component = fixture.componentInstance;
      fixture.detectChanges();
    });

    it('should display delete button', () => {
      const deleteButton = fixture.nativeElement.querySelector('button[color="warn"]');
      expect(deleteButton).toBeTruthy();
    });

    it('should handle account deletion', () => {
      component.delete();
      
      expect(mockUserService.delete).toHaveBeenCalledWith('1');
      expect(mockSnackBar.open).toHaveBeenCalledWith(
        'Your account has been deleted !',
        'Close',
        { duration: 3000 }
      );
      expect(mockSessionService.logOut).toHaveBeenCalled();
      expect(mockRouter.navigate).toHaveBeenCalledWith(['/']);
    });
  });

  describe('Date formatting', () => {
    it('should display correct formatted dates', () => {
      const fixedDate = new Date('2023-01-01');
      const formattedDate = new DatePipe('en-US').transform(fixedDate, 'longDate');
      
      const dateUser = { 
        ...baseUser, 
        createdAt: fixedDate, 
        updatedAt: fixedDate 
      };
      mockUserService.getById = jest.fn().mockReturnValue(of(dateUser));
      
      fixture = TestBed.createComponent(MeComponent);
      component = fixture.componentInstance;
      fixture.detectChanges();
  
      const compiled = fixture.nativeElement;
      const paragraphs = Array.from(compiled.querySelectorAll('p'))
        .map((p: unknown) => (p as HTMLElement).textContent?.trim());
  
      // Correction avec 2 espaces après le deux-points
      expect(paragraphs).toEqual(
        expect.arrayContaining([
          expect.stringContaining(`Create at:  ${formattedDate}`), // Double espace
          expect.stringContaining(`Last update:  ${formattedDate}`)
        ])
      );
    });
  });
});