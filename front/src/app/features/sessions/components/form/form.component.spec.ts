import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ReactiveFormsModule } from '@angular/forms';
import { MatCardModule } from '@angular/material/card';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatSelectModule } from '@angular/material/select';
import { MatSnackBar, MatSnackBarModule } from '@angular/material/snack-bar';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { ActivatedRoute, Router } from '@angular/router';
import { of } from 'rxjs';
import { FormComponent } from './form.component';
import { SessionService } from 'src/app/services/session.service';
import { SessionApiService } from '../../services/session-api.service';
import { TeacherService } from '../../../../services/teacher.service';

describe('FormComponent', () => {
  let component: FormComponent;
  let fixture: ComponentFixture<FormComponent>;
  let mockSessionService: any;
  let mockSessionApiService: any;
  let mockRouter: any;
  let mockMatSnackBar: any;
  let mockTeacherService: any;
  let mockActivatedRoute: any;

  beforeEach(async () => {
    mockSessionService = {
      sessionInformation: {
        admin: true
      }
    };

    mockSessionApiService = {
      detail: jest.fn().mockReturnValue(of({
        id: '1',
        name: 'Existing Session',
        date: '2023-01-01',
        teacher_id: 1,
        description: 'Existing Description'
      })),
      create: jest.fn().mockReturnValue(of({})),
      update: jest.fn().mockReturnValue(of({}))
    };

    mockRouter = {
      navigate: jest.fn(),
      url: '/sessions'
    };

    mockMatSnackBar = {
      open: jest.fn()
    };

    mockTeacherService = {
      all: jest.fn().mockReturnValue(of([
        { id: 1, firstName: 'John', lastName: 'Doe' },
        { id: 2, firstName: 'Jane', lastName: 'Smith' }
      ]))
    };

    mockActivatedRoute = {
      snapshot: {
        paramMap: {
          get: jest.fn().mockReturnValue('1')
        }
      }
    };

    await TestBed.configureTestingModule({
      imports: [
        HttpClientModule,
        MatCardModule,
        MatIconModule,
        MatFormFieldModule,
        MatInputModule,
        ReactiveFormsModule,
        MatSnackBarModule,
        MatSelectModule,
        BrowserAnimationsModule
      ],
      providers: [
        { provide: SessionService, useValue: mockSessionService },
        { provide: SessionApiService, useValue: mockSessionApiService },
        { provide: Router, useValue: mockRouter },
        { provide: MatSnackBar, useValue: mockMatSnackBar },
        { provide: TeacherService, useValue: mockTeacherService },
        { provide: ActivatedRoute, useValue: mockActivatedRoute }
      ],
      declarations: [FormComponent]
    }).compileComponents();

    fixture = TestBed.createComponent(FormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should redirect non-admin users', () => {
    mockSessionService.sessionInformation.admin = false;
    const routerSpy = jest.spyOn(mockRouter, 'navigate');
    
    fixture = TestBed.createComponent(FormComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
    
    expect(routerSpy).toHaveBeenCalledWith(['/sessions']);
  });

  describe('Form Initialization', () => {
    it('should initialize create form', () => {
      expect(component.onUpdate).toBeFalsy();
      expect(component.sessionForm).toBeDefined();
      expect(component.sessionForm?.get('name')?.value).toBe('');
    });
  });

  describe('Form Validation', () => {
    it('should have invalid empty form', () => {
      expect(component.sessionForm?.valid).toBeFalsy();
    });

    it('should validate name field', () => {
      const name = component.sessionForm?.get('name');
      expect(name?.hasError('required')).toBeTruthy();
      name?.setValue('Test');
      expect(name?.valid).toBeTruthy();
    });

    it('should validate date field', () => {
      const date = component.sessionForm?.get('date');
      expect(date?.hasError('required')).toBeTruthy();
      date?.setValue('2023-01-01');
      expect(date?.valid).toBeTruthy();
    });
  });

  describe('Form Submission', () => {
    beforeEach(() => {
      component.sessionForm?.patchValue({
        name: 'Test Session',
        date: '2023-01-01',
        teacher_id: 1,
        description: 'Test Description'
      });
    });

    it('should create session', () => {
      component.submit();
      expect(mockSessionApiService.create).toHaveBeenCalled();
    });
  });

  describe('Teacher Selection', () => {
    it('should load teachers', () => {
      expect(mockTeacherService.all).toHaveBeenCalled();
      component.teachers$.subscribe(teachers => {
        expect(teachers.length).toBe(2);
      });
    });
  });
});