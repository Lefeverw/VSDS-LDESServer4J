package be.vlaanderen.informatievlaanderen.ldes.server.snapshot.config;

import be.vlaanderen.informatievlaanderen.ldes.server.domain.viewcreation.valueobjects.ConfigProperties;
import be.vlaanderen.informatievlaanderen.ldes.server.fragmentation.FragmentationStrategy;
import be.vlaanderen.informatievlaanderen.ldes.server.fragmentation.FragmentationStrategyImpl;
import be.vlaanderen.informatievlaanderen.ldes.server.fragmentation.FragmentationStrategyWrapper;
import be.vlaanderen.informatievlaanderen.ldes.server.fragmentation.NonCriticalTasksExecutor;
import be.vlaanderen.informatievlaanderen.ldes.server.fragmentation.repository.AllocationRepository;
import be.vlaanderen.informatievlaanderen.ldes.server.fragmentation.repository.FragmentRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static be.vlaanderen.informatievlaanderen.ldes.server.domain.view.service.ViewServiceImpl.DEFAULT_VIEW_FRAGMENTATION_PROPERTIES;
import static be.vlaanderen.informatievlaanderen.ldes.server.domain.view.service.ViewServiceImpl.DEFAULT_VIEW_FRAGMENTATION_STRATEGY;

@Configuration
public class SnapshotConfig {

	@Bean
	@Qualifier("snapshot-fragmentation")
	public FragmentationStrategy snapshotFragmentationStrategy(ApplicationContext applicationContext,
			FragmentRepository fragmentRepository, AllocationRepository allocationRepository,
			NonCriticalTasksExecutor nonCriticalTasksExecutor, ApplicationEventPublisher eventPublisher) {
		FragmentationStrategyWrapper fragmentationStrategyWrapper = (FragmentationStrategyWrapper) applicationContext
				.getBean(DEFAULT_VIEW_FRAGMENTATION_STRATEGY);
		return fragmentationStrategyWrapper.wrapFragmentationStrategy(
				applicationContext,
				new FragmentationStrategyImpl(fragmentRepository, allocationRepository,
						nonCriticalTasksExecutor,
						eventPublisher),
				new ConfigProperties(DEFAULT_VIEW_FRAGMENTATION_PROPERTIES));

	}
}