package be.vlaanderen.informatievlaanderen.ldes.server.fetchrest.treenode;

import be.vlaanderen.informatievlaanderen.ldes.server.domain.ldesfragmentrequest.valueobjects.FragmentPair;
import be.vlaanderen.informatievlaanderen.ldes.server.domain.ldesfragmentrequest.valueobjects.LdesFragmentRequest;
import be.vlaanderen.informatievlaanderen.ldes.server.domain.viewcreation.valueobjects.ViewName;
import be.vlaanderen.informatievlaanderen.ldes.server.fetchapplication.valueobjects.TreeNodeDto;
import be.vlaanderen.informatievlaanderen.ldes.server.fetchapplication.services.TreeNodeFetcher;
import be.vlaanderen.informatievlaanderen.ldes.server.fetchrest.caching.CachingStrategy;
import be.vlaanderen.informatievlaanderen.ldes.server.fetchrest.treenode.services.TreenodeUrlDecoder;
import be.vlaanderen.informatievlaanderen.ldes.server.fetchrest.config.RestConfig;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static org.springframework.http.HttpHeaders.CACHE_CONTROL;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;

@RestController
public class TreeNodeController implements OpenApiTreeNodeController {

	private final RestConfig restConfig;
	private final TreeNodeFetcher treeNodeFetcher;
	private final CachingStrategy cachingStrategy;

	public TreeNodeController(RestConfig restConfig, TreeNodeFetcher treeNodeFetcher, CachingStrategy cachingStrategy) {
		this.restConfig = restConfig;
		this.treeNodeFetcher = treeNodeFetcher;
		this.cachingStrategy = cachingStrategy;
	}

	@Override
	@CrossOrigin(origins = "*", allowedHeaders = "")
	@GetMapping(value = "{collectionname}/{view}")
	public ResponseEntity<TreeNodeDto> retrieveLdesFragment(HttpServletResponse response,
			@PathVariable("view") String view,
			@RequestParam Map<String, String> requestParameters,
			@RequestHeader(HttpHeaders.ACCEPT) String language,
			@PathVariable("collectionname") String collectionName) {
		final ViewName viewName = new ViewName(collectionName, view);
		TreeNodeDto treeNodeDto = returnRequestedTreeNode(response, viewName, requestParameters);
		setContentTypeHeader(language, response);
		response.setHeader(HttpHeaders.CONTENT_DISPOSITION, RestConfig.INLINE);
		return ResponseEntity
				.ok()
				.eTag(cachingStrategy.generateCacheIdentifier(treeNodeDto, language))
				.body(treeNodeDto);
	}

	private TreeNodeDto returnRequestedTreeNode(HttpServletResponse response, ViewName viewName,
			Map<String, String> fragmentationMap) {
		LdesFragmentRequest ldesFragmentRequest = new LdesFragmentRequest(viewName,
				fragmentationMap.entrySet()
						.stream().map(entry -> new FragmentPair(entry.getKey(),
								TreenodeUrlDecoder.decode(entry.getValue())))
						.toList());

		TreeNodeDto treeNodeDto = treeNodeFetcher.getFragment(ldesFragmentRequest);
		setCacheControlHeader(response, treeNodeDto);
		return treeNodeDto;

	}

	private void setCacheControlHeader(HttpServletResponse response, TreeNodeDto treeNodeDto) {
		if (treeNodeDto.isImmutable()) {
			response.setHeader(CACHE_CONTROL, restConfig.generateImmutableCacheControl());
		} else {
			response.setHeader(CACHE_CONTROL, restConfig.generateMutableCacheControl());
		}
	}

	private void setContentTypeHeader(String language, HttpServletResponse response) {
		if (language.equals(MediaType.ALL_VALUE) || language.contains(MediaType.TEXT_HTML_VALUE))
			response.setHeader(CONTENT_TYPE, RestConfig.TEXT_TURTLE);
		else
			response.setHeader(CONTENT_TYPE, language.split(",")[0]);
	}

}
