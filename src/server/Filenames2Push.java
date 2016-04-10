package server;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.SynchronousQueue;

import bean.PrintJob;

public class Filenames2Push {
public static Queue<PrintJob> filenameQueue;
static{
	filenameQueue = new LinkedList<PrintJob>();
}

}
