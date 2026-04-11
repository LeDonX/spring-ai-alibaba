package org.ledon.springai.multiplatformandmodel;

/**
 * @author LeDon
 * @date 2026-04-09 23:16
 */
public class MultiPlatformAndModelOptions {
    private String platform;
    private String model;
    private Double temperature;

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }
}
