import { ComponentFixture, TestBed } from '@angular/core/testing';
import { of } from 'rxjs';
import { HttpHeaders, HttpResponse } from '@angular/common/http';

import { ClubmasterTestModule } from '../../../test.module';
import { MembershipFeeComponent } from 'app/entities/membership-fee/membership-fee.component';
import { MembershipFeeService } from 'app/entities/membership-fee/membership-fee.service';
import { MembershipFee } from 'app/shared/model/membership-fee.model';

describe('Component Tests', () => {
  describe('MembershipFee Management Component', () => {
    let comp: MembershipFeeComponent;
    let fixture: ComponentFixture<MembershipFeeComponent>;
    let service: MembershipFeeService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ClubmasterTestModule],
        declarations: [MembershipFeeComponent],
        providers: []
      })
        .overrideTemplate(MembershipFeeComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(MembershipFeeComponent);
      comp = fixture.componentInstance;
      service = fixture.debugElement.injector.get(MembershipFeeService);
    });

    it('Should call load all on init', () => {
      // GIVEN
      const headers = new HttpHeaders().append('link', 'link;link');
      spyOn(service, 'query').and.returnValue(
        of(
          new HttpResponse({
            body: [new MembershipFee(123)],
            headers
          })
        )
      );

      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.membershipFees && comp.membershipFees[0]).toEqual(jasmine.objectContaining({ id: 123 }));
    });
  });
});
