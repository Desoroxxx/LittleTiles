package team.creative.littletiles.client.render.cache.pipeline;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL15C;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.MemoryTracker;

import net.minecraft.client.renderer.RenderType;
import team.creative.littletiles.client.render.cache.BlockBufferCache;
import team.creative.littletiles.client.render.mc.RebuildTaskExtender;
import team.creative.littletiles.client.render.mc.RenderChunkExtender;
import team.creative.littletiles.client.render.mc.VertexBufferExtender;
import team.creative.littletiles.common.block.entity.BETiles;

public abstract class LittleRenderPipelineType {
    
    private static final List<LittleRenderPipelineType> TYPES = new ArrayList<>();
    public static final LittleRenderPipelineTypeForge FORGE = new LittleRenderPipelineTypeForge();
    
    public static int typeCount() {
        return TYPES.size();
    }
    
    public static LittleRenderPipelineType get(int id) {
        return TYPES.get(id);
    }
    
    public static void startCompile(RenderChunkExtender chunk, RebuildTaskExtender task) {
        chunk.startBuilding(task);
    }
    
    public static void compile(RenderChunkExtender chunk, BETiles be, RebuildTaskExtender rebuildTask) {
        be.updateQuadCache(chunk);
        
        BlockBufferCache cache = be.render.getBufferCache();
        for (RenderType layer : RenderType.chunkBufferLayers()) {
            synchronized (cache) {
                if (!cache.has(layer))
                    continue;
                cache.setUploaded(layer, rebuildTask.upload(layer, cache));
            }
        }
    }
    
    public static void endCompile(RenderChunkExtender chunk, RebuildTaskExtender task) {
        chunk.endBuilding(task);
        task.clear();
    }
    
    public final Supplier<LittleRenderPipeline> factory;
    public final int id;
    
    public abstract boolean canBeUploadedDirectly();
    
    public abstract ByteBuffer downloadUploadedData(VertexBufferExtender buffer, long offset, int size);
    
    protected LittleRenderPipelineType(Supplier<LittleRenderPipeline> factory) {
        this.factory = factory;
        id = TYPES.size();
        TYPES.add(this);
    }
    
    public static class LittleRenderPipelineTypeForge extends LittleRenderPipelineType {
        
        private LittleRenderPipelineTypeForge() {
            super(LittleRenderPipelineForge::new);
        }
        
        @Override
        public boolean canBeUploadedDirectly() {
            return true;
        }
        
        @Override
        public ByteBuffer downloadUploadedData(VertexBufferExtender buffer, long offset, int size) {
            GlStateManager._glBindBuffer(GL15.GL_ARRAY_BUFFER, buffer.getVertexBufferId());
            try {
                ByteBuffer result = MemoryTracker.create(size);
                GL15C.glGetBufferSubData(GL15.GL_ARRAY_BUFFER, offset, result);
                return result;
            } catch (IllegalArgumentException | IllegalStateException e) {
                if (!(e instanceof IllegalStateException))
                    e.printStackTrace();
                return null;
            } finally {
                
            }
        }
        
    }
    
}
