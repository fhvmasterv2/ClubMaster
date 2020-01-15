import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { of } from 'rxjs';

import { ClubmasterTestModule } from '../../../test.module';
import { MembershipFeeUpdateComponent } from 'app/entities/membership-fee/membership-fee-update.component';
import { MembershipFeeService } from 'app/entities/membership-fee/membership-fee.service';
import { MembershipFee } from 'app/shared/model/membership-fee.model';

describe('Component Tests', () => {
  describe('MembershipFee Management Update Component', () => {
    let comp: MembershipFeeUpdateComponent;
    let fixture: ComponentFixture<MembershipFeeUpdateComponent>;
    let service: MembershipFeeService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ClubmasterTestModule],
        declarations: [MembershipFeeUpdateComponent],
        providers: [FormBuilder]
      })
        .overrideTemplate(MembershipFeeUpdateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(MembershipFeeUpdateComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(MembershipFeeService);
    });

    describe('save', () => {
      it('Should call update service on save for existing entity', fakeAsync(() => {
        // GIVEN
        const entity = new MembershipFee(123);
        spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.update).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));

      it('Should call create service on save for new entity', fakeAsync(() => {
        // GIVEN
        const entity = new MembershipFee();
        spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
        comp.updateForm(entity);
        // WHEN
        comp.save();
        tick(); // simulate async

        // THEN
        expect(service.create).toHaveBeenCalledWith(entity);
        expect(comp.isSaving).toEqual(false);
      }));
    });
  });
});
