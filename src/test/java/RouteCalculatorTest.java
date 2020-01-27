import core.Line;
import core.Station;
import junit.framework.TestCase;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RouteCalculatorTest extends TestCase {
    List<Station> straightRoute, oneTransferRoute, twoTransfersRoute;
    StationIndex stationIndex;
    RouteCalculator calculator;

    @Override
    protected void setUp() throws Exception {
        stationIndex = new StationIndex();
        calculator = new RouteCalculator(stationIndex);
        straightRoute = new ArrayList<>();
        oneTransferRoute = new ArrayList<>();
        twoTransfersRoute  = new ArrayList<>();
        Line line1 = new Line(1, "Line 1");
        Line line2 = new Line(2, "Line 2");
        Line line3 = new Line(3, "Line 3");
        List<Station> connection1 = new ArrayList<>();
        List<Station> connection2 = new ArrayList<>();

        stationIndex.addStation(new Station("A", line1));
        stationIndex.addStation(new Station("B", line1));
        stationIndex.addStation(new Station("C", line1));
        stationIndex.addStation(new Station("X", line2));
        stationIndex.addStation(new Station("Y", line2));
        stationIndex.addStation(new Station("Z", line2));
        stationIndex.addStation(new Station("M", line3));
        stationIndex.addStation(new Station("N", line3));
        stationIndex.addStation(new Station("O", line3));

        line1.addStation(stationIndex.getStation("A"));
        line1.addStation(stationIndex.getStation("B"));
        line1.addStation(stationIndex.getStation("C"));
        line2.addStation(stationIndex.getStation("X"));
        line2.addStation(stationIndex.getStation("Y"));
        line2.addStation(stationIndex.getStation("Z"));
        line3.addStation(stationIndex.getStation("M"));
        line3.addStation(stationIndex.getStation("N"));
        line3.addStation(stationIndex.getStation("O"));

        stationIndex.addLine(line1);
        stationIndex.addLine(line2);
        stationIndex.addLine(line3);

        /**
         *      Line 1: A-B-C;
         *      Line 2: X-Y-Z;
         *      Line 3: M-N-O;
         *      Connections: Y/B, N/C;
         *
         *                              Line 1
         *                                A
         *                                :
         *                                :
         *      line 2         X --------B/Y ------- Z
         *                                :
         *                                :
         *                                :
         *      line 3         M --------C/N ------- O
         *
         */

        connection1.add(stationIndex.getStation("B"));
        connection1.add(stationIndex.getStation("Y"));
        connection2.add(stationIndex.getStation("C"));
        connection2.add(stationIndex.getStation("N"));

        stationIndex.addConnection(connection1);
        stationIndex.addConnection(connection2);

        straightRoute.add(stationIndex.getStation("A"));
        straightRoute.add(stationIndex.getStation("B"));
        straightRoute.add(stationIndex.getStation("C"));

        oneTransferRoute.add(stationIndex.getStation("X"));
        oneTransferRoute.add(stationIndex.getStation("Y"));
        oneTransferRoute.add(stationIndex.getStation("B"));
        oneTransferRoute.add(stationIndex.getStation("C"));

        twoTransfersRoute.add(stationIndex.getStation("M"));
        twoTransfersRoute.add(stationIndex.getStation("N"));
        twoTransfersRoute.add(stationIndex.getStation("C"));
        twoTransfersRoute.add(stationIndex.getStation("B"));
        twoTransfersRoute.add(stationIndex.getStation("Y"));
        twoTransfersRoute.add(stationIndex.getStation("Z"));

    }

    public void testCalculateDuration() {
        double actual = RouteCalculator.calculateDuration(straightRoute);
        double expected = 5;
        assertEquals(expected, actual);
    }

    public void test_routeOnTheLine() {
        Station from = stationIndex.getStation("A");
        Station to = stationIndex.getStation("C");
        List<Station> actual = calculator.getShortestRoute(from, to);
        List<Station> expected = straightRoute;
        assertEquals(expected, actual);
    }

    public void test_routeWithOneConnection() {
        Station from = stationIndex.getStation("X");
        Station to = stationIndex.getStation("C");
        List<Station> actual = calculator.getShortestRoute(from, to);
        List<Station> expected = oneTransferRoute;
        assertEquals(expected, actual);
    }
    public void test_routeWithTwoConnections() {
        Station from = stationIndex.getStation("M");
        Station to = stationIndex.getStation("Z");
        List<Station> actual = calculator.getShortestRoute(from, to);
        List<Station> expected = twoTransfersRoute;
        assertEquals(expected, actual);
    }

    }


