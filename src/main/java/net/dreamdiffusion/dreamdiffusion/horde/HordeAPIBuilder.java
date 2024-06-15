package net.dreamdiffusion.dreamdiffusion.horde;

import net.dreamdiffusion.dreamdiffusion.config.Language;
import net.dreamdiffusion.dreamdiffusion.config.Settings;
import org.bukkit.boss.BossBar;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Random;
import java.util.concurrent.TimeUnit;

public class HordeAPIBuilder {
    private static final String CLIENT_AGENT = "DreamDiffusionMinecraft-1.0.0-cozmyc";

    private static final int WIDTH = 1024;
    private static final int HEIGHT = 1024;

    private String prompt;
    private String model;
    private int steps;
    private String sampler;
    private double guidanceScale;
    private int clipSkip;
    private boolean karras;

    private final HttpClient httpClient;
    public static boolean stopFlag;

    public HordeAPIBuilder() {
        this.httpClient = HttpClient.newHttpClient();
        stopFlag = false;
    }

    public HordeAPIBuilder setPrompt(String prompt) {
        this.prompt = prompt;
        return this;
    }

    public HordeAPIBuilder setModel(String model) {
        this.model = model;
        return this;
    }

    public HordeAPIBuilder setSteps(Integer steps) {
        this.steps = steps;
        return this;
    }

    public HordeAPIBuilder setSampler(String sampler) {
        this.sampler = sampler;
        return this;
    }

    public HordeAPIBuilder setGuidanceScale(Double guidanceScale) {
        this.guidanceScale = guidanceScale;
        return this;
    }

    public HordeAPIBuilder setClipSkip(Integer clipSkip) {
        this.clipSkip = clipSkip;
        return this;
    }

    public HordeAPIBuilder setKarras(Boolean karras) {
        this.karras = karras;
        return this;
    }

    public String generateImageUrl(BossBar bossBar) throws Exception {
        String taskId = queueGeneration(makePayload());
        return checkStatus(taskId, bossBar);
    }

    private JSONObject makeHeaders() {
        JSONObject headers = new JSONObject();
        headers.put("Content-Type", "application/json");
        headers.put("Client-Agent", CLIENT_AGENT);
        headers.put("apikey", Settings.hordeApiKey);
        return headers;
    }

    private JSONObject makeRequest(String url, JSONObject payload, JSONObject headers) throws Exception {
        HttpRequest.Builder requestBuilder = HttpRequest.newBuilder()
                .uri(URI.create(url));

        headers.keySet().forEach(key -> requestBuilder.header(key, headers.getString(key)));

        if (payload == null) {
            requestBuilder.GET();
        } else {
            requestBuilder.POST(HttpRequest.BodyPublishers.ofString(payload.toString()));
        }

        HttpRequest request = requestBuilder.build();
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        return new JSONObject(response.body());
    }

    private String generateSeed() {
        return String.valueOf(new Random().nextInt(1_000_000_000));
    }

    private boolean isFinished(JSONObject response) {
        return response.getInt("finished") == 1;
    }

    private String getImage(JSONObject response) {
        return response.getJSONArray("generations").getJSONObject(0).getString("img");
    }

    private String queueGeneration(JSONObject payload) throws Exception {
        JSONObject respJson = makeRequest(Settings.hordeUrl + "/v2/generate/async", payload, makeHeaders());
        // Bukkit.getLogger().info(respJson.toString()); // debug
        return respJson.getString("id");
    }

    private String checkStatus(String taskId, BossBar bossBar) throws Exception {
        while (!stopFlag) {
            JSONObject status = makeRequest(Settings.hordeUrl + "/v2/generate/check/" + taskId, null, makeHeaders());
            // Bukkit.getLogger().info(status.toString()); // debug
            if (isFinished(status)) {
                break;
            }
            int queuePosition = status.getInt("queue_position");
            if (queuePosition == 0) {
                bossBar.setTitle("§3" + Language.barGenerating);
                bossBar.setProgress(1.0);
            } else {
                bossBar.setTitle("§3" + Language.barQueuePosition + ": §b" +  queuePosition);
                bossBar.setProgress(1.0 - (queuePosition / 100.0));
            }

            TimeUnit.SECONDS.sleep(1);
        }
        if (stopFlag) {
            return "";
        }

        JSONObject finalStatus = makeRequest(Settings.hordeUrl + "/v2/generate/status/" + taskId, null, makeHeaders());
        // Bukkit.getLogger().info(finalStatus.toString()); // debug

        return getImage(finalStatus);
    }

    private JSONObject makePayload() {
        JSONObject payload = new JSONObject();
        payload.put("prompt", prompt);

        JSONObject params = new JSONObject();
        params.put("sampler_name", sampler);
        params.put("toggles", new JSONArray().put(1).put(4));
        params.put("cfg_scale", guidanceScale);
        params.put("seed", generateSeed());
        params.put("height", HEIGHT);
        params.put("width", WIDTH);
        params.put("karras", karras);
        params.put("steps", steps);
        params.put("tiling", false);
        params.put("n", 1);
        params.put("clip_skip", clipSkip);

        payload.put("params", params);
        payload.put("nsfw", true);
        payload.put("trusted_workers", false);
        payload.put("slow_workers", true);
        payload.put("censor_nsfw", false);
        payload.put("workers", new JSONArray());
        payload.put("worker_blacklist", false);
        payload.put("models", new JSONArray().put(model));
        payload.put("r2", true);
        payload.put("shared", true);

        return payload;
    }
}

