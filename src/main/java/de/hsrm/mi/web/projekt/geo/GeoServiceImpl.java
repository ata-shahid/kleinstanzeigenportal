package de.hsrm.mi.web.projekt.geo;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class GeoServiceImpl implements GeoService {

    // Logger
    private static final Logger logger = LoggerFactory.getLogger(GeoServiceImpl.class);

    // Address types that should be filtered out
    private static final Set<String> IGNORIERTE_TYPEN = Set.of("country", "state", "region", "postcode");

    private final RestClient restClient;

    public GeoServiceImpl(RestClient.Builder restClientBuilder) {
        this.restClient = restClientBuilder
                .baseUrl("https://nominatim.openstreetmap.org")
                .defaultHeader("User-Agent", "2026web-praktikum/1.0")
                .build();
    }

    @Override
    public List<GeoAdresse> findeAdressen(String such) {
        logger.info("findeAdressen({})", such);

        if (such == null) {
            logger.error("findeAdressen() - such ist null, gebe leere Liste zurück");
            return Collections.emptyList();
        }

        List<GeoAdresse> alleAdressen = restClient.get()
                .uri("/search?format=json&countrycodes=de&q={q}", such)
                .retrieve()
                .body(new ParameterizedTypeReference<List<GeoAdresse>>() {});

        if (alleAdressen == null) {
            logger.warn("findeAdressen({}) - Nominatim lieferte null zurück", such);
            return Collections.emptyList();
        }

        // Filter out entries
        List<GeoAdresse> gefilterteAdressen = alleAdressen.stream()
                .filter(a -> a.addresstype() == null || !IGNORIERTE_TYPEN.contains(a.addresstype()))
                .toList();

        logger.info("findeAdressen({}) -> {} Ergebnis(se) (von {} gesamt, nach Filterung)",
                such, gefilterteAdressen.size(), alleAdressen.size());

        return gefilterteAdressen;
    }
}
