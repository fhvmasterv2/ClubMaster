import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ClubmasterTestModule } from '../../../test.module';
import { MembershipFeeDetailComponent } from 'app/entities/membership-fee/membership-fee-detail.component';
import { MembershipFee } from 'app/shared/model/membership-fee.model';

describe('Component Tests', () => {
  describe('MembershipFee Management Detail Component', () => {
    let comp: MembershipFeeDetailComponent;
    let fixture: ComponentFixture<MembershipFeeDetailComponent>;
    const route = ({ data: of({ membershipFee: new MembershipFee(123) }) } as any) as ActivatedRoute;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [ClubmasterTestModule],
        declarations: [MembershipFeeDetailComponent],
        providers: [{ provide: ActivatedRoute, useValue: route }]
      })
        .overrideTemplate(MembershipFeeDetailComponent, '')
        .compileComponents();
      fixture = TestBed.createComponent(MembershipFeeDetailComponent);
      comp = fixture.componentInstance;
    });

    describe('OnInit', () => {
      it('Should load membershipFee on init', () => {
        // WHEN
        comp.ngOnInit();

        // THEN
        expect(comp.membershipFee).toEqual(jasmine.objectContaining({ id: 123 }));
      });
    });
  });
});
