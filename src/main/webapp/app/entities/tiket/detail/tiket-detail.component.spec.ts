import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { TiketDetailComponent } from './tiket-detail.component';

describe('Component Tests', () => {
  describe('Tiket Management Detail Component', () => {
    let comp: TiketDetailComponent;
    let fixture: ComponentFixture<TiketDetailComponent>;

    beforeEach(() => {
      TestBed.configureTestingModule({
        declarations: [TiketDetailComponent],
        providers: [
          {
            provide: ActivatedRoute,
            useValue: { data: of({ tiket: { id: 123 } }) },
          },
        ],
      })
        .overrideTemplate(TiketDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(TiketDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load tiket on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.tiket).toEqual(expect.objectContaining({ id: 123 }));
      });
    });
  });
});
