package com.bobbyprod;

import static org.mockito.Mockito.verify;

class AcceptCompatibleTask {

    /*@Mock
    private IMediator mediator;

    private AssemblyStation assemblyStation;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        assemblyStation = new AssemblyStation("AS1", "Main Assembly Station");
    }

    @Test
    void testAcceptTaskWithCompatibleTask() {
        Task task = new Task();
        task.setActionType(ActionType.ASSEMBLE_ITEM);
        task.setCompatibleAssetType(AssetType.ASSEMBLY_STATION);

        assemblyStation.acceptTask(task);

        assertEquals(AssetState.IDLE, assemblyStation.getState(), "Assembly station should return to IDLE state after processing a compatible task.");
    }

    @Test
    void testAcceptTaskWithIncompatibleTask() {
        Task task = new Task();
        task.setActionType(ActionType.MOVE_TO_WAREHOUSE);  // Assuming this is not handled by AssemblyStation
        task.setCompatibleAssetType(AssetType.AGV);  // Incorrect type for an assembly station

        assemblyStation.acceptTask(task);

        assertEquals(AssetState.ERROR, assemblyStation.getState(), "Assembly station should be in ERROR state after processing an incompatible task.");
        verify(mediator, never()).notify(assemblyStation, task);  // Verify that mediator does not get called
    }*/
}
