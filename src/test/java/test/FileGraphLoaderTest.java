package test;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import software.ulpgc.pathfinder.FileGraphLoader;
import software.ulpgc.pathfinder.GraphContainer;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class FileGraphLoaderTest {
	@TempDir
	Path dir;

	@Test
	void testLoadValidFile() throws IOException {
		GraphContainer container = new FileGraphLoader(write("A,B,1.0", "B,C,2.5")).load();
		assertEquals(List.of("A", "B", "C"), container.shortestPathBetween("A", "C"));
		assertEquals(3.5, container.pathWeightBetween("A", "C"));
	}

	@Test
	void testInvalidLinesAreReportedAndSkipped() throws IOException {
		PrintStream original = System.err;
		ByteArrayOutputStream captured = new ByteArrayOutputStream();
		System.setErr(new PrintStream(captured));
		try {
			GraphContainer container = new FileGraphLoader(write("A,B", "A,B,x", "A,B,2")).load();
			assertEquals(2.0, container.pathWeightBetween("A", "B"));
		}
		finally {
			System.setErr(original);
		}
		String output = captured.toString();
		assertTrue(output.contains("Invalid line at 0"));
		assertTrue(output.contains("Invalid line at 1"));
	}

	@Test
	void testMissingFileThrowsIOException() {
		assertThrows(IOException.class, () -> new FileGraphLoader(new File(dir.toFile(), "no-existe.csv")).load());
	}

	@Test
	void testParseExceptionCanBeCreated() {
		assertNotNull(new FileGraphLoader.ParseException());
	}

	private File write(String... lines) throws IOException {
		Path file = dir.resolve("graph.csv");
		Files.write(file, List.of(lines));
		return file.toFile();
	}
}
