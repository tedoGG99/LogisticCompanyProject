package com.nbu.logistics.serviceTest;

import com.nbu.logistics.data.Office;
import com.nbu.logistics.repositories.OfficeRepository;
import com.nbu.logistics.services.OfficeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OfficeServiceTest {

    @Mock
    private OfficeRepository officeRepository;

    @InjectMocks
    private OfficeService officeService;

    private Office office;

    @BeforeEach
    void setUp() {
        office = new Office();
        office.setId(1);
    }

    // ------------------------
    // getAllOffices
    // ------------------------
    @Test
    void getAllOffices_shouldReturnList() {
        when(officeRepository.findAll()).thenReturn(List.of(office));

        List<Office> result = officeService.getAllOffices();

        assertEquals(1, result.size());
        verify(officeRepository, times(1)).findAll();
    }

    // ------------------------
    // getOfficeById - FOUND
    // ------------------------
    @Test
    void getOfficeById_existingId_shouldReturnOffice() {
        when(officeRepository.findById(1)).thenReturn(Optional.of(office));

        Office result = officeService.getOfficeById(1);

        assertNotNull(result);
        assertEquals(1, result.getId());
        verify(officeRepository).findById(1);
    }

    // ------------------------
    // getOfficeById - NOT FOUND
    // ------------------------
    @Test
    void getOfficeById_missingId_shouldThrowException() {
        when(officeRepository.findById(1)).thenReturn(Optional.empty());

        RuntimeException ex = assertThrows(
                RuntimeException.class,
                () -> officeService.getOfficeById(1)
        );

        assertEquals("Office not found", ex.getMessage());
        verify(officeRepository).findById(1);
    }

    // ------------------------
    // createOffice - VALID
    // ------------------------
    @Test
    void createOffice_validOffice_shouldSave() {
        officeService.createOffice(office);

        verify(officeRepository, times(1)).save(office);
    }

    // ------------------------
    // createOffice - NULL
    // ------------------------
    @Test
    void createOffice_nullOffice_shouldThrowException() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> officeService.createOffice(null)
        );

        assertEquals("Office cannot be null", ex.getMessage());
        verify(officeRepository, never()).save(any());
    }

    // ------------------------
    // findOfficeById
    // ------------------------
    @Test
    void findOfficeById_shouldReturnOptional() {
        when(officeRepository.findById(1)).thenReturn(Optional.of(office));

        Optional<Office> result = officeService.findOfficeById(1);

        assertTrue(result.isPresent());
        verify(officeRepository).findById(1);
    }
}
