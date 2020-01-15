package at.fhv.web.rest;

import at.fhv.service.MembershipFeeService;
import at.fhv.web.rest.errors.BadRequestAlertException;
import at.fhv.service.dto.MembershipFeeDTO;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link at.fhv.domain.MembershipFee}.
 */
@RestController
@RequestMapping("/api")
public class MembershipFeeResource {

    private final Logger log = LoggerFactory.getLogger(MembershipFeeResource.class);

    private static final String ENTITY_NAME = "membershipFee";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final MembershipFeeService membershipFeeService;

    public MembershipFeeResource(MembershipFeeService membershipFeeService) {
        this.membershipFeeService = membershipFeeService;
    }

    /**
     * {@code POST  /membership-fees} : Create a new membershipFee.
     *
     * @param membershipFeeDTO the membershipFeeDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new membershipFeeDTO, or with status {@code 400 (Bad Request)} if the membershipFee has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/membership-fees")
    public ResponseEntity<MembershipFeeDTO> createMembershipFee(@RequestBody MembershipFeeDTO membershipFeeDTO) throws URISyntaxException {
        log.debug("REST request to save MembershipFee : {}", membershipFeeDTO);
        if (membershipFeeDTO.getId() != null) {
            throw new BadRequestAlertException("A new membershipFee cannot already have an ID", ENTITY_NAME, "idexists");
        }
        MembershipFeeDTO result = membershipFeeService.save(membershipFeeDTO);
        return ResponseEntity.created(new URI("/api/membership-fees/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /membership-fees} : Updates an existing membershipFee.
     *
     * @param membershipFeeDTO the membershipFeeDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated membershipFeeDTO,
     * or with status {@code 400 (Bad Request)} if the membershipFeeDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the membershipFeeDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/membership-fees")
    public ResponseEntity<MembershipFeeDTO> updateMembershipFee(@RequestBody MembershipFeeDTO membershipFeeDTO) throws URISyntaxException {
        log.debug("REST request to update MembershipFee : {}", membershipFeeDTO);
        if (membershipFeeDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        MembershipFeeDTO result = membershipFeeService.save(membershipFeeDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, membershipFeeDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /membership-fees} : get all the membershipFees.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of membershipFees in body.
     */
    @GetMapping("/membership-fees")
    public List<MembershipFeeDTO> getAllMembershipFees() {
        log.debug("REST request to get all MembershipFees");
        return membershipFeeService.findAll();
    }

    /**
     * {@code GET  /membership-fees/:id} : get the "id" membershipFee.
     *
     * @param id the id of the membershipFeeDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the membershipFeeDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/membership-fees/{id}")
    public ResponseEntity<MembershipFeeDTO> getMembershipFee(@PathVariable Long id) {
        log.debug("REST request to get MembershipFee : {}", id);
        Optional<MembershipFeeDTO> membershipFeeDTO = membershipFeeService.findOne(id);
        return ResponseUtil.wrapOrNotFound(membershipFeeDTO);
    }

    /**
     * {@code DELETE  /membership-fees/:id} : delete the "id" membershipFee.
     *
     * @param id the id of the membershipFeeDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/membership-fees/{id}")
    public ResponseEntity<Void> deleteMembershipFee(@PathVariable Long id) {
        log.debug("REST request to delete MembershipFee : {}", id);
        membershipFeeService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString())).build();
    }
}
