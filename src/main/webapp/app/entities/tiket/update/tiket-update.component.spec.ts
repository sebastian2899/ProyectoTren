jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { TiketService } from '../service/tiket.service';
import { ITiket, Tiket } from '../tiket.model';

import { TiketUpdateComponent } from './tiket-update.component';

describe('Component Tests', () => {
  describe('Tiket Management Update Component', () => {
    let comp: TiketUpdateComponent;
    let fixture: ComponentFixture<TiketUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let tiketService: TiketService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [TiketUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(TiketUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(TiketUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      tiketService = TestBed.inject(TiketService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const tiket: ITiket = { id: 456 };

        activatedRoute.data = of({ tiket });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(tiket));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Tiket>>();
        const tiket = { id: 123 };
        jest.spyOn(tiketService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ tiket });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: tiket }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(tiketService.update).toHaveBeenCalledWith(tiket);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Tiket>>();
        const tiket = new Tiket();
        jest.spyOn(tiketService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ tiket });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: tiket }));
        saveSubject.complete();

        // THEN
        expect(tiketService.create).toHaveBeenCalledWith(tiket);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Tiket>>();
        const tiket = { id: 123 };
        jest.spyOn(tiketService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ tiket });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(tiketService.update).toHaveBeenCalledWith(tiket);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
