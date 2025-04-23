import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { Router } from '@angular/router';
import { RouterTestingModule } from '@angular/router/testing';
import { of } from 'rxjs';
import { SessionService } from '../../../../services/session.service';
import { SessionApiService } from '../../services/session-api.service';
import { TeacherService } from '../../../../services/teacher.service';
import { DetailComponent } from './detail.component';
import { MatIconModule } from '@angular/material/icon';
import { MatCardModule } from '@angular/material/card';
import { MatButtonModule } from '@angular/material/button';
import { MatFormFieldModule } from '@angular/material/form-field';

describe('DetailComponent', () => {
  let component: DetailComponent;
  let fixture: ComponentFixture<DetailComponent>;
  let sessionApiService: any;
  let teacherService: any;
  let router: Router;
  let matSnackBar: MatSnackBar;

  beforeEach(async () => {
    const mockSessionService = {
      sessionInformation: {
        admin: true,
        id: 1,
        token: 'mock-token',
        type: 'user',
        username: 'test@test.com',
        firstName: 'John',
        lastName: 'Doe'
      }
    };

    await TestBed.configureTestingModule({
      imports: [
        RouterTestingModule,
        HttpClientTestingModule,
        MatSnackBarModule,
        ReactiveFormsModule,
        // Ajout des modules Material manquants
        MatIconModule,
        MatCardModule,
        MatButtonModule,
        MatFormFieldModule
      ],
      declarations: [DetailComponent],
      providers: [
        { provide: SessionService, useValue: mockSessionService },
        {
          provide: SessionApiService,
          useValue: {
            detail: jest.fn().mockReturnValue(of({
              id: 1,
              name: 'Session',
              users: [1],
              teacher_id: 1,
              description: 'Description',
              date: new Date(),
              createdAt: new Date(),
              updatedAt: new Date()
            })),
            delete: jest.fn().mockReturnValue(of({})),
            participate: jest.fn().mockReturnValue(of({})),
            unParticipate: jest.fn().mockReturnValue(of({}))
          }
        },
        {
          provide: TeacherService,
          useValue: {
            detail: jest.fn().mockReturnValue(of({
              id: 1,
              firstName: 'Teacher',
              lastName: 'Name',
              createdAt: new Date(),
              updatedAt: new Date()
            }))
          }
        }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(DetailComponent);
    component = fixture.componentInstance;
    
    // Initialisation explicite des propriétés
    component.sessionId = '1';
    component.userId = '1';

    sessionApiService = TestBed.inject(SessionApiService);
    teacherService = TestBed.inject(TeacherService);
    router = TestBed.inject(Router);
    matSnackBar = TestBed.inject(MatSnackBar);

    // Mock des méthodes qui causent des effets de bord
    jest.spyOn(matSnackBar, 'open').mockImplementation();
    jest.spyOn(router, 'navigate').mockImplementation(() => Promise.resolve(true));

    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize session data', () => {
    component.ngOnInit();
    expect(sessionApiService.detail).toHaveBeenCalledWith('1');
    expect(teacherService.detail).toHaveBeenCalledWith('1');
    expect(component.session).toBeTruthy();
    expect(component.teacher).toBeTruthy();
  });

  describe('delete', () => {
    it('should call delete and navigate to sessions', () => {
      component.delete();
      
      expect(sessionApiService.delete).toHaveBeenCalledWith('1');
      expect(matSnackBar.open).toHaveBeenCalledWith(
        'Session deleted !', 
        'Close', 
        { duration: 3000 }
      );
      expect(router.navigate).toHaveBeenCalledWith(['sessions']);
    });
  });

  describe('participation', () => {
    it('should participate when not participating', () => {
      component.isParticipate = false;
      component.participate();
      expect(sessionApiService.participate).toHaveBeenCalledWith('1', '1');
      expect(sessionApiService.detail).toHaveBeenCalled();
    });

    it('should unparticipate when participating', () => {
      component.isParticipate = true;
      component.unParticipate();
      expect(sessionApiService.unParticipate).toHaveBeenCalledWith('1', '1');
      expect(sessionApiService.detail).toHaveBeenCalled();
    });
  });

  it('should go back', () => {
    const historySpy = jest.spyOn(window.history, 'back');
    component.back();
    expect(historySpy).toHaveBeenCalled();
  });
});