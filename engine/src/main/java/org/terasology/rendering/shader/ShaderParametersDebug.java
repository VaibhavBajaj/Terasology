/*
 * Copyright 2016 MovingBlocks
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.terasology.rendering.shader;

import org.lwjgl.opengl.GL13;
import org.terasology.assets.ResourceUrn;
import org.terasology.config.Config;
import org.terasology.registry.CoreRegistry;
import org.terasology.rendering.assets.material.Material;
import org.terasology.rendering.cameras.Camera;
import org.terasology.rendering.dag.nodes.AmbientOcclusionPassesNode;
import org.terasology.rendering.dag.nodes.BloomPassesNode;
import org.terasology.rendering.dag.nodes.ChunksRefractiveReflectiveNode;
import org.terasology.rendering.dag.nodes.LightShaftsNode;
import org.terasology.rendering.dag.nodes.OutlineNode;
import org.terasology.rendering.dag.nodes.ShadowMapNode;
import org.terasology.rendering.dag.nodes.SkyBandsNode;
import org.terasology.rendering.opengl.DefaultDynamicFBOs;
import org.terasology.rendering.opengl.fbms.DynamicFBOsManager;
import org.terasology.rendering.opengl.fbms.ShadowMapResolutionDependentFBOs;
import org.terasology.rendering.world.WorldRenderer;

/**
 * Shader parameters for the Debug shader program.
 *
 */
public class ShaderParametersDebug extends ShaderParametersBase {

    @Override
    public void applyParameters(Material program) {
        super.applyParameters(program);

        Config config = CoreRegistry.get(Config.class);

        int texId = 0;

        // TODO: switch from CoreRegistry to Context
        DynamicFBOsManager dynamicFBOsManager = CoreRegistry.get(DynamicFBOsManager.class);
        ShadowMapResolutionDependentFBOs shadowMapResolutionDependentFBOs = CoreRegistry.get(ShadowMapResolutionDependentFBOs.class);


        // TODO: review - might have to go into a debug node
        final ResourceUrn sceneOpaqueUrn = DefaultDynamicFBOs.READ_ONLY_GBUFFER.getName();


        switch (config.getRendering().getDebug().getStage()) {
            case SHADOW_MAP:
                GL13.glActiveTexture(GL13.GL_TEXTURE0 + texId);
                shadowMapResolutionDependentFBOs.bindFboDepthTexture(ShadowMapNode.SHADOW_MAP);
                program.setInt("texDebug", texId++, true);
                break;
            case OPAQUE_COLOR:
                GL13.glActiveTexture(GL13.GL_TEXTURE0 + texId);
                dynamicFBOsManager.bindFboColorTexture(sceneOpaqueUrn);
                program.setInt("texDebug", texId++, true);
                break;
            case OPAQUE_NORMALS:
                GL13.glActiveTexture(GL13.GL_TEXTURE0 + texId);
                dynamicFBOsManager.bindFboNormalsTexture(sceneOpaqueUrn);
                program.setInt("texDebug", texId++, true);
                break;
            case OPAQUE_DEPTH:
                GL13.glActiveTexture(GL13.GL_TEXTURE0 + texId);
                dynamicFBOsManager.bindFboDepthTexture(sceneOpaqueUrn);
                program.setInt("texDebug", texId++, true);
                break;
            case OPAQUE_SUNLIGHT:
            case OPAQUE_LIGHT_BUFFER:
                GL13.glActiveTexture(GL13.GL_TEXTURE0 + texId);
                dynamicFBOsManager.bindFboLightBufferTexture(sceneOpaqueUrn);
                program.setInt("texDebug", texId++, true);
                break;
            case TRANSPARENT_COLOR:
                GL13.glActiveTexture(GL13.GL_TEXTURE0 + texId);
                dynamicFBOsManager.bindFboColorTexture(ChunksRefractiveReflectiveNode.REFRACTIVE_REFLECTIVE);
                program.setInt("texDebug", texId++, true);
                break;
            case SSAO:
                GL13.glActiveTexture(GL13.GL_TEXTURE0 + texId);
                dynamicFBOsManager.bindFboColorTexture(AmbientOcclusionPassesNode.SSAO);
                program.setInt("texDebug", texId++, true);
                break;
            case SOBEL:
                GL13.glActiveTexture(GL13.GL_TEXTURE0 + texId);
                dynamicFBOsManager.bindFboColorTexture(OutlineNode.OUTLINE);
                program.setInt("texDebug", texId++, true);
                break;
            case BAKED_OCCLUSION:
                GL13.glActiveTexture(GL13.GL_TEXTURE0 + texId);
                dynamicFBOsManager.bindFboColorTexture(sceneOpaqueUrn);
                program.setInt("texDebug", texId++, true);
                break;
            case RECONSTRUCTED_POSITION:
                Camera activeCamera = CoreRegistry.get(WorldRenderer.class).getActiveCamera();
                if (activeCamera != null) {
                    program.setMatrix4("invProjMatrix", activeCamera.getInverseProjectionMatrix(), true);
                }

                GL13.glActiveTexture(GL13.GL_TEXTURE0 + texId);
                dynamicFBOsManager.bindFboDepthTexture(sceneOpaqueUrn);
                program.setInt("texDebug", texId++, true);
                break;
            case BLOOM:
                GL13.glActiveTexture(GL13.GL_TEXTURE0 + texId);
                dynamicFBOsManager.bindFboColorTexture(BloomPassesNode.BLOOM_2);
                program.setInt("texDebug", texId++, true);
                break;
            case HIGH_PASS:
                GL13.glActiveTexture(GL13.GL_TEXTURE0 + texId);
                dynamicFBOsManager.bindFboColorTexture(BloomPassesNode.HIGH_PASS);
                program.setInt("texDebug", texId++, true);
                break;
            case SKY_BAND:
                GL13.glActiveTexture(GL13.GL_TEXTURE0 + texId);
                dynamicFBOsManager.bindFboColorTexture(SkyBandsNode.SKY_BAND_1);
                program.setInt("texDebug", texId++, true);
                break;
            case LIGHT_SHAFTS:
                GL13.glActiveTexture(GL13.GL_TEXTURE0 + texId);
                dynamicFBOsManager.bindFboColorTexture(LightShaftsNode.LIGHT_SHAFTS);
                program.setInt("texDebug", texId++, true);
                break;
            default:
                break;
        }

        program.setInt("debugRenderingStage", CoreRegistry.get(Config.class).getRendering().getDebug().getStage().getIndex());
    }
}
