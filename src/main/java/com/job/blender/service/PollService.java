package com.job.blender.service;

import com.grack.nanojson.JsonParserException;

import java.io.IOException;

public interface PollService {

    void poll() throws JsonParserException, IOException;
}
