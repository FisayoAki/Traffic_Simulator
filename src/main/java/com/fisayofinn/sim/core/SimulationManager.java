package com.fisayofinn.sim.core;

import com.fisayofinn.sim.math.HeavyTail;
import com.fisayofinn.sim.sources.OnOffSource;

import java.util.ArrayList;
import java.util.*;

public class SimulationManager {

    private EventQueue eventQueue;
    private ArrayList<OnOffSource> sources;
    private double simTime, endTime;
    private int activeSources;

    public SimulationManager(double endTime, int sourceCount){
        this.endTime = endTime;
        this.simTime = 0;
        this.activeSources = 0;
        this.eventQueue = new EventQueue();
        this.sources = new ArrayList<>();

        for(int i = 1; i <= sourceCount; i++){
            sources.add(new OnOffSource(i,onDurations,offDurations, startOn));
        }

        // Create the END event from user input
        eventQueue.addEvent(new Event(endTime, EventType.END, -1));


    }

    public void simLoop(){
        while(simTime < endTime && !eventQueue.isEmpty()){
            Event nextEvent = eventQueue.retrieveEvent();
            updateSimTime(nextEvent.getTime());
            processEvent(nextEvent);
        }
    }

    private void processEvent(Event event){
        switch(event.getType()){
            case ON:
                //switch User state
                // increment sources count
                // add new Event
                break;
            case OFF:
                //switch User state
                // increment sources count
                // add new Event
                break;
            case RECORD:
                // record state (maybe to a map)
                // add new Record Event (if using intervals)
                break;
            case END:
                // do not process any more events
                // maybe remove all events from future?
                // end simulation
                break;

        }
    }

    private void updateSimTime(double eventTime){
        simTime = eventTime;
    }

}
