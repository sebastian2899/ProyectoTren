jest.mock('@angular/router');

import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { of, Subject } from 'rxjs';

import { TrenService } from '../service/tren.service';
import { ITren, Tren } from '../tren.model';

import { TrenUpdateComponent } from './tren-update.component';

describe('Component Tests', () => {
  describe('Tren Management Update Component', () => {
    let comp: TrenUpdateComponent;
    let fixture: ComponentFixture<TrenUpdateComponent>;
    let activatedRoute: ActivatedRoute;
    let trenService: TrenService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [TrenUpdateComponent],
        providers: [FormBuilder, ActivatedRoute],
      })
        .overrideTemplate(TrenUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(TrenUpdateComponent);
      activatedRoute = TestBed.inject(ActivatedRoute);
      trenService = TestBed.inject(TrenService);

      comp = fixture.componentInstance;
    });

    describe('ngOnInit', () => {
      it('Should update editForm', () => {
        const tren: ITren = { id: 456 };

        activatedRoute.data = of({ tren });
        comp.ngOnInit();

        expect(comp.editForm.value).toEqual(expect.objectContaining(tren));
      });
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Tren>>();
        const tren = { id: 123 };
        jest.spyOn(trenService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ tren });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: tren }));
        saveSubject.complete();

        // THEN
        expect(comp.previousState).toHaveBeenCalled();
        expect(trenService.update).toHaveBeenCalledWith(tren);
        expect(comp.isSaving).toEqual(false);
      });

      it('Should call create service on save for new entity', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Tren>>();
        const tren = new Tren();
        jest.spyOn(trenService, 'create').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ tren });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.next(new HttpResponse({ body: tren }));
        saveSubject.complete();

        // THEN
        expect(trenService.create).toHaveBeenCalledWith(tren);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).toHaveBeenCalled();
      });

      it('Should set isSaving to false on error', () => {
        // GIVEN
        const saveSubject = new Subject<HttpResponse<Tren>>();
        const tren = { id: 123 };
        jest.spyOn(trenService, 'update').mockReturnValue(saveSubject);
        jest.spyOn(comp, 'previousState');
        activatedRoute.data = of({ tren });
        comp.ngOnInit();

        // WHEN
        comp.save();
        expect(comp.isSaving).toEqual(true);
        saveSubject.error('This is an error!');

        // THEN
        expect(trenService.update).toHaveBeenCalledWith(tren);
        expect(comp.isSaving).toEqual(false);
        expect(comp.previousState).not.toHaveBeenCalled();
      });
    });
  });
});
