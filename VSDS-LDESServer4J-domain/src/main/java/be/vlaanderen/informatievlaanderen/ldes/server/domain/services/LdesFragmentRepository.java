package be.vlaanderen.informatievlaanderen.ldes.server.domain.services;

import org.json.simple.JSONObject;

public interface LdesFragmentRepository {

    JSONObject saveLdesFragment(JSONObject ldesFragment);
}
