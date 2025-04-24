import { HttpClientModule } from '@angular/common/http';
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { expect } from '@jest/globals';
import { SessionService } from 'src/app/services/session.service';
import { SessionApiService } from '../../services/session-api.service';
import { of } from 'rxjs';
import { Session } from '../../interfaces/session.interface';
import { By } from '@angular/platform-browser';
import { RouterTestingModule } from '@angular/router/testing';
import { NO_ERRORS_SCHEMA } from '@angular/core';

import { ListComponent } from './list.component';

describe('ListComponent', () => {
  let component: ListComponent;
  let fixture: ComponentFixture<ListComponent>;
  let sessionApiService: SessionApiService;
  let sessionService: SessionService;

  const mockSessionService = {
    sessionInformation: {
      admin: true
    }
  };

  const mockSessions: Session[] = [
    {
      id: 1,
      name: 'Session 1',
      description: 'Description 1',
      date: new Date(),
      teacher_id: 1,
      users: [1, 2],
      createdAt: new Date(),
      updatedAt: new Date()
    },
    {
      id: 2,
      name: 'Session 2',
      description: 'Description 2',
      date: new Date(),
      teacher_id: 2,
      users: [1, 3],
      createdAt: new Date(),
      updatedAt: new Date()
    }
  ];

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ListComponent],
      imports: [
        HttpClientModule, 
        MatCardModule, 
        MatIconModule,
        RouterTestingModule
      ],
      providers: [
        { provide: SessionService, useValue: mockSessionService },
        {
          provide: SessionApiService,
          useValue: {
            all: jest.fn(() => of(mockSessions))
          }
        }
      ],
      schemas: [NO_ERRORS_SCHEMA]
    }).compileComponents();

    fixture = TestBed.createComponent(ListComponent);
    component = fixture.componentInstance;
    sessionApiService = TestBed.inject(SessionApiService);
    sessionService = TestBed.inject(SessionService);
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should get user session information', () => {
    expect(component.user).toEqual(mockSessionService.sessionInformation);
  });

  it('should call sessionApiService.all on init', () => {
    expect(sessionApiService.all).toHaveBeenCalled();
  });

  it('should display sessions', () => {
    const sessionCards = fixture.debugElement.queryAll(By.css('.item'));
    expect(sessionCards.length).toBe(2);
  });

  it('should display session information correctly', () => {
    const firstSessionCard = fixture.debugElement.query(By.css('.item'));
    const title = firstSessionCard.query(By.css('mat-card-title')).nativeElement.textContent;
    const subtitle = firstSessionCard.query(By.css('mat-card-subtitle')).nativeElement.textContent;
    const description = firstSessionCard.query(By.css('mat-card-content p')).nativeElement.textContent;

    expect(title).toContain(mockSessions[0].name);
    expect(subtitle).toContain('Session on');
    expect(description).toContain(mockSessions[0].description);
  });

  it('should display create button for admin user', () => {
    const createButton = fixture.debugElement.query(By.css('button'));
    expect(createButton.nativeElement.textContent).toContain('Create');
  });

  it('should display edit button for admin user', () => {
    const buttons = fixture.debugElement.queryAll(By.css('button'));
    const editButtons = buttons.filter(button => 
      button.nativeElement.textContent.includes('Edit')
    );
    expect(editButtons.length).toBe(2);
  });

  it('should display detail button for each session', () => {
    const buttons = fixture.debugElement.queryAll(By.css('button'));
    const detailButtons = buttons.filter(button => 
      button.nativeElement.textContent.includes('Detail')
    );
    expect(detailButtons.length).toBe(2);
  });

  it('should not display create/edit buttons for non-admin user', () => {
    // Change user to non-admin
    (sessionService as any).sessionInformation = { admin: false };
    fixture.detectChanges();

    const buttons = fixture.debugElement.queryAll(By.css('button'));
    const createButtons = buttons.filter(button => 
      button.nativeElement.textContent.includes('Create')
    );
    const editButtons = buttons.filter(button => 
      button.nativeElement.textContent.includes('Edit')
    );

    expect(createButtons.length).toBe(0);
    expect(editButtons.length).toBe(0);
  });
});