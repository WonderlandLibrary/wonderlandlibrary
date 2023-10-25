/*
 * Decompiled with CFR 0.150.
 */
package org.lwjgl.opengl;

import java.util.HashSet;
import java.util.Set;
import org.lwjgl.LWJGLException;
import org.lwjgl.LWJGLUtil;
import org.lwjgl.opengl.APIUtil;
import org.lwjgl.opengl.GLContext;
import org.lwjgl.opengl.StateTracker;

/*
 * This class specifies class file version 49.0 but uses Java 6 signatures.  Assumed Java 6.
 */
public class ContextCapabilities {
    static final boolean DEBUG = false;
    final APIUtil util = new APIUtil();
    final StateTracker tracker = new StateTracker();
    public final boolean GL_AMD_blend_minmax_factor;
    public final boolean GL_AMD_conservative_depth;
    public final boolean GL_AMD_debug_output;
    public final boolean GL_AMD_depth_clamp_separate;
    public final boolean GL_AMD_draw_buffers_blend;
    public final boolean GL_AMD_interleaved_elements;
    public final boolean GL_AMD_multi_draw_indirect;
    public final boolean GL_AMD_name_gen_delete;
    public final boolean GL_AMD_performance_monitor;
    public final boolean GL_AMD_pinned_memory;
    public final boolean GL_AMD_query_buffer_object;
    public final boolean GL_AMD_sample_positions;
    public final boolean GL_AMD_seamless_cubemap_per_texture;
    public final boolean GL_AMD_shader_atomic_counter_ops;
    public final boolean GL_AMD_shader_stencil_export;
    public final boolean GL_AMD_shader_trinary_minmax;
    public final boolean GL_AMD_sparse_texture;
    public final boolean GL_AMD_stencil_operation_extended;
    public final boolean GL_AMD_texture_texture4;
    public final boolean GL_AMD_transform_feedback3_lines_triangles;
    public final boolean GL_AMD_vertex_shader_layer;
    public final boolean GL_AMD_vertex_shader_tessellator;
    public final boolean GL_AMD_vertex_shader_viewport_index;
    public final boolean GL_APPLE_aux_depth_stencil;
    public final boolean GL_APPLE_client_storage;
    public final boolean GL_APPLE_element_array;
    public final boolean GL_APPLE_fence;
    public final boolean GL_APPLE_float_pixels;
    public final boolean GL_APPLE_flush_buffer_range;
    public final boolean GL_APPLE_object_purgeable;
    public final boolean GL_APPLE_packed_pixels;
    public final boolean GL_APPLE_rgb_422;
    public final boolean GL_APPLE_row_bytes;
    public final boolean GL_APPLE_texture_range;
    public final boolean GL_APPLE_vertex_array_object;
    public final boolean GL_APPLE_vertex_array_range;
    public final boolean GL_APPLE_vertex_program_evaluators;
    public final boolean GL_APPLE_ycbcr_422;
    public final boolean GL_ARB_ES2_compatibility;
    public final boolean GL_ARB_ES3_1_compatibility;
    public final boolean GL_ARB_ES3_compatibility;
    public final boolean GL_ARB_arrays_of_arrays;
    public final boolean GL_ARB_base_instance;
    public final boolean GL_ARB_bindless_texture;
    public final boolean GL_ARB_blend_func_extended;
    public final boolean GL_ARB_buffer_storage;
    public final boolean GL_ARB_cl_event;
    public final boolean GL_ARB_clear_buffer_object;
    public final boolean GL_ARB_clear_texture;
    public final boolean GL_ARB_clip_control;
    public final boolean GL_ARB_color_buffer_float;
    public final boolean GL_ARB_compatibility;
    public final boolean GL_ARB_compressed_texture_pixel_storage;
    public final boolean GL_ARB_compute_shader;
    public final boolean GL_ARB_compute_variable_group_size;
    public final boolean GL_ARB_conditional_render_inverted;
    public final boolean GL_ARB_conservative_depth;
    public final boolean GL_ARB_copy_buffer;
    public final boolean GL_ARB_copy_image;
    public final boolean GL_ARB_cull_distance;
    public final boolean GL_ARB_debug_output;
    public final boolean GL_ARB_depth_buffer_float;
    public final boolean GL_ARB_depth_clamp;
    public final boolean GL_ARB_depth_texture;
    public final boolean GL_ARB_derivative_control;
    public final boolean GL_ARB_direct_state_access;
    public final boolean GL_ARB_draw_buffers;
    public final boolean GL_ARB_draw_buffers_blend;
    public final boolean GL_ARB_draw_elements_base_vertex;
    public final boolean GL_ARB_draw_indirect;
    public final boolean GL_ARB_draw_instanced;
    public final boolean GL_ARB_enhanced_layouts;
    public final boolean GL_ARB_explicit_attrib_location;
    public final boolean GL_ARB_explicit_uniform_location;
    public final boolean GL_ARB_fragment_coord_conventions;
    public final boolean GL_ARB_fragment_layer_viewport;
    public final boolean GL_ARB_fragment_program;
    public final boolean GL_ARB_fragment_program_shadow;
    public final boolean GL_ARB_fragment_shader;
    public final boolean GL_ARB_framebuffer_no_attachments;
    public final boolean GL_ARB_framebuffer_object;
    public final boolean GL_ARB_framebuffer_sRGB;
    public final boolean GL_ARB_geometry_shader4;
    public final boolean GL_ARB_get_program_binary;
    public final boolean GL_ARB_get_texture_sub_image;
    public final boolean GL_ARB_gpu_shader5;
    public final boolean GL_ARB_gpu_shader_fp64;
    public final boolean GL_ARB_half_float_pixel;
    public final boolean GL_ARB_half_float_vertex;
    public final boolean GL_ARB_imaging;
    public final boolean GL_ARB_indirect_parameters;
    public final boolean GL_ARB_instanced_arrays;
    public final boolean GL_ARB_internalformat_query;
    public final boolean GL_ARB_internalformat_query2;
    public final boolean GL_ARB_invalidate_subdata;
    public final boolean GL_ARB_map_buffer_alignment;
    public final boolean GL_ARB_map_buffer_range;
    public final boolean GL_ARB_matrix_palette;
    public final boolean GL_ARB_multi_bind;
    public final boolean GL_ARB_multi_draw_indirect;
    public final boolean GL_ARB_multisample;
    public final boolean GL_ARB_multitexture;
    public final boolean GL_ARB_occlusion_query;
    public final boolean GL_ARB_occlusion_query2;
    public final boolean GL_ARB_pipeline_statistics_query;
    public final boolean GL_ARB_pixel_buffer_object;
    public final boolean GL_ARB_point_parameters;
    public final boolean GL_ARB_point_sprite;
    public final boolean GL_ARB_program_interface_query;
    public final boolean GL_ARB_provoking_vertex;
    public final boolean GL_ARB_query_buffer_object;
    public final boolean GL_ARB_robust_buffer_access_behavior;
    public final boolean GL_ARB_robustness;
    public final boolean GL_ARB_robustness_isolation;
    public final boolean GL_ARB_sample_shading;
    public final boolean GL_ARB_sampler_objects;
    public final boolean GL_ARB_seamless_cube_map;
    public final boolean GL_ARB_seamless_cubemap_per_texture;
    public final boolean GL_ARB_separate_shader_objects;
    public final boolean GL_ARB_shader_atomic_counters;
    public final boolean GL_ARB_shader_bit_encoding;
    public final boolean GL_ARB_shader_draw_parameters;
    public final boolean GL_ARB_shader_group_vote;
    public final boolean GL_ARB_shader_image_load_store;
    public final boolean GL_ARB_shader_image_size;
    public final boolean GL_ARB_shader_objects;
    public final boolean GL_ARB_shader_precision;
    public final boolean GL_ARB_shader_stencil_export;
    public final boolean GL_ARB_shader_storage_buffer_object;
    public final boolean GL_ARB_shader_subroutine;
    public final boolean GL_ARB_shader_texture_image_samples;
    public final boolean GL_ARB_shader_texture_lod;
    public final boolean GL_ARB_shading_language_100;
    public final boolean GL_ARB_shading_language_420pack;
    public final boolean GL_ARB_shading_language_include;
    public final boolean GL_ARB_shading_language_packing;
    public final boolean GL_ARB_shadow;
    public final boolean GL_ARB_shadow_ambient;
    public final boolean GL_ARB_sparse_buffer;
    public final boolean GL_ARB_sparse_texture;
    public final boolean GL_ARB_stencil_texturing;
    public final boolean GL_ARB_sync;
    public final boolean GL_ARB_tessellation_shader;
    public final boolean GL_ARB_texture_barrier;
    public final boolean GL_ARB_texture_border_clamp;
    public final boolean GL_ARB_texture_buffer_object;
    public final boolean GL_ARB_texture_buffer_object_rgb32;
    public final boolean GL_ARB_texture_buffer_range;
    public final boolean GL_ARB_texture_compression;
    public final boolean GL_ARB_texture_compression_bptc;
    public final boolean GL_ARB_texture_compression_rgtc;
    public final boolean GL_ARB_texture_cube_map;
    public final boolean GL_ARB_texture_cube_map_array;
    public final boolean GL_ARB_texture_env_add;
    public final boolean GL_ARB_texture_env_combine;
    public final boolean GL_ARB_texture_env_crossbar;
    public final boolean GL_ARB_texture_env_dot3;
    public final boolean GL_ARB_texture_float;
    public final boolean GL_ARB_texture_gather;
    public final boolean GL_ARB_texture_mirror_clamp_to_edge;
    public final boolean GL_ARB_texture_mirrored_repeat;
    public final boolean GL_ARB_texture_multisample;
    public final boolean GL_ARB_texture_non_power_of_two;
    public final boolean GL_ARB_texture_query_levels;
    public final boolean GL_ARB_texture_query_lod;
    public final boolean GL_ARB_texture_rectangle;
    public final boolean GL_ARB_texture_rg;
    public final boolean GL_ARB_texture_rgb10_a2ui;
    public final boolean GL_ARB_texture_stencil8;
    public final boolean GL_ARB_texture_storage;
    public final boolean GL_ARB_texture_storage_multisample;
    public final boolean GL_ARB_texture_swizzle;
    public final boolean GL_ARB_texture_view;
    public final boolean GL_ARB_timer_query;
    public final boolean GL_ARB_transform_feedback2;
    public final boolean GL_ARB_transform_feedback3;
    public final boolean GL_ARB_transform_feedback_instanced;
    public final boolean GL_ARB_transform_feedback_overflow_query;
    public final boolean GL_ARB_transpose_matrix;
    public final boolean GL_ARB_uniform_buffer_object;
    public final boolean GL_ARB_vertex_array_bgra;
    public final boolean GL_ARB_vertex_array_object;
    public final boolean GL_ARB_vertex_attrib_64bit;
    public final boolean GL_ARB_vertex_attrib_binding;
    public final boolean GL_ARB_vertex_blend;
    public final boolean GL_ARB_vertex_buffer_object;
    public final boolean GL_ARB_vertex_program;
    public final boolean GL_ARB_vertex_shader;
    public final boolean GL_ARB_vertex_type_10f_11f_11f_rev;
    public final boolean GL_ARB_vertex_type_2_10_10_10_rev;
    public final boolean GL_ARB_viewport_array;
    public final boolean GL_ARB_window_pos;
    public final boolean GL_ATI_draw_buffers;
    public final boolean GL_ATI_element_array;
    public final boolean GL_ATI_envmap_bumpmap;
    public final boolean GL_ATI_fragment_shader;
    public final boolean GL_ATI_map_object_buffer;
    public final boolean GL_ATI_meminfo;
    public final boolean GL_ATI_pn_triangles;
    public final boolean GL_ATI_separate_stencil;
    public final boolean GL_ATI_shader_texture_lod;
    public final boolean GL_ATI_text_fragment_shader;
    public final boolean GL_ATI_texture_compression_3dc;
    public final boolean GL_ATI_texture_env_combine3;
    public final boolean GL_ATI_texture_float;
    public final boolean GL_ATI_texture_mirror_once;
    public final boolean GL_ATI_vertex_array_object;
    public final boolean GL_ATI_vertex_attrib_array_object;
    public final boolean GL_ATI_vertex_streams;
    public final boolean GL_EXT_Cg_shader;
    public final boolean GL_EXT_abgr;
    public final boolean GL_EXT_bgra;
    public final boolean GL_EXT_bindable_uniform;
    public final boolean GL_EXT_blend_color;
    public final boolean GL_EXT_blend_equation_separate;
    public final boolean GL_EXT_blend_func_separate;
    public final boolean GL_EXT_blend_minmax;
    public final boolean GL_EXT_blend_subtract;
    public final boolean GL_EXT_compiled_vertex_array;
    public final boolean GL_EXT_depth_bounds_test;
    public final boolean GL_EXT_direct_state_access;
    public final boolean GL_EXT_draw_buffers2;
    public final boolean GL_EXT_draw_instanced;
    public final boolean GL_EXT_draw_range_elements;
    public final boolean GL_EXT_fog_coord;
    public final boolean GL_EXT_framebuffer_blit;
    public final boolean GL_EXT_framebuffer_multisample;
    public final boolean GL_EXT_framebuffer_multisample_blit_scaled;
    public final boolean GL_EXT_framebuffer_object;
    public final boolean GL_EXT_framebuffer_sRGB;
    public final boolean GL_EXT_geometry_shader4;
    public final boolean GL_EXT_gpu_program_parameters;
    public final boolean GL_EXT_gpu_shader4;
    public final boolean GL_EXT_multi_draw_arrays;
    public final boolean GL_EXT_packed_depth_stencil;
    public final boolean GL_EXT_packed_float;
    public final boolean GL_EXT_packed_pixels;
    public final boolean GL_EXT_paletted_texture;
    public final boolean GL_EXT_pixel_buffer_object;
    public final boolean GL_EXT_point_parameters;
    public final boolean GL_EXT_provoking_vertex;
    public final boolean GL_EXT_rescale_normal;
    public final boolean GL_EXT_secondary_color;
    public final boolean GL_EXT_separate_shader_objects;
    public final boolean GL_EXT_separate_specular_color;
    public final boolean GL_EXT_shader_image_load_store;
    public final boolean GL_EXT_shadow_funcs;
    public final boolean GL_EXT_shared_texture_palette;
    public final boolean GL_EXT_stencil_clear_tag;
    public final boolean GL_EXT_stencil_two_side;
    public final boolean GL_EXT_stencil_wrap;
    public final boolean GL_EXT_texture_3d;
    public final boolean GL_EXT_texture_array;
    public final boolean GL_EXT_texture_buffer_object;
    public final boolean GL_EXT_texture_compression_latc;
    public final boolean GL_EXT_texture_compression_rgtc;
    public final boolean GL_EXT_texture_compression_s3tc;
    public final boolean GL_EXT_texture_env_combine;
    public final boolean GL_EXT_texture_env_dot3;
    public final boolean GL_EXT_texture_filter_anisotropic;
    public final boolean GL_EXT_texture_integer;
    public final boolean GL_EXT_texture_lod_bias;
    public final boolean GL_EXT_texture_mirror_clamp;
    public final boolean GL_EXT_texture_rectangle;
    public final boolean GL_EXT_texture_sRGB;
    public final boolean GL_EXT_texture_sRGB_decode;
    public final boolean GL_EXT_texture_shared_exponent;
    public final boolean GL_EXT_texture_snorm;
    public final boolean GL_EXT_texture_swizzle;
    public final boolean GL_EXT_timer_query;
    public final boolean GL_EXT_transform_feedback;
    public final boolean GL_EXT_vertex_array_bgra;
    public final boolean GL_EXT_vertex_attrib_64bit;
    public final boolean GL_EXT_vertex_shader;
    public final boolean GL_EXT_vertex_weighting;
    public final boolean OpenGL11;
    public final boolean OpenGL12;
    public final boolean OpenGL13;
    public final boolean OpenGL14;
    public final boolean OpenGL15;
    public final boolean OpenGL20;
    public final boolean OpenGL21;
    public final boolean OpenGL30;
    public final boolean OpenGL31;
    public final boolean OpenGL32;
    public final boolean OpenGL33;
    public final boolean OpenGL40;
    public final boolean OpenGL41;
    public final boolean OpenGL42;
    public final boolean OpenGL43;
    public final boolean OpenGL44;
    public final boolean OpenGL45;
    public final boolean GL_GREMEDY_frame_terminator;
    public final boolean GL_GREMEDY_string_marker;
    public final boolean GL_HP_occlusion_test;
    public final boolean GL_IBM_rasterpos_clip;
    public final boolean GL_INTEL_map_texture;
    public final boolean GL_KHR_context_flush_control;
    public final boolean GL_KHR_debug;
    public final boolean GL_KHR_robust_buffer_access_behavior;
    public final boolean GL_KHR_robustness;
    public final boolean GL_KHR_texture_compression_astc_ldr;
    public final boolean GL_NVX_gpu_memory_info;
    public final boolean GL_NV_bindless_multi_draw_indirect;
    public final boolean GL_NV_bindless_texture;
    public final boolean GL_NV_blend_equation_advanced;
    public final boolean GL_NV_blend_square;
    public final boolean GL_NV_compute_program5;
    public final boolean GL_NV_conditional_render;
    public final boolean GL_NV_copy_depth_to_color;
    public final boolean GL_NV_copy_image;
    public final boolean GL_NV_deep_texture3D;
    public final boolean GL_NV_depth_buffer_float;
    public final boolean GL_NV_depth_clamp;
    public final boolean GL_NV_draw_texture;
    public final boolean GL_NV_evaluators;
    public final boolean GL_NV_explicit_multisample;
    public final boolean GL_NV_fence;
    public final boolean GL_NV_float_buffer;
    public final boolean GL_NV_fog_distance;
    public final boolean GL_NV_fragment_program;
    public final boolean GL_NV_fragment_program2;
    public final boolean GL_NV_fragment_program4;
    public final boolean GL_NV_fragment_program_option;
    public final boolean GL_NV_framebuffer_multisample_coverage;
    public final boolean GL_NV_geometry_program4;
    public final boolean GL_NV_geometry_shader4;
    public final boolean GL_NV_gpu_program4;
    public final boolean GL_NV_gpu_program5;
    public final boolean GL_NV_gpu_program5_mem_extended;
    public final boolean GL_NV_gpu_shader5;
    public final boolean GL_NV_half_float;
    public final boolean GL_NV_light_max_exponent;
    public final boolean GL_NV_multisample_coverage;
    public final boolean GL_NV_multisample_filter_hint;
    public final boolean GL_NV_occlusion_query;
    public final boolean GL_NV_packed_depth_stencil;
    public final boolean GL_NV_parameter_buffer_object;
    public final boolean GL_NV_parameter_buffer_object2;
    public final boolean GL_NV_path_rendering;
    public final boolean GL_NV_pixel_data_range;
    public final boolean GL_NV_point_sprite;
    public final boolean GL_NV_present_video;
    public final boolean GL_NV_primitive_restart;
    public final boolean GL_NV_register_combiners;
    public final boolean GL_NV_register_combiners2;
    public final boolean GL_NV_shader_atomic_counters;
    public final boolean GL_NV_shader_atomic_float;
    public final boolean GL_NV_shader_buffer_load;
    public final boolean GL_NV_shader_buffer_store;
    public final boolean GL_NV_shader_storage_buffer_object;
    public final boolean GL_NV_tessellation_program5;
    public final boolean GL_NV_texgen_reflection;
    public final boolean GL_NV_texture_barrier;
    public final boolean GL_NV_texture_compression_vtc;
    public final boolean GL_NV_texture_env_combine4;
    public final boolean GL_NV_texture_expand_normal;
    public final boolean GL_NV_texture_multisample;
    public final boolean GL_NV_texture_rectangle;
    public final boolean GL_NV_texture_shader;
    public final boolean GL_NV_texture_shader2;
    public final boolean GL_NV_texture_shader3;
    public final boolean GL_NV_transform_feedback;
    public final boolean GL_NV_transform_feedback2;
    public final boolean GL_NV_vertex_array_range;
    public final boolean GL_NV_vertex_array_range2;
    public final boolean GL_NV_vertex_attrib_integer_64bit;
    public final boolean GL_NV_vertex_buffer_unified_memory;
    public final boolean GL_NV_vertex_program;
    public final boolean GL_NV_vertex_program1_1;
    public final boolean GL_NV_vertex_program2;
    public final boolean GL_NV_vertex_program2_option;
    public final boolean GL_NV_vertex_program3;
    public final boolean GL_NV_vertex_program4;
    public final boolean GL_NV_video_capture;
    public final boolean GL_SGIS_generate_mipmap;
    public final boolean GL_SGIS_texture_lod;
    public final boolean GL_SUN_slice_accum;
    long glDebugMessageEnableAMD;
    long glDebugMessageInsertAMD;
    long glDebugMessageCallbackAMD;
    long glGetDebugMessageLogAMD;
    long glBlendFuncIndexedAMD;
    long glBlendFuncSeparateIndexedAMD;
    long glBlendEquationIndexedAMD;
    long glBlendEquationSeparateIndexedAMD;
    long glVertexAttribParameteriAMD;
    long glMultiDrawArraysIndirectAMD;
    long glMultiDrawElementsIndirectAMD;
    long glGenNamesAMD;
    long glDeleteNamesAMD;
    long glIsNameAMD;
    long glGetPerfMonitorGroupsAMD;
    long glGetPerfMonitorCountersAMD;
    long glGetPerfMonitorGroupStringAMD;
    long glGetPerfMonitorCounterStringAMD;
    long glGetPerfMonitorCounterInfoAMD;
    long glGenPerfMonitorsAMD;
    long glDeletePerfMonitorsAMD;
    long glSelectPerfMonitorCountersAMD;
    long glBeginPerfMonitorAMD;
    long glEndPerfMonitorAMD;
    long glGetPerfMonitorCounterDataAMD;
    long glSetMultisamplefvAMD;
    long glTexStorageSparseAMD;
    long glTextureStorageSparseAMD;
    long glStencilOpValueAMD;
    long glTessellationFactorAMD;
    long glTessellationModeAMD;
    long glElementPointerAPPLE;
    long glDrawElementArrayAPPLE;
    long glDrawRangeElementArrayAPPLE;
    long glMultiDrawElementArrayAPPLE;
    long glMultiDrawRangeElementArrayAPPLE;
    long glGenFencesAPPLE;
    long glDeleteFencesAPPLE;
    long glSetFenceAPPLE;
    long glIsFenceAPPLE;
    long glTestFenceAPPLE;
    long glFinishFenceAPPLE;
    long glTestObjectAPPLE;
    long glFinishObjectAPPLE;
    long glBufferParameteriAPPLE;
    long glFlushMappedBufferRangeAPPLE;
    long glObjectPurgeableAPPLE;
    long glObjectUnpurgeableAPPLE;
    long glGetObjectParameterivAPPLE;
    long glTextureRangeAPPLE;
    long glGetTexParameterPointervAPPLE;
    long glBindVertexArrayAPPLE;
    long glDeleteVertexArraysAPPLE;
    long glGenVertexArraysAPPLE;
    long glIsVertexArrayAPPLE;
    long glVertexArrayRangeAPPLE;
    long glFlushVertexArrayRangeAPPLE;
    long glVertexArrayParameteriAPPLE;
    long glEnableVertexAttribAPPLE;
    long glDisableVertexAttribAPPLE;
    long glIsVertexAttribEnabledAPPLE;
    long glMapVertexAttrib1dAPPLE;
    long glMapVertexAttrib1fAPPLE;
    long glMapVertexAttrib2dAPPLE;
    long glMapVertexAttrib2fAPPLE;
    long glGetTextureHandleARB;
    long glGetTextureSamplerHandleARB;
    long glMakeTextureHandleResidentARB;
    long glMakeTextureHandleNonResidentARB;
    long glGetImageHandleARB;
    long glMakeImageHandleResidentARB;
    long glMakeImageHandleNonResidentARB;
    long glUniformHandleui64ARB;
    long glUniformHandleui64vARB;
    long glProgramUniformHandleui64ARB;
    long glProgramUniformHandleui64vARB;
    long glIsTextureHandleResidentARB;
    long glIsImageHandleResidentARB;
    long glVertexAttribL1ui64ARB;
    long glVertexAttribL1ui64vARB;
    long glGetVertexAttribLui64vARB;
    long glBindBufferARB;
    long glDeleteBuffersARB;
    long glGenBuffersARB;
    long glIsBufferARB;
    long glBufferDataARB;
    long glBufferSubDataARB;
    long glGetBufferSubDataARB;
    long glMapBufferARB;
    long glUnmapBufferARB;
    long glGetBufferParameterivARB;
    long glGetBufferPointervARB;
    long glNamedBufferStorageEXT;
    long glCreateSyncFromCLeventARB;
    long glClearNamedBufferDataEXT;
    long glClearNamedBufferSubDataEXT;
    long glClampColorARB;
    long glDispatchComputeGroupSizeARB;
    long glDebugMessageControlARB;
    long glDebugMessageInsertARB;
    long glDebugMessageCallbackARB;
    long glGetDebugMessageLogARB;
    long glDrawBuffersARB;
    long glBlendEquationiARB;
    long glBlendEquationSeparateiARB;
    long glBlendFunciARB;
    long glBlendFuncSeparateiARB;
    long glDrawArraysInstancedARB;
    long glDrawElementsInstancedARB;
    long glNamedFramebufferParameteriEXT;
    long glGetNamedFramebufferParameterivEXT;
    long glProgramParameteriARB;
    long glFramebufferTextureARB;
    long glFramebufferTextureLayerARB;
    long glFramebufferTextureFaceARB;
    long glProgramUniform1dEXT;
    long glProgramUniform2dEXT;
    long glProgramUniform3dEXT;
    long glProgramUniform4dEXT;
    long glProgramUniform1dvEXT;
    long glProgramUniform2dvEXT;
    long glProgramUniform3dvEXT;
    long glProgramUniform4dvEXT;
    long glProgramUniformMatrix2dvEXT;
    long glProgramUniformMatrix3dvEXT;
    long glProgramUniformMatrix4dvEXT;
    long glProgramUniformMatrix2x3dvEXT;
    long glProgramUniformMatrix2x4dvEXT;
    long glProgramUniformMatrix3x2dvEXT;
    long glProgramUniformMatrix3x4dvEXT;
    long glProgramUniformMatrix4x2dvEXT;
    long glProgramUniformMatrix4x3dvEXT;
    long glColorTable;
    long glColorSubTable;
    long glColorTableParameteriv;
    long glColorTableParameterfv;
    long glCopyColorSubTable;
    long glCopyColorTable;
    long glGetColorTable;
    long glGetColorTableParameteriv;
    long glGetColorTableParameterfv;
    long glHistogram;
    long glResetHistogram;
    long glGetHistogram;
    long glGetHistogramParameterfv;
    long glGetHistogramParameteriv;
    long glMinmax;
    long glResetMinmax;
    long glGetMinmax;
    long glGetMinmaxParameterfv;
    long glGetMinmaxParameteriv;
    long glConvolutionFilter1D;
    long glConvolutionFilter2D;
    long glConvolutionParameterf;
    long glConvolutionParameterfv;
    long glConvolutionParameteri;
    long glConvolutionParameteriv;
    long glCopyConvolutionFilter1D;
    long glCopyConvolutionFilter2D;
    long glGetConvolutionFilter;
    long glGetConvolutionParameterfv;
    long glGetConvolutionParameteriv;
    long glSeparableFilter2D;
    long glGetSeparableFilter;
    long glMultiDrawArraysIndirectCountARB;
    long glMultiDrawElementsIndirectCountARB;
    long glVertexAttribDivisorARB;
    long glCurrentPaletteMatrixARB;
    long glMatrixIndexPointerARB;
    long glMatrixIndexubvARB;
    long glMatrixIndexusvARB;
    long glMatrixIndexuivARB;
    long glSampleCoverageARB;
    long glClientActiveTextureARB;
    long glActiveTextureARB;
    long glMultiTexCoord1fARB;
    long glMultiTexCoord1dARB;
    long glMultiTexCoord1iARB;
    long glMultiTexCoord1sARB;
    long glMultiTexCoord2fARB;
    long glMultiTexCoord2dARB;
    long glMultiTexCoord2iARB;
    long glMultiTexCoord2sARB;
    long glMultiTexCoord3fARB;
    long glMultiTexCoord3dARB;
    long glMultiTexCoord3iARB;
    long glMultiTexCoord3sARB;
    long glMultiTexCoord4fARB;
    long glMultiTexCoord4dARB;
    long glMultiTexCoord4iARB;
    long glMultiTexCoord4sARB;
    long glGenQueriesARB;
    long glDeleteQueriesARB;
    long glIsQueryARB;
    long glBeginQueryARB;
    long glEndQueryARB;
    long glGetQueryivARB;
    long glGetQueryObjectivARB;
    long glGetQueryObjectuivARB;
    long glPointParameterfARB;
    long glPointParameterfvARB;
    long glProgramStringARB;
    long glBindProgramARB;
    long glDeleteProgramsARB;
    long glGenProgramsARB;
    long glProgramEnvParameter4fARB;
    long glProgramEnvParameter4dARB;
    long glProgramEnvParameter4fvARB;
    long glProgramEnvParameter4dvARB;
    long glProgramLocalParameter4fARB;
    long glProgramLocalParameter4dARB;
    long glProgramLocalParameter4fvARB;
    long glProgramLocalParameter4dvARB;
    long glGetProgramEnvParameterfvARB;
    long glGetProgramEnvParameterdvARB;
    long glGetProgramLocalParameterfvARB;
    long glGetProgramLocalParameterdvARB;
    long glGetProgramivARB;
    long glGetProgramStringARB;
    long glIsProgramARB;
    long glGetGraphicsResetStatusARB;
    long glGetnMapdvARB;
    long glGetnMapfvARB;
    long glGetnMapivARB;
    long glGetnPixelMapfvARB;
    long glGetnPixelMapuivARB;
    long glGetnPixelMapusvARB;
    long glGetnPolygonStippleARB;
    long glGetnTexImageARB;
    long glReadnPixelsARB;
    long glGetnColorTableARB;
    long glGetnConvolutionFilterARB;
    long glGetnSeparableFilterARB;
    long glGetnHistogramARB;
    long glGetnMinmaxARB;
    long glGetnCompressedTexImageARB;
    long glGetnUniformfvARB;
    long glGetnUniformivARB;
    long glGetnUniformuivARB;
    long glGetnUniformdvARB;
    long glMinSampleShadingARB;
    long glDeleteObjectARB;
    long glGetHandleARB;
    long glDetachObjectARB;
    long glCreateShaderObjectARB;
    long glShaderSourceARB;
    long glCompileShaderARB;
    long glCreateProgramObjectARB;
    long glAttachObjectARB;
    long glLinkProgramARB;
    long glUseProgramObjectARB;
    long glValidateProgramARB;
    long glUniform1fARB;
    long glUniform2fARB;
    long glUniform3fARB;
    long glUniform4fARB;
    long glUniform1iARB;
    long glUniform2iARB;
    long glUniform3iARB;
    long glUniform4iARB;
    long glUniform1fvARB;
    long glUniform2fvARB;
    long glUniform3fvARB;
    long glUniform4fvARB;
    long glUniform1ivARB;
    long glUniform2ivARB;
    long glUniform3ivARB;
    long glUniform4ivARB;
    long glUniformMatrix2fvARB;
    long glUniformMatrix3fvARB;
    long glUniformMatrix4fvARB;
    long glGetObjectParameterfvARB;
    long glGetObjectParameterivARB;
    long glGetInfoLogARB;
    long glGetAttachedObjectsARB;
    long glGetUniformLocationARB;
    long glGetActiveUniformARB;
    long glGetUniformfvARB;
    long glGetUniformivARB;
    long glGetShaderSourceARB;
    long glNamedStringARB;
    long glDeleteNamedStringARB;
    long glCompileShaderIncludeARB;
    long glIsNamedStringARB;
    long glGetNamedStringARB;
    long glGetNamedStringivARB;
    long glBufferPageCommitmentARB;
    long glTexPageCommitmentARB;
    long glTexturePageCommitmentEXT;
    long glTexBufferARB;
    long glTextureBufferRangeEXT;
    long glCompressedTexImage1DARB;
    long glCompressedTexImage2DARB;
    long glCompressedTexImage3DARB;
    long glCompressedTexSubImage1DARB;
    long glCompressedTexSubImage2DARB;
    long glCompressedTexSubImage3DARB;
    long glGetCompressedTexImageARB;
    long glTextureStorage1DEXT;
    long glTextureStorage2DEXT;
    long glTextureStorage3DEXT;
    long glTextureStorage2DMultisampleEXT;
    long glTextureStorage3DMultisampleEXT;
    long glLoadTransposeMatrixfARB;
    long glMultTransposeMatrixfARB;
    long glVertexArrayVertexAttribLOffsetEXT;
    long glWeightbvARB;
    long glWeightsvARB;
    long glWeightivARB;
    long glWeightfvARB;
    long glWeightdvARB;
    long glWeightubvARB;
    long glWeightusvARB;
    long glWeightuivARB;
    long glWeightPointerARB;
    long glVertexBlendARB;
    long glVertexAttrib1sARB;
    long glVertexAttrib1fARB;
    long glVertexAttrib1dARB;
    long glVertexAttrib2sARB;
    long glVertexAttrib2fARB;
    long glVertexAttrib2dARB;
    long glVertexAttrib3sARB;
    long glVertexAttrib3fARB;
    long glVertexAttrib3dARB;
    long glVertexAttrib4sARB;
    long glVertexAttrib4fARB;
    long glVertexAttrib4dARB;
    long glVertexAttrib4NubARB;
    long glVertexAttribPointerARB;
    long glEnableVertexAttribArrayARB;
    long glDisableVertexAttribArrayARB;
    long glBindAttribLocationARB;
    long glGetActiveAttribARB;
    long glGetAttribLocationARB;
    long glGetVertexAttribfvARB;
    long glGetVertexAttribdvARB;
    long glGetVertexAttribivARB;
    long glGetVertexAttribPointervARB;
    long glWindowPos2fARB;
    long glWindowPos2dARB;
    long glWindowPos2iARB;
    long glWindowPos2sARB;
    long glWindowPos3fARB;
    long glWindowPos3dARB;
    long glWindowPos3iARB;
    long glWindowPos3sARB;
    long glDrawBuffersATI;
    long glElementPointerATI;
    long glDrawElementArrayATI;
    long glDrawRangeElementArrayATI;
    long glTexBumpParameterfvATI;
    long glTexBumpParameterivATI;
    long glGetTexBumpParameterfvATI;
    long glGetTexBumpParameterivATI;
    long glGenFragmentShadersATI;
    long glBindFragmentShaderATI;
    long glDeleteFragmentShaderATI;
    long glBeginFragmentShaderATI;
    long glEndFragmentShaderATI;
    long glPassTexCoordATI;
    long glSampleMapATI;
    long glColorFragmentOp1ATI;
    long glColorFragmentOp2ATI;
    long glColorFragmentOp3ATI;
    long glAlphaFragmentOp1ATI;
    long glAlphaFragmentOp2ATI;
    long glAlphaFragmentOp3ATI;
    long glSetFragmentShaderConstantATI;
    long glMapObjectBufferATI;
    long glUnmapObjectBufferATI;
    long glPNTrianglesfATI;
    long glPNTrianglesiATI;
    long glStencilOpSeparateATI;
    long glStencilFuncSeparateATI;
    long glNewObjectBufferATI;
    long glIsObjectBufferATI;
    long glUpdateObjectBufferATI;
    long glGetObjectBufferfvATI;
    long glGetObjectBufferivATI;
    long glFreeObjectBufferATI;
    long glArrayObjectATI;
    long glGetArrayObjectfvATI;
    long glGetArrayObjectivATI;
    long glVariantArrayObjectATI;
    long glGetVariantArrayObjectfvATI;
    long glGetVariantArrayObjectivATI;
    long glVertexAttribArrayObjectATI;
    long glGetVertexAttribArrayObjectfvATI;
    long glGetVertexAttribArrayObjectivATI;
    long glVertexStream2fATI;
    long glVertexStream2dATI;
    long glVertexStream2iATI;
    long glVertexStream2sATI;
    long glVertexStream3fATI;
    long glVertexStream3dATI;
    long glVertexStream3iATI;
    long glVertexStream3sATI;
    long glVertexStream4fATI;
    long glVertexStream4dATI;
    long glVertexStream4iATI;
    long glVertexStream4sATI;
    long glNormalStream3bATI;
    long glNormalStream3fATI;
    long glNormalStream3dATI;
    long glNormalStream3iATI;
    long glNormalStream3sATI;
    long glClientActiveVertexStreamATI;
    long glVertexBlendEnvfATI;
    long glVertexBlendEnviATI;
    long glUniformBufferEXT;
    long glGetUniformBufferSizeEXT;
    long glGetUniformOffsetEXT;
    long glBlendColorEXT;
    long glBlendEquationSeparateEXT;
    long glBlendFuncSeparateEXT;
    long glBlendEquationEXT;
    long glLockArraysEXT;
    long glUnlockArraysEXT;
    long glDepthBoundsEXT;
    long glClientAttribDefaultEXT;
    long glPushClientAttribDefaultEXT;
    long glMatrixLoadfEXT;
    long glMatrixLoaddEXT;
    long glMatrixMultfEXT;
    long glMatrixMultdEXT;
    long glMatrixLoadIdentityEXT;
    long glMatrixRotatefEXT;
    long glMatrixRotatedEXT;
    long glMatrixScalefEXT;
    long glMatrixScaledEXT;
    long glMatrixTranslatefEXT;
    long glMatrixTranslatedEXT;
    long glMatrixOrthoEXT;
    long glMatrixFrustumEXT;
    long glMatrixPushEXT;
    long glMatrixPopEXT;
    long glTextureParameteriEXT;
    long glTextureParameterivEXT;
    long glTextureParameterfEXT;
    long glTextureParameterfvEXT;
    long glTextureImage1DEXT;
    long glTextureImage2DEXT;
    long glTextureSubImage1DEXT;
    long glTextureSubImage2DEXT;
    long glCopyTextureImage1DEXT;
    long glCopyTextureImage2DEXT;
    long glCopyTextureSubImage1DEXT;
    long glCopyTextureSubImage2DEXT;
    long glGetTextureImageEXT;
    long glGetTextureParameterfvEXT;
    long glGetTextureParameterivEXT;
    long glGetTextureLevelParameterfvEXT;
    long glGetTextureLevelParameterivEXT;
    long glTextureImage3DEXT;
    long glTextureSubImage3DEXT;
    long glCopyTextureSubImage3DEXT;
    long glBindMultiTextureEXT;
    long glMultiTexCoordPointerEXT;
    long glMultiTexEnvfEXT;
    long glMultiTexEnvfvEXT;
    long glMultiTexEnviEXT;
    long glMultiTexEnvivEXT;
    long glMultiTexGendEXT;
    long glMultiTexGendvEXT;
    long glMultiTexGenfEXT;
    long glMultiTexGenfvEXT;
    long glMultiTexGeniEXT;
    long glMultiTexGenivEXT;
    long glGetMultiTexEnvfvEXT;
    long glGetMultiTexEnvivEXT;
    long glGetMultiTexGendvEXT;
    long glGetMultiTexGenfvEXT;
    long glGetMultiTexGenivEXT;
    long glMultiTexParameteriEXT;
    long glMultiTexParameterivEXT;
    long glMultiTexParameterfEXT;
    long glMultiTexParameterfvEXT;
    long glMultiTexImage1DEXT;
    long glMultiTexImage2DEXT;
    long glMultiTexSubImage1DEXT;
    long glMultiTexSubImage2DEXT;
    long glCopyMultiTexImage1DEXT;
    long glCopyMultiTexImage2DEXT;
    long glCopyMultiTexSubImage1DEXT;
    long glCopyMultiTexSubImage2DEXT;
    long glGetMultiTexImageEXT;
    long glGetMultiTexParameterfvEXT;
    long glGetMultiTexParameterivEXT;
    long glGetMultiTexLevelParameterfvEXT;
    long glGetMultiTexLevelParameterivEXT;
    long glMultiTexImage3DEXT;
    long glMultiTexSubImage3DEXT;
    long glCopyMultiTexSubImage3DEXT;
    long glEnableClientStateIndexedEXT;
    long glDisableClientStateIndexedEXT;
    long glEnableClientStateiEXT;
    long glDisableClientStateiEXT;
    long glGetFloatIndexedvEXT;
    long glGetDoubleIndexedvEXT;
    long glGetPointerIndexedvEXT;
    long glGetFloati_vEXT;
    long glGetDoublei_vEXT;
    long glGetPointeri_vEXT;
    long glNamedProgramStringEXT;
    long glNamedProgramLocalParameter4dEXT;
    long glNamedProgramLocalParameter4dvEXT;
    long glNamedProgramLocalParameter4fEXT;
    long glNamedProgramLocalParameter4fvEXT;
    long glGetNamedProgramLocalParameterdvEXT;
    long glGetNamedProgramLocalParameterfvEXT;
    long glGetNamedProgramivEXT;
    long glGetNamedProgramStringEXT;
    long glCompressedTextureImage3DEXT;
    long glCompressedTextureImage2DEXT;
    long glCompressedTextureImage1DEXT;
    long glCompressedTextureSubImage3DEXT;
    long glCompressedTextureSubImage2DEXT;
    long glCompressedTextureSubImage1DEXT;
    long glGetCompressedTextureImageEXT;
    long glCompressedMultiTexImage3DEXT;
    long glCompressedMultiTexImage2DEXT;
    long glCompressedMultiTexImage1DEXT;
    long glCompressedMultiTexSubImage3DEXT;
    long glCompressedMultiTexSubImage2DEXT;
    long glCompressedMultiTexSubImage1DEXT;
    long glGetCompressedMultiTexImageEXT;
    long glMatrixLoadTransposefEXT;
    long glMatrixLoadTransposedEXT;
    long glMatrixMultTransposefEXT;
    long glMatrixMultTransposedEXT;
    long glNamedBufferDataEXT;
    long glNamedBufferSubDataEXT;
    long glMapNamedBufferEXT;
    long glUnmapNamedBufferEXT;
    long glGetNamedBufferParameterivEXT;
    long glGetNamedBufferPointervEXT;
    long glGetNamedBufferSubDataEXT;
    long glProgramUniform1fEXT;
    long glProgramUniform2fEXT;
    long glProgramUniform3fEXT;
    long glProgramUniform4fEXT;
    long glProgramUniform1iEXT;
    long glProgramUniform2iEXT;
    long glProgramUniform3iEXT;
    long glProgramUniform4iEXT;
    long glProgramUniform1fvEXT;
    long glProgramUniform2fvEXT;
    long glProgramUniform3fvEXT;
    long glProgramUniform4fvEXT;
    long glProgramUniform1ivEXT;
    long glProgramUniform2ivEXT;
    long glProgramUniform3ivEXT;
    long glProgramUniform4ivEXT;
    long glProgramUniformMatrix2fvEXT;
    long glProgramUniformMatrix3fvEXT;
    long glProgramUniformMatrix4fvEXT;
    long glProgramUniformMatrix2x3fvEXT;
    long glProgramUniformMatrix3x2fvEXT;
    long glProgramUniformMatrix2x4fvEXT;
    long glProgramUniformMatrix4x2fvEXT;
    long glProgramUniformMatrix3x4fvEXT;
    long glProgramUniformMatrix4x3fvEXT;
    long glTextureBufferEXT;
    long glMultiTexBufferEXT;
    long glTextureParameterIivEXT;
    long glTextureParameterIuivEXT;
    long glGetTextureParameterIivEXT;
    long glGetTextureParameterIuivEXT;
    long glMultiTexParameterIivEXT;
    long glMultiTexParameterIuivEXT;
    long glGetMultiTexParameterIivEXT;
    long glGetMultiTexParameterIuivEXT;
    long glProgramUniform1uiEXT;
    long glProgramUniform2uiEXT;
    long glProgramUniform3uiEXT;
    long glProgramUniform4uiEXT;
    long glProgramUniform1uivEXT;
    long glProgramUniform2uivEXT;
    long glProgramUniform3uivEXT;
    long glProgramUniform4uivEXT;
    long glNamedProgramLocalParameters4fvEXT;
    long glNamedProgramLocalParameterI4iEXT;
    long glNamedProgramLocalParameterI4ivEXT;
    long glNamedProgramLocalParametersI4ivEXT;
    long glNamedProgramLocalParameterI4uiEXT;
    long glNamedProgramLocalParameterI4uivEXT;
    long glNamedProgramLocalParametersI4uivEXT;
    long glGetNamedProgramLocalParameterIivEXT;
    long glGetNamedProgramLocalParameterIuivEXT;
    long glNamedRenderbufferStorageEXT;
    long glGetNamedRenderbufferParameterivEXT;
    long glNamedRenderbufferStorageMultisampleEXT;
    long glNamedRenderbufferStorageMultisampleCoverageEXT;
    long glCheckNamedFramebufferStatusEXT;
    long glNamedFramebufferTexture1DEXT;
    long glNamedFramebufferTexture2DEXT;
    long glNamedFramebufferTexture3DEXT;
    long glNamedFramebufferRenderbufferEXT;
    long glGetNamedFramebufferAttachmentParameterivEXT;
    long glGenerateTextureMipmapEXT;
    long glGenerateMultiTexMipmapEXT;
    long glFramebufferDrawBufferEXT;
    long glFramebufferDrawBuffersEXT;
    long glFramebufferReadBufferEXT;
    long glGetFramebufferParameterivEXT;
    long glNamedCopyBufferSubDataEXT;
    long glNamedFramebufferTextureEXT;
    long glNamedFramebufferTextureLayerEXT;
    long glNamedFramebufferTextureFaceEXT;
    long glTextureRenderbufferEXT;
    long glMultiTexRenderbufferEXT;
    long glVertexArrayVertexOffsetEXT;
    long glVertexArrayColorOffsetEXT;
    long glVertexArrayEdgeFlagOffsetEXT;
    long glVertexArrayIndexOffsetEXT;
    long glVertexArrayNormalOffsetEXT;
    long glVertexArrayTexCoordOffsetEXT;
    long glVertexArrayMultiTexCoordOffsetEXT;
    long glVertexArrayFogCoordOffsetEXT;
    long glVertexArraySecondaryColorOffsetEXT;
    long glVertexArrayVertexAttribOffsetEXT;
    long glVertexArrayVertexAttribIOffsetEXT;
    long glEnableVertexArrayEXT;
    long glDisableVertexArrayEXT;
    long glEnableVertexArrayAttribEXT;
    long glDisableVertexArrayAttribEXT;
    long glGetVertexArrayIntegervEXT;
    long glGetVertexArrayPointervEXT;
    long glGetVertexArrayIntegeri_vEXT;
    long glGetVertexArrayPointeri_vEXT;
    long glMapNamedBufferRangeEXT;
    long glFlushMappedNamedBufferRangeEXT;
    long glColorMaskIndexedEXT;
    long glGetBooleanIndexedvEXT;
    long glGetIntegerIndexedvEXT;
    long glEnableIndexedEXT;
    long glDisableIndexedEXT;
    long glIsEnabledIndexedEXT;
    long glDrawArraysInstancedEXT;
    long glDrawElementsInstancedEXT;
    long glDrawRangeElementsEXT;
    long glFogCoordfEXT;
    long glFogCoorddEXT;
    long glFogCoordPointerEXT;
    long glBlitFramebufferEXT;
    long glRenderbufferStorageMultisampleEXT;
    long glIsRenderbufferEXT;
    long glBindRenderbufferEXT;
    long glDeleteRenderbuffersEXT;
    long glGenRenderbuffersEXT;
    long glRenderbufferStorageEXT;
    long glGetRenderbufferParameterivEXT;
    long glIsFramebufferEXT;
    long glBindFramebufferEXT;
    long glDeleteFramebuffersEXT;
    long glGenFramebuffersEXT;
    long glCheckFramebufferStatusEXT;
    long glFramebufferTexture1DEXT;
    long glFramebufferTexture2DEXT;
    long glFramebufferTexture3DEXT;
    long glFramebufferRenderbufferEXT;
    long glGetFramebufferAttachmentParameterivEXT;
    long glGenerateMipmapEXT;
    long glProgramParameteriEXT;
    long glFramebufferTextureEXT;
    long glFramebufferTextureLayerEXT;
    long glFramebufferTextureFaceEXT;
    long glProgramEnvParameters4fvEXT;
    long glProgramLocalParameters4fvEXT;
    long glVertexAttribI1iEXT;
    long glVertexAttribI2iEXT;
    long glVertexAttribI3iEXT;
    long glVertexAttribI4iEXT;
    long glVertexAttribI1uiEXT;
    long glVertexAttribI2uiEXT;
    long glVertexAttribI3uiEXT;
    long glVertexAttribI4uiEXT;
    long glVertexAttribI1ivEXT;
    long glVertexAttribI2ivEXT;
    long glVertexAttribI3ivEXT;
    long glVertexAttribI4ivEXT;
    long glVertexAttribI1uivEXT;
    long glVertexAttribI2uivEXT;
    long glVertexAttribI3uivEXT;
    long glVertexAttribI4uivEXT;
    long glVertexAttribI4bvEXT;
    long glVertexAttribI4svEXT;
    long glVertexAttribI4ubvEXT;
    long glVertexAttribI4usvEXT;
    long glVertexAttribIPointerEXT;
    long glGetVertexAttribIivEXT;
    long glGetVertexAttribIuivEXT;
    long glUniform1uiEXT;
    long glUniform2uiEXT;
    long glUniform3uiEXT;
    long glUniform4uiEXT;
    long glUniform1uivEXT;
    long glUniform2uivEXT;
    long glUniform3uivEXT;
    long glUniform4uivEXT;
    long glGetUniformuivEXT;
    long glBindFragDataLocationEXT;
    long glGetFragDataLocationEXT;
    long glMultiDrawArraysEXT;
    long glColorTableEXT;
    long glColorSubTableEXT;
    long glGetColorTableEXT;
    long glGetColorTableParameterivEXT;
    long glGetColorTableParameterfvEXT;
    long glPointParameterfEXT;
    long glPointParameterfvEXT;
    long glProvokingVertexEXT;
    long glSecondaryColor3bEXT;
    long glSecondaryColor3fEXT;
    long glSecondaryColor3dEXT;
    long glSecondaryColor3ubEXT;
    long glSecondaryColorPointerEXT;
    long glUseShaderProgramEXT;
    long glActiveProgramEXT;
    long glCreateShaderProgramEXT;
    long glBindImageTextureEXT;
    long glMemoryBarrierEXT;
    long glStencilClearTagEXT;
    long glActiveStencilFaceEXT;
    long glTexBufferEXT;
    long glClearColorIiEXT;
    long glClearColorIuiEXT;
    long glTexParameterIivEXT;
    long glTexParameterIuivEXT;
    long glGetTexParameterIivEXT;
    long glGetTexParameterIuivEXT;
    long glGetQueryObjecti64vEXT;
    long glGetQueryObjectui64vEXT;
    long glBindBufferRangeEXT;
    long glBindBufferOffsetEXT;
    long glBindBufferBaseEXT;
    long glBeginTransformFeedbackEXT;
    long glEndTransformFeedbackEXT;
    long glTransformFeedbackVaryingsEXT;
    long glGetTransformFeedbackVaryingEXT;
    long glVertexAttribL1dEXT;
    long glVertexAttribL2dEXT;
    long glVertexAttribL3dEXT;
    long glVertexAttribL4dEXT;
    long glVertexAttribL1dvEXT;
    long glVertexAttribL2dvEXT;
    long glVertexAttribL3dvEXT;
    long glVertexAttribL4dvEXT;
    long glVertexAttribLPointerEXT;
    long glGetVertexAttribLdvEXT;
    long glBeginVertexShaderEXT;
    long glEndVertexShaderEXT;
    long glBindVertexShaderEXT;
    long glGenVertexShadersEXT;
    long glDeleteVertexShaderEXT;
    long glShaderOp1EXT;
    long glShaderOp2EXT;
    long glShaderOp3EXT;
    long glSwizzleEXT;
    long glWriteMaskEXT;
    long glInsertComponentEXT;
    long glExtractComponentEXT;
    long glGenSymbolsEXT;
    long glSetInvariantEXT;
    long glSetLocalConstantEXT;
    long glVariantbvEXT;
    long glVariantsvEXT;
    long glVariantivEXT;
    long glVariantfvEXT;
    long glVariantdvEXT;
    long glVariantubvEXT;
    long glVariantusvEXT;
    long glVariantuivEXT;
    long glVariantPointerEXT;
    long glEnableVariantClientStateEXT;
    long glDisableVariantClientStateEXT;
    long glBindLightParameterEXT;
    long glBindMaterialParameterEXT;
    long glBindTexGenParameterEXT;
    long glBindTextureUnitParameterEXT;
    long glBindParameterEXT;
    long glIsVariantEnabledEXT;
    long glGetVariantBooleanvEXT;
    long glGetVariantIntegervEXT;
    long glGetVariantFloatvEXT;
    long glGetVariantPointervEXT;
    long glGetInvariantBooleanvEXT;
    long glGetInvariantIntegervEXT;
    long glGetInvariantFloatvEXT;
    long glGetLocalConstantBooleanvEXT;
    long glGetLocalConstantIntegervEXT;
    long glGetLocalConstantFloatvEXT;
    long glVertexWeightfEXT;
    long glVertexWeightPointerEXT;
    long glAccum;
    long glAlphaFunc;
    long glClearColor;
    long glClearAccum;
    long glClear;
    long glCallLists;
    long glCallList;
    long glBlendFunc;
    long glBitmap;
    long glBindTexture;
    long glPrioritizeTextures;
    long glAreTexturesResident;
    long glBegin;
    long glEnd;
    long glArrayElement;
    long glClearDepth;
    long glDeleteLists;
    long glDeleteTextures;
    long glCullFace;
    long glCopyTexSubImage2D;
    long glCopyTexSubImage1D;
    long glCopyTexImage2D;
    long glCopyTexImage1D;
    long glCopyPixels;
    long glColorPointer;
    long glColorMaterial;
    long glColorMask;
    long glColor3b;
    long glColor3f;
    long glColor3d;
    long glColor3ub;
    long glColor4b;
    long glColor4f;
    long glColor4d;
    long glColor4ub;
    long glClipPlane;
    long glClearStencil;
    long glEvalPoint1;
    long glEvalPoint2;
    long glEvalMesh1;
    long glEvalMesh2;
    long glEvalCoord1f;
    long glEvalCoord1d;
    long glEvalCoord2f;
    long glEvalCoord2d;
    long glEnableClientState;
    long glDisableClientState;
    long glEnable;
    long glDisable;
    long glEdgeFlagPointer;
    long glEdgeFlag;
    long glDrawPixels;
    long glDrawElements;
    long glDrawBuffer;
    long glDrawArrays;
    long glDepthRange;
    long glDepthMask;
    long glDepthFunc;
    long glFeedbackBuffer;
    long glGetPixelMapfv;
    long glGetPixelMapuiv;
    long glGetPixelMapusv;
    long glGetMaterialfv;
    long glGetMaterialiv;
    long glGetMapfv;
    long glGetMapdv;
    long glGetMapiv;
    long glGetLightfv;
    long glGetLightiv;
    long glGetError;
    long glGetClipPlane;
    long glGetBooleanv;
    long glGetDoublev;
    long glGetFloatv;
    long glGetIntegerv;
    long glGenTextures;
    long glGenLists;
    long glFrustum;
    long glFrontFace;
    long glFogf;
    long glFogi;
    long glFogfv;
    long glFogiv;
    long glFlush;
    long glFinish;
    long glGetPointerv;
    long glIsEnabled;
    long glInterleavedArrays;
    long glInitNames;
    long glHint;
    long glGetTexParameterfv;
    long glGetTexParameteriv;
    long glGetTexLevelParameterfv;
    long glGetTexLevelParameteriv;
    long glGetTexImage;
    long glGetTexGeniv;
    long glGetTexGenfv;
    long glGetTexGendv;
    long glGetTexEnviv;
    long glGetTexEnvfv;
    long glGetString;
    long glGetPolygonStipple;
    long glIsList;
    long glMaterialf;
    long glMateriali;
    long glMaterialfv;
    long glMaterialiv;
    long glMapGrid1f;
    long glMapGrid1d;
    long glMapGrid2f;
    long glMapGrid2d;
    long glMap2f;
    long glMap2d;
    long glMap1f;
    long glMap1d;
    long glLogicOp;
    long glLoadName;
    long glLoadMatrixf;
    long glLoadMatrixd;
    long glLoadIdentity;
    long glListBase;
    long glLineWidth;
    long glLineStipple;
    long glLightModelf;
    long glLightModeli;
    long glLightModelfv;
    long glLightModeliv;
    long glLightf;
    long glLighti;
    long glLightfv;
    long glLightiv;
    long glIsTexture;
    long glMatrixMode;
    long glPolygonStipple;
    long glPolygonOffset;
    long glPolygonMode;
    long glPointSize;
    long glPixelZoom;
    long glPixelTransferf;
    long glPixelTransferi;
    long glPixelStoref;
    long glPixelStorei;
    long glPixelMapfv;
    long glPixelMapuiv;
    long glPixelMapusv;
    long glPassThrough;
    long glOrtho;
    long glNormalPointer;
    long glNormal3b;
    long glNormal3f;
    long glNormal3d;
    long glNormal3i;
    long glNewList;
    long glEndList;
    long glMultMatrixf;
    long glMultMatrixd;
    long glShadeModel;
    long glSelectBuffer;
    long glScissor;
    long glScalef;
    long glScaled;
    long glRotatef;
    long glRotated;
    long glRenderMode;
    long glRectf;
    long glRectd;
    long glRecti;
    long glReadPixels;
    long glReadBuffer;
    long glRasterPos2f;
    long glRasterPos2d;
    long glRasterPos2i;
    long glRasterPos3f;
    long glRasterPos3d;
    long glRasterPos3i;
    long glRasterPos4f;
    long glRasterPos4d;
    long glRasterPos4i;
    long glPushName;
    long glPopName;
    long glPushMatrix;
    long glPopMatrix;
    long glPushClientAttrib;
    long glPopClientAttrib;
    long glPushAttrib;
    long glPopAttrib;
    long glStencilFunc;
    long glVertexPointer;
    long glVertex2f;
    long glVertex2d;
    long glVertex2i;
    long glVertex3f;
    long glVertex3d;
    long glVertex3i;
    long glVertex4f;
    long glVertex4d;
    long glVertex4i;
    long glTranslatef;
    long glTranslated;
    long glTexImage1D;
    long glTexImage2D;
    long glTexSubImage1D;
    long glTexSubImage2D;
    long glTexParameterf;
    long glTexParameteri;
    long glTexParameterfv;
    long glTexParameteriv;
    long glTexGenf;
    long glTexGend;
    long glTexGenfv;
    long glTexGendv;
    long glTexGeni;
    long glTexGeniv;
    long glTexEnvf;
    long glTexEnvi;
    long glTexEnvfv;
    long glTexEnviv;
    long glTexCoordPointer;
    long glTexCoord1f;
    long glTexCoord1d;
    long glTexCoord2f;
    long glTexCoord2d;
    long glTexCoord3f;
    long glTexCoord3d;
    long glTexCoord4f;
    long glTexCoord4d;
    long glStencilOp;
    long glStencilMask;
    long glViewport;
    long glDrawRangeElements;
    long glTexImage3D;
    long glTexSubImage3D;
    long glCopyTexSubImage3D;
    long glActiveTexture;
    long glClientActiveTexture;
    long glCompressedTexImage1D;
    long glCompressedTexImage2D;
    long glCompressedTexImage3D;
    long glCompressedTexSubImage1D;
    long glCompressedTexSubImage2D;
    long glCompressedTexSubImage3D;
    long glGetCompressedTexImage;
    long glMultiTexCoord1f;
    long glMultiTexCoord1d;
    long glMultiTexCoord2f;
    long glMultiTexCoord2d;
    long glMultiTexCoord3f;
    long glMultiTexCoord3d;
    long glMultiTexCoord4f;
    long glMultiTexCoord4d;
    long glLoadTransposeMatrixf;
    long glLoadTransposeMatrixd;
    long glMultTransposeMatrixf;
    long glMultTransposeMatrixd;
    long glSampleCoverage;
    long glBlendEquation;
    long glBlendColor;
    long glFogCoordf;
    long glFogCoordd;
    long glFogCoordPointer;
    long glMultiDrawArrays;
    long glPointParameteri;
    long glPointParameterf;
    long glPointParameteriv;
    long glPointParameterfv;
    long glSecondaryColor3b;
    long glSecondaryColor3f;
    long glSecondaryColor3d;
    long glSecondaryColor3ub;
    long glSecondaryColorPointer;
    long glBlendFuncSeparate;
    long glWindowPos2f;
    long glWindowPos2d;
    long glWindowPos2i;
    long glWindowPos3f;
    long glWindowPos3d;
    long glWindowPos3i;
    long glBindBuffer;
    long glDeleteBuffers;
    long glGenBuffers;
    long glIsBuffer;
    long glBufferData;
    long glBufferSubData;
    long glGetBufferSubData;
    long glMapBuffer;
    long glUnmapBuffer;
    long glGetBufferParameteriv;
    long glGetBufferPointerv;
    long glGenQueries;
    long glDeleteQueries;
    long glIsQuery;
    long glBeginQuery;
    long glEndQuery;
    long glGetQueryiv;
    long glGetQueryObjectiv;
    long glGetQueryObjectuiv;
    long glShaderSource;
    long glCreateShader;
    long glIsShader;
    long glCompileShader;
    long glDeleteShader;
    long glCreateProgram;
    long glIsProgram;
    long glAttachShader;
    long glDetachShader;
    long glLinkProgram;
    long glUseProgram;
    long glValidateProgram;
    long glDeleteProgram;
    long glUniform1f;
    long glUniform2f;
    long glUniform3f;
    long glUniform4f;
    long glUniform1i;
    long glUniform2i;
    long glUniform3i;
    long glUniform4i;
    long glUniform1fv;
    long glUniform2fv;
    long glUniform3fv;
    long glUniform4fv;
    long glUniform1iv;
    long glUniform2iv;
    long glUniform3iv;
    long glUniform4iv;
    long glUniformMatrix2fv;
    long glUniformMatrix3fv;
    long glUniformMatrix4fv;
    long glGetShaderiv;
    long glGetProgramiv;
    long glGetShaderInfoLog;
    long glGetProgramInfoLog;
    long glGetAttachedShaders;
    long glGetUniformLocation;
    long glGetActiveUniform;
    long glGetUniformfv;
    long glGetUniformiv;
    long glGetShaderSource;
    long glVertexAttrib1s;
    long glVertexAttrib1f;
    long glVertexAttrib1d;
    long glVertexAttrib2s;
    long glVertexAttrib2f;
    long glVertexAttrib2d;
    long glVertexAttrib3s;
    long glVertexAttrib3f;
    long glVertexAttrib3d;
    long glVertexAttrib4s;
    long glVertexAttrib4f;
    long glVertexAttrib4d;
    long glVertexAttrib4Nub;
    long glVertexAttribPointer;
    long glEnableVertexAttribArray;
    long glDisableVertexAttribArray;
    long glGetVertexAttribfv;
    long glGetVertexAttribdv;
    long glGetVertexAttribiv;
    long glGetVertexAttribPointerv;
    long glBindAttribLocation;
    long glGetActiveAttrib;
    long glGetAttribLocation;
    long glDrawBuffers;
    long glStencilOpSeparate;
    long glStencilFuncSeparate;
    long glStencilMaskSeparate;
    long glBlendEquationSeparate;
    long glUniformMatrix2x3fv;
    long glUniformMatrix3x2fv;
    long glUniformMatrix2x4fv;
    long glUniformMatrix4x2fv;
    long glUniformMatrix3x4fv;
    long glUniformMatrix4x3fv;
    long glGetStringi;
    long glClearBufferfv;
    long glClearBufferiv;
    long glClearBufferuiv;
    long glClearBufferfi;
    long glVertexAttribI1i;
    long glVertexAttribI2i;
    long glVertexAttribI3i;
    long glVertexAttribI4i;
    long glVertexAttribI1ui;
    long glVertexAttribI2ui;
    long glVertexAttribI3ui;
    long glVertexAttribI4ui;
    long glVertexAttribI1iv;
    long glVertexAttribI2iv;
    long glVertexAttribI3iv;
    long glVertexAttribI4iv;
    long glVertexAttribI1uiv;
    long glVertexAttribI2uiv;
    long glVertexAttribI3uiv;
    long glVertexAttribI4uiv;
    long glVertexAttribI4bv;
    long glVertexAttribI4sv;
    long glVertexAttribI4ubv;
    long glVertexAttribI4usv;
    long glVertexAttribIPointer;
    long glGetVertexAttribIiv;
    long glGetVertexAttribIuiv;
    long glUniform1ui;
    long glUniform2ui;
    long glUniform3ui;
    long glUniform4ui;
    long glUniform1uiv;
    long glUniform2uiv;
    long glUniform3uiv;
    long glUniform4uiv;
    long glGetUniformuiv;
    long glBindFragDataLocation;
    long glGetFragDataLocation;
    long glBeginConditionalRender;
    long glEndConditionalRender;
    long glMapBufferRange;
    long glFlushMappedBufferRange;
    long glClampColor;
    long glIsRenderbuffer;
    long glBindRenderbuffer;
    long glDeleteRenderbuffers;
    long glGenRenderbuffers;
    long glRenderbufferStorage;
    long glGetRenderbufferParameteriv;
    long glIsFramebuffer;
    long glBindFramebuffer;
    long glDeleteFramebuffers;
    long glGenFramebuffers;
    long glCheckFramebufferStatus;
    long glFramebufferTexture1D;
    long glFramebufferTexture2D;
    long glFramebufferTexture3D;
    long glFramebufferRenderbuffer;
    long glGetFramebufferAttachmentParameteriv;
    long glGenerateMipmap;
    long glRenderbufferStorageMultisample;
    long glBlitFramebuffer;
    long glTexParameterIiv;
    long glTexParameterIuiv;
    long glGetTexParameterIiv;
    long glGetTexParameterIuiv;
    long glFramebufferTextureLayer;
    long glColorMaski;
    long glGetBooleani_v;
    long glGetIntegeri_v;
    long glEnablei;
    long glDisablei;
    long glIsEnabledi;
    long glBindBufferRange;
    long glBindBufferBase;
    long glBeginTransformFeedback;
    long glEndTransformFeedback;
    long glTransformFeedbackVaryings;
    long glGetTransformFeedbackVarying;
    long glBindVertexArray;
    long glDeleteVertexArrays;
    long glGenVertexArrays;
    long glIsVertexArray;
    long glDrawArraysInstanced;
    long glDrawElementsInstanced;
    long glCopyBufferSubData;
    long glPrimitiveRestartIndex;
    long glTexBuffer;
    long glGetUniformIndices;
    long glGetActiveUniformsiv;
    long glGetActiveUniformName;
    long glGetUniformBlockIndex;
    long glGetActiveUniformBlockiv;
    long glGetActiveUniformBlockName;
    long glUniformBlockBinding;
    long glGetBufferParameteri64v;
    long glDrawElementsBaseVertex;
    long glDrawRangeElementsBaseVertex;
    long glDrawElementsInstancedBaseVertex;
    long glProvokingVertex;
    long glTexImage2DMultisample;
    long glTexImage3DMultisample;
    long glGetMultisamplefv;
    long glSampleMaski;
    long glFramebufferTexture;
    long glFenceSync;
    long glIsSync;
    long glDeleteSync;
    long glClientWaitSync;
    long glWaitSync;
    long glGetInteger64v;
    long glGetInteger64i_v;
    long glGetSynciv;
    long glBindFragDataLocationIndexed;
    long glGetFragDataIndex;
    long glGenSamplers;
    long glDeleteSamplers;
    long glIsSampler;
    long glBindSampler;
    long glSamplerParameteri;
    long glSamplerParameterf;
    long glSamplerParameteriv;
    long glSamplerParameterfv;
    long glSamplerParameterIiv;
    long glSamplerParameterIuiv;
    long glGetSamplerParameteriv;
    long glGetSamplerParameterfv;
    long glGetSamplerParameterIiv;
    long glGetSamplerParameterIuiv;
    long glQueryCounter;
    long glGetQueryObjecti64v;
    long glGetQueryObjectui64v;
    long glVertexAttribDivisor;
    long glVertexP2ui;
    long glVertexP3ui;
    long glVertexP4ui;
    long glVertexP2uiv;
    long glVertexP3uiv;
    long glVertexP4uiv;
    long glTexCoordP1ui;
    long glTexCoordP2ui;
    long glTexCoordP3ui;
    long glTexCoordP4ui;
    long glTexCoordP1uiv;
    long glTexCoordP2uiv;
    long glTexCoordP3uiv;
    long glTexCoordP4uiv;
    long glMultiTexCoordP1ui;
    long glMultiTexCoordP2ui;
    long glMultiTexCoordP3ui;
    long glMultiTexCoordP4ui;
    long glMultiTexCoordP1uiv;
    long glMultiTexCoordP2uiv;
    long glMultiTexCoordP3uiv;
    long glMultiTexCoordP4uiv;
    long glNormalP3ui;
    long glNormalP3uiv;
    long glColorP3ui;
    long glColorP4ui;
    long glColorP3uiv;
    long glColorP4uiv;
    long glSecondaryColorP3ui;
    long glSecondaryColorP3uiv;
    long glVertexAttribP1ui;
    long glVertexAttribP2ui;
    long glVertexAttribP3ui;
    long glVertexAttribP4ui;
    long glVertexAttribP1uiv;
    long glVertexAttribP2uiv;
    long glVertexAttribP3uiv;
    long glVertexAttribP4uiv;
    long glBlendEquationi;
    long glBlendEquationSeparatei;
    long glBlendFunci;
    long glBlendFuncSeparatei;
    long glDrawArraysIndirect;
    long glDrawElementsIndirect;
    long glUniform1d;
    long glUniform2d;
    long glUniform3d;
    long glUniform4d;
    long glUniform1dv;
    long glUniform2dv;
    long glUniform3dv;
    long glUniform4dv;
    long glUniformMatrix2dv;
    long glUniformMatrix3dv;
    long glUniformMatrix4dv;
    long glUniformMatrix2x3dv;
    long glUniformMatrix2x4dv;
    long glUniformMatrix3x2dv;
    long glUniformMatrix3x4dv;
    long glUniformMatrix4x2dv;
    long glUniformMatrix4x3dv;
    long glGetUniformdv;
    long glMinSampleShading;
    long glGetSubroutineUniformLocation;
    long glGetSubroutineIndex;
    long glGetActiveSubroutineUniformiv;
    long glGetActiveSubroutineUniformName;
    long glGetActiveSubroutineName;
    long glUniformSubroutinesuiv;
    long glGetUniformSubroutineuiv;
    long glGetProgramStageiv;
    long glPatchParameteri;
    long glPatchParameterfv;
    long glBindTransformFeedback;
    long glDeleteTransformFeedbacks;
    long glGenTransformFeedbacks;
    long glIsTransformFeedback;
    long glPauseTransformFeedback;
    long glResumeTransformFeedback;
    long glDrawTransformFeedback;
    long glDrawTransformFeedbackStream;
    long glBeginQueryIndexed;
    long glEndQueryIndexed;
    long glGetQueryIndexediv;
    long glReleaseShaderCompiler;
    long glShaderBinary;
    long glGetShaderPrecisionFormat;
    long glDepthRangef;
    long glClearDepthf;
    long glGetProgramBinary;
    long glProgramBinary;
    long glProgramParameteri;
    long glUseProgramStages;
    long glActiveShaderProgram;
    long glCreateShaderProgramv;
    long glBindProgramPipeline;
    long glDeleteProgramPipelines;
    long glGenProgramPipelines;
    long glIsProgramPipeline;
    long glGetProgramPipelineiv;
    long glProgramUniform1i;
    long glProgramUniform2i;
    long glProgramUniform3i;
    long glProgramUniform4i;
    long glProgramUniform1f;
    long glProgramUniform2f;
    long glProgramUniform3f;
    long glProgramUniform4f;
    long glProgramUniform1d;
    long glProgramUniform2d;
    long glProgramUniform3d;
    long glProgramUniform4d;
    long glProgramUniform1iv;
    long glProgramUniform2iv;
    long glProgramUniform3iv;
    long glProgramUniform4iv;
    long glProgramUniform1fv;
    long glProgramUniform2fv;
    long glProgramUniform3fv;
    long glProgramUniform4fv;
    long glProgramUniform1dv;
    long glProgramUniform2dv;
    long glProgramUniform3dv;
    long glProgramUniform4dv;
    long glProgramUniform1ui;
    long glProgramUniform2ui;
    long glProgramUniform3ui;
    long glProgramUniform4ui;
    long glProgramUniform1uiv;
    long glProgramUniform2uiv;
    long glProgramUniform3uiv;
    long glProgramUniform4uiv;
    long glProgramUniformMatrix2fv;
    long glProgramUniformMatrix3fv;
    long glProgramUniformMatrix4fv;
    long glProgramUniformMatrix2dv;
    long glProgramUniformMatrix3dv;
    long glProgramUniformMatrix4dv;
    long glProgramUniformMatrix2x3fv;
    long glProgramUniformMatrix3x2fv;
    long glProgramUniformMatrix2x4fv;
    long glProgramUniformMatrix4x2fv;
    long glProgramUniformMatrix3x4fv;
    long glProgramUniformMatrix4x3fv;
    long glProgramUniformMatrix2x3dv;
    long glProgramUniformMatrix3x2dv;
    long glProgramUniformMatrix2x4dv;
    long glProgramUniformMatrix4x2dv;
    long glProgramUniformMatrix3x4dv;
    long glProgramUniformMatrix4x3dv;
    long glValidateProgramPipeline;
    long glGetProgramPipelineInfoLog;
    long glVertexAttribL1d;
    long glVertexAttribL2d;
    long glVertexAttribL3d;
    long glVertexAttribL4d;
    long glVertexAttribL1dv;
    long glVertexAttribL2dv;
    long glVertexAttribL3dv;
    long glVertexAttribL4dv;
    long glVertexAttribLPointer;
    long glGetVertexAttribLdv;
    long glViewportArrayv;
    long glViewportIndexedf;
    long glViewportIndexedfv;
    long glScissorArrayv;
    long glScissorIndexed;
    long glScissorIndexedv;
    long glDepthRangeArrayv;
    long glDepthRangeIndexed;
    long glGetFloati_v;
    long glGetDoublei_v;
    long glGetActiveAtomicCounterBufferiv;
    long glTexStorage1D;
    long glTexStorage2D;
    long glTexStorage3D;
    long glDrawTransformFeedbackInstanced;
    long glDrawTransformFeedbackStreamInstanced;
    long glDrawArraysInstancedBaseInstance;
    long glDrawElementsInstancedBaseInstance;
    long glDrawElementsInstancedBaseVertexBaseInstance;
    long glBindImageTexture;
    long glMemoryBarrier;
    long glGetInternalformativ;
    long glClearBufferData;
    long glClearBufferSubData;
    long glDispatchCompute;
    long glDispatchComputeIndirect;
    long glCopyImageSubData;
    long glDebugMessageControl;
    long glDebugMessageInsert;
    long glDebugMessageCallback;
    long glGetDebugMessageLog;
    long glPushDebugGroup;
    long glPopDebugGroup;
    long glObjectLabel;
    long glGetObjectLabel;
    long glObjectPtrLabel;
    long glGetObjectPtrLabel;
    long glFramebufferParameteri;
    long glGetFramebufferParameteriv;
    long glGetInternalformati64v;
    long glInvalidateTexSubImage;
    long glInvalidateTexImage;
    long glInvalidateBufferSubData;
    long glInvalidateBufferData;
    long glInvalidateFramebuffer;
    long glInvalidateSubFramebuffer;
    long glMultiDrawArraysIndirect;
    long glMultiDrawElementsIndirect;
    long glGetProgramInterfaceiv;
    long glGetProgramResourceIndex;
    long glGetProgramResourceName;
    long glGetProgramResourceiv;
    long glGetProgramResourceLocation;
    long glGetProgramResourceLocationIndex;
    long glShaderStorageBlockBinding;
    long glTexBufferRange;
    long glTexStorage2DMultisample;
    long glTexStorage3DMultisample;
    long glTextureView;
    long glBindVertexBuffer;
    long glVertexAttribFormat;
    long glVertexAttribIFormat;
    long glVertexAttribLFormat;
    long glVertexAttribBinding;
    long glVertexBindingDivisor;
    long glBufferStorage;
    long glClearTexImage;
    long glClearTexSubImage;
    long glBindBuffersBase;
    long glBindBuffersRange;
    long glBindTextures;
    long glBindSamplers;
    long glBindImageTextures;
    long glBindVertexBuffers;
    long glClipControl;
    long glCreateTransformFeedbacks;
    long glTransformFeedbackBufferBase;
    long glTransformFeedbackBufferRange;
    long glGetTransformFeedbackiv;
    long glGetTransformFeedbacki_v;
    long glGetTransformFeedbacki64_v;
    long glCreateBuffers;
    long glNamedBufferStorage;
    long glNamedBufferData;
    long glNamedBufferSubData;
    long glCopyNamedBufferSubData;
    long glClearNamedBufferData;
    long glClearNamedBufferSubData;
    long glMapNamedBuffer;
    long glMapNamedBufferRange;
    long glUnmapNamedBuffer;
    long glFlushMappedNamedBufferRange;
    long glGetNamedBufferParameteriv;
    long glGetNamedBufferParameteri64v;
    long glGetNamedBufferPointerv;
    long glGetNamedBufferSubData;
    long glCreateFramebuffers;
    long glNamedFramebufferRenderbuffer;
    long glNamedFramebufferParameteri;
    long glNamedFramebufferTexture;
    long glNamedFramebufferTextureLayer;
    long glNamedFramebufferDrawBuffer;
    long glNamedFramebufferDrawBuffers;
    long glNamedFramebufferReadBuffer;
    long glInvalidateNamedFramebufferData;
    long glInvalidateNamedFramebufferSubData;
    long glClearNamedFramebufferiv;
    long glClearNamedFramebufferuiv;
    long glClearNamedFramebufferfv;
    long glClearNamedFramebufferfi;
    long glBlitNamedFramebuffer;
    long glCheckNamedFramebufferStatus;
    long glGetNamedFramebufferParameteriv;
    long glGetNamedFramebufferAttachmentParameteriv;
    long glCreateRenderbuffers;
    long glNamedRenderbufferStorage;
    long glNamedRenderbufferStorageMultisample;
    long glGetNamedRenderbufferParameteriv;
    long glCreateTextures;
    long glTextureBuffer;
    long glTextureBufferRange;
    long glTextureStorage1D;
    long glTextureStorage2D;
    long glTextureStorage3D;
    long glTextureStorage2DMultisample;
    long glTextureStorage3DMultisample;
    long glTextureSubImage1D;
    long glTextureSubImage2D;
    long glTextureSubImage3D;
    long glCompressedTextureSubImage1D;
    long glCompressedTextureSubImage2D;
    long glCompressedTextureSubImage3D;
    long glCopyTextureSubImage1D;
    long glCopyTextureSubImage2D;
    long glCopyTextureSubImage3D;
    long glTextureParameterf;
    long glTextureParameterfv;
    long glTextureParameteri;
    long glTextureParameterIiv;
    long glTextureParameterIuiv;
    long glTextureParameteriv;
    long glGenerateTextureMipmap;
    long glBindTextureUnit;
    long glGetTextureImage;
    long glGetCompressedTextureImage;
    long glGetTextureLevelParameterfv;
    long glGetTextureLevelParameteriv;
    long glGetTextureParameterfv;
    long glGetTextureParameterIiv;
    long glGetTextureParameterIuiv;
    long glGetTextureParameteriv;
    long glCreateVertexArrays;
    long glDisableVertexArrayAttrib;
    long glEnableVertexArrayAttrib;
    long glVertexArrayElementBuffer;
    long glVertexArrayVertexBuffer;
    long glVertexArrayVertexBuffers;
    long glVertexArrayAttribFormat;
    long glVertexArrayAttribIFormat;
    long glVertexArrayAttribLFormat;
    long glVertexArrayAttribBinding;
    long glVertexArrayBindingDivisor;
    long glGetVertexArrayiv;
    long glGetVertexArrayIndexediv;
    long glGetVertexArrayIndexed64iv;
    long glCreateSamplers;
    long glCreateProgramPipelines;
    long glCreateQueries;
    long glMemoryBarrierByRegion;
    long glGetTextureSubImage;
    long glGetCompressedTextureSubImage;
    long glTextureBarrier;
    long glGetGraphicsResetStatus;
    long glReadnPixels;
    long glGetnUniformfv;
    long glGetnUniformiv;
    long glGetnUniformuiv;
    long glFrameTerminatorGREMEDY;
    long glStringMarkerGREMEDY;
    long glMapTexture2DINTEL;
    long glUnmapTexture2DINTEL;
    long glSyncTextureINTEL;
    long glMultiDrawArraysIndirectBindlessNV;
    long glMultiDrawElementsIndirectBindlessNV;
    long glGetTextureHandleNV;
    long glGetTextureSamplerHandleNV;
    long glMakeTextureHandleResidentNV;
    long glMakeTextureHandleNonResidentNV;
    long glGetImageHandleNV;
    long glMakeImageHandleResidentNV;
    long glMakeImageHandleNonResidentNV;
    long glUniformHandleui64NV;
    long glUniformHandleui64vNV;
    long glProgramUniformHandleui64NV;
    long glProgramUniformHandleui64vNV;
    long glIsTextureHandleResidentNV;
    long glIsImageHandleResidentNV;
    long glBlendParameteriNV;
    long glBlendBarrierNV;
    long glBeginConditionalRenderNV;
    long glEndConditionalRenderNV;
    long glCopyImageSubDataNV;
    long glDepthRangedNV;
    long glClearDepthdNV;
    long glDepthBoundsdNV;
    long glDrawTextureNV;
    long glGetMapControlPointsNV;
    long glMapControlPointsNV;
    long glMapParameterfvNV;
    long glMapParameterivNV;
    long glGetMapParameterfvNV;
    long glGetMapParameterivNV;
    long glGetMapAttribParameterfvNV;
    long glGetMapAttribParameterivNV;
    long glEvalMapsNV;
    long glGetMultisamplefvNV;
    long glSampleMaskIndexedNV;
    long glTexRenderbufferNV;
    long glGenFencesNV;
    long glDeleteFencesNV;
    long glSetFenceNV;
    long glTestFenceNV;
    long glFinishFenceNV;
    long glIsFenceNV;
    long glGetFenceivNV;
    long glProgramNamedParameter4fNV;
    long glProgramNamedParameter4dNV;
    long glGetProgramNamedParameterfvNV;
    long glGetProgramNamedParameterdvNV;
    long glRenderbufferStorageMultisampleCoverageNV;
    long glProgramVertexLimitNV;
    long glProgramLocalParameterI4iNV;
    long glProgramLocalParameterI4ivNV;
    long glProgramLocalParametersI4ivNV;
    long glProgramLocalParameterI4uiNV;
    long glProgramLocalParameterI4uivNV;
    long glProgramLocalParametersI4uivNV;
    long glProgramEnvParameterI4iNV;
    long glProgramEnvParameterI4ivNV;
    long glProgramEnvParametersI4ivNV;
    long glProgramEnvParameterI4uiNV;
    long glProgramEnvParameterI4uivNV;
    long glProgramEnvParametersI4uivNV;
    long glGetProgramLocalParameterIivNV;
    long glGetProgramLocalParameterIuivNV;
    long glGetProgramEnvParameterIivNV;
    long glGetProgramEnvParameterIuivNV;
    long glUniform1i64NV;
    long glUniform2i64NV;
    long glUniform3i64NV;
    long glUniform4i64NV;
    long glUniform1i64vNV;
    long glUniform2i64vNV;
    long glUniform3i64vNV;
    long glUniform4i64vNV;
    long glUniform1ui64NV;
    long glUniform2ui64NV;
    long glUniform3ui64NV;
    long glUniform4ui64NV;
    long glUniform1ui64vNV;
    long glUniform2ui64vNV;
    long glUniform3ui64vNV;
    long glUniform4ui64vNV;
    long glGetUniformi64vNV;
    long glGetUniformui64vNV;
    long glProgramUniform1i64NV;
    long glProgramUniform2i64NV;
    long glProgramUniform3i64NV;
    long glProgramUniform4i64NV;
    long glProgramUniform1i64vNV;
    long glProgramUniform2i64vNV;
    long glProgramUniform3i64vNV;
    long glProgramUniform4i64vNV;
    long glProgramUniform1ui64NV;
    long glProgramUniform2ui64NV;
    long glProgramUniform3ui64NV;
    long glProgramUniform4ui64NV;
    long glProgramUniform1ui64vNV;
    long glProgramUniform2ui64vNV;
    long glProgramUniform3ui64vNV;
    long glProgramUniform4ui64vNV;
    long glVertex2hNV;
    long glVertex3hNV;
    long glVertex4hNV;
    long glNormal3hNV;
    long glColor3hNV;
    long glColor4hNV;
    long glTexCoord1hNV;
    long glTexCoord2hNV;
    long glTexCoord3hNV;
    long glTexCoord4hNV;
    long glMultiTexCoord1hNV;
    long glMultiTexCoord2hNV;
    long glMultiTexCoord3hNV;
    long glMultiTexCoord4hNV;
    long glFogCoordhNV;
    long glSecondaryColor3hNV;
    long glVertexWeighthNV;
    long glVertexAttrib1hNV;
    long glVertexAttrib2hNV;
    long glVertexAttrib3hNV;
    long glVertexAttrib4hNV;
    long glVertexAttribs1hvNV;
    long glVertexAttribs2hvNV;
    long glVertexAttribs3hvNV;
    long glVertexAttribs4hvNV;
    long glGenOcclusionQueriesNV;
    long glDeleteOcclusionQueriesNV;
    long glIsOcclusionQueryNV;
    long glBeginOcclusionQueryNV;
    long glEndOcclusionQueryNV;
    long glGetOcclusionQueryuivNV;
    long glGetOcclusionQueryivNV;
    long glProgramBufferParametersfvNV;
    long glProgramBufferParametersIivNV;
    long glProgramBufferParametersIuivNV;
    long glPathCommandsNV;
    long glPathCoordsNV;
    long glPathSubCommandsNV;
    long glPathSubCoordsNV;
    long glPathStringNV;
    long glPathGlyphsNV;
    long glPathGlyphRangeNV;
    long glWeightPathsNV;
    long glCopyPathNV;
    long glInterpolatePathsNV;
    long glTransformPathNV;
    long glPathParameterivNV;
    long glPathParameteriNV;
    long glPathParameterfvNV;
    long glPathParameterfNV;
    long glPathDashArrayNV;
    long glGenPathsNV;
    long glDeletePathsNV;
    long glIsPathNV;
    long glPathStencilFuncNV;
    long glPathStencilDepthOffsetNV;
    long glStencilFillPathNV;
    long glStencilStrokePathNV;
    long glStencilFillPathInstancedNV;
    long glStencilStrokePathInstancedNV;
    long glPathCoverDepthFuncNV;
    long glPathColorGenNV;
    long glPathTexGenNV;
    long glPathFogGenNV;
    long glCoverFillPathNV;
    long glCoverStrokePathNV;
    long glCoverFillPathInstancedNV;
    long glCoverStrokePathInstancedNV;
    long glGetPathParameterivNV;
    long glGetPathParameterfvNV;
    long glGetPathCommandsNV;
    long glGetPathCoordsNV;
    long glGetPathDashArrayNV;
    long glGetPathMetricsNV;
    long glGetPathMetricRangeNV;
    long glGetPathSpacingNV;
    long glGetPathColorGenivNV;
    long glGetPathColorGenfvNV;
    long glGetPathTexGenivNV;
    long glGetPathTexGenfvNV;
    long glIsPointInFillPathNV;
    long glIsPointInStrokePathNV;
    long glGetPathLengthNV;
    long glPointAlongPathNV;
    long glPixelDataRangeNV;
    long glFlushPixelDataRangeNV;
    long glPointParameteriNV;
    long glPointParameterivNV;
    long glPresentFrameKeyedNV;
    long glPresentFrameDualFillNV;
    long glGetVideoivNV;
    long glGetVideouivNV;
    long glGetVideoi64vNV;
    long glGetVideoui64vNV;
    long glPrimitiveRestartNV;
    long glPrimitiveRestartIndexNV;
    long glLoadProgramNV;
    long glBindProgramNV;
    long glDeleteProgramsNV;
    long glGenProgramsNV;
    long glGetProgramivNV;
    long glGetProgramStringNV;
    long glIsProgramNV;
    long glAreProgramsResidentNV;
    long glRequestResidentProgramsNV;
    long glCombinerParameterfNV;
    long glCombinerParameterfvNV;
    long glCombinerParameteriNV;
    long glCombinerParameterivNV;
    long glCombinerInputNV;
    long glCombinerOutputNV;
    long glFinalCombinerInputNV;
    long glGetCombinerInputParameterfvNV;
    long glGetCombinerInputParameterivNV;
    long glGetCombinerOutputParameterfvNV;
    long glGetCombinerOutputParameterivNV;
    long glGetFinalCombinerInputParameterfvNV;
    long glGetFinalCombinerInputParameterivNV;
    long glCombinerStageParameterfvNV;
    long glGetCombinerStageParameterfvNV;
    long glMakeBufferResidentNV;
    long glMakeBufferNonResidentNV;
    long glIsBufferResidentNV;
    long glMakeNamedBufferResidentNV;
    long glMakeNamedBufferNonResidentNV;
    long glIsNamedBufferResidentNV;
    long glGetBufferParameterui64vNV;
    long glGetNamedBufferParameterui64vNV;
    long glGetIntegerui64vNV;
    long glUniformui64NV;
    long glUniformui64vNV;
    long glProgramUniformui64NV;
    long glProgramUniformui64vNV;
    long glTextureBarrierNV;
    long glTexImage2DMultisampleCoverageNV;
    long glTexImage3DMultisampleCoverageNV;
    long glTextureImage2DMultisampleNV;
    long glTextureImage3DMultisampleNV;
    long glTextureImage2DMultisampleCoverageNV;
    long glTextureImage3DMultisampleCoverageNV;
    long glBindBufferRangeNV;
    long glBindBufferOffsetNV;
    long glBindBufferBaseNV;
    long glTransformFeedbackAttribsNV;
    long glTransformFeedbackVaryingsNV;
    long glBeginTransformFeedbackNV;
    long glEndTransformFeedbackNV;
    long glGetVaryingLocationNV;
    long glGetActiveVaryingNV;
    long glActiveVaryingNV;
    long glGetTransformFeedbackVaryingNV;
    long glBindTransformFeedbackNV;
    long glDeleteTransformFeedbacksNV;
    long glGenTransformFeedbacksNV;
    long glIsTransformFeedbackNV;
    long glPauseTransformFeedbackNV;
    long glResumeTransformFeedbackNV;
    long glDrawTransformFeedbackNV;
    long glVertexArrayRangeNV;
    long glFlushVertexArrayRangeNV;
    long glAllocateMemoryNV;
    long glFreeMemoryNV;
    long glVertexAttribL1i64NV;
    long glVertexAttribL2i64NV;
    long glVertexAttribL3i64NV;
    long glVertexAttribL4i64NV;
    long glVertexAttribL1i64vNV;
    long glVertexAttribL2i64vNV;
    long glVertexAttribL3i64vNV;
    long glVertexAttribL4i64vNV;
    long glVertexAttribL1ui64NV;
    long glVertexAttribL2ui64NV;
    long glVertexAttribL3ui64NV;
    long glVertexAttribL4ui64NV;
    long glVertexAttribL1ui64vNV;
    long glVertexAttribL2ui64vNV;
    long glVertexAttribL3ui64vNV;
    long glVertexAttribL4ui64vNV;
    long glGetVertexAttribLi64vNV;
    long glGetVertexAttribLui64vNV;
    long glVertexAttribLFormatNV;
    long glBufferAddressRangeNV;
    long glVertexFormatNV;
    long glNormalFormatNV;
    long glColorFormatNV;
    long glIndexFormatNV;
    long glTexCoordFormatNV;
    long glEdgeFlagFormatNV;
    long glSecondaryColorFormatNV;
    long glFogCoordFormatNV;
    long glVertexAttribFormatNV;
    long glVertexAttribIFormatNV;
    long glGetIntegerui64i_vNV;
    long glExecuteProgramNV;
    long glGetProgramParameterfvNV;
    long glGetProgramParameterdvNV;
    long glGetTrackMatrixivNV;
    long glGetVertexAttribfvNV;
    long glGetVertexAttribdvNV;
    long glGetVertexAttribivNV;
    long glGetVertexAttribPointervNV;
    long glProgramParameter4fNV;
    long glProgramParameter4dNV;
    long glProgramParameters4fvNV;
    long glProgramParameters4dvNV;
    long glTrackMatrixNV;
    long glVertexAttribPointerNV;
    long glVertexAttrib1sNV;
    long glVertexAttrib1fNV;
    long glVertexAttrib1dNV;
    long glVertexAttrib2sNV;
    long glVertexAttrib2fNV;
    long glVertexAttrib2dNV;
    long glVertexAttrib3sNV;
    long glVertexAttrib3fNV;
    long glVertexAttrib3dNV;
    long glVertexAttrib4sNV;
    long glVertexAttrib4fNV;
    long glVertexAttrib4dNV;
    long glVertexAttrib4ubNV;
    long glVertexAttribs1svNV;
    long glVertexAttribs1fvNV;
    long glVertexAttribs1dvNV;
    long glVertexAttribs2svNV;
    long glVertexAttribs2fvNV;
    long glVertexAttribs2dvNV;
    long glVertexAttribs3svNV;
    long glVertexAttribs3fvNV;
    long glVertexAttribs3dvNV;
    long glVertexAttribs4svNV;
    long glVertexAttribs4fvNV;
    long glVertexAttribs4dvNV;
    long glBeginVideoCaptureNV;
    long glBindVideoCaptureStreamBufferNV;
    long glBindVideoCaptureStreamTextureNV;
    long glEndVideoCaptureNV;
    long glGetVideoCaptureivNV;
    long glGetVideoCaptureStreamivNV;
    long glGetVideoCaptureStreamfvNV;
    long glGetVideoCaptureStreamdvNV;
    long glVideoCaptureNV;
    long glVideoCaptureStreamParameterivNV;
    long glVideoCaptureStreamParameterfvNV;
    long glVideoCaptureStreamParameterdvNV;

    private boolean AMD_debug_output_initNativeFunctionAddresses() {
        this.glDebugMessageEnableAMD = GLContext.getFunctionAddress(new String[]{"glDebugMessageEnableAMD", "glDebugMessageEnableAMDX"});
        boolean bl = this.glDebugMessageEnableAMD != 0L;
        this.glDebugMessageInsertAMD = GLContext.getFunctionAddress(new String[]{"glDebugMessageInsertAMD", "glDebugMessageInsertAMDX"});
        this.glDebugMessageCallbackAMD = GLContext.getFunctionAddress(new String[]{"glDebugMessageCallbackAMD", "glDebugMessageCallbackAMDX"});
        this.glGetDebugMessageLogAMD = GLContext.getFunctionAddress(new String[]{"glGetDebugMessageLogAMD", "glGetDebugMessageLogAMDX"});
        return bl & this.glDebugMessageInsertAMD != 0L & this.glDebugMessageCallbackAMD != 0L & this.glGetDebugMessageLogAMD != 0L;
    }

    private boolean AMD_draw_buffers_blend_initNativeFunctionAddresses() {
        this.glBlendFuncIndexedAMD = GLContext.getFunctionAddress("glBlendFuncIndexedAMD");
        boolean bl = this.glBlendFuncIndexedAMD != 0L;
        this.glBlendFuncSeparateIndexedAMD = GLContext.getFunctionAddress("glBlendFuncSeparateIndexedAMD");
        this.glBlendEquationIndexedAMD = GLContext.getFunctionAddress("glBlendEquationIndexedAMD");
        this.glBlendEquationSeparateIndexedAMD = GLContext.getFunctionAddress("glBlendEquationSeparateIndexedAMD");
        return bl & this.glBlendFuncSeparateIndexedAMD != 0L & this.glBlendEquationIndexedAMD != 0L & this.glBlendEquationSeparateIndexedAMD != 0L;
    }

    private boolean AMD_interleaved_elements_initNativeFunctionAddresses() {
        this.glVertexAttribParameteriAMD = GLContext.getFunctionAddress("glVertexAttribParameteriAMD");
        return this.glVertexAttribParameteriAMD != 0L;
    }

    private boolean AMD_multi_draw_indirect_initNativeFunctionAddresses() {
        this.glMultiDrawArraysIndirectAMD = GLContext.getFunctionAddress("glMultiDrawArraysIndirectAMD");
        boolean bl = this.glMultiDrawArraysIndirectAMD != 0L;
        this.glMultiDrawElementsIndirectAMD = GLContext.getFunctionAddress("glMultiDrawElementsIndirectAMD");
        return bl & this.glMultiDrawElementsIndirectAMD != 0L;
    }

    private boolean AMD_name_gen_delete_initNativeFunctionAddresses() {
        this.glGenNamesAMD = GLContext.getFunctionAddress("glGenNamesAMD");
        boolean bl = this.glGenNamesAMD != 0L;
        this.glDeleteNamesAMD = GLContext.getFunctionAddress("glDeleteNamesAMD");
        this.glIsNameAMD = GLContext.getFunctionAddress("glIsNameAMD");
        return bl & this.glDeleteNamesAMD != 0L & this.glIsNameAMD != 0L;
    }

    private boolean AMD_performance_monitor_initNativeFunctionAddresses() {
        this.glGetPerfMonitorGroupsAMD = GLContext.getFunctionAddress("glGetPerfMonitorGroupsAMD");
        boolean bl = this.glGetPerfMonitorGroupsAMD != 0L;
        this.glGetPerfMonitorCountersAMD = GLContext.getFunctionAddress("glGetPerfMonitorCountersAMD");
        this.glGetPerfMonitorGroupStringAMD = GLContext.getFunctionAddress("glGetPerfMonitorGroupStringAMD");
        this.glGetPerfMonitorCounterStringAMD = GLContext.getFunctionAddress("glGetPerfMonitorCounterStringAMD");
        this.glGetPerfMonitorCounterInfoAMD = GLContext.getFunctionAddress("glGetPerfMonitorCounterInfoAMD");
        this.glGenPerfMonitorsAMD = GLContext.getFunctionAddress("glGenPerfMonitorsAMD");
        this.glDeletePerfMonitorsAMD = GLContext.getFunctionAddress("glDeletePerfMonitorsAMD");
        this.glSelectPerfMonitorCountersAMD = GLContext.getFunctionAddress("glSelectPerfMonitorCountersAMD");
        this.glBeginPerfMonitorAMD = GLContext.getFunctionAddress("glBeginPerfMonitorAMD");
        this.glEndPerfMonitorAMD = GLContext.getFunctionAddress("glEndPerfMonitorAMD");
        this.glGetPerfMonitorCounterDataAMD = GLContext.getFunctionAddress("glGetPerfMonitorCounterDataAMD");
        return bl & this.glGetPerfMonitorCountersAMD != 0L & this.glGetPerfMonitorGroupStringAMD != 0L & this.glGetPerfMonitorCounterStringAMD != 0L & this.glGetPerfMonitorCounterInfoAMD != 0L & this.glGenPerfMonitorsAMD != 0L & this.glDeletePerfMonitorsAMD != 0L & this.glSelectPerfMonitorCountersAMD != 0L & this.glBeginPerfMonitorAMD != 0L & this.glEndPerfMonitorAMD != 0L & this.glGetPerfMonitorCounterDataAMD != 0L;
    }

    private boolean AMD_sample_positions_initNativeFunctionAddresses() {
        this.glSetMultisamplefvAMD = GLContext.getFunctionAddress("glSetMultisamplefvAMD");
        return this.glSetMultisamplefvAMD != 0L;
    }

    private boolean AMD_sparse_texture_initNativeFunctionAddresses() {
        this.glTexStorageSparseAMD = GLContext.getFunctionAddress("glTexStorageSparseAMD");
        boolean bl = this.glTexStorageSparseAMD != 0L;
        this.glTextureStorageSparseAMD = GLContext.getFunctionAddress("glTextureStorageSparseAMD");
        return bl & this.glTextureStorageSparseAMD != 0L;
    }

    private boolean AMD_stencil_operation_extended_initNativeFunctionAddresses() {
        this.glStencilOpValueAMD = GLContext.getFunctionAddress("glStencilOpValueAMD");
        return this.glStencilOpValueAMD != 0L;
    }

    private boolean AMD_vertex_shader_tessellator_initNativeFunctionAddresses() {
        this.glTessellationFactorAMD = GLContext.getFunctionAddress("glTessellationFactorAMD");
        boolean bl = this.glTessellationFactorAMD != 0L;
        this.glTessellationModeAMD = GLContext.getFunctionAddress("glTessellationModeAMD");
        return bl & this.glTessellationModeAMD != 0L;
    }

    private boolean APPLE_element_array_initNativeFunctionAddresses() {
        this.glElementPointerAPPLE = GLContext.getFunctionAddress("glElementPointerAPPLE");
        boolean bl = this.glElementPointerAPPLE != 0L;
        this.glDrawElementArrayAPPLE = GLContext.getFunctionAddress("glDrawElementArrayAPPLE");
        this.glDrawRangeElementArrayAPPLE = GLContext.getFunctionAddress("glDrawRangeElementArrayAPPLE");
        this.glMultiDrawElementArrayAPPLE = GLContext.getFunctionAddress("glMultiDrawElementArrayAPPLE");
        this.glMultiDrawRangeElementArrayAPPLE = GLContext.getFunctionAddress("glMultiDrawRangeElementArrayAPPLE");
        return bl & this.glDrawElementArrayAPPLE != 0L & this.glDrawRangeElementArrayAPPLE != 0L & this.glMultiDrawElementArrayAPPLE != 0L & this.glMultiDrawRangeElementArrayAPPLE != 0L;
    }

    private boolean APPLE_fence_initNativeFunctionAddresses() {
        this.glGenFencesAPPLE = GLContext.getFunctionAddress("glGenFencesAPPLE");
        boolean bl = this.glGenFencesAPPLE != 0L;
        this.glDeleteFencesAPPLE = GLContext.getFunctionAddress("glDeleteFencesAPPLE");
        this.glSetFenceAPPLE = GLContext.getFunctionAddress("glSetFenceAPPLE");
        this.glIsFenceAPPLE = GLContext.getFunctionAddress("glIsFenceAPPLE");
        this.glTestFenceAPPLE = GLContext.getFunctionAddress("glTestFenceAPPLE");
        this.glFinishFenceAPPLE = GLContext.getFunctionAddress("glFinishFenceAPPLE");
        this.glTestObjectAPPLE = GLContext.getFunctionAddress("glTestObjectAPPLE");
        this.glFinishObjectAPPLE = GLContext.getFunctionAddress("glFinishObjectAPPLE");
        return bl & this.glDeleteFencesAPPLE != 0L & this.glSetFenceAPPLE != 0L & this.glIsFenceAPPLE != 0L & this.glTestFenceAPPLE != 0L & this.glFinishFenceAPPLE != 0L & this.glTestObjectAPPLE != 0L & this.glFinishObjectAPPLE != 0L;
    }

    private boolean APPLE_flush_buffer_range_initNativeFunctionAddresses() {
        this.glBufferParameteriAPPLE = GLContext.getFunctionAddress("glBufferParameteriAPPLE");
        boolean bl = this.glBufferParameteriAPPLE != 0L;
        this.glFlushMappedBufferRangeAPPLE = GLContext.getFunctionAddress("glFlushMappedBufferRangeAPPLE");
        return bl & this.glFlushMappedBufferRangeAPPLE != 0L;
    }

    private boolean APPLE_object_purgeable_initNativeFunctionAddresses() {
        this.glObjectPurgeableAPPLE = GLContext.getFunctionAddress("glObjectPurgeableAPPLE");
        boolean bl = this.glObjectPurgeableAPPLE != 0L;
        this.glObjectUnpurgeableAPPLE = GLContext.getFunctionAddress("glObjectUnpurgeableAPPLE");
        this.glGetObjectParameterivAPPLE = GLContext.getFunctionAddress("glGetObjectParameterivAPPLE");
        return bl & this.glObjectUnpurgeableAPPLE != 0L & this.glGetObjectParameterivAPPLE != 0L;
    }

    private boolean APPLE_texture_range_initNativeFunctionAddresses() {
        this.glTextureRangeAPPLE = GLContext.getFunctionAddress("glTextureRangeAPPLE");
        boolean bl = this.glTextureRangeAPPLE != 0L;
        this.glGetTexParameterPointervAPPLE = GLContext.getFunctionAddress("glGetTexParameterPointervAPPLE");
        return bl & this.glGetTexParameterPointervAPPLE != 0L;
    }

    private boolean APPLE_vertex_array_object_initNativeFunctionAddresses() {
        this.glBindVertexArrayAPPLE = GLContext.getFunctionAddress("glBindVertexArrayAPPLE");
        boolean bl = this.glBindVertexArrayAPPLE != 0L;
        this.glDeleteVertexArraysAPPLE = GLContext.getFunctionAddress("glDeleteVertexArraysAPPLE");
        this.glGenVertexArraysAPPLE = GLContext.getFunctionAddress("glGenVertexArraysAPPLE");
        this.glIsVertexArrayAPPLE = GLContext.getFunctionAddress("glIsVertexArrayAPPLE");
        return bl & this.glDeleteVertexArraysAPPLE != 0L & this.glGenVertexArraysAPPLE != 0L & this.glIsVertexArrayAPPLE != 0L;
    }

    private boolean APPLE_vertex_array_range_initNativeFunctionAddresses() {
        this.glVertexArrayRangeAPPLE = GLContext.getFunctionAddress("glVertexArrayRangeAPPLE");
        boolean bl = this.glVertexArrayRangeAPPLE != 0L;
        this.glFlushVertexArrayRangeAPPLE = GLContext.getFunctionAddress("glFlushVertexArrayRangeAPPLE");
        this.glVertexArrayParameteriAPPLE = GLContext.getFunctionAddress("glVertexArrayParameteriAPPLE");
        return bl & this.glFlushVertexArrayRangeAPPLE != 0L & this.glVertexArrayParameteriAPPLE != 0L;
    }

    private boolean APPLE_vertex_program_evaluators_initNativeFunctionAddresses() {
        this.glEnableVertexAttribAPPLE = GLContext.getFunctionAddress("glEnableVertexAttribAPPLE");
        boolean bl = this.glEnableVertexAttribAPPLE != 0L;
        this.glDisableVertexAttribAPPLE = GLContext.getFunctionAddress("glDisableVertexAttribAPPLE");
        this.glIsVertexAttribEnabledAPPLE = GLContext.getFunctionAddress("glIsVertexAttribEnabledAPPLE");
        this.glMapVertexAttrib1dAPPLE = GLContext.getFunctionAddress("glMapVertexAttrib1dAPPLE");
        this.glMapVertexAttrib1fAPPLE = GLContext.getFunctionAddress("glMapVertexAttrib1fAPPLE");
        this.glMapVertexAttrib2dAPPLE = GLContext.getFunctionAddress("glMapVertexAttrib2dAPPLE");
        this.glMapVertexAttrib2fAPPLE = GLContext.getFunctionAddress("glMapVertexAttrib2fAPPLE");
        return bl & this.glDisableVertexAttribAPPLE != 0L & this.glIsVertexAttribEnabledAPPLE != 0L & this.glMapVertexAttrib1dAPPLE != 0L & this.glMapVertexAttrib1fAPPLE != 0L & this.glMapVertexAttrib2dAPPLE != 0L & this.glMapVertexAttrib2fAPPLE != 0L;
    }

    private boolean ARB_ES2_compatibility_initNativeFunctionAddresses() {
        this.glReleaseShaderCompiler = GLContext.getFunctionAddress("glReleaseShaderCompiler");
        boolean bl = this.glReleaseShaderCompiler != 0L;
        this.glShaderBinary = GLContext.getFunctionAddress("glShaderBinary");
        this.glGetShaderPrecisionFormat = GLContext.getFunctionAddress("glGetShaderPrecisionFormat");
        this.glDepthRangef = GLContext.getFunctionAddress("glDepthRangef");
        this.glClearDepthf = GLContext.getFunctionAddress("glClearDepthf");
        return bl & this.glShaderBinary != 0L & this.glGetShaderPrecisionFormat != 0L & this.glDepthRangef != 0L & this.glClearDepthf != 0L;
    }

    private boolean ARB_ES3_1_compatibility_initNativeFunctionAddresses() {
        this.glMemoryBarrierByRegion = GLContext.getFunctionAddress("glMemoryBarrierByRegion");
        return this.glMemoryBarrierByRegion != 0L;
    }

    private boolean ARB_base_instance_initNativeFunctionAddresses() {
        this.glDrawArraysInstancedBaseInstance = GLContext.getFunctionAddress("glDrawArraysInstancedBaseInstance");
        boolean bl = this.glDrawArraysInstancedBaseInstance != 0L;
        this.glDrawElementsInstancedBaseInstance = GLContext.getFunctionAddress("glDrawElementsInstancedBaseInstance");
        this.glDrawElementsInstancedBaseVertexBaseInstance = GLContext.getFunctionAddress("glDrawElementsInstancedBaseVertexBaseInstance");
        return bl & this.glDrawElementsInstancedBaseInstance != 0L & this.glDrawElementsInstancedBaseVertexBaseInstance != 0L;
    }

    private boolean ARB_bindless_texture_initNativeFunctionAddresses() {
        this.glGetTextureHandleARB = GLContext.getFunctionAddress("glGetTextureHandleARB");
        boolean bl = this.glGetTextureHandleARB != 0L;
        this.glGetTextureSamplerHandleARB = GLContext.getFunctionAddress("glGetTextureSamplerHandleARB");
        this.glMakeTextureHandleResidentARB = GLContext.getFunctionAddress("glMakeTextureHandleResidentARB");
        this.glMakeTextureHandleNonResidentARB = GLContext.getFunctionAddress("glMakeTextureHandleNonResidentARB");
        this.glGetImageHandleARB = GLContext.getFunctionAddress("glGetImageHandleARB");
        this.glMakeImageHandleResidentARB = GLContext.getFunctionAddress("glMakeImageHandleResidentARB");
        this.glMakeImageHandleNonResidentARB = GLContext.getFunctionAddress("glMakeImageHandleNonResidentARB");
        this.glUniformHandleui64ARB = GLContext.getFunctionAddress("glUniformHandleui64ARB");
        this.glUniformHandleui64vARB = GLContext.getFunctionAddress("glUniformHandleui64vARB");
        this.glProgramUniformHandleui64ARB = GLContext.getFunctionAddress("glProgramUniformHandleui64ARB");
        this.glProgramUniformHandleui64vARB = GLContext.getFunctionAddress("glProgramUniformHandleui64vARB");
        this.glIsTextureHandleResidentARB = GLContext.getFunctionAddress("glIsTextureHandleResidentARB");
        this.glIsImageHandleResidentARB = GLContext.getFunctionAddress("glIsImageHandleResidentARB");
        this.glVertexAttribL1ui64ARB = GLContext.getFunctionAddress("glVertexAttribL1ui64ARB");
        this.glVertexAttribL1ui64vARB = GLContext.getFunctionAddress("glVertexAttribL1ui64vARB");
        this.glGetVertexAttribLui64vARB = GLContext.getFunctionAddress("glGetVertexAttribLui64vARB");
        return bl & this.glGetTextureSamplerHandleARB != 0L & this.glMakeTextureHandleResidentARB != 0L & this.glMakeTextureHandleNonResidentARB != 0L & this.glGetImageHandleARB != 0L & this.glMakeImageHandleResidentARB != 0L & this.glMakeImageHandleNonResidentARB != 0L & this.glUniformHandleui64ARB != 0L & this.glUniformHandleui64vARB != 0L & this.glProgramUniformHandleui64ARB != 0L & this.glProgramUniformHandleui64vARB != 0L & this.glIsTextureHandleResidentARB != 0L & this.glIsImageHandleResidentARB != 0L & this.glVertexAttribL1ui64ARB != 0L & this.glVertexAttribL1ui64vARB != 0L & this.glGetVertexAttribLui64vARB != 0L;
    }

    private boolean ARB_blend_func_extended_initNativeFunctionAddresses() {
        this.glBindFragDataLocationIndexed = GLContext.getFunctionAddress("glBindFragDataLocationIndexed");
        boolean bl = this.glBindFragDataLocationIndexed != 0L;
        this.glGetFragDataIndex = GLContext.getFunctionAddress("glGetFragDataIndex");
        return bl & this.glGetFragDataIndex != 0L;
    }

    private boolean ARB_buffer_object_initNativeFunctionAddresses() {
        this.glBindBufferARB = GLContext.getFunctionAddress("glBindBufferARB");
        boolean bl = this.glBindBufferARB != 0L;
        this.glDeleteBuffersARB = GLContext.getFunctionAddress("glDeleteBuffersARB");
        this.glGenBuffersARB = GLContext.getFunctionAddress("glGenBuffersARB");
        this.glIsBufferARB = GLContext.getFunctionAddress("glIsBufferARB");
        this.glBufferDataARB = GLContext.getFunctionAddress("glBufferDataARB");
        this.glBufferSubDataARB = GLContext.getFunctionAddress("glBufferSubDataARB");
        this.glGetBufferSubDataARB = GLContext.getFunctionAddress("glGetBufferSubDataARB");
        this.glMapBufferARB = GLContext.getFunctionAddress("glMapBufferARB");
        this.glUnmapBufferARB = GLContext.getFunctionAddress("glUnmapBufferARB");
        this.glGetBufferParameterivARB = GLContext.getFunctionAddress("glGetBufferParameterivARB");
        this.glGetBufferPointervARB = GLContext.getFunctionAddress("glGetBufferPointervARB");
        return bl & this.glDeleteBuffersARB != 0L & this.glGenBuffersARB != 0L & this.glIsBufferARB != 0L & this.glBufferDataARB != 0L & this.glBufferSubDataARB != 0L & this.glGetBufferSubDataARB != 0L & this.glMapBufferARB != 0L & this.glUnmapBufferARB != 0L & this.glGetBufferParameterivARB != 0L & this.glGetBufferPointervARB != 0L;
    }

    private boolean ARB_buffer_storage_initNativeFunctionAddresses(Set<String> supported_extensions) {
        this.glBufferStorage = GLContext.getFunctionAddress("glBufferStorage");
        return this.glBufferStorage != 0L & (!supported_extensions.contains("GL_EXT_direct_state_access") || (this.glNamedBufferStorageEXT = GLContext.getFunctionAddress("glNamedBufferStorageEXT")) != 0L);
    }

    private boolean ARB_cl_event_initNativeFunctionAddresses() {
        this.glCreateSyncFromCLeventARB = GLContext.getFunctionAddress("glCreateSyncFromCLeventARB");
        return this.glCreateSyncFromCLeventARB != 0L;
    }

    private boolean ARB_clear_buffer_object_initNativeFunctionAddresses(Set<String> supported_extensions) {
        this.glClearBufferData = GLContext.getFunctionAddress("glClearBufferData");
        boolean bl = this.glClearBufferData != 0L;
        this.glClearBufferSubData = GLContext.getFunctionAddress("glClearBufferSubData");
        return bl & this.glClearBufferSubData != 0L & (!supported_extensions.contains("GL_EXT_direct_state_access") || (this.glClearNamedBufferDataEXT = GLContext.getFunctionAddress("glClearNamedBufferDataEXT")) != 0L) & (!supported_extensions.contains("GL_EXT_direct_state_access") || (this.glClearNamedBufferSubDataEXT = GLContext.getFunctionAddress("glClearNamedBufferSubDataEXT")) != 0L);
    }

    private boolean ARB_clear_texture_initNativeFunctionAddresses() {
        this.glClearTexImage = GLContext.getFunctionAddress("glClearTexImage");
        boolean bl = this.glClearTexImage != 0L;
        this.glClearTexSubImage = GLContext.getFunctionAddress("glClearTexSubImage");
        return bl & this.glClearTexSubImage != 0L;
    }

    private boolean ARB_clip_control_initNativeFunctionAddresses() {
        this.glClipControl = GLContext.getFunctionAddress("glClipControl");
        return this.glClipControl != 0L;
    }

    private boolean ARB_color_buffer_float_initNativeFunctionAddresses() {
        this.glClampColorARB = GLContext.getFunctionAddress("glClampColorARB");
        return this.glClampColorARB != 0L;
    }

    private boolean ARB_compute_shader_initNativeFunctionAddresses() {
        this.glDispatchCompute = GLContext.getFunctionAddress("glDispatchCompute");
        boolean bl = this.glDispatchCompute != 0L;
        this.glDispatchComputeIndirect = GLContext.getFunctionAddress("glDispatchComputeIndirect");
        return bl & this.glDispatchComputeIndirect != 0L;
    }

    private boolean ARB_compute_variable_group_size_initNativeFunctionAddresses() {
        this.glDispatchComputeGroupSizeARB = GLContext.getFunctionAddress("glDispatchComputeGroupSizeARB");
        return this.glDispatchComputeGroupSizeARB != 0L;
    }

    private boolean ARB_copy_buffer_initNativeFunctionAddresses() {
        this.glCopyBufferSubData = GLContext.getFunctionAddress("glCopyBufferSubData");
        return this.glCopyBufferSubData != 0L;
    }

    private boolean ARB_copy_image_initNativeFunctionAddresses() {
        this.glCopyImageSubData = GLContext.getFunctionAddress("glCopyImageSubData");
        return this.glCopyImageSubData != 0L;
    }

    private boolean ARB_debug_output_initNativeFunctionAddresses() {
        this.glDebugMessageControlARB = GLContext.getFunctionAddress("glDebugMessageControlARB");
        boolean bl = this.glDebugMessageControlARB != 0L;
        this.glDebugMessageInsertARB = GLContext.getFunctionAddress("glDebugMessageInsertARB");
        this.glDebugMessageCallbackARB = GLContext.getFunctionAddress("glDebugMessageCallbackARB");
        this.glGetDebugMessageLogARB = GLContext.getFunctionAddress("glGetDebugMessageLogARB");
        return bl & this.glDebugMessageInsertARB != 0L & this.glDebugMessageCallbackARB != 0L & this.glGetDebugMessageLogARB != 0L;
    }

    private boolean ARB_direct_state_access_initNativeFunctionAddresses() {
        this.glCreateTransformFeedbacks = GLContext.getFunctionAddress("glCreateTransformFeedbacks");
        boolean bl = this.glCreateTransformFeedbacks != 0L;
        this.glTransformFeedbackBufferBase = GLContext.getFunctionAddress("glTransformFeedbackBufferBase");
        this.glTransformFeedbackBufferRange = GLContext.getFunctionAddress("glTransformFeedbackBufferRange");
        this.glGetTransformFeedbackiv = GLContext.getFunctionAddress("glGetTransformFeedbackiv");
        this.glGetTransformFeedbacki_v = GLContext.getFunctionAddress("glGetTransformFeedbacki_v");
        this.glGetTransformFeedbacki64_v = GLContext.getFunctionAddress("glGetTransformFeedbacki64_v");
        this.glCreateBuffers = GLContext.getFunctionAddress("glCreateBuffers");
        this.glNamedBufferStorage = GLContext.getFunctionAddress("glNamedBufferStorage");
        this.glNamedBufferData = GLContext.getFunctionAddress("glNamedBufferData");
        this.glNamedBufferSubData = GLContext.getFunctionAddress("glNamedBufferSubData");
        this.glCopyNamedBufferSubData = GLContext.getFunctionAddress("glCopyNamedBufferSubData");
        this.glClearNamedBufferData = GLContext.getFunctionAddress("glClearNamedBufferData");
        this.glClearNamedBufferSubData = GLContext.getFunctionAddress("glClearNamedBufferSubData");
        this.glMapNamedBuffer = GLContext.getFunctionAddress("glMapNamedBuffer");
        this.glMapNamedBufferRange = GLContext.getFunctionAddress("glMapNamedBufferRange");
        this.glUnmapNamedBuffer = GLContext.getFunctionAddress("glUnmapNamedBuffer");
        this.glFlushMappedNamedBufferRange = GLContext.getFunctionAddress("glFlushMappedNamedBufferRange");
        this.glGetNamedBufferParameteriv = GLContext.getFunctionAddress("glGetNamedBufferParameteriv");
        this.glGetNamedBufferParameteri64v = GLContext.getFunctionAddress("glGetNamedBufferParameteri64v");
        this.glGetNamedBufferPointerv = GLContext.getFunctionAddress("glGetNamedBufferPointerv");
        this.glGetNamedBufferSubData = GLContext.getFunctionAddress("glGetNamedBufferSubData");
        this.glCreateFramebuffers = GLContext.getFunctionAddress("glCreateFramebuffers");
        this.glNamedFramebufferRenderbuffer = GLContext.getFunctionAddress("glNamedFramebufferRenderbuffer");
        this.glNamedFramebufferParameteri = GLContext.getFunctionAddress("glNamedFramebufferParameteri");
        this.glNamedFramebufferTexture = GLContext.getFunctionAddress("glNamedFramebufferTexture");
        this.glNamedFramebufferTextureLayer = GLContext.getFunctionAddress("glNamedFramebufferTextureLayer");
        this.glNamedFramebufferDrawBuffer = GLContext.getFunctionAddress("glNamedFramebufferDrawBuffer");
        this.glNamedFramebufferDrawBuffers = GLContext.getFunctionAddress("glNamedFramebufferDrawBuffers");
        this.glNamedFramebufferReadBuffer = GLContext.getFunctionAddress("glNamedFramebufferReadBuffer");
        this.glInvalidateNamedFramebufferData = GLContext.getFunctionAddress("glInvalidateNamedFramebufferData");
        this.glInvalidateNamedFramebufferSubData = GLContext.getFunctionAddress("glInvalidateNamedFramebufferSubData");
        this.glClearNamedFramebufferiv = GLContext.getFunctionAddress("glClearNamedFramebufferiv");
        this.glClearNamedFramebufferuiv = GLContext.getFunctionAddress("glClearNamedFramebufferuiv");
        this.glClearNamedFramebufferfv = GLContext.getFunctionAddress("glClearNamedFramebufferfv");
        this.glClearNamedFramebufferfi = GLContext.getFunctionAddress("glClearNamedFramebufferfi");
        this.glBlitNamedFramebuffer = GLContext.getFunctionAddress("glBlitNamedFramebuffer");
        this.glCheckNamedFramebufferStatus = GLContext.getFunctionAddress("glCheckNamedFramebufferStatus");
        this.glGetNamedFramebufferParameteriv = GLContext.getFunctionAddress("glGetNamedFramebufferParameteriv");
        this.glGetNamedFramebufferAttachmentParameteriv = GLContext.getFunctionAddress("glGetNamedFramebufferAttachmentParameteriv");
        this.glCreateRenderbuffers = GLContext.getFunctionAddress("glCreateRenderbuffers");
        this.glNamedRenderbufferStorage = GLContext.getFunctionAddress("glNamedRenderbufferStorage");
        this.glNamedRenderbufferStorageMultisample = GLContext.getFunctionAddress("glNamedRenderbufferStorageMultisample");
        this.glGetNamedRenderbufferParameteriv = GLContext.getFunctionAddress("glGetNamedRenderbufferParameteriv");
        this.glCreateTextures = GLContext.getFunctionAddress("glCreateTextures");
        this.glTextureBuffer = GLContext.getFunctionAddress("glTextureBuffer");
        this.glTextureBufferRange = GLContext.getFunctionAddress("glTextureBufferRange");
        this.glTextureStorage1D = GLContext.getFunctionAddress("glTextureStorage1D");
        this.glTextureStorage2D = GLContext.getFunctionAddress("glTextureStorage2D");
        this.glTextureStorage3D = GLContext.getFunctionAddress("glTextureStorage3D");
        this.glTextureStorage2DMultisample = GLContext.getFunctionAddress("glTextureStorage2DMultisample");
        this.glTextureStorage3DMultisample = GLContext.getFunctionAddress("glTextureStorage3DMultisample");
        this.glTextureSubImage1D = GLContext.getFunctionAddress("glTextureSubImage1D");
        this.glTextureSubImage2D = GLContext.getFunctionAddress("glTextureSubImage2D");
        this.glTextureSubImage3D = GLContext.getFunctionAddress("glTextureSubImage3D");
        this.glCompressedTextureSubImage1D = GLContext.getFunctionAddress("glCompressedTextureSubImage1D");
        this.glCompressedTextureSubImage2D = GLContext.getFunctionAddress("glCompressedTextureSubImage2D");
        this.glCompressedTextureSubImage3D = GLContext.getFunctionAddress("glCompressedTextureSubImage3D");
        this.glCopyTextureSubImage1D = GLContext.getFunctionAddress("glCopyTextureSubImage1D");
        this.glCopyTextureSubImage2D = GLContext.getFunctionAddress("glCopyTextureSubImage2D");
        this.glCopyTextureSubImage3D = GLContext.getFunctionAddress("glCopyTextureSubImage3D");
        this.glTextureParameterf = GLContext.getFunctionAddress("glTextureParameterf");
        this.glTextureParameterfv = GLContext.getFunctionAddress("glTextureParameterfv");
        this.glTextureParameteri = GLContext.getFunctionAddress("glTextureParameteri");
        this.glTextureParameterIiv = GLContext.getFunctionAddress("glTextureParameterIiv");
        this.glTextureParameterIuiv = GLContext.getFunctionAddress("glTextureParameterIuiv");
        this.glTextureParameteriv = GLContext.getFunctionAddress("glTextureParameteriv");
        this.glGenerateTextureMipmap = GLContext.getFunctionAddress("glGenerateTextureMipmap");
        this.glBindTextureUnit = GLContext.getFunctionAddress("glBindTextureUnit");
        this.glGetTextureImage = GLContext.getFunctionAddress("glGetTextureImage");
        this.glGetCompressedTextureImage = GLContext.getFunctionAddress("glGetCompressedTextureImage");
        this.glGetTextureLevelParameterfv = GLContext.getFunctionAddress("glGetTextureLevelParameterfv");
        this.glGetTextureLevelParameteriv = GLContext.getFunctionAddress("glGetTextureLevelParameteriv");
        this.glGetTextureParameterfv = GLContext.getFunctionAddress("glGetTextureParameterfv");
        this.glGetTextureParameterIiv = GLContext.getFunctionAddress("glGetTextureParameterIiv");
        this.glGetTextureParameterIuiv = GLContext.getFunctionAddress("glGetTextureParameterIuiv");
        this.glGetTextureParameteriv = GLContext.getFunctionAddress("glGetTextureParameteriv");
        this.glCreateVertexArrays = GLContext.getFunctionAddress("glCreateVertexArrays");
        this.glDisableVertexArrayAttrib = GLContext.getFunctionAddress("glDisableVertexArrayAttrib");
        this.glEnableVertexArrayAttrib = GLContext.getFunctionAddress("glEnableVertexArrayAttrib");
        this.glVertexArrayElementBuffer = GLContext.getFunctionAddress("glVertexArrayElementBuffer");
        this.glVertexArrayVertexBuffer = GLContext.getFunctionAddress("glVertexArrayVertexBuffer");
        this.glVertexArrayVertexBuffers = GLContext.getFunctionAddress("glVertexArrayVertexBuffers");
        this.glVertexArrayAttribFormat = GLContext.getFunctionAddress("glVertexArrayAttribFormat");
        this.glVertexArrayAttribIFormat = GLContext.getFunctionAddress("glVertexArrayAttribIFormat");
        this.glVertexArrayAttribLFormat = GLContext.getFunctionAddress("glVertexArrayAttribLFormat");
        this.glVertexArrayAttribBinding = GLContext.getFunctionAddress("glVertexArrayAttribBinding");
        this.glVertexArrayBindingDivisor = GLContext.getFunctionAddress("glVertexArrayBindingDivisor");
        this.glGetVertexArrayiv = GLContext.getFunctionAddress("glGetVertexArrayiv");
        this.glGetVertexArrayIndexediv = GLContext.getFunctionAddress("glGetVertexArrayIndexediv");
        this.glGetVertexArrayIndexed64iv = GLContext.getFunctionAddress("glGetVertexArrayIndexed64iv");
        this.glCreateSamplers = GLContext.getFunctionAddress("glCreateSamplers");
        this.glCreateProgramPipelines = GLContext.getFunctionAddress("glCreateProgramPipelines");
        this.glCreateQueries = GLContext.getFunctionAddress("glCreateQueries");
        return bl & this.glTransformFeedbackBufferBase != 0L & this.glTransformFeedbackBufferRange != 0L & this.glGetTransformFeedbackiv != 0L & this.glGetTransformFeedbacki_v != 0L & this.glGetTransformFeedbacki64_v != 0L & this.glCreateBuffers != 0L & this.glNamedBufferStorage != 0L & this.glNamedBufferData != 0L & this.glNamedBufferSubData != 0L & this.glCopyNamedBufferSubData != 0L & this.glClearNamedBufferData != 0L & this.glClearNamedBufferSubData != 0L & this.glMapNamedBuffer != 0L & this.glMapNamedBufferRange != 0L & this.glUnmapNamedBuffer != 0L & this.glFlushMappedNamedBufferRange != 0L & this.glGetNamedBufferParameteriv != 0L & this.glGetNamedBufferParameteri64v != 0L & this.glGetNamedBufferPointerv != 0L & this.glGetNamedBufferSubData != 0L & this.glCreateFramebuffers != 0L & this.glNamedFramebufferRenderbuffer != 0L & this.glNamedFramebufferParameteri != 0L & this.glNamedFramebufferTexture != 0L & this.glNamedFramebufferTextureLayer != 0L & this.glNamedFramebufferDrawBuffer != 0L & this.glNamedFramebufferDrawBuffers != 0L & this.glNamedFramebufferReadBuffer != 0L & this.glInvalidateNamedFramebufferData != 0L & this.glInvalidateNamedFramebufferSubData != 0L & this.glClearNamedFramebufferiv != 0L & this.glClearNamedFramebufferuiv != 0L & this.glClearNamedFramebufferfv != 0L & this.glClearNamedFramebufferfi != 0L & this.glBlitNamedFramebuffer != 0L & this.glCheckNamedFramebufferStatus != 0L & this.glGetNamedFramebufferParameteriv != 0L & this.glGetNamedFramebufferAttachmentParameteriv != 0L & this.glCreateRenderbuffers != 0L & this.glNamedRenderbufferStorage != 0L & this.glNamedRenderbufferStorageMultisample != 0L & this.glGetNamedRenderbufferParameteriv != 0L & this.glCreateTextures != 0L & this.glTextureBuffer != 0L & this.glTextureBufferRange != 0L & this.glTextureStorage1D != 0L & this.glTextureStorage2D != 0L & this.glTextureStorage3D != 0L & this.glTextureStorage2DMultisample != 0L & this.glTextureStorage3DMultisample != 0L & this.glTextureSubImage1D != 0L & this.glTextureSubImage2D != 0L & this.glTextureSubImage3D != 0L & this.glCompressedTextureSubImage1D != 0L & this.glCompressedTextureSubImage2D != 0L & this.glCompressedTextureSubImage3D != 0L & this.glCopyTextureSubImage1D != 0L & this.glCopyTextureSubImage2D != 0L & this.glCopyTextureSubImage3D != 0L & this.glTextureParameterf != 0L & this.glTextureParameterfv != 0L & this.glTextureParameteri != 0L & this.glTextureParameterIiv != 0L & this.glTextureParameterIuiv != 0L & this.glTextureParameteriv != 0L & this.glGenerateTextureMipmap != 0L & this.glBindTextureUnit != 0L & this.glGetTextureImage != 0L & this.glGetCompressedTextureImage != 0L & this.glGetTextureLevelParameterfv != 0L & this.glGetTextureLevelParameteriv != 0L & this.glGetTextureParameterfv != 0L & this.glGetTextureParameterIiv != 0L & this.glGetTextureParameterIuiv != 0L & this.glGetTextureParameteriv != 0L & this.glCreateVertexArrays != 0L & this.glDisableVertexArrayAttrib != 0L & this.glEnableVertexArrayAttrib != 0L & this.glVertexArrayElementBuffer != 0L & this.glVertexArrayVertexBuffer != 0L & this.glVertexArrayVertexBuffers != 0L & this.glVertexArrayAttribFormat != 0L & this.glVertexArrayAttribIFormat != 0L & this.glVertexArrayAttribLFormat != 0L & this.glVertexArrayAttribBinding != 0L & this.glVertexArrayBindingDivisor != 0L & this.glGetVertexArrayiv != 0L & this.glGetVertexArrayIndexediv != 0L & this.glGetVertexArrayIndexed64iv != 0L & this.glCreateSamplers != 0L & this.glCreateProgramPipelines != 0L & this.glCreateQueries != 0L;
    }

    private boolean ARB_draw_buffers_initNativeFunctionAddresses() {
        this.glDrawBuffersARB = GLContext.getFunctionAddress("glDrawBuffersARB");
        return this.glDrawBuffersARB != 0L;
    }

    private boolean ARB_draw_buffers_blend_initNativeFunctionAddresses() {
        this.glBlendEquationiARB = GLContext.getFunctionAddress("glBlendEquationiARB");
        boolean bl = this.glBlendEquationiARB != 0L;
        this.glBlendEquationSeparateiARB = GLContext.getFunctionAddress("glBlendEquationSeparateiARB");
        this.glBlendFunciARB = GLContext.getFunctionAddress("glBlendFunciARB");
        this.glBlendFuncSeparateiARB = GLContext.getFunctionAddress("glBlendFuncSeparateiARB");
        return bl & this.glBlendEquationSeparateiARB != 0L & this.glBlendFunciARB != 0L & this.glBlendFuncSeparateiARB != 0L;
    }

    private boolean ARB_draw_elements_base_vertex_initNativeFunctionAddresses() {
        this.glDrawElementsBaseVertex = GLContext.getFunctionAddress("glDrawElementsBaseVertex");
        boolean bl = this.glDrawElementsBaseVertex != 0L;
        this.glDrawRangeElementsBaseVertex = GLContext.getFunctionAddress("glDrawRangeElementsBaseVertex");
        this.glDrawElementsInstancedBaseVertex = GLContext.getFunctionAddress("glDrawElementsInstancedBaseVertex");
        return bl & this.glDrawRangeElementsBaseVertex != 0L & this.glDrawElementsInstancedBaseVertex != 0L;
    }

    private boolean ARB_draw_indirect_initNativeFunctionAddresses() {
        this.glDrawArraysIndirect = GLContext.getFunctionAddress("glDrawArraysIndirect");
        boolean bl = this.glDrawArraysIndirect != 0L;
        this.glDrawElementsIndirect = GLContext.getFunctionAddress("glDrawElementsIndirect");
        return bl & this.glDrawElementsIndirect != 0L;
    }

    private boolean ARB_draw_instanced_initNativeFunctionAddresses() {
        this.glDrawArraysInstancedARB = GLContext.getFunctionAddress("glDrawArraysInstancedARB");
        boolean bl = this.glDrawArraysInstancedARB != 0L;
        this.glDrawElementsInstancedARB = GLContext.getFunctionAddress("glDrawElementsInstancedARB");
        return bl & this.glDrawElementsInstancedARB != 0L;
    }

    private boolean ARB_framebuffer_no_attachments_initNativeFunctionAddresses(Set<String> supported_extensions) {
        this.glFramebufferParameteri = GLContext.getFunctionAddress("glFramebufferParameteri");
        boolean bl = this.glFramebufferParameteri != 0L;
        this.glGetFramebufferParameteriv = GLContext.getFunctionAddress("glGetFramebufferParameteriv");
        return bl & this.glGetFramebufferParameteriv != 0L & (!supported_extensions.contains("GL_EXT_direct_state_access") || (this.glNamedFramebufferParameteriEXT = GLContext.getFunctionAddress("glNamedFramebufferParameteriEXT")) != 0L) & (!supported_extensions.contains("GL_EXT_direct_state_access") || (this.glGetNamedFramebufferParameterivEXT = GLContext.getFunctionAddress("glGetNamedFramebufferParameterivEXT")) != 0L);
    }

    private boolean ARB_framebuffer_object_initNativeFunctionAddresses() {
        this.glIsRenderbuffer = GLContext.getFunctionAddress("glIsRenderbuffer");
        boolean bl = this.glIsRenderbuffer != 0L;
        this.glBindRenderbuffer = GLContext.getFunctionAddress("glBindRenderbuffer");
        this.glDeleteRenderbuffers = GLContext.getFunctionAddress("glDeleteRenderbuffers");
        this.glGenRenderbuffers = GLContext.getFunctionAddress("glGenRenderbuffers");
        this.glRenderbufferStorage = GLContext.getFunctionAddress("glRenderbufferStorage");
        this.glRenderbufferStorageMultisample = GLContext.getFunctionAddress("glRenderbufferStorageMultisample");
        this.glGetRenderbufferParameteriv = GLContext.getFunctionAddress("glGetRenderbufferParameteriv");
        this.glIsFramebuffer = GLContext.getFunctionAddress("glIsFramebuffer");
        this.glBindFramebuffer = GLContext.getFunctionAddress("glBindFramebuffer");
        this.glDeleteFramebuffers = GLContext.getFunctionAddress("glDeleteFramebuffers");
        this.glGenFramebuffers = GLContext.getFunctionAddress("glGenFramebuffers");
        this.glCheckFramebufferStatus = GLContext.getFunctionAddress("glCheckFramebufferStatus");
        this.glFramebufferTexture1D = GLContext.getFunctionAddress("glFramebufferTexture1D");
        this.glFramebufferTexture2D = GLContext.getFunctionAddress("glFramebufferTexture2D");
        this.glFramebufferTexture3D = GLContext.getFunctionAddress("glFramebufferTexture3D");
        this.glFramebufferTextureLayer = GLContext.getFunctionAddress("glFramebufferTextureLayer");
        this.glFramebufferRenderbuffer = GLContext.getFunctionAddress("glFramebufferRenderbuffer");
        this.glGetFramebufferAttachmentParameteriv = GLContext.getFunctionAddress("glGetFramebufferAttachmentParameteriv");
        this.glBlitFramebuffer = GLContext.getFunctionAddress("glBlitFramebuffer");
        this.glGenerateMipmap = GLContext.getFunctionAddress("glGenerateMipmap");
        return bl & this.glBindRenderbuffer != 0L & this.glDeleteRenderbuffers != 0L & this.glGenRenderbuffers != 0L & this.glRenderbufferStorage != 0L & this.glRenderbufferStorageMultisample != 0L & this.glGetRenderbufferParameteriv != 0L & this.glIsFramebuffer != 0L & this.glBindFramebuffer != 0L & this.glDeleteFramebuffers != 0L & this.glGenFramebuffers != 0L & this.glCheckFramebufferStatus != 0L & this.glFramebufferTexture1D != 0L & this.glFramebufferTexture2D != 0L & this.glFramebufferTexture3D != 0L & this.glFramebufferTextureLayer != 0L & this.glFramebufferRenderbuffer != 0L & this.glGetFramebufferAttachmentParameteriv != 0L & this.glBlitFramebuffer != 0L & this.glGenerateMipmap != 0L;
    }

    private boolean ARB_geometry_shader4_initNativeFunctionAddresses() {
        this.glProgramParameteriARB = GLContext.getFunctionAddress("glProgramParameteriARB");
        boolean bl = this.glProgramParameteriARB != 0L;
        this.glFramebufferTextureARB = GLContext.getFunctionAddress("glFramebufferTextureARB");
        this.glFramebufferTextureLayerARB = GLContext.getFunctionAddress("glFramebufferTextureLayerARB");
        this.glFramebufferTextureFaceARB = GLContext.getFunctionAddress("glFramebufferTextureFaceARB");
        return bl & this.glFramebufferTextureARB != 0L & this.glFramebufferTextureLayerARB != 0L & this.glFramebufferTextureFaceARB != 0L;
    }

    private boolean ARB_get_program_binary_initNativeFunctionAddresses() {
        this.glGetProgramBinary = GLContext.getFunctionAddress("glGetProgramBinary");
        boolean bl = this.glGetProgramBinary != 0L;
        this.glProgramBinary = GLContext.getFunctionAddress("glProgramBinary");
        this.glProgramParameteri = GLContext.getFunctionAddress("glProgramParameteri");
        return bl & this.glProgramBinary != 0L & this.glProgramParameteri != 0L;
    }

    private boolean ARB_get_texture_sub_image_initNativeFunctionAddresses() {
        this.glGetTextureSubImage = GLContext.getFunctionAddress("glGetTextureSubImage");
        boolean bl = this.glGetTextureSubImage != 0L;
        this.glGetCompressedTextureSubImage = GLContext.getFunctionAddress("glGetCompressedTextureSubImage");
        return bl & this.glGetCompressedTextureSubImage != 0L;
    }

    private boolean ARB_gpu_shader_fp64_initNativeFunctionAddresses(Set<String> supported_extensions) {
        this.glUniform1d = GLContext.getFunctionAddress("glUniform1d");
        boolean bl = this.glUniform1d != 0L;
        this.glUniform2d = GLContext.getFunctionAddress("glUniform2d");
        this.glUniform3d = GLContext.getFunctionAddress("glUniform3d");
        this.glUniform4d = GLContext.getFunctionAddress("glUniform4d");
        this.glUniform1dv = GLContext.getFunctionAddress("glUniform1dv");
        this.glUniform2dv = GLContext.getFunctionAddress("glUniform2dv");
        this.glUniform3dv = GLContext.getFunctionAddress("glUniform3dv");
        this.glUniform4dv = GLContext.getFunctionAddress("glUniform4dv");
        this.glUniformMatrix2dv = GLContext.getFunctionAddress("glUniformMatrix2dv");
        this.glUniformMatrix3dv = GLContext.getFunctionAddress("glUniformMatrix3dv");
        this.glUniformMatrix4dv = GLContext.getFunctionAddress("glUniformMatrix4dv");
        this.glUniformMatrix2x3dv = GLContext.getFunctionAddress("glUniformMatrix2x3dv");
        this.glUniformMatrix2x4dv = GLContext.getFunctionAddress("glUniformMatrix2x4dv");
        this.glUniformMatrix3x2dv = GLContext.getFunctionAddress("glUniformMatrix3x2dv");
        this.glUniformMatrix3x4dv = GLContext.getFunctionAddress("glUniformMatrix3x4dv");
        this.glUniformMatrix4x2dv = GLContext.getFunctionAddress("glUniformMatrix4x2dv");
        this.glUniformMatrix4x3dv = GLContext.getFunctionAddress("glUniformMatrix4x3dv");
        this.glGetUniformdv = GLContext.getFunctionAddress("glGetUniformdv");
        return bl & this.glUniform2d != 0L & this.glUniform3d != 0L & this.glUniform4d != 0L & this.glUniform1dv != 0L & this.glUniform2dv != 0L & this.glUniform3dv != 0L & this.glUniform4dv != 0L & this.glUniformMatrix2dv != 0L & this.glUniformMatrix3dv != 0L & this.glUniformMatrix4dv != 0L & this.glUniformMatrix2x3dv != 0L & this.glUniformMatrix2x4dv != 0L & this.glUniformMatrix3x2dv != 0L & this.glUniformMatrix3x4dv != 0L & this.glUniformMatrix4x2dv != 0L & this.glUniformMatrix4x3dv != 0L & this.glGetUniformdv != 0L & (!supported_extensions.contains("GL_EXT_direct_state_access") || (this.glProgramUniform1dEXT = GLContext.getFunctionAddress("glProgramUniform1dEXT")) != 0L) & (!supported_extensions.contains("GL_EXT_direct_state_access") || (this.glProgramUniform2dEXT = GLContext.getFunctionAddress("glProgramUniform2dEXT")) != 0L) & (!supported_extensions.contains("GL_EXT_direct_state_access") || (this.glProgramUniform3dEXT = GLContext.getFunctionAddress("glProgramUniform3dEXT")) != 0L) & (!supported_extensions.contains("GL_EXT_direct_state_access") || (this.glProgramUniform4dEXT = GLContext.getFunctionAddress("glProgramUniform4dEXT")) != 0L) & (!supported_extensions.contains("GL_EXT_direct_state_access") || (this.glProgramUniform1dvEXT = GLContext.getFunctionAddress("glProgramUniform1dvEXT")) != 0L) & (!supported_extensions.contains("GL_EXT_direct_state_access") || (this.glProgramUniform2dvEXT = GLContext.getFunctionAddress("glProgramUniform2dvEXT")) != 0L) & (!supported_extensions.contains("GL_EXT_direct_state_access") || (this.glProgramUniform3dvEXT = GLContext.getFunctionAddress("glProgramUniform3dvEXT")) != 0L) & (!supported_extensions.contains("GL_EXT_direct_state_access") || (this.glProgramUniform4dvEXT = GLContext.getFunctionAddress("glProgramUniform4dvEXT")) != 0L) & (!supported_extensions.contains("GL_EXT_direct_state_access") || (this.glProgramUniformMatrix2dvEXT = GLContext.getFunctionAddress("glProgramUniformMatrix2dvEXT")) != 0L) & (!supported_extensions.contains("GL_EXT_direct_state_access") || (this.glProgramUniformMatrix3dvEXT = GLContext.getFunctionAddress("glProgramUniformMatrix3dvEXT")) != 0L) & (!supported_extensions.contains("GL_EXT_direct_state_access") || (this.glProgramUniformMatrix4dvEXT = GLContext.getFunctionAddress("glProgramUniformMatrix4dvEXT")) != 0L) & (!supported_extensions.contains("GL_EXT_direct_state_access") || (this.glProgramUniformMatrix2x3dvEXT = GLContext.getFunctionAddress("glProgramUniformMatrix2x3dvEXT")) != 0L) & (!supported_extensions.contains("GL_EXT_direct_state_access") || (this.glProgramUniformMatrix2x4dvEXT = GLContext.getFunctionAddress("glProgramUniformMatrix2x4dvEXT")) != 0L) & (!supported_extensions.contains("GL_EXT_direct_state_access") || (this.glProgramUniformMatrix3x2dvEXT = GLContext.getFunctionAddress("glProgramUniformMatrix3x2dvEXT")) != 0L) & (!supported_extensions.contains("GL_EXT_direct_state_access") || (this.glProgramUniformMatrix3x4dvEXT = GLContext.getFunctionAddress("glProgramUniformMatrix3x4dvEXT")) != 0L) & (!supported_extensions.contains("GL_EXT_direct_state_access") || (this.glProgramUniformMatrix4x2dvEXT = GLContext.getFunctionAddress("glProgramUniformMatrix4x2dvEXT")) != 0L) & (!supported_extensions.contains("GL_EXT_direct_state_access") || (this.glProgramUniformMatrix4x3dvEXT = GLContext.getFunctionAddress("glProgramUniformMatrix4x3dvEXT")) != 0L);
    }

    private boolean ARB_imaging_initNativeFunctionAddresses(boolean forwardCompatible) {
        boolean bl = (forwardCompatible || (this.glColorTable = GLContext.getFunctionAddress("glColorTable")) != 0L) & (forwardCompatible || (this.glColorSubTable = GLContext.getFunctionAddress("glColorSubTable")) != 0L) & (forwardCompatible || (this.glColorTableParameteriv = GLContext.getFunctionAddress("glColorTableParameteriv")) != 0L) & (forwardCompatible || (this.glColorTableParameterfv = GLContext.getFunctionAddress("glColorTableParameterfv")) != 0L) & (forwardCompatible || (this.glCopyColorSubTable = GLContext.getFunctionAddress("glCopyColorSubTable")) != 0L) & (forwardCompatible || (this.glCopyColorTable = GLContext.getFunctionAddress("glCopyColorTable")) != 0L) & (forwardCompatible || (this.glGetColorTable = GLContext.getFunctionAddress("glGetColorTable")) != 0L) & (forwardCompatible || (this.glGetColorTableParameteriv = GLContext.getFunctionAddress("glGetColorTableParameteriv")) != 0L) & (forwardCompatible || (this.glGetColorTableParameterfv = GLContext.getFunctionAddress("glGetColorTableParameterfv")) != 0L);
        this.glBlendEquation = GLContext.getFunctionAddress("glBlendEquation");
        this.glBlendColor = GLContext.getFunctionAddress("glBlendColor");
        return bl & this.glBlendEquation != 0L & this.glBlendColor != 0L & (forwardCompatible || (this.glHistogram = GLContext.getFunctionAddress("glHistogram")) != 0L) & (forwardCompatible || (this.glResetHistogram = GLContext.getFunctionAddress("glResetHistogram")) != 0L) & (forwardCompatible || (this.glGetHistogram = GLContext.getFunctionAddress("glGetHistogram")) != 0L) & (forwardCompatible || (this.glGetHistogramParameterfv = GLContext.getFunctionAddress("glGetHistogramParameterfv")) != 0L) & (forwardCompatible || (this.glGetHistogramParameteriv = GLContext.getFunctionAddress("glGetHistogramParameteriv")) != 0L) & (forwardCompatible || (this.glMinmax = GLContext.getFunctionAddress("glMinmax")) != 0L) & (forwardCompatible || (this.glResetMinmax = GLContext.getFunctionAddress("glResetMinmax")) != 0L) & (forwardCompatible || (this.glGetMinmax = GLContext.getFunctionAddress("glGetMinmax")) != 0L) & (forwardCompatible || (this.glGetMinmaxParameterfv = GLContext.getFunctionAddress("glGetMinmaxParameterfv")) != 0L) & (forwardCompatible || (this.glGetMinmaxParameteriv = GLContext.getFunctionAddress("glGetMinmaxParameteriv")) != 0L) & (forwardCompatible || (this.glConvolutionFilter1D = GLContext.getFunctionAddress("glConvolutionFilter1D")) != 0L) & (forwardCompatible || (this.glConvolutionFilter2D = GLContext.getFunctionAddress("glConvolutionFilter2D")) != 0L) & (forwardCompatible || (this.glConvolutionParameterf = GLContext.getFunctionAddress("glConvolutionParameterf")) != 0L) & (forwardCompatible || (this.glConvolutionParameterfv = GLContext.getFunctionAddress("glConvolutionParameterfv")) != 0L) & (forwardCompatible || (this.glConvolutionParameteri = GLContext.getFunctionAddress("glConvolutionParameteri")) != 0L) & (forwardCompatible || (this.glConvolutionParameteriv = GLContext.getFunctionAddress("glConvolutionParameteriv")) != 0L) & (forwardCompatible || (this.glCopyConvolutionFilter1D = GLContext.getFunctionAddress("glCopyConvolutionFilter1D")) != 0L) & (forwardCompatible || (this.glCopyConvolutionFilter2D = GLContext.getFunctionAddress("glCopyConvolutionFilter2D")) != 0L) & (forwardCompatible || (this.glGetConvolutionFilter = GLContext.getFunctionAddress("glGetConvolutionFilter")) != 0L) & (forwardCompatible || (this.glGetConvolutionParameterfv = GLContext.getFunctionAddress("glGetConvolutionParameterfv")) != 0L) & (forwardCompatible || (this.glGetConvolutionParameteriv = GLContext.getFunctionAddress("glGetConvolutionParameteriv")) != 0L) & (forwardCompatible || (this.glSeparableFilter2D = GLContext.getFunctionAddress("glSeparableFilter2D")) != 0L) & (forwardCompatible || (this.glGetSeparableFilter = GLContext.getFunctionAddress("glGetSeparableFilter")) != 0L);
    }

    private boolean ARB_indirect_parameters_initNativeFunctionAddresses() {
        this.glMultiDrawArraysIndirectCountARB = GLContext.getFunctionAddress("glMultiDrawArraysIndirectCountARB");
        boolean bl = this.glMultiDrawArraysIndirectCountARB != 0L;
        this.glMultiDrawElementsIndirectCountARB = GLContext.getFunctionAddress("glMultiDrawElementsIndirectCountARB");
        return bl & this.glMultiDrawElementsIndirectCountARB != 0L;
    }

    private boolean ARB_instanced_arrays_initNativeFunctionAddresses() {
        this.glVertexAttribDivisorARB = GLContext.getFunctionAddress("glVertexAttribDivisorARB");
        return this.glVertexAttribDivisorARB != 0L;
    }

    private boolean ARB_internalformat_query_initNativeFunctionAddresses() {
        this.glGetInternalformativ = GLContext.getFunctionAddress("glGetInternalformativ");
        return this.glGetInternalformativ != 0L;
    }

    private boolean ARB_internalformat_query2_initNativeFunctionAddresses() {
        this.glGetInternalformati64v = GLContext.getFunctionAddress("glGetInternalformati64v");
        return this.glGetInternalformati64v != 0L;
    }

    private boolean ARB_invalidate_subdata_initNativeFunctionAddresses() {
        this.glInvalidateTexSubImage = GLContext.getFunctionAddress("glInvalidateTexSubImage");
        boolean bl = this.glInvalidateTexSubImage != 0L;
        this.glInvalidateTexImage = GLContext.getFunctionAddress("glInvalidateTexImage");
        this.glInvalidateBufferSubData = GLContext.getFunctionAddress("glInvalidateBufferSubData");
        this.glInvalidateBufferData = GLContext.getFunctionAddress("glInvalidateBufferData");
        this.glInvalidateFramebuffer = GLContext.getFunctionAddress("glInvalidateFramebuffer");
        this.glInvalidateSubFramebuffer = GLContext.getFunctionAddress("glInvalidateSubFramebuffer");
        return bl & this.glInvalidateTexImage != 0L & this.glInvalidateBufferSubData != 0L & this.glInvalidateBufferData != 0L & this.glInvalidateFramebuffer != 0L & this.glInvalidateSubFramebuffer != 0L;
    }

    private boolean ARB_map_buffer_range_initNativeFunctionAddresses() {
        this.glMapBufferRange = GLContext.getFunctionAddress("glMapBufferRange");
        boolean bl = this.glMapBufferRange != 0L;
        this.glFlushMappedBufferRange = GLContext.getFunctionAddress("glFlushMappedBufferRange");
        return bl & this.glFlushMappedBufferRange != 0L;
    }

    private boolean ARB_matrix_palette_initNativeFunctionAddresses() {
        this.glCurrentPaletteMatrixARB = GLContext.getFunctionAddress("glCurrentPaletteMatrixARB");
        boolean bl = this.glCurrentPaletteMatrixARB != 0L;
        this.glMatrixIndexPointerARB = GLContext.getFunctionAddress("glMatrixIndexPointerARB");
        this.glMatrixIndexubvARB = GLContext.getFunctionAddress("glMatrixIndexubvARB");
        this.glMatrixIndexusvARB = GLContext.getFunctionAddress("glMatrixIndexusvARB");
        this.glMatrixIndexuivARB = GLContext.getFunctionAddress("glMatrixIndexuivARB");
        return bl & this.glMatrixIndexPointerARB != 0L & this.glMatrixIndexubvARB != 0L & this.glMatrixIndexusvARB != 0L & this.glMatrixIndexuivARB != 0L;
    }

    private boolean ARB_multi_bind_initNativeFunctionAddresses() {
        this.glBindBuffersBase = GLContext.getFunctionAddress("glBindBuffersBase");
        boolean bl = this.glBindBuffersBase != 0L;
        this.glBindBuffersRange = GLContext.getFunctionAddress("glBindBuffersRange");
        this.glBindTextures = GLContext.getFunctionAddress("glBindTextures");
        this.glBindSamplers = GLContext.getFunctionAddress("glBindSamplers");
        this.glBindImageTextures = GLContext.getFunctionAddress("glBindImageTextures");
        this.glBindVertexBuffers = GLContext.getFunctionAddress("glBindVertexBuffers");
        return bl & this.glBindBuffersRange != 0L & this.glBindTextures != 0L & this.glBindSamplers != 0L & this.glBindImageTextures != 0L & this.glBindVertexBuffers != 0L;
    }

    private boolean ARB_multi_draw_indirect_initNativeFunctionAddresses() {
        this.glMultiDrawArraysIndirect = GLContext.getFunctionAddress("glMultiDrawArraysIndirect");
        boolean bl = this.glMultiDrawArraysIndirect != 0L;
        this.glMultiDrawElementsIndirect = GLContext.getFunctionAddress("glMultiDrawElementsIndirect");
        return bl & this.glMultiDrawElementsIndirect != 0L;
    }

    private boolean ARB_multisample_initNativeFunctionAddresses() {
        this.glSampleCoverageARB = GLContext.getFunctionAddress("glSampleCoverageARB");
        return this.glSampleCoverageARB != 0L;
    }

    private boolean ARB_multitexture_initNativeFunctionAddresses() {
        this.glClientActiveTextureARB = GLContext.getFunctionAddress("glClientActiveTextureARB");
        boolean bl = this.glClientActiveTextureARB != 0L;
        this.glActiveTextureARB = GLContext.getFunctionAddress("glActiveTextureARB");
        this.glMultiTexCoord1fARB = GLContext.getFunctionAddress("glMultiTexCoord1fARB");
        this.glMultiTexCoord1dARB = GLContext.getFunctionAddress("glMultiTexCoord1dARB");
        this.glMultiTexCoord1iARB = GLContext.getFunctionAddress("glMultiTexCoord1iARB");
        this.glMultiTexCoord1sARB = GLContext.getFunctionAddress("glMultiTexCoord1sARB");
        this.glMultiTexCoord2fARB = GLContext.getFunctionAddress("glMultiTexCoord2fARB");
        this.glMultiTexCoord2dARB = GLContext.getFunctionAddress("glMultiTexCoord2dARB");
        this.glMultiTexCoord2iARB = GLContext.getFunctionAddress("glMultiTexCoord2iARB");
        this.glMultiTexCoord2sARB = GLContext.getFunctionAddress("glMultiTexCoord2sARB");
        this.glMultiTexCoord3fARB = GLContext.getFunctionAddress("glMultiTexCoord3fARB");
        this.glMultiTexCoord3dARB = GLContext.getFunctionAddress("glMultiTexCoord3dARB");
        this.glMultiTexCoord3iARB = GLContext.getFunctionAddress("glMultiTexCoord3iARB");
        this.glMultiTexCoord3sARB = GLContext.getFunctionAddress("glMultiTexCoord3sARB");
        this.glMultiTexCoord4fARB = GLContext.getFunctionAddress("glMultiTexCoord4fARB");
        this.glMultiTexCoord4dARB = GLContext.getFunctionAddress("glMultiTexCoord4dARB");
        this.glMultiTexCoord4iARB = GLContext.getFunctionAddress("glMultiTexCoord4iARB");
        this.glMultiTexCoord4sARB = GLContext.getFunctionAddress("glMultiTexCoord4sARB");
        return bl & this.glActiveTextureARB != 0L & this.glMultiTexCoord1fARB != 0L & this.glMultiTexCoord1dARB != 0L & this.glMultiTexCoord1iARB != 0L & this.glMultiTexCoord1sARB != 0L & this.glMultiTexCoord2fARB != 0L & this.glMultiTexCoord2dARB != 0L & this.glMultiTexCoord2iARB != 0L & this.glMultiTexCoord2sARB != 0L & this.glMultiTexCoord3fARB != 0L & this.glMultiTexCoord3dARB != 0L & this.glMultiTexCoord3iARB != 0L & this.glMultiTexCoord3sARB != 0L & this.glMultiTexCoord4fARB != 0L & this.glMultiTexCoord4dARB != 0L & this.glMultiTexCoord4iARB != 0L & this.glMultiTexCoord4sARB != 0L;
    }

    private boolean ARB_occlusion_query_initNativeFunctionAddresses() {
        this.glGenQueriesARB = GLContext.getFunctionAddress("glGenQueriesARB");
        boolean bl = this.glGenQueriesARB != 0L;
        this.glDeleteQueriesARB = GLContext.getFunctionAddress("glDeleteQueriesARB");
        this.glIsQueryARB = GLContext.getFunctionAddress("glIsQueryARB");
        this.glBeginQueryARB = GLContext.getFunctionAddress("glBeginQueryARB");
        this.glEndQueryARB = GLContext.getFunctionAddress("glEndQueryARB");
        this.glGetQueryivARB = GLContext.getFunctionAddress("glGetQueryivARB");
        this.glGetQueryObjectivARB = GLContext.getFunctionAddress("glGetQueryObjectivARB");
        this.glGetQueryObjectuivARB = GLContext.getFunctionAddress("glGetQueryObjectuivARB");
        return bl & this.glDeleteQueriesARB != 0L & this.glIsQueryARB != 0L & this.glBeginQueryARB != 0L & this.glEndQueryARB != 0L & this.glGetQueryivARB != 0L & this.glGetQueryObjectivARB != 0L & this.glGetQueryObjectuivARB != 0L;
    }

    private boolean ARB_point_parameters_initNativeFunctionAddresses() {
        this.glPointParameterfARB = GLContext.getFunctionAddress("glPointParameterfARB");
        boolean bl = this.glPointParameterfARB != 0L;
        this.glPointParameterfvARB = GLContext.getFunctionAddress("glPointParameterfvARB");
        return bl & this.glPointParameterfvARB != 0L;
    }

    private boolean ARB_program_initNativeFunctionAddresses() {
        this.glProgramStringARB = GLContext.getFunctionAddress("glProgramStringARB");
        boolean bl = this.glProgramStringARB != 0L;
        this.glBindProgramARB = GLContext.getFunctionAddress("glBindProgramARB");
        this.glDeleteProgramsARB = GLContext.getFunctionAddress("glDeleteProgramsARB");
        this.glGenProgramsARB = GLContext.getFunctionAddress("glGenProgramsARB");
        this.glProgramEnvParameter4fARB = GLContext.getFunctionAddress("glProgramEnvParameter4fARB");
        this.glProgramEnvParameter4dARB = GLContext.getFunctionAddress("glProgramEnvParameter4dARB");
        this.glProgramEnvParameter4fvARB = GLContext.getFunctionAddress("glProgramEnvParameter4fvARB");
        this.glProgramEnvParameter4dvARB = GLContext.getFunctionAddress("glProgramEnvParameter4dvARB");
        this.glProgramLocalParameter4fARB = GLContext.getFunctionAddress("glProgramLocalParameter4fARB");
        this.glProgramLocalParameter4dARB = GLContext.getFunctionAddress("glProgramLocalParameter4dARB");
        this.glProgramLocalParameter4fvARB = GLContext.getFunctionAddress("glProgramLocalParameter4fvARB");
        this.glProgramLocalParameter4dvARB = GLContext.getFunctionAddress("glProgramLocalParameter4dvARB");
        this.glGetProgramEnvParameterfvARB = GLContext.getFunctionAddress("glGetProgramEnvParameterfvARB");
        this.glGetProgramEnvParameterdvARB = GLContext.getFunctionAddress("glGetProgramEnvParameterdvARB");
        this.glGetProgramLocalParameterfvARB = GLContext.getFunctionAddress("glGetProgramLocalParameterfvARB");
        this.glGetProgramLocalParameterdvARB = GLContext.getFunctionAddress("glGetProgramLocalParameterdvARB");
        this.glGetProgramivARB = GLContext.getFunctionAddress("glGetProgramivARB");
        this.glGetProgramStringARB = GLContext.getFunctionAddress("glGetProgramStringARB");
        this.glIsProgramARB = GLContext.getFunctionAddress("glIsProgramARB");
        return bl & this.glBindProgramARB != 0L & this.glDeleteProgramsARB != 0L & this.glGenProgramsARB != 0L & this.glProgramEnvParameter4fARB != 0L & this.glProgramEnvParameter4dARB != 0L & this.glProgramEnvParameter4fvARB != 0L & this.glProgramEnvParameter4dvARB != 0L & this.glProgramLocalParameter4fARB != 0L & this.glProgramLocalParameter4dARB != 0L & this.glProgramLocalParameter4fvARB != 0L & this.glProgramLocalParameter4dvARB != 0L & this.glGetProgramEnvParameterfvARB != 0L & this.glGetProgramEnvParameterdvARB != 0L & this.glGetProgramLocalParameterfvARB != 0L & this.glGetProgramLocalParameterdvARB != 0L & this.glGetProgramivARB != 0L & this.glGetProgramStringARB != 0L & this.glIsProgramARB != 0L;
    }

    private boolean ARB_program_interface_query_initNativeFunctionAddresses() {
        this.glGetProgramInterfaceiv = GLContext.getFunctionAddress("glGetProgramInterfaceiv");
        boolean bl = this.glGetProgramInterfaceiv != 0L;
        this.glGetProgramResourceIndex = GLContext.getFunctionAddress("glGetProgramResourceIndex");
        this.glGetProgramResourceName = GLContext.getFunctionAddress("glGetProgramResourceName");
        this.glGetProgramResourceiv = GLContext.getFunctionAddress("glGetProgramResourceiv");
        this.glGetProgramResourceLocation = GLContext.getFunctionAddress("glGetProgramResourceLocation");
        this.glGetProgramResourceLocationIndex = GLContext.getFunctionAddress("glGetProgramResourceLocationIndex");
        return bl & this.glGetProgramResourceIndex != 0L & this.glGetProgramResourceName != 0L & this.glGetProgramResourceiv != 0L & this.glGetProgramResourceLocation != 0L & this.glGetProgramResourceLocationIndex != 0L;
    }

    private boolean ARB_provoking_vertex_initNativeFunctionAddresses() {
        this.glProvokingVertex = GLContext.getFunctionAddress("glProvokingVertex");
        return this.glProvokingVertex != 0L;
    }

    private boolean ARB_robustness_initNativeFunctionAddresses(boolean forwardCompatible, Set<String> supported_extensions) {
        this.glGetGraphicsResetStatusARB = GLContext.getFunctionAddress("glGetGraphicsResetStatusARB");
        boolean bl = this.glGetGraphicsResetStatusARB != 0L & (forwardCompatible || (this.glGetnMapdvARB = GLContext.getFunctionAddress("glGetnMapdvARB")) != 0L) & (forwardCompatible || (this.glGetnMapfvARB = GLContext.getFunctionAddress("glGetnMapfvARB")) != 0L) & (forwardCompatible || (this.glGetnMapivARB = GLContext.getFunctionAddress("glGetnMapivARB")) != 0L) & (forwardCompatible || (this.glGetnPixelMapfvARB = GLContext.getFunctionAddress("glGetnPixelMapfvARB")) != 0L) & (forwardCompatible || (this.glGetnPixelMapuivARB = GLContext.getFunctionAddress("glGetnPixelMapuivARB")) != 0L) & (forwardCompatible || (this.glGetnPixelMapusvARB = GLContext.getFunctionAddress("glGetnPixelMapusvARB")) != 0L) & (forwardCompatible || (this.glGetnPolygonStippleARB = GLContext.getFunctionAddress("glGetnPolygonStippleARB")) != 0L);
        this.glGetnTexImageARB = GLContext.getFunctionAddress("glGetnTexImageARB");
        this.glReadnPixelsARB = GLContext.getFunctionAddress("glReadnPixelsARB");
        return bl & this.glGetnTexImageARB != 0L & this.glReadnPixelsARB != 0L & (!supported_extensions.contains("GL_ARB_imaging") || (this.glGetnColorTableARB = GLContext.getFunctionAddress("glGetnColorTableARB")) != 0L) & (!supported_extensions.contains("GL_ARB_imaging") || (this.glGetnConvolutionFilterARB = GLContext.getFunctionAddress("glGetnConvolutionFilterARB")) != 0L) & (!supported_extensions.contains("GL_ARB_imaging") || (this.glGetnSeparableFilterARB = GLContext.getFunctionAddress("glGetnSeparableFilterARB")) != 0L) & (!supported_extensions.contains("GL_ARB_imaging") || (this.glGetnHistogramARB = GLContext.getFunctionAddress("glGetnHistogramARB")) != 0L) & (!supported_extensions.contains("GL_ARB_imaging") || (this.glGetnMinmaxARB = GLContext.getFunctionAddress("glGetnMinmaxARB")) != 0L) & (!supported_extensions.contains("OpenGL13") || (this.glGetnCompressedTexImageARB = GLContext.getFunctionAddress("glGetnCompressedTexImageARB")) != 0L) & (!supported_extensions.contains("OpenGL20") || (this.glGetnUniformfvARB = GLContext.getFunctionAddress("glGetnUniformfvARB")) != 0L) & (!supported_extensions.contains("OpenGL20") || (this.glGetnUniformivARB = GLContext.getFunctionAddress("glGetnUniformivARB")) != 0L) & (!supported_extensions.contains("OpenGL20") || (this.glGetnUniformuivARB = GLContext.getFunctionAddress("glGetnUniformuivARB")) != 0L) & (!supported_extensions.contains("OpenGL20") || (this.glGetnUniformdvARB = GLContext.getFunctionAddress("glGetnUniformdvARB")) != 0L);
    }

    private boolean ARB_sample_shading_initNativeFunctionAddresses() {
        this.glMinSampleShadingARB = GLContext.getFunctionAddress("glMinSampleShadingARB");
        return this.glMinSampleShadingARB != 0L;
    }

    private boolean ARB_sampler_objects_initNativeFunctionAddresses() {
        this.glGenSamplers = GLContext.getFunctionAddress("glGenSamplers");
        boolean bl = this.glGenSamplers != 0L;
        this.glDeleteSamplers = GLContext.getFunctionAddress("glDeleteSamplers");
        this.glIsSampler = GLContext.getFunctionAddress("glIsSampler");
        this.glBindSampler = GLContext.getFunctionAddress("glBindSampler");
        this.glSamplerParameteri = GLContext.getFunctionAddress("glSamplerParameteri");
        this.glSamplerParameterf = GLContext.getFunctionAddress("glSamplerParameterf");
        this.glSamplerParameteriv = GLContext.getFunctionAddress("glSamplerParameteriv");
        this.glSamplerParameterfv = GLContext.getFunctionAddress("glSamplerParameterfv");
        this.glSamplerParameterIiv = GLContext.getFunctionAddress("glSamplerParameterIiv");
        this.glSamplerParameterIuiv = GLContext.getFunctionAddress("glSamplerParameterIuiv");
        this.glGetSamplerParameteriv = GLContext.getFunctionAddress("glGetSamplerParameteriv");
        this.glGetSamplerParameterfv = GLContext.getFunctionAddress("glGetSamplerParameterfv");
        this.glGetSamplerParameterIiv = GLContext.getFunctionAddress("glGetSamplerParameterIiv");
        this.glGetSamplerParameterIuiv = GLContext.getFunctionAddress("glGetSamplerParameterIuiv");
        return bl & this.glDeleteSamplers != 0L & this.glIsSampler != 0L & this.glBindSampler != 0L & this.glSamplerParameteri != 0L & this.glSamplerParameterf != 0L & this.glSamplerParameteriv != 0L & this.glSamplerParameterfv != 0L & this.glSamplerParameterIiv != 0L & this.glSamplerParameterIuiv != 0L & this.glGetSamplerParameteriv != 0L & this.glGetSamplerParameterfv != 0L & this.glGetSamplerParameterIiv != 0L & this.glGetSamplerParameterIuiv != 0L;
    }

    private boolean ARB_separate_shader_objects_initNativeFunctionAddresses() {
        this.glUseProgramStages = GLContext.getFunctionAddress("glUseProgramStages");
        boolean bl = this.glUseProgramStages != 0L;
        this.glActiveShaderProgram = GLContext.getFunctionAddress("glActiveShaderProgram");
        this.glCreateShaderProgramv = GLContext.getFunctionAddress("glCreateShaderProgramv");
        this.glBindProgramPipeline = GLContext.getFunctionAddress("glBindProgramPipeline");
        this.glDeleteProgramPipelines = GLContext.getFunctionAddress("glDeleteProgramPipelines");
        this.glGenProgramPipelines = GLContext.getFunctionAddress("glGenProgramPipelines");
        this.glIsProgramPipeline = GLContext.getFunctionAddress("glIsProgramPipeline");
        this.glProgramParameteri = GLContext.getFunctionAddress("glProgramParameteri");
        this.glGetProgramPipelineiv = GLContext.getFunctionAddress("glGetProgramPipelineiv");
        this.glProgramUniform1i = GLContext.getFunctionAddress("glProgramUniform1i");
        this.glProgramUniform2i = GLContext.getFunctionAddress("glProgramUniform2i");
        this.glProgramUniform3i = GLContext.getFunctionAddress("glProgramUniform3i");
        this.glProgramUniform4i = GLContext.getFunctionAddress("glProgramUniform4i");
        this.glProgramUniform1f = GLContext.getFunctionAddress("glProgramUniform1f");
        this.glProgramUniform2f = GLContext.getFunctionAddress("glProgramUniform2f");
        this.glProgramUniform3f = GLContext.getFunctionAddress("glProgramUniform3f");
        this.glProgramUniform4f = GLContext.getFunctionAddress("glProgramUniform4f");
        this.glProgramUniform1d = GLContext.getFunctionAddress("glProgramUniform1d");
        this.glProgramUniform2d = GLContext.getFunctionAddress("glProgramUniform2d");
        this.glProgramUniform3d = GLContext.getFunctionAddress("glProgramUniform3d");
        this.glProgramUniform4d = GLContext.getFunctionAddress("glProgramUniform4d");
        this.glProgramUniform1iv = GLContext.getFunctionAddress("glProgramUniform1iv");
        this.glProgramUniform2iv = GLContext.getFunctionAddress("glProgramUniform2iv");
        this.glProgramUniform3iv = GLContext.getFunctionAddress("glProgramUniform3iv");
        this.glProgramUniform4iv = GLContext.getFunctionAddress("glProgramUniform4iv");
        this.glProgramUniform1fv = GLContext.getFunctionAddress("glProgramUniform1fv");
        this.glProgramUniform2fv = GLContext.getFunctionAddress("glProgramUniform2fv");
        this.glProgramUniform3fv = GLContext.getFunctionAddress("glProgramUniform3fv");
        this.glProgramUniform4fv = GLContext.getFunctionAddress("glProgramUniform4fv");
        this.glProgramUniform1dv = GLContext.getFunctionAddress("glProgramUniform1dv");
        this.glProgramUniform2dv = GLContext.getFunctionAddress("glProgramUniform2dv");
        this.glProgramUniform3dv = GLContext.getFunctionAddress("glProgramUniform3dv");
        this.glProgramUniform4dv = GLContext.getFunctionAddress("glProgramUniform4dv");
        this.glProgramUniform1ui = GLContext.getFunctionAddress("glProgramUniform1ui");
        this.glProgramUniform2ui = GLContext.getFunctionAddress("glProgramUniform2ui");
        this.glProgramUniform3ui = GLContext.getFunctionAddress("glProgramUniform3ui");
        this.glProgramUniform4ui = GLContext.getFunctionAddress("glProgramUniform4ui");
        this.glProgramUniform1uiv = GLContext.getFunctionAddress("glProgramUniform1uiv");
        this.glProgramUniform2uiv = GLContext.getFunctionAddress("glProgramUniform2uiv");
        this.glProgramUniform3uiv = GLContext.getFunctionAddress("glProgramUniform3uiv");
        this.glProgramUniform4uiv = GLContext.getFunctionAddress("glProgramUniform4uiv");
        this.glProgramUniformMatrix2fv = GLContext.getFunctionAddress("glProgramUniformMatrix2fv");
        this.glProgramUniformMatrix3fv = GLContext.getFunctionAddress("glProgramUniformMatrix3fv");
        this.glProgramUniformMatrix4fv = GLContext.getFunctionAddress("glProgramUniformMatrix4fv");
        this.glProgramUniformMatrix2dv = GLContext.getFunctionAddress("glProgramUniformMatrix2dv");
        this.glProgramUniformMatrix3dv = GLContext.getFunctionAddress("glProgramUniformMatrix3dv");
        this.glProgramUniformMatrix4dv = GLContext.getFunctionAddress("glProgramUniformMatrix4dv");
        this.glProgramUniformMatrix2x3fv = GLContext.getFunctionAddress("glProgramUniformMatrix2x3fv");
        this.glProgramUniformMatrix3x2fv = GLContext.getFunctionAddress("glProgramUniformMatrix3x2fv");
        this.glProgramUniformMatrix2x4fv = GLContext.getFunctionAddress("glProgramUniformMatrix2x4fv");
        this.glProgramUniformMatrix4x2fv = GLContext.getFunctionAddress("glProgramUniformMatrix4x2fv");
        this.glProgramUniformMatrix3x4fv = GLContext.getFunctionAddress("glProgramUniformMatrix3x4fv");
        this.glProgramUniformMatrix4x3fv = GLContext.getFunctionAddress("glProgramUniformMatrix4x3fv");
        this.glProgramUniformMatrix2x3dv = GLContext.getFunctionAddress("glProgramUniformMatrix2x3dv");
        this.glProgramUniformMatrix3x2dv = GLContext.getFunctionAddress("glProgramUniformMatrix3x2dv");
        this.glProgramUniformMatrix2x4dv = GLContext.getFunctionAddress("glProgramUniformMatrix2x4dv");
        this.glProgramUniformMatrix4x2dv = GLContext.getFunctionAddress("glProgramUniformMatrix4x2dv");
        this.glProgramUniformMatrix3x4dv = GLContext.getFunctionAddress("glProgramUniformMatrix3x4dv");
        this.glProgramUniformMatrix4x3dv = GLContext.getFunctionAddress("glProgramUniformMatrix4x3dv");
        this.glValidateProgramPipeline = GLContext.getFunctionAddress("glValidateProgramPipeline");
        this.glGetProgramPipelineInfoLog = GLContext.getFunctionAddress("glGetProgramPipelineInfoLog");
        return bl & this.glActiveShaderProgram != 0L & this.glCreateShaderProgramv != 0L & this.glBindProgramPipeline != 0L & this.glDeleteProgramPipelines != 0L & this.glGenProgramPipelines != 0L & this.glIsProgramPipeline != 0L & this.glProgramParameteri != 0L & this.glGetProgramPipelineiv != 0L & this.glProgramUniform1i != 0L & this.glProgramUniform2i != 0L & this.glProgramUniform3i != 0L & this.glProgramUniform4i != 0L & this.glProgramUniform1f != 0L & this.glProgramUniform2f != 0L & this.glProgramUniform3f != 0L & this.glProgramUniform4f != 0L & this.glProgramUniform1d != 0L & this.glProgramUniform2d != 0L & this.glProgramUniform3d != 0L & this.glProgramUniform4d != 0L & this.glProgramUniform1iv != 0L & this.glProgramUniform2iv != 0L & this.glProgramUniform3iv != 0L & this.glProgramUniform4iv != 0L & this.glProgramUniform1fv != 0L & this.glProgramUniform2fv != 0L & this.glProgramUniform3fv != 0L & this.glProgramUniform4fv != 0L & this.glProgramUniform1dv != 0L & this.glProgramUniform2dv != 0L & this.glProgramUniform3dv != 0L & this.glProgramUniform4dv != 0L & this.glProgramUniform1ui != 0L & this.glProgramUniform2ui != 0L & this.glProgramUniform3ui != 0L & this.glProgramUniform4ui != 0L & this.glProgramUniform1uiv != 0L & this.glProgramUniform2uiv != 0L & this.glProgramUniform3uiv != 0L & this.glProgramUniform4uiv != 0L & this.glProgramUniformMatrix2fv != 0L & this.glProgramUniformMatrix3fv != 0L & this.glProgramUniformMatrix4fv != 0L & this.glProgramUniformMatrix2dv != 0L & this.glProgramUniformMatrix3dv != 0L & this.glProgramUniformMatrix4dv != 0L & this.glProgramUniformMatrix2x3fv != 0L & this.glProgramUniformMatrix3x2fv != 0L & this.glProgramUniformMatrix2x4fv != 0L & this.glProgramUniformMatrix4x2fv != 0L & this.glProgramUniformMatrix3x4fv != 0L & this.glProgramUniformMatrix4x3fv != 0L & this.glProgramUniformMatrix2x3dv != 0L & this.glProgramUniformMatrix3x2dv != 0L & this.glProgramUniformMatrix2x4dv != 0L & this.glProgramUniformMatrix4x2dv != 0L & this.glProgramUniformMatrix3x4dv != 0L & this.glProgramUniformMatrix4x3dv != 0L & this.glValidateProgramPipeline != 0L & this.glGetProgramPipelineInfoLog != 0L;
    }

    private boolean ARB_shader_atomic_counters_initNativeFunctionAddresses() {
        this.glGetActiveAtomicCounterBufferiv = GLContext.getFunctionAddress("glGetActiveAtomicCounterBufferiv");
        return this.glGetActiveAtomicCounterBufferiv != 0L;
    }

    private boolean ARB_shader_image_load_store_initNativeFunctionAddresses() {
        this.glBindImageTexture = GLContext.getFunctionAddress("glBindImageTexture");
        boolean bl = this.glBindImageTexture != 0L;
        this.glMemoryBarrier = GLContext.getFunctionAddress("glMemoryBarrier");
        return bl & this.glMemoryBarrier != 0L;
    }

    private boolean ARB_shader_objects_initNativeFunctionAddresses() {
        this.glDeleteObjectARB = GLContext.getFunctionAddress("glDeleteObjectARB");
        boolean bl = this.glDeleteObjectARB != 0L;
        this.glGetHandleARB = GLContext.getFunctionAddress("glGetHandleARB");
        this.glDetachObjectARB = GLContext.getFunctionAddress("glDetachObjectARB");
        this.glCreateShaderObjectARB = GLContext.getFunctionAddress("glCreateShaderObjectARB");
        this.glShaderSourceARB = GLContext.getFunctionAddress("glShaderSourceARB");
        this.glCompileShaderARB = GLContext.getFunctionAddress("glCompileShaderARB");
        this.glCreateProgramObjectARB = GLContext.getFunctionAddress("glCreateProgramObjectARB");
        this.glAttachObjectARB = GLContext.getFunctionAddress("glAttachObjectARB");
        this.glLinkProgramARB = GLContext.getFunctionAddress("glLinkProgramARB");
        this.glUseProgramObjectARB = GLContext.getFunctionAddress("glUseProgramObjectARB");
        this.glValidateProgramARB = GLContext.getFunctionAddress("glValidateProgramARB");
        this.glUniform1fARB = GLContext.getFunctionAddress("glUniform1fARB");
        this.glUniform2fARB = GLContext.getFunctionAddress("glUniform2fARB");
        this.glUniform3fARB = GLContext.getFunctionAddress("glUniform3fARB");
        this.glUniform4fARB = GLContext.getFunctionAddress("glUniform4fARB");
        this.glUniform1iARB = GLContext.getFunctionAddress("glUniform1iARB");
        this.glUniform2iARB = GLContext.getFunctionAddress("glUniform2iARB");
        this.glUniform3iARB = GLContext.getFunctionAddress("glUniform3iARB");
        this.glUniform4iARB = GLContext.getFunctionAddress("glUniform4iARB");
        this.glUniform1fvARB = GLContext.getFunctionAddress("glUniform1fvARB");
        this.glUniform2fvARB = GLContext.getFunctionAddress("glUniform2fvARB");
        this.glUniform3fvARB = GLContext.getFunctionAddress("glUniform3fvARB");
        this.glUniform4fvARB = GLContext.getFunctionAddress("glUniform4fvARB");
        this.glUniform1ivARB = GLContext.getFunctionAddress("glUniform1ivARB");
        this.glUniform2ivARB = GLContext.getFunctionAddress("glUniform2ivARB");
        this.glUniform3ivARB = GLContext.getFunctionAddress("glUniform3ivARB");
        this.glUniform4ivARB = GLContext.getFunctionAddress("glUniform4ivARB");
        this.glUniformMatrix2fvARB = GLContext.getFunctionAddress("glUniformMatrix2fvARB");
        this.glUniformMatrix3fvARB = GLContext.getFunctionAddress("glUniformMatrix3fvARB");
        this.glUniformMatrix4fvARB = GLContext.getFunctionAddress("glUniformMatrix4fvARB");
        this.glGetObjectParameterfvARB = GLContext.getFunctionAddress("glGetObjectParameterfvARB");
        this.glGetObjectParameterivARB = GLContext.getFunctionAddress("glGetObjectParameterivARB");
        this.glGetInfoLogARB = GLContext.getFunctionAddress("glGetInfoLogARB");
        this.glGetAttachedObjectsARB = GLContext.getFunctionAddress("glGetAttachedObjectsARB");
        this.glGetUniformLocationARB = GLContext.getFunctionAddress("glGetUniformLocationARB");
        this.glGetActiveUniformARB = GLContext.getFunctionAddress("glGetActiveUniformARB");
        this.glGetUniformfvARB = GLContext.getFunctionAddress("glGetUniformfvARB");
        this.glGetUniformivARB = GLContext.getFunctionAddress("glGetUniformivARB");
        this.glGetShaderSourceARB = GLContext.getFunctionAddress("glGetShaderSourceARB");
        return bl & this.glGetHandleARB != 0L & this.glDetachObjectARB != 0L & this.glCreateShaderObjectARB != 0L & this.glShaderSourceARB != 0L & this.glCompileShaderARB != 0L & this.glCreateProgramObjectARB != 0L & this.glAttachObjectARB != 0L & this.glLinkProgramARB != 0L & this.glUseProgramObjectARB != 0L & this.glValidateProgramARB != 0L & this.glUniform1fARB != 0L & this.glUniform2fARB != 0L & this.glUniform3fARB != 0L & this.glUniform4fARB != 0L & this.glUniform1iARB != 0L & this.glUniform2iARB != 0L & this.glUniform3iARB != 0L & this.glUniform4iARB != 0L & this.glUniform1fvARB != 0L & this.glUniform2fvARB != 0L & this.glUniform3fvARB != 0L & this.glUniform4fvARB != 0L & this.glUniform1ivARB != 0L & this.glUniform2ivARB != 0L & this.glUniform3ivARB != 0L & this.glUniform4ivARB != 0L & this.glUniformMatrix2fvARB != 0L & this.glUniformMatrix3fvARB != 0L & this.glUniformMatrix4fvARB != 0L & this.glGetObjectParameterfvARB != 0L & this.glGetObjectParameterivARB != 0L & this.glGetInfoLogARB != 0L & this.glGetAttachedObjectsARB != 0L & this.glGetUniformLocationARB != 0L & this.glGetActiveUniformARB != 0L & this.glGetUniformfvARB != 0L & this.glGetUniformivARB != 0L & this.glGetShaderSourceARB != 0L;
    }

    private boolean ARB_shader_storage_buffer_object_initNativeFunctionAddresses() {
        this.glShaderStorageBlockBinding = GLContext.getFunctionAddress("glShaderStorageBlockBinding");
        return this.glShaderStorageBlockBinding != 0L;
    }

    private boolean ARB_shader_subroutine_initNativeFunctionAddresses() {
        this.glGetSubroutineUniformLocation = GLContext.getFunctionAddress("glGetSubroutineUniformLocation");
        boolean bl = this.glGetSubroutineUniformLocation != 0L;
        this.glGetSubroutineIndex = GLContext.getFunctionAddress("glGetSubroutineIndex");
        this.glGetActiveSubroutineUniformiv = GLContext.getFunctionAddress("glGetActiveSubroutineUniformiv");
        this.glGetActiveSubroutineUniformName = GLContext.getFunctionAddress("glGetActiveSubroutineUniformName");
        this.glGetActiveSubroutineName = GLContext.getFunctionAddress("glGetActiveSubroutineName");
        this.glUniformSubroutinesuiv = GLContext.getFunctionAddress("glUniformSubroutinesuiv");
        this.glGetUniformSubroutineuiv = GLContext.getFunctionAddress("glGetUniformSubroutineuiv");
        this.glGetProgramStageiv = GLContext.getFunctionAddress("glGetProgramStageiv");
        return bl & this.glGetSubroutineIndex != 0L & this.glGetActiveSubroutineUniformiv != 0L & this.glGetActiveSubroutineUniformName != 0L & this.glGetActiveSubroutineName != 0L & this.glUniformSubroutinesuiv != 0L & this.glGetUniformSubroutineuiv != 0L & this.glGetProgramStageiv != 0L;
    }

    private boolean ARB_shading_language_include_initNativeFunctionAddresses() {
        this.glNamedStringARB = GLContext.getFunctionAddress("glNamedStringARB");
        boolean bl = this.glNamedStringARB != 0L;
        this.glDeleteNamedStringARB = GLContext.getFunctionAddress("glDeleteNamedStringARB");
        this.glCompileShaderIncludeARB = GLContext.getFunctionAddress("glCompileShaderIncludeARB");
        this.glIsNamedStringARB = GLContext.getFunctionAddress("glIsNamedStringARB");
        this.glGetNamedStringARB = GLContext.getFunctionAddress("glGetNamedStringARB");
        this.glGetNamedStringivARB = GLContext.getFunctionAddress("glGetNamedStringivARB");
        return bl & this.glDeleteNamedStringARB != 0L & this.glCompileShaderIncludeARB != 0L & this.glIsNamedStringARB != 0L & this.glGetNamedStringARB != 0L & this.glGetNamedStringivARB != 0L;
    }

    private boolean ARB_sparse_buffer_initNativeFunctionAddresses() {
        this.glBufferPageCommitmentARB = GLContext.getFunctionAddress("glBufferPageCommitmentARB");
        return this.glBufferPageCommitmentARB != 0L;
    }

    private boolean ARB_sparse_texture_initNativeFunctionAddresses(Set<String> supported_extensions) {
        this.glTexPageCommitmentARB = GLContext.getFunctionAddress("glTexPageCommitmentARB");
        return this.glTexPageCommitmentARB != 0L & (!supported_extensions.contains("GL_EXT_direct_state_access") || (this.glTexturePageCommitmentEXT = GLContext.getFunctionAddress("glTexturePageCommitmentEXT")) != 0L);
    }

    private boolean ARB_sync_initNativeFunctionAddresses() {
        this.glFenceSync = GLContext.getFunctionAddress("glFenceSync");
        boolean bl = this.glFenceSync != 0L;
        this.glIsSync = GLContext.getFunctionAddress("glIsSync");
        this.glDeleteSync = GLContext.getFunctionAddress("glDeleteSync");
        this.glClientWaitSync = GLContext.getFunctionAddress("glClientWaitSync");
        this.glWaitSync = GLContext.getFunctionAddress("glWaitSync");
        this.glGetInteger64v = GLContext.getFunctionAddress("glGetInteger64v");
        this.glGetSynciv = GLContext.getFunctionAddress("glGetSynciv");
        return bl & this.glIsSync != 0L & this.glDeleteSync != 0L & this.glClientWaitSync != 0L & this.glWaitSync != 0L & this.glGetInteger64v != 0L & this.glGetSynciv != 0L;
    }

    private boolean ARB_tessellation_shader_initNativeFunctionAddresses() {
        this.glPatchParameteri = GLContext.getFunctionAddress("glPatchParameteri");
        boolean bl = this.glPatchParameteri != 0L;
        this.glPatchParameterfv = GLContext.getFunctionAddress("glPatchParameterfv");
        return bl & this.glPatchParameterfv != 0L;
    }

    private boolean ARB_texture_barrier_initNativeFunctionAddresses() {
        this.glTextureBarrier = GLContext.getFunctionAddress("glTextureBarrier");
        return this.glTextureBarrier != 0L;
    }

    private boolean ARB_texture_buffer_object_initNativeFunctionAddresses() {
        this.glTexBufferARB = GLContext.getFunctionAddress("glTexBufferARB");
        return this.glTexBufferARB != 0L;
    }

    private boolean ARB_texture_buffer_range_initNativeFunctionAddresses(Set<String> supported_extensions) {
        this.glTexBufferRange = GLContext.getFunctionAddress("glTexBufferRange");
        return this.glTexBufferRange != 0L & (!supported_extensions.contains("GL_EXT_direct_state_access") || (this.glTextureBufferRangeEXT = GLContext.getFunctionAddress("glTextureBufferRangeEXT")) != 0L);
    }

    private boolean ARB_texture_compression_initNativeFunctionAddresses() {
        this.glCompressedTexImage1DARB = GLContext.getFunctionAddress("glCompressedTexImage1DARB");
        boolean bl = this.glCompressedTexImage1DARB != 0L;
        this.glCompressedTexImage2DARB = GLContext.getFunctionAddress("glCompressedTexImage2DARB");
        this.glCompressedTexImage3DARB = GLContext.getFunctionAddress("glCompressedTexImage3DARB");
        this.glCompressedTexSubImage1DARB = GLContext.getFunctionAddress("glCompressedTexSubImage1DARB");
        this.glCompressedTexSubImage2DARB = GLContext.getFunctionAddress("glCompressedTexSubImage2DARB");
        this.glCompressedTexSubImage3DARB = GLContext.getFunctionAddress("glCompressedTexSubImage3DARB");
        this.glGetCompressedTexImageARB = GLContext.getFunctionAddress("glGetCompressedTexImageARB");
        return bl & this.glCompressedTexImage2DARB != 0L & this.glCompressedTexImage3DARB != 0L & this.glCompressedTexSubImage1DARB != 0L & this.glCompressedTexSubImage2DARB != 0L & this.glCompressedTexSubImage3DARB != 0L & this.glGetCompressedTexImageARB != 0L;
    }

    private boolean ARB_texture_multisample_initNativeFunctionAddresses() {
        this.glTexImage2DMultisample = GLContext.getFunctionAddress("glTexImage2DMultisample");
        boolean bl = this.glTexImage2DMultisample != 0L;
        this.glTexImage3DMultisample = GLContext.getFunctionAddress("glTexImage3DMultisample");
        this.glGetMultisamplefv = GLContext.getFunctionAddress("glGetMultisamplefv");
        this.glSampleMaski = GLContext.getFunctionAddress("glSampleMaski");
        return bl & this.glTexImage3DMultisample != 0L & this.glGetMultisamplefv != 0L & this.glSampleMaski != 0L;
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    private boolean ARB_texture_storage_initNativeFunctionAddresses(Set<String> supported_extensions) {
        this.glTexStorage1D = GLContext.getFunctionAddress(new String[]{"glTexStorage1D", "glTexStorage1DEXT"});
        v0 = this.glTexStorage1D != 0L;
        this.glTexStorage2D = GLContext.getFunctionAddress(new String[]{"glTexStorage2D", "glTexStorage2DEXT"});
        this.glTexStorage3D = GLContext.getFunctionAddress(new String[]{"glTexStorage3D", "glTexStorage3DEXT"});
        v1 = v0 & this.glTexStorage2D != 0L & this.glTexStorage3D != 0L;
        if (!supported_extensions.contains("GL_EXT_direct_state_access")) ** GOTO lbl-1000
        this.glTextureStorage1DEXT = GLContext.getFunctionAddress(new String[]{"glTextureStorage1DEXT", "glTextureStorage1DEXTEXT"});
        if (this.glTextureStorage1DEXT != 0L) lbl-1000:
        // 2 sources

        {
            v2 = true;
        } else {
            v2 = false;
        }
        v3 = v1 & v2;
        if (!supported_extensions.contains("GL_EXT_direct_state_access")) ** GOTO lbl-1000
        this.glTextureStorage2DEXT = GLContext.getFunctionAddress(new String[]{"glTextureStorage2DEXT", "glTextureStorage2DEXTEXT"});
        if (this.glTextureStorage2DEXT != 0L) lbl-1000:
        // 2 sources

        {
            v4 = true;
        } else {
            v4 = false;
        }
        v5 = v3 & v4;
        if (supported_extensions.contains("GL_EXT_direct_state_access")) {
            this.glTextureStorage3DEXT = GLContext.getFunctionAddress(new String[]{"glTextureStorage3DEXT", "glTextureStorage3DEXTEXT"});
            if (this.glTextureStorage3DEXT == 0L) {
                v6 = false;
                return v5 & v6;
            }
        }
        v6 = true;
        return v5 & v6;
    }

    private boolean ARB_texture_storage_multisample_initNativeFunctionAddresses(Set<String> supported_extensions) {
        this.glTexStorage2DMultisample = GLContext.getFunctionAddress("glTexStorage2DMultisample");
        boolean bl = this.glTexStorage2DMultisample != 0L;
        this.glTexStorage3DMultisample = GLContext.getFunctionAddress("glTexStorage3DMultisample");
        return bl & this.glTexStorage3DMultisample != 0L & (!supported_extensions.contains("GL_EXT_direct_state_access") || (this.glTextureStorage2DMultisampleEXT = GLContext.getFunctionAddress("glTextureStorage2DMultisampleEXT")) != 0L) & (!supported_extensions.contains("GL_EXT_direct_state_access") || (this.glTextureStorage3DMultisampleEXT = GLContext.getFunctionAddress("glTextureStorage3DMultisampleEXT")) != 0L);
    }

    private boolean ARB_texture_view_initNativeFunctionAddresses() {
        this.glTextureView = GLContext.getFunctionAddress("glTextureView");
        return this.glTextureView != 0L;
    }

    private boolean ARB_timer_query_initNativeFunctionAddresses() {
        this.glQueryCounter = GLContext.getFunctionAddress("glQueryCounter");
        boolean bl = this.glQueryCounter != 0L;
        this.glGetQueryObjecti64v = GLContext.getFunctionAddress("glGetQueryObjecti64v");
        this.glGetQueryObjectui64v = GLContext.getFunctionAddress("glGetQueryObjectui64v");
        return bl & this.glGetQueryObjecti64v != 0L & this.glGetQueryObjectui64v != 0L;
    }

    private boolean ARB_transform_feedback2_initNativeFunctionAddresses() {
        this.glBindTransformFeedback = GLContext.getFunctionAddress("glBindTransformFeedback");
        boolean bl = this.glBindTransformFeedback != 0L;
        this.glDeleteTransformFeedbacks = GLContext.getFunctionAddress("glDeleteTransformFeedbacks");
        this.glGenTransformFeedbacks = GLContext.getFunctionAddress("glGenTransformFeedbacks");
        this.glIsTransformFeedback = GLContext.getFunctionAddress("glIsTransformFeedback");
        this.glPauseTransformFeedback = GLContext.getFunctionAddress("glPauseTransformFeedback");
        this.glResumeTransformFeedback = GLContext.getFunctionAddress("glResumeTransformFeedback");
        this.glDrawTransformFeedback = GLContext.getFunctionAddress("glDrawTransformFeedback");
        return bl & this.glDeleteTransformFeedbacks != 0L & this.glGenTransformFeedbacks != 0L & this.glIsTransformFeedback != 0L & this.glPauseTransformFeedback != 0L & this.glResumeTransformFeedback != 0L & this.glDrawTransformFeedback != 0L;
    }

    private boolean ARB_transform_feedback3_initNativeFunctionAddresses() {
        this.glDrawTransformFeedbackStream = GLContext.getFunctionAddress("glDrawTransformFeedbackStream");
        boolean bl = this.glDrawTransformFeedbackStream != 0L;
        this.glBeginQueryIndexed = GLContext.getFunctionAddress("glBeginQueryIndexed");
        this.glEndQueryIndexed = GLContext.getFunctionAddress("glEndQueryIndexed");
        this.glGetQueryIndexediv = GLContext.getFunctionAddress("glGetQueryIndexediv");
        return bl & this.glBeginQueryIndexed != 0L & this.glEndQueryIndexed != 0L & this.glGetQueryIndexediv != 0L;
    }

    private boolean ARB_transform_feedback_instanced_initNativeFunctionAddresses() {
        this.glDrawTransformFeedbackInstanced = GLContext.getFunctionAddress("glDrawTransformFeedbackInstanced");
        boolean bl = this.glDrawTransformFeedbackInstanced != 0L;
        this.glDrawTransformFeedbackStreamInstanced = GLContext.getFunctionAddress("glDrawTransformFeedbackStreamInstanced");
        return bl & this.glDrawTransformFeedbackStreamInstanced != 0L;
    }

    private boolean ARB_transpose_matrix_initNativeFunctionAddresses() {
        this.glLoadTransposeMatrixfARB = GLContext.getFunctionAddress("glLoadTransposeMatrixfARB");
        boolean bl = this.glLoadTransposeMatrixfARB != 0L;
        this.glMultTransposeMatrixfARB = GLContext.getFunctionAddress("glMultTransposeMatrixfARB");
        return bl & this.glMultTransposeMatrixfARB != 0L;
    }

    private boolean ARB_uniform_buffer_object_initNativeFunctionAddresses() {
        this.glGetUniformIndices = GLContext.getFunctionAddress("glGetUniformIndices");
        boolean bl = this.glGetUniformIndices != 0L;
        this.glGetActiveUniformsiv = GLContext.getFunctionAddress("glGetActiveUniformsiv");
        this.glGetActiveUniformName = GLContext.getFunctionAddress("glGetActiveUniformName");
        this.glGetUniformBlockIndex = GLContext.getFunctionAddress("glGetUniformBlockIndex");
        this.glGetActiveUniformBlockiv = GLContext.getFunctionAddress("glGetActiveUniformBlockiv");
        this.glGetActiveUniformBlockName = GLContext.getFunctionAddress("glGetActiveUniformBlockName");
        this.glBindBufferRange = GLContext.getFunctionAddress("glBindBufferRange");
        this.glBindBufferBase = GLContext.getFunctionAddress("glBindBufferBase");
        this.glGetIntegeri_v = GLContext.getFunctionAddress("glGetIntegeri_v");
        this.glUniformBlockBinding = GLContext.getFunctionAddress("glUniformBlockBinding");
        return bl & this.glGetActiveUniformsiv != 0L & this.glGetActiveUniformName != 0L & this.glGetUniformBlockIndex != 0L & this.glGetActiveUniformBlockiv != 0L & this.glGetActiveUniformBlockName != 0L & this.glBindBufferRange != 0L & this.glBindBufferBase != 0L & this.glGetIntegeri_v != 0L & this.glUniformBlockBinding != 0L;
    }

    private boolean ARB_vertex_array_object_initNativeFunctionAddresses() {
        this.glBindVertexArray = GLContext.getFunctionAddress("glBindVertexArray");
        boolean bl = this.glBindVertexArray != 0L;
        this.glDeleteVertexArrays = GLContext.getFunctionAddress("glDeleteVertexArrays");
        this.glGenVertexArrays = GLContext.getFunctionAddress("glGenVertexArrays");
        this.glIsVertexArray = GLContext.getFunctionAddress("glIsVertexArray");
        return bl & this.glDeleteVertexArrays != 0L & this.glGenVertexArrays != 0L & this.glIsVertexArray != 0L;
    }

    private boolean ARB_vertex_attrib_64bit_initNativeFunctionAddresses(Set<String> supported_extensions) {
        this.glVertexAttribL1d = GLContext.getFunctionAddress("glVertexAttribL1d");
        boolean bl = this.glVertexAttribL1d != 0L;
        this.glVertexAttribL2d = GLContext.getFunctionAddress("glVertexAttribL2d");
        this.glVertexAttribL3d = GLContext.getFunctionAddress("glVertexAttribL3d");
        this.glVertexAttribL4d = GLContext.getFunctionAddress("glVertexAttribL4d");
        this.glVertexAttribL1dv = GLContext.getFunctionAddress("glVertexAttribL1dv");
        this.glVertexAttribL2dv = GLContext.getFunctionAddress("glVertexAttribL2dv");
        this.glVertexAttribL3dv = GLContext.getFunctionAddress("glVertexAttribL3dv");
        this.glVertexAttribL4dv = GLContext.getFunctionAddress("glVertexAttribL4dv");
        this.glVertexAttribLPointer = GLContext.getFunctionAddress("glVertexAttribLPointer");
        this.glGetVertexAttribLdv = GLContext.getFunctionAddress("glGetVertexAttribLdv");
        return bl & this.glVertexAttribL2d != 0L & this.glVertexAttribL3d != 0L & this.glVertexAttribL4d != 0L & this.glVertexAttribL1dv != 0L & this.glVertexAttribL2dv != 0L & this.glVertexAttribL3dv != 0L & this.glVertexAttribL4dv != 0L & this.glVertexAttribLPointer != 0L & this.glGetVertexAttribLdv != 0L & (!supported_extensions.contains("GL_EXT_direct_state_access") || (this.glVertexArrayVertexAttribLOffsetEXT = GLContext.getFunctionAddress("glVertexArrayVertexAttribLOffsetEXT")) != 0L);
    }

    private boolean ARB_vertex_attrib_binding_initNativeFunctionAddresses() {
        this.glBindVertexBuffer = GLContext.getFunctionAddress("glBindVertexBuffer");
        boolean bl = this.glBindVertexBuffer != 0L;
        this.glVertexAttribFormat = GLContext.getFunctionAddress("glVertexAttribFormat");
        this.glVertexAttribIFormat = GLContext.getFunctionAddress("glVertexAttribIFormat");
        this.glVertexAttribLFormat = GLContext.getFunctionAddress("glVertexAttribLFormat");
        this.glVertexAttribBinding = GLContext.getFunctionAddress("glVertexAttribBinding");
        this.glVertexBindingDivisor = GLContext.getFunctionAddress("glVertexBindingDivisor");
        return bl & this.glVertexAttribFormat != 0L & this.glVertexAttribIFormat != 0L & this.glVertexAttribLFormat != 0L & this.glVertexAttribBinding != 0L & this.glVertexBindingDivisor != 0L;
    }

    private boolean ARB_vertex_blend_initNativeFunctionAddresses() {
        this.glWeightbvARB = GLContext.getFunctionAddress("glWeightbvARB");
        boolean bl = this.glWeightbvARB != 0L;
        this.glWeightsvARB = GLContext.getFunctionAddress("glWeightsvARB");
        this.glWeightivARB = GLContext.getFunctionAddress("glWeightivARB");
        this.glWeightfvARB = GLContext.getFunctionAddress("glWeightfvARB");
        this.glWeightdvARB = GLContext.getFunctionAddress("glWeightdvARB");
        this.glWeightubvARB = GLContext.getFunctionAddress("glWeightubvARB");
        this.glWeightusvARB = GLContext.getFunctionAddress("glWeightusvARB");
        this.glWeightuivARB = GLContext.getFunctionAddress("glWeightuivARB");
        this.glWeightPointerARB = GLContext.getFunctionAddress("glWeightPointerARB");
        this.glVertexBlendARB = GLContext.getFunctionAddress("glVertexBlendARB");
        return bl & this.glWeightsvARB != 0L & this.glWeightivARB != 0L & this.glWeightfvARB != 0L & this.glWeightdvARB != 0L & this.glWeightubvARB != 0L & this.glWeightusvARB != 0L & this.glWeightuivARB != 0L & this.glWeightPointerARB != 0L & this.glVertexBlendARB != 0L;
    }

    private boolean ARB_vertex_program_initNativeFunctionAddresses() {
        this.glVertexAttrib1sARB = GLContext.getFunctionAddress("glVertexAttrib1sARB");
        boolean bl = this.glVertexAttrib1sARB != 0L;
        this.glVertexAttrib1fARB = GLContext.getFunctionAddress("glVertexAttrib1fARB");
        this.glVertexAttrib1dARB = GLContext.getFunctionAddress("glVertexAttrib1dARB");
        this.glVertexAttrib2sARB = GLContext.getFunctionAddress("glVertexAttrib2sARB");
        this.glVertexAttrib2fARB = GLContext.getFunctionAddress("glVertexAttrib2fARB");
        this.glVertexAttrib2dARB = GLContext.getFunctionAddress("glVertexAttrib2dARB");
        this.glVertexAttrib3sARB = GLContext.getFunctionAddress("glVertexAttrib3sARB");
        this.glVertexAttrib3fARB = GLContext.getFunctionAddress("glVertexAttrib3fARB");
        this.glVertexAttrib3dARB = GLContext.getFunctionAddress("glVertexAttrib3dARB");
        this.glVertexAttrib4sARB = GLContext.getFunctionAddress("glVertexAttrib4sARB");
        this.glVertexAttrib4fARB = GLContext.getFunctionAddress("glVertexAttrib4fARB");
        this.glVertexAttrib4dARB = GLContext.getFunctionAddress("glVertexAttrib4dARB");
        this.glVertexAttrib4NubARB = GLContext.getFunctionAddress("glVertexAttrib4NubARB");
        this.glVertexAttribPointerARB = GLContext.getFunctionAddress("glVertexAttribPointerARB");
        this.glEnableVertexAttribArrayARB = GLContext.getFunctionAddress("glEnableVertexAttribArrayARB");
        this.glDisableVertexAttribArrayARB = GLContext.getFunctionAddress("glDisableVertexAttribArrayARB");
        this.glGetVertexAttribfvARB = GLContext.getFunctionAddress("glGetVertexAttribfvARB");
        this.glGetVertexAttribdvARB = GLContext.getFunctionAddress("glGetVertexAttribdvARB");
        this.glGetVertexAttribivARB = GLContext.getFunctionAddress("glGetVertexAttribivARB");
        this.glGetVertexAttribPointervARB = GLContext.getFunctionAddress("glGetVertexAttribPointervARB");
        return bl & this.glVertexAttrib1fARB != 0L & this.glVertexAttrib1dARB != 0L & this.glVertexAttrib2sARB != 0L & this.glVertexAttrib2fARB != 0L & this.glVertexAttrib2dARB != 0L & this.glVertexAttrib3sARB != 0L & this.glVertexAttrib3fARB != 0L & this.glVertexAttrib3dARB != 0L & this.glVertexAttrib4sARB != 0L & this.glVertexAttrib4fARB != 0L & this.glVertexAttrib4dARB != 0L & this.glVertexAttrib4NubARB != 0L & this.glVertexAttribPointerARB != 0L & this.glEnableVertexAttribArrayARB != 0L & this.glDisableVertexAttribArrayARB != 0L & this.glGetVertexAttribfvARB != 0L & this.glGetVertexAttribdvARB != 0L & this.glGetVertexAttribivARB != 0L & this.glGetVertexAttribPointervARB != 0L;
    }

    private boolean ARB_vertex_shader_initNativeFunctionAddresses() {
        this.glVertexAttrib1sARB = GLContext.getFunctionAddress("glVertexAttrib1sARB");
        boolean bl = this.glVertexAttrib1sARB != 0L;
        this.glVertexAttrib1fARB = GLContext.getFunctionAddress("glVertexAttrib1fARB");
        this.glVertexAttrib1dARB = GLContext.getFunctionAddress("glVertexAttrib1dARB");
        this.glVertexAttrib2sARB = GLContext.getFunctionAddress("glVertexAttrib2sARB");
        this.glVertexAttrib2fARB = GLContext.getFunctionAddress("glVertexAttrib2fARB");
        this.glVertexAttrib2dARB = GLContext.getFunctionAddress("glVertexAttrib2dARB");
        this.glVertexAttrib3sARB = GLContext.getFunctionAddress("glVertexAttrib3sARB");
        this.glVertexAttrib3fARB = GLContext.getFunctionAddress("glVertexAttrib3fARB");
        this.glVertexAttrib3dARB = GLContext.getFunctionAddress("glVertexAttrib3dARB");
        this.glVertexAttrib4sARB = GLContext.getFunctionAddress("glVertexAttrib4sARB");
        this.glVertexAttrib4fARB = GLContext.getFunctionAddress("glVertexAttrib4fARB");
        this.glVertexAttrib4dARB = GLContext.getFunctionAddress("glVertexAttrib4dARB");
        this.glVertexAttrib4NubARB = GLContext.getFunctionAddress("glVertexAttrib4NubARB");
        this.glVertexAttribPointerARB = GLContext.getFunctionAddress("glVertexAttribPointerARB");
        this.glEnableVertexAttribArrayARB = GLContext.getFunctionAddress("glEnableVertexAttribArrayARB");
        this.glDisableVertexAttribArrayARB = GLContext.getFunctionAddress("glDisableVertexAttribArrayARB");
        this.glBindAttribLocationARB = GLContext.getFunctionAddress("glBindAttribLocationARB");
        this.glGetActiveAttribARB = GLContext.getFunctionAddress("glGetActiveAttribARB");
        this.glGetAttribLocationARB = GLContext.getFunctionAddress("glGetAttribLocationARB");
        this.glGetVertexAttribfvARB = GLContext.getFunctionAddress("glGetVertexAttribfvARB");
        this.glGetVertexAttribdvARB = GLContext.getFunctionAddress("glGetVertexAttribdvARB");
        this.glGetVertexAttribivARB = GLContext.getFunctionAddress("glGetVertexAttribivARB");
        this.glGetVertexAttribPointervARB = GLContext.getFunctionAddress("glGetVertexAttribPointervARB");
        return bl & this.glVertexAttrib1fARB != 0L & this.glVertexAttrib1dARB != 0L & this.glVertexAttrib2sARB != 0L & this.glVertexAttrib2fARB != 0L & this.glVertexAttrib2dARB != 0L & this.glVertexAttrib3sARB != 0L & this.glVertexAttrib3fARB != 0L & this.glVertexAttrib3dARB != 0L & this.glVertexAttrib4sARB != 0L & this.glVertexAttrib4fARB != 0L & this.glVertexAttrib4dARB != 0L & this.glVertexAttrib4NubARB != 0L & this.glVertexAttribPointerARB != 0L & this.glEnableVertexAttribArrayARB != 0L & this.glDisableVertexAttribArrayARB != 0L & this.glBindAttribLocationARB != 0L & this.glGetActiveAttribARB != 0L & this.glGetAttribLocationARB != 0L & this.glGetVertexAttribfvARB != 0L & this.glGetVertexAttribdvARB != 0L & this.glGetVertexAttribivARB != 0L & this.glGetVertexAttribPointervARB != 0L;
    }

    private boolean ARB_vertex_type_2_10_10_10_rev_initNativeFunctionAddresses() {
        this.glVertexP2ui = GLContext.getFunctionAddress("glVertexP2ui");
        boolean bl = this.glVertexP2ui != 0L;
        this.glVertexP3ui = GLContext.getFunctionAddress("glVertexP3ui");
        this.glVertexP4ui = GLContext.getFunctionAddress("glVertexP4ui");
        this.glVertexP2uiv = GLContext.getFunctionAddress("glVertexP2uiv");
        this.glVertexP3uiv = GLContext.getFunctionAddress("glVertexP3uiv");
        this.glVertexP4uiv = GLContext.getFunctionAddress("glVertexP4uiv");
        this.glTexCoordP1ui = GLContext.getFunctionAddress("glTexCoordP1ui");
        this.glTexCoordP2ui = GLContext.getFunctionAddress("glTexCoordP2ui");
        this.glTexCoordP3ui = GLContext.getFunctionAddress("glTexCoordP3ui");
        this.glTexCoordP4ui = GLContext.getFunctionAddress("glTexCoordP4ui");
        this.glTexCoordP1uiv = GLContext.getFunctionAddress("glTexCoordP1uiv");
        this.glTexCoordP2uiv = GLContext.getFunctionAddress("glTexCoordP2uiv");
        this.glTexCoordP3uiv = GLContext.getFunctionAddress("glTexCoordP3uiv");
        this.glTexCoordP4uiv = GLContext.getFunctionAddress("glTexCoordP4uiv");
        this.glMultiTexCoordP1ui = GLContext.getFunctionAddress("glMultiTexCoordP1ui");
        this.glMultiTexCoordP2ui = GLContext.getFunctionAddress("glMultiTexCoordP2ui");
        this.glMultiTexCoordP3ui = GLContext.getFunctionAddress("glMultiTexCoordP3ui");
        this.glMultiTexCoordP4ui = GLContext.getFunctionAddress("glMultiTexCoordP4ui");
        this.glMultiTexCoordP1uiv = GLContext.getFunctionAddress("glMultiTexCoordP1uiv");
        this.glMultiTexCoordP2uiv = GLContext.getFunctionAddress("glMultiTexCoordP2uiv");
        this.glMultiTexCoordP3uiv = GLContext.getFunctionAddress("glMultiTexCoordP3uiv");
        this.glMultiTexCoordP4uiv = GLContext.getFunctionAddress("glMultiTexCoordP4uiv");
        this.glNormalP3ui = GLContext.getFunctionAddress("glNormalP3ui");
        this.glNormalP3uiv = GLContext.getFunctionAddress("glNormalP3uiv");
        this.glColorP3ui = GLContext.getFunctionAddress("glColorP3ui");
        this.glColorP4ui = GLContext.getFunctionAddress("glColorP4ui");
        this.glColorP3uiv = GLContext.getFunctionAddress("glColorP3uiv");
        this.glColorP4uiv = GLContext.getFunctionAddress("glColorP4uiv");
        this.glSecondaryColorP3ui = GLContext.getFunctionAddress("glSecondaryColorP3ui");
        this.glSecondaryColorP3uiv = GLContext.getFunctionAddress("glSecondaryColorP3uiv");
        this.glVertexAttribP1ui = GLContext.getFunctionAddress("glVertexAttribP1ui");
        this.glVertexAttribP2ui = GLContext.getFunctionAddress("glVertexAttribP2ui");
        this.glVertexAttribP3ui = GLContext.getFunctionAddress("glVertexAttribP3ui");
        this.glVertexAttribP4ui = GLContext.getFunctionAddress("glVertexAttribP4ui");
        this.glVertexAttribP1uiv = GLContext.getFunctionAddress("glVertexAttribP1uiv");
        this.glVertexAttribP2uiv = GLContext.getFunctionAddress("glVertexAttribP2uiv");
        this.glVertexAttribP3uiv = GLContext.getFunctionAddress("glVertexAttribP3uiv");
        this.glVertexAttribP4uiv = GLContext.getFunctionAddress("glVertexAttribP4uiv");
        return bl & this.glVertexP3ui != 0L & this.glVertexP4ui != 0L & this.glVertexP2uiv != 0L & this.glVertexP3uiv != 0L & this.glVertexP4uiv != 0L & this.glTexCoordP1ui != 0L & this.glTexCoordP2ui != 0L & this.glTexCoordP3ui != 0L & this.glTexCoordP4ui != 0L & this.glTexCoordP1uiv != 0L & this.glTexCoordP2uiv != 0L & this.glTexCoordP3uiv != 0L & this.glTexCoordP4uiv != 0L & this.glMultiTexCoordP1ui != 0L & this.glMultiTexCoordP2ui != 0L & this.glMultiTexCoordP3ui != 0L & this.glMultiTexCoordP4ui != 0L & this.glMultiTexCoordP1uiv != 0L & this.glMultiTexCoordP2uiv != 0L & this.glMultiTexCoordP3uiv != 0L & this.glMultiTexCoordP4uiv != 0L & this.glNormalP3ui != 0L & this.glNormalP3uiv != 0L & this.glColorP3ui != 0L & this.glColorP4ui != 0L & this.glColorP3uiv != 0L & this.glColorP4uiv != 0L & this.glSecondaryColorP3ui != 0L & this.glSecondaryColorP3uiv != 0L & this.glVertexAttribP1ui != 0L & this.glVertexAttribP2ui != 0L & this.glVertexAttribP3ui != 0L & this.glVertexAttribP4ui != 0L & this.glVertexAttribP1uiv != 0L & this.glVertexAttribP2uiv != 0L & this.glVertexAttribP3uiv != 0L & this.glVertexAttribP4uiv != 0L;
    }

    private boolean ARB_viewport_array_initNativeFunctionAddresses() {
        this.glViewportArrayv = GLContext.getFunctionAddress("glViewportArrayv");
        boolean bl = this.glViewportArrayv != 0L;
        this.glViewportIndexedf = GLContext.getFunctionAddress("glViewportIndexedf");
        this.glViewportIndexedfv = GLContext.getFunctionAddress("glViewportIndexedfv");
        this.glScissorArrayv = GLContext.getFunctionAddress("glScissorArrayv");
        this.glScissorIndexed = GLContext.getFunctionAddress("glScissorIndexed");
        this.glScissorIndexedv = GLContext.getFunctionAddress("glScissorIndexedv");
        this.glDepthRangeArrayv = GLContext.getFunctionAddress("glDepthRangeArrayv");
        this.glDepthRangeIndexed = GLContext.getFunctionAddress("glDepthRangeIndexed");
        this.glGetFloati_v = GLContext.getFunctionAddress("glGetFloati_v");
        this.glGetDoublei_v = GLContext.getFunctionAddress("glGetDoublei_v");
        this.glGetIntegerIndexedvEXT = GLContext.getFunctionAddress("glGetIntegerIndexedvEXT");
        this.glEnableIndexedEXT = GLContext.getFunctionAddress("glEnableIndexedEXT");
        this.glDisableIndexedEXT = GLContext.getFunctionAddress("glDisableIndexedEXT");
        this.glIsEnabledIndexedEXT = GLContext.getFunctionAddress("glIsEnabledIndexedEXT");
        return bl & this.glViewportIndexedf != 0L & this.glViewportIndexedfv != 0L & this.glScissorArrayv != 0L & this.glScissorIndexed != 0L & this.glScissorIndexedv != 0L & this.glDepthRangeArrayv != 0L & this.glDepthRangeIndexed != 0L & this.glGetFloati_v != 0L & this.glGetDoublei_v != 0L & this.glGetIntegerIndexedvEXT != 0L & this.glEnableIndexedEXT != 0L & this.glDisableIndexedEXT != 0L & this.glIsEnabledIndexedEXT != 0L;
    }

    private boolean ARB_window_pos_initNativeFunctionAddresses(boolean forwardCompatible) {
        return (forwardCompatible || (this.glWindowPos2fARB = GLContext.getFunctionAddress("glWindowPos2fARB")) != 0L) & (forwardCompatible || (this.glWindowPos2dARB = GLContext.getFunctionAddress("glWindowPos2dARB")) != 0L) & (forwardCompatible || (this.glWindowPos2iARB = GLContext.getFunctionAddress("glWindowPos2iARB")) != 0L) & (forwardCompatible || (this.glWindowPos2sARB = GLContext.getFunctionAddress("glWindowPos2sARB")) != 0L) & (forwardCompatible || (this.glWindowPos3fARB = GLContext.getFunctionAddress("glWindowPos3fARB")) != 0L) & (forwardCompatible || (this.glWindowPos3dARB = GLContext.getFunctionAddress("glWindowPos3dARB")) != 0L) & (forwardCompatible || (this.glWindowPos3iARB = GLContext.getFunctionAddress("glWindowPos3iARB")) != 0L) & (forwardCompatible || (this.glWindowPos3sARB = GLContext.getFunctionAddress("glWindowPos3sARB")) != 0L);
    }

    private boolean ATI_draw_buffers_initNativeFunctionAddresses() {
        this.glDrawBuffersATI = GLContext.getFunctionAddress("glDrawBuffersATI");
        return this.glDrawBuffersATI != 0L;
    }

    private boolean ATI_element_array_initNativeFunctionAddresses() {
        this.glElementPointerATI = GLContext.getFunctionAddress("glElementPointerATI");
        boolean bl = this.glElementPointerATI != 0L;
        this.glDrawElementArrayATI = GLContext.getFunctionAddress("glDrawElementArrayATI");
        this.glDrawRangeElementArrayATI = GLContext.getFunctionAddress("glDrawRangeElementArrayATI");
        return bl & this.glDrawElementArrayATI != 0L & this.glDrawRangeElementArrayATI != 0L;
    }

    private boolean ATI_envmap_bumpmap_initNativeFunctionAddresses() {
        this.glTexBumpParameterfvATI = GLContext.getFunctionAddress("glTexBumpParameterfvATI");
        boolean bl = this.glTexBumpParameterfvATI != 0L;
        this.glTexBumpParameterivATI = GLContext.getFunctionAddress("glTexBumpParameterivATI");
        this.glGetTexBumpParameterfvATI = GLContext.getFunctionAddress("glGetTexBumpParameterfvATI");
        this.glGetTexBumpParameterivATI = GLContext.getFunctionAddress("glGetTexBumpParameterivATI");
        return bl & this.glTexBumpParameterivATI != 0L & this.glGetTexBumpParameterfvATI != 0L & this.glGetTexBumpParameterivATI != 0L;
    }

    private boolean ATI_fragment_shader_initNativeFunctionAddresses() {
        this.glGenFragmentShadersATI = GLContext.getFunctionAddress("glGenFragmentShadersATI");
        boolean bl = this.glGenFragmentShadersATI != 0L;
        this.glBindFragmentShaderATI = GLContext.getFunctionAddress("glBindFragmentShaderATI");
        this.glDeleteFragmentShaderATI = GLContext.getFunctionAddress("glDeleteFragmentShaderATI");
        this.glBeginFragmentShaderATI = GLContext.getFunctionAddress("glBeginFragmentShaderATI");
        this.glEndFragmentShaderATI = GLContext.getFunctionAddress("glEndFragmentShaderATI");
        this.glPassTexCoordATI = GLContext.getFunctionAddress("glPassTexCoordATI");
        this.glSampleMapATI = GLContext.getFunctionAddress("glSampleMapATI");
        this.glColorFragmentOp1ATI = GLContext.getFunctionAddress("glColorFragmentOp1ATI");
        this.glColorFragmentOp2ATI = GLContext.getFunctionAddress("glColorFragmentOp2ATI");
        this.glColorFragmentOp3ATI = GLContext.getFunctionAddress("glColorFragmentOp3ATI");
        this.glAlphaFragmentOp1ATI = GLContext.getFunctionAddress("glAlphaFragmentOp1ATI");
        this.glAlphaFragmentOp2ATI = GLContext.getFunctionAddress("glAlphaFragmentOp2ATI");
        this.glAlphaFragmentOp3ATI = GLContext.getFunctionAddress("glAlphaFragmentOp3ATI");
        this.glSetFragmentShaderConstantATI = GLContext.getFunctionAddress("glSetFragmentShaderConstantATI");
        return bl & this.glBindFragmentShaderATI != 0L & this.glDeleteFragmentShaderATI != 0L & this.glBeginFragmentShaderATI != 0L & this.glEndFragmentShaderATI != 0L & this.glPassTexCoordATI != 0L & this.glSampleMapATI != 0L & this.glColorFragmentOp1ATI != 0L & this.glColorFragmentOp2ATI != 0L & this.glColorFragmentOp3ATI != 0L & this.glAlphaFragmentOp1ATI != 0L & this.glAlphaFragmentOp2ATI != 0L & this.glAlphaFragmentOp3ATI != 0L & this.glSetFragmentShaderConstantATI != 0L;
    }

    private boolean ATI_map_object_buffer_initNativeFunctionAddresses() {
        this.glMapObjectBufferATI = GLContext.getFunctionAddress("glMapObjectBufferATI");
        boolean bl = this.glMapObjectBufferATI != 0L;
        this.glUnmapObjectBufferATI = GLContext.getFunctionAddress("glUnmapObjectBufferATI");
        return bl & this.glUnmapObjectBufferATI != 0L;
    }

    private boolean ATI_pn_triangles_initNativeFunctionAddresses() {
        this.glPNTrianglesfATI = GLContext.getFunctionAddress("glPNTrianglesfATI");
        boolean bl = this.glPNTrianglesfATI != 0L;
        this.glPNTrianglesiATI = GLContext.getFunctionAddress("glPNTrianglesiATI");
        return bl & this.glPNTrianglesiATI != 0L;
    }

    private boolean ATI_separate_stencil_initNativeFunctionAddresses() {
        this.glStencilOpSeparateATI = GLContext.getFunctionAddress("glStencilOpSeparateATI");
        boolean bl = this.glStencilOpSeparateATI != 0L;
        this.glStencilFuncSeparateATI = GLContext.getFunctionAddress("glStencilFuncSeparateATI");
        return bl & this.glStencilFuncSeparateATI != 0L;
    }

    private boolean ATI_vertex_array_object_initNativeFunctionAddresses() {
        this.glNewObjectBufferATI = GLContext.getFunctionAddress("glNewObjectBufferATI");
        boolean bl = this.glNewObjectBufferATI != 0L;
        this.glIsObjectBufferATI = GLContext.getFunctionAddress("glIsObjectBufferATI");
        this.glUpdateObjectBufferATI = GLContext.getFunctionAddress("glUpdateObjectBufferATI");
        this.glGetObjectBufferfvATI = GLContext.getFunctionAddress("glGetObjectBufferfvATI");
        this.glGetObjectBufferivATI = GLContext.getFunctionAddress("glGetObjectBufferivATI");
        this.glFreeObjectBufferATI = GLContext.getFunctionAddress("glFreeObjectBufferATI");
        this.glArrayObjectATI = GLContext.getFunctionAddress("glArrayObjectATI");
        this.glGetArrayObjectfvATI = GLContext.getFunctionAddress("glGetArrayObjectfvATI");
        this.glGetArrayObjectivATI = GLContext.getFunctionAddress("glGetArrayObjectivATI");
        this.glVariantArrayObjectATI = GLContext.getFunctionAddress("glVariantArrayObjectATI");
        this.glGetVariantArrayObjectfvATI = GLContext.getFunctionAddress("glGetVariantArrayObjectfvATI");
        this.glGetVariantArrayObjectivATI = GLContext.getFunctionAddress("glGetVariantArrayObjectivATI");
        return bl & this.glIsObjectBufferATI != 0L & this.glUpdateObjectBufferATI != 0L & this.glGetObjectBufferfvATI != 0L & this.glGetObjectBufferivATI != 0L & this.glFreeObjectBufferATI != 0L & this.glArrayObjectATI != 0L & this.glGetArrayObjectfvATI != 0L & this.glGetArrayObjectivATI != 0L & this.glVariantArrayObjectATI != 0L & this.glGetVariantArrayObjectfvATI != 0L & this.glGetVariantArrayObjectivATI != 0L;
    }

    private boolean ATI_vertex_attrib_array_object_initNativeFunctionAddresses() {
        this.glVertexAttribArrayObjectATI = GLContext.getFunctionAddress("glVertexAttribArrayObjectATI");
        boolean bl = this.glVertexAttribArrayObjectATI != 0L;
        this.glGetVertexAttribArrayObjectfvATI = GLContext.getFunctionAddress("glGetVertexAttribArrayObjectfvATI");
        this.glGetVertexAttribArrayObjectivATI = GLContext.getFunctionAddress("glGetVertexAttribArrayObjectivATI");
        return bl & this.glGetVertexAttribArrayObjectfvATI != 0L & this.glGetVertexAttribArrayObjectivATI != 0L;
    }

    private boolean ATI_vertex_streams_initNativeFunctionAddresses() {
        this.glVertexStream2fATI = GLContext.getFunctionAddress("glVertexStream2fATI");
        boolean bl = this.glVertexStream2fATI != 0L;
        this.glVertexStream2dATI = GLContext.getFunctionAddress("glVertexStream2dATI");
        this.glVertexStream2iATI = GLContext.getFunctionAddress("glVertexStream2iATI");
        this.glVertexStream2sATI = GLContext.getFunctionAddress("glVertexStream2sATI");
        this.glVertexStream3fATI = GLContext.getFunctionAddress("glVertexStream3fATI");
        this.glVertexStream3dATI = GLContext.getFunctionAddress("glVertexStream3dATI");
        this.glVertexStream3iATI = GLContext.getFunctionAddress("glVertexStream3iATI");
        this.glVertexStream3sATI = GLContext.getFunctionAddress("glVertexStream3sATI");
        this.glVertexStream4fATI = GLContext.getFunctionAddress("glVertexStream4fATI");
        this.glVertexStream4dATI = GLContext.getFunctionAddress("glVertexStream4dATI");
        this.glVertexStream4iATI = GLContext.getFunctionAddress("glVertexStream4iATI");
        this.glVertexStream4sATI = GLContext.getFunctionAddress("glVertexStream4sATI");
        this.glNormalStream3bATI = GLContext.getFunctionAddress("glNormalStream3bATI");
        this.glNormalStream3fATI = GLContext.getFunctionAddress("glNormalStream3fATI");
        this.glNormalStream3dATI = GLContext.getFunctionAddress("glNormalStream3dATI");
        this.glNormalStream3iATI = GLContext.getFunctionAddress("glNormalStream3iATI");
        this.glNormalStream3sATI = GLContext.getFunctionAddress("glNormalStream3sATI");
        this.glClientActiveVertexStreamATI = GLContext.getFunctionAddress("glClientActiveVertexStreamATI");
        this.glVertexBlendEnvfATI = GLContext.getFunctionAddress("glVertexBlendEnvfATI");
        this.glVertexBlendEnviATI = GLContext.getFunctionAddress("glVertexBlendEnviATI");
        return bl & this.glVertexStream2dATI != 0L & this.glVertexStream2iATI != 0L & this.glVertexStream2sATI != 0L & this.glVertexStream3fATI != 0L & this.glVertexStream3dATI != 0L & this.glVertexStream3iATI != 0L & this.glVertexStream3sATI != 0L & this.glVertexStream4fATI != 0L & this.glVertexStream4dATI != 0L & this.glVertexStream4iATI != 0L & this.glVertexStream4sATI != 0L & this.glNormalStream3bATI != 0L & this.glNormalStream3fATI != 0L & this.glNormalStream3dATI != 0L & this.glNormalStream3iATI != 0L & this.glNormalStream3sATI != 0L & this.glClientActiveVertexStreamATI != 0L & this.glVertexBlendEnvfATI != 0L & this.glVertexBlendEnviATI != 0L;
    }

    private boolean EXT_bindable_uniform_initNativeFunctionAddresses() {
        this.glUniformBufferEXT = GLContext.getFunctionAddress("glUniformBufferEXT");
        boolean bl = this.glUniformBufferEXT != 0L;
        this.glGetUniformBufferSizeEXT = GLContext.getFunctionAddress("glGetUniformBufferSizeEXT");
        this.glGetUniformOffsetEXT = GLContext.getFunctionAddress("glGetUniformOffsetEXT");
        return bl & this.glGetUniformBufferSizeEXT != 0L & this.glGetUniformOffsetEXT != 0L;
    }

    private boolean EXT_blend_color_initNativeFunctionAddresses() {
        this.glBlendColorEXT = GLContext.getFunctionAddress("glBlendColorEXT");
        return this.glBlendColorEXT != 0L;
    }

    private boolean EXT_blend_equation_separate_initNativeFunctionAddresses() {
        this.glBlendEquationSeparateEXT = GLContext.getFunctionAddress("glBlendEquationSeparateEXT");
        return this.glBlendEquationSeparateEXT != 0L;
    }

    private boolean EXT_blend_func_separate_initNativeFunctionAddresses() {
        this.glBlendFuncSeparateEXT = GLContext.getFunctionAddress("glBlendFuncSeparateEXT");
        return this.glBlendFuncSeparateEXT != 0L;
    }

    private boolean EXT_blend_minmax_initNativeFunctionAddresses() {
        this.glBlendEquationEXT = GLContext.getFunctionAddress("glBlendEquationEXT");
        return this.glBlendEquationEXT != 0L;
    }

    private boolean EXT_compiled_vertex_array_initNativeFunctionAddresses() {
        this.glLockArraysEXT = GLContext.getFunctionAddress("glLockArraysEXT");
        boolean bl = this.glLockArraysEXT != 0L;
        this.glUnlockArraysEXT = GLContext.getFunctionAddress("glUnlockArraysEXT");
        return bl & this.glUnlockArraysEXT != 0L;
    }

    private boolean EXT_depth_bounds_test_initNativeFunctionAddresses() {
        this.glDepthBoundsEXT = GLContext.getFunctionAddress("glDepthBoundsEXT");
        return this.glDepthBoundsEXT != 0L;
    }

    private boolean EXT_direct_state_access_initNativeFunctionAddresses(boolean forwardCompatible, Set<String> supported_extensions) {
        boolean bl = (forwardCompatible || (this.glClientAttribDefaultEXT = GLContext.getFunctionAddress("glClientAttribDefaultEXT")) != 0L) & (forwardCompatible || (this.glPushClientAttribDefaultEXT = GLContext.getFunctionAddress("glPushClientAttribDefaultEXT")) != 0L) & (forwardCompatible || (this.glMatrixLoadfEXT = GLContext.getFunctionAddress("glMatrixLoadfEXT")) != 0L) & (forwardCompatible || (this.glMatrixLoaddEXT = GLContext.getFunctionAddress("glMatrixLoaddEXT")) != 0L) & (forwardCompatible || (this.glMatrixMultfEXT = GLContext.getFunctionAddress("glMatrixMultfEXT")) != 0L) & (forwardCompatible || (this.glMatrixMultdEXT = GLContext.getFunctionAddress("glMatrixMultdEXT")) != 0L) & (forwardCompatible || (this.glMatrixLoadIdentityEXT = GLContext.getFunctionAddress("glMatrixLoadIdentityEXT")) != 0L) & (forwardCompatible || (this.glMatrixRotatefEXT = GLContext.getFunctionAddress("glMatrixRotatefEXT")) != 0L) & (forwardCompatible || (this.glMatrixRotatedEXT = GLContext.getFunctionAddress("glMatrixRotatedEXT")) != 0L) & (forwardCompatible || (this.glMatrixScalefEXT = GLContext.getFunctionAddress("glMatrixScalefEXT")) != 0L) & (forwardCompatible || (this.glMatrixScaledEXT = GLContext.getFunctionAddress("glMatrixScaledEXT")) != 0L) & (forwardCompatible || (this.glMatrixTranslatefEXT = GLContext.getFunctionAddress("glMatrixTranslatefEXT")) != 0L) & (forwardCompatible || (this.glMatrixTranslatedEXT = GLContext.getFunctionAddress("glMatrixTranslatedEXT")) != 0L) & (forwardCompatible || (this.glMatrixOrthoEXT = GLContext.getFunctionAddress("glMatrixOrthoEXT")) != 0L) & (forwardCompatible || (this.glMatrixFrustumEXT = GLContext.getFunctionAddress("glMatrixFrustumEXT")) != 0L) & (forwardCompatible || (this.glMatrixPushEXT = GLContext.getFunctionAddress("glMatrixPushEXT")) != 0L) & (forwardCompatible || (this.glMatrixPopEXT = GLContext.getFunctionAddress("glMatrixPopEXT")) != 0L);
        this.glTextureParameteriEXT = GLContext.getFunctionAddress("glTextureParameteriEXT");
        this.glTextureParameterivEXT = GLContext.getFunctionAddress("glTextureParameterivEXT");
        this.glTextureParameterfEXT = GLContext.getFunctionAddress("glTextureParameterfEXT");
        this.glTextureParameterfvEXT = GLContext.getFunctionAddress("glTextureParameterfvEXT");
        this.glTextureImage1DEXT = GLContext.getFunctionAddress("glTextureImage1DEXT");
        this.glTextureImage2DEXT = GLContext.getFunctionAddress("glTextureImage2DEXT");
        this.glTextureSubImage1DEXT = GLContext.getFunctionAddress("glTextureSubImage1DEXT");
        this.glTextureSubImage2DEXT = GLContext.getFunctionAddress("glTextureSubImage2DEXT");
        this.glCopyTextureImage1DEXT = GLContext.getFunctionAddress("glCopyTextureImage1DEXT");
        this.glCopyTextureImage2DEXT = GLContext.getFunctionAddress("glCopyTextureImage2DEXT");
        this.glCopyTextureSubImage1DEXT = GLContext.getFunctionAddress("glCopyTextureSubImage1DEXT");
        this.glCopyTextureSubImage2DEXT = GLContext.getFunctionAddress("glCopyTextureSubImage2DEXT");
        this.glGetTextureImageEXT = GLContext.getFunctionAddress("glGetTextureImageEXT");
        this.glGetTextureParameterfvEXT = GLContext.getFunctionAddress("glGetTextureParameterfvEXT");
        this.glGetTextureParameterivEXT = GLContext.getFunctionAddress("glGetTextureParameterivEXT");
        this.glGetTextureLevelParameterfvEXT = GLContext.getFunctionAddress("glGetTextureLevelParameterfvEXT");
        this.glGetTextureLevelParameterivEXT = GLContext.getFunctionAddress("glGetTextureLevelParameterivEXT");
        boolean bl2 = bl & this.glTextureParameteriEXT != 0L & this.glTextureParameterivEXT != 0L & this.glTextureParameterfEXT != 0L & this.glTextureParameterfvEXT != 0L & this.glTextureImage1DEXT != 0L & this.glTextureImage2DEXT != 0L & this.glTextureSubImage1DEXT != 0L & this.glTextureSubImage2DEXT != 0L & this.glCopyTextureImage1DEXT != 0L & this.glCopyTextureImage2DEXT != 0L & this.glCopyTextureSubImage1DEXT != 0L & this.glCopyTextureSubImage2DEXT != 0L & this.glGetTextureImageEXT != 0L & this.glGetTextureParameterfvEXT != 0L & this.glGetTextureParameterivEXT != 0L & this.glGetTextureLevelParameterfvEXT != 0L & this.glGetTextureLevelParameterivEXT != 0L & (!supported_extensions.contains("OpenGL12") || (this.glTextureImage3DEXT = GLContext.getFunctionAddress("glTextureImage3DEXT")) != 0L) & (!supported_extensions.contains("OpenGL12") || (this.glTextureSubImage3DEXT = GLContext.getFunctionAddress("glTextureSubImage3DEXT")) != 0L) & (!supported_extensions.contains("OpenGL12") || (this.glCopyTextureSubImage3DEXT = GLContext.getFunctionAddress("glCopyTextureSubImage3DEXT")) != 0L) & (!supported_extensions.contains("OpenGL13") || (this.glBindMultiTextureEXT = GLContext.getFunctionAddress("glBindMultiTextureEXT")) != 0L) & (forwardCompatible || !supported_extensions.contains("OpenGL13") || (this.glMultiTexCoordPointerEXT = GLContext.getFunctionAddress("glMultiTexCoordPointerEXT")) != 0L) & (forwardCompatible || !supported_extensions.contains("OpenGL13") || (this.glMultiTexEnvfEXT = GLContext.getFunctionAddress("glMultiTexEnvfEXT")) != 0L) & (forwardCompatible || !supported_extensions.contains("OpenGL13") || (this.glMultiTexEnvfvEXT = GLContext.getFunctionAddress("glMultiTexEnvfvEXT")) != 0L) & (forwardCompatible || !supported_extensions.contains("OpenGL13") || (this.glMultiTexEnviEXT = GLContext.getFunctionAddress("glMultiTexEnviEXT")) != 0L) & (forwardCompatible || !supported_extensions.contains("OpenGL13") || (this.glMultiTexEnvivEXT = GLContext.getFunctionAddress("glMultiTexEnvivEXT")) != 0L) & (forwardCompatible || !supported_extensions.contains("OpenGL13") || (this.glMultiTexGendEXT = GLContext.getFunctionAddress("glMultiTexGendEXT")) != 0L) & (forwardCompatible || !supported_extensions.contains("OpenGL13") || (this.glMultiTexGendvEXT = GLContext.getFunctionAddress("glMultiTexGendvEXT")) != 0L) & (forwardCompatible || !supported_extensions.contains("OpenGL13") || (this.glMultiTexGenfEXT = GLContext.getFunctionAddress("glMultiTexGenfEXT")) != 0L) & (forwardCompatible || !supported_extensions.contains("OpenGL13") || (this.glMultiTexGenfvEXT = GLContext.getFunctionAddress("glMultiTexGenfvEXT")) != 0L) & (forwardCompatible || !supported_extensions.contains("OpenGL13") || (this.glMultiTexGeniEXT = GLContext.getFunctionAddress("glMultiTexGeniEXT")) != 0L) & (forwardCompatible || !supported_extensions.contains("OpenGL13") || (this.glMultiTexGenivEXT = GLContext.getFunctionAddress("glMultiTexGenivEXT")) != 0L) & (forwardCompatible || !supported_extensions.contains("OpenGL13") || (this.glGetMultiTexEnvfvEXT = GLContext.getFunctionAddress("glGetMultiTexEnvfvEXT")) != 0L) & (forwardCompatible || !supported_extensions.contains("OpenGL13") || (this.glGetMultiTexEnvivEXT = GLContext.getFunctionAddress("glGetMultiTexEnvivEXT")) != 0L) & (forwardCompatible || !supported_extensions.contains("OpenGL13") || (this.glGetMultiTexGendvEXT = GLContext.getFunctionAddress("glGetMultiTexGendvEXT")) != 0L) & (forwardCompatible || !supported_extensions.contains("OpenGL13") || (this.glGetMultiTexGenfvEXT = GLContext.getFunctionAddress("glGetMultiTexGenfvEXT")) != 0L) & (forwardCompatible || !supported_extensions.contains("OpenGL13") || (this.glGetMultiTexGenivEXT = GLContext.getFunctionAddress("glGetMultiTexGenivEXT")) != 0L) & (!supported_extensions.contains("OpenGL13") || (this.glMultiTexParameteriEXT = GLContext.getFunctionAddress("glMultiTexParameteriEXT")) != 0L) & (!supported_extensions.contains("OpenGL13") || (this.glMultiTexParameterivEXT = GLContext.getFunctionAddress("glMultiTexParameterivEXT")) != 0L) & (!supported_extensions.contains("OpenGL13") || (this.glMultiTexParameterfEXT = GLContext.getFunctionAddress("glMultiTexParameterfEXT")) != 0L) & (!supported_extensions.contains("OpenGL13") || (this.glMultiTexParameterfvEXT = GLContext.getFunctionAddress("glMultiTexParameterfvEXT")) != 0L) & (!supported_extensions.contains("OpenGL13") || (this.glMultiTexImage1DEXT = GLContext.getFunctionAddress("glMultiTexImage1DEXT")) != 0L) & (!supported_extensions.contains("OpenGL13") || (this.glMultiTexImage2DEXT = GLContext.getFunctionAddress("glMultiTexImage2DEXT")) != 0L) & (!supported_extensions.contains("OpenGL13") || (this.glMultiTexSubImage1DEXT = GLContext.getFunctionAddress("glMultiTexSubImage1DEXT")) != 0L) & (!supported_extensions.contains("OpenGL13") || (this.glMultiTexSubImage2DEXT = GLContext.getFunctionAddress("glMultiTexSubImage2DEXT")) != 0L) & (!supported_extensions.contains("OpenGL13") || (this.glCopyMultiTexImage1DEXT = GLContext.getFunctionAddress("glCopyMultiTexImage1DEXT")) != 0L) & (!supported_extensions.contains("OpenGL13") || (this.glCopyMultiTexImage2DEXT = GLContext.getFunctionAddress("glCopyMultiTexImage2DEXT")) != 0L) & (!supported_extensions.contains("OpenGL13") || (this.glCopyMultiTexSubImage1DEXT = GLContext.getFunctionAddress("glCopyMultiTexSubImage1DEXT")) != 0L) & (!supported_extensions.contains("OpenGL13") || (this.glCopyMultiTexSubImage2DEXT = GLContext.getFunctionAddress("glCopyMultiTexSubImage2DEXT")) != 0L) & (!supported_extensions.contains("OpenGL13") || (this.glGetMultiTexImageEXT = GLContext.getFunctionAddress("glGetMultiTexImageEXT")) != 0L) & (!supported_extensions.contains("OpenGL13") || (this.glGetMultiTexParameterfvEXT = GLContext.getFunctionAddress("glGetMultiTexParameterfvEXT")) != 0L) & (!supported_extensions.contains("OpenGL13") || (this.glGetMultiTexParameterivEXT = GLContext.getFunctionAddress("glGetMultiTexParameterivEXT")) != 0L) & (!supported_extensions.contains("OpenGL13") || (this.glGetMultiTexLevelParameterfvEXT = GLContext.getFunctionAddress("glGetMultiTexLevelParameterfvEXT")) != 0L) & (!supported_extensions.contains("OpenGL13") || (this.glGetMultiTexLevelParameterivEXT = GLContext.getFunctionAddress("glGetMultiTexLevelParameterivEXT")) != 0L) & (!supported_extensions.contains("OpenGL13") || (this.glMultiTexImage3DEXT = GLContext.getFunctionAddress("glMultiTexImage3DEXT")) != 0L) & (!supported_extensions.contains("OpenGL13") || (this.glMultiTexSubImage3DEXT = GLContext.getFunctionAddress("glMultiTexSubImage3DEXT")) != 0L) & (!supported_extensions.contains("OpenGL13") || (this.glCopyMultiTexSubImage3DEXT = GLContext.getFunctionAddress("glCopyMultiTexSubImage3DEXT")) != 0L) & (forwardCompatible || !supported_extensions.contains("OpenGL13") || (this.glEnableClientStateIndexedEXT = GLContext.getFunctionAddress("glEnableClientStateIndexedEXT")) != 0L) & (forwardCompatible || !supported_extensions.contains("OpenGL13") || (this.glDisableClientStateIndexedEXT = GLContext.getFunctionAddress("glDisableClientStateIndexedEXT")) != 0L);
        if (!supported_extensions.contains("OpenGL30") || (this.glEnableClientStateiEXT = GLContext.getFunctionAddress("glEnableClientStateiEXT")) == 0L) {
            // empty if block
        }
        boolean bl3 = bl2 & true;
        if (!supported_extensions.contains("OpenGL30") || (this.glDisableClientStateiEXT = GLContext.getFunctionAddress("glDisableClientStateiEXT")) == 0L) {
            // empty if block
        }
        boolean bl4 = bl3 & true & (!supported_extensions.contains("OpenGL13") || (this.glGetFloatIndexedvEXT = GLContext.getFunctionAddress("glGetFloatIndexedvEXT")) != 0L) & (!supported_extensions.contains("OpenGL13") || (this.glGetDoubleIndexedvEXT = GLContext.getFunctionAddress("glGetDoubleIndexedvEXT")) != 0L) & (!supported_extensions.contains("OpenGL13") || (this.glGetPointerIndexedvEXT = GLContext.getFunctionAddress("glGetPointerIndexedvEXT")) != 0L);
        if (!supported_extensions.contains("OpenGL30") || (this.glGetFloati_vEXT = GLContext.getFunctionAddress("glGetFloati_vEXT")) == 0L) {
            // empty if block
        }
        boolean bl5 = bl4 & true;
        if (!supported_extensions.contains("OpenGL30") || (this.glGetDoublei_vEXT = GLContext.getFunctionAddress("glGetDoublei_vEXT")) == 0L) {
            // empty if block
        }
        boolean bl6 = bl5 & true;
        if (!supported_extensions.contains("OpenGL30") || (this.glGetPointeri_vEXT = GLContext.getFunctionAddress("glGetPointeri_vEXT")) == 0L) {
            // empty if block
        }
        return bl6 & true & (!supported_extensions.contains("OpenGL13") || (this.glEnableIndexedEXT = GLContext.getFunctionAddress("glEnableIndexedEXT")) != 0L) & (!supported_extensions.contains("OpenGL13") || (this.glDisableIndexedEXT = GLContext.getFunctionAddress("glDisableIndexedEXT")) != 0L) & (!supported_extensions.contains("OpenGL13") || (this.glIsEnabledIndexedEXT = GLContext.getFunctionAddress("glIsEnabledIndexedEXT")) != 0L) & (!supported_extensions.contains("OpenGL13") || (this.glGetIntegerIndexedvEXT = GLContext.getFunctionAddress("glGetIntegerIndexedvEXT")) != 0L) & (!supported_extensions.contains("OpenGL13") || (this.glGetBooleanIndexedvEXT = GLContext.getFunctionAddress("glGetBooleanIndexedvEXT")) != 0L) & (!supported_extensions.contains("GL_ARB_vertex_program") || (this.glNamedProgramStringEXT = GLContext.getFunctionAddress("glNamedProgramStringEXT")) != 0L) & (!supported_extensions.contains("GL_ARB_vertex_program") || (this.glNamedProgramLocalParameter4dEXT = GLContext.getFunctionAddress("glNamedProgramLocalParameter4dEXT")) != 0L) & (!supported_extensions.contains("GL_ARB_vertex_program") || (this.glNamedProgramLocalParameter4dvEXT = GLContext.getFunctionAddress("glNamedProgramLocalParameter4dvEXT")) != 0L) & (!supported_extensions.contains("GL_ARB_vertex_program") || (this.glNamedProgramLocalParameter4fEXT = GLContext.getFunctionAddress("glNamedProgramLocalParameter4fEXT")) != 0L) & (!supported_extensions.contains("GL_ARB_vertex_program") || (this.glNamedProgramLocalParameter4fvEXT = GLContext.getFunctionAddress("glNamedProgramLocalParameter4fvEXT")) != 0L) & (!supported_extensions.contains("GL_ARB_vertex_program") || (this.glGetNamedProgramLocalParameterdvEXT = GLContext.getFunctionAddress("glGetNamedProgramLocalParameterdvEXT")) != 0L) & (!supported_extensions.contains("GL_ARB_vertex_program") || (this.glGetNamedProgramLocalParameterfvEXT = GLContext.getFunctionAddress("glGetNamedProgramLocalParameterfvEXT")) != 0L) & (!supported_extensions.contains("GL_ARB_vertex_program") || (this.glGetNamedProgramivEXT = GLContext.getFunctionAddress("glGetNamedProgramivEXT")) != 0L) & (!supported_extensions.contains("GL_ARB_vertex_program") || (this.glGetNamedProgramStringEXT = GLContext.getFunctionAddress("glGetNamedProgramStringEXT")) != 0L) & (!supported_extensions.contains("OpenGL13") || (this.glCompressedTextureImage3DEXT = GLContext.getFunctionAddress("glCompressedTextureImage3DEXT")) != 0L) & (!supported_extensions.contains("OpenGL13") || (this.glCompressedTextureImage2DEXT = GLContext.getFunctionAddress("glCompressedTextureImage2DEXT")) != 0L) & (!supported_extensions.contains("OpenGL13") || (this.glCompressedTextureImage1DEXT = GLContext.getFunctionAddress("glCompressedTextureImage1DEXT")) != 0L) & (!supported_extensions.contains("OpenGL13") || (this.glCompressedTextureSubImage3DEXT = GLContext.getFunctionAddress("glCompressedTextureSubImage3DEXT")) != 0L) & (!supported_extensions.contains("OpenGL13") || (this.glCompressedTextureSubImage2DEXT = GLContext.getFunctionAddress("glCompressedTextureSubImage2DEXT")) != 0L) & (!supported_extensions.contains("OpenGL13") || (this.glCompressedTextureSubImage1DEXT = GLContext.getFunctionAddress("glCompressedTextureSubImage1DEXT")) != 0L) & (!supported_extensions.contains("OpenGL13") || (this.glGetCompressedTextureImageEXT = GLContext.getFunctionAddress("glGetCompressedTextureImageEXT")) != 0L) & (!supported_extensions.contains("OpenGL13") || (this.glCompressedMultiTexImage3DEXT = GLContext.getFunctionAddress("glCompressedMultiTexImage3DEXT")) != 0L) & (!supported_extensions.contains("OpenGL13") || (this.glCompressedMultiTexImage2DEXT = GLContext.getFunctionAddress("glCompressedMultiTexImage2DEXT")) != 0L) & (!supported_extensions.contains("OpenGL13") || (this.glCompressedMultiTexImage1DEXT = GLContext.getFunctionAddress("glCompressedMultiTexImage1DEXT")) != 0L) & (!supported_extensions.contains("OpenGL13") || (this.glCompressedMultiTexSubImage3DEXT = GLContext.getFunctionAddress("glCompressedMultiTexSubImage3DEXT")) != 0L) & (!supported_extensions.contains("OpenGL13") || (this.glCompressedMultiTexSubImage2DEXT = GLContext.getFunctionAddress("glCompressedMultiTexSubImage2DEXT")) != 0L) & (!supported_extensions.contains("OpenGL13") || (this.glCompressedMultiTexSubImage1DEXT = GLContext.getFunctionAddress("glCompressedMultiTexSubImage1DEXT")) != 0L) & (!supported_extensions.contains("OpenGL13") || (this.glGetCompressedMultiTexImageEXT = GLContext.getFunctionAddress("glGetCompressedMultiTexImageEXT")) != 0L) & (forwardCompatible || !supported_extensions.contains("OpenGL13") || (this.glMatrixLoadTransposefEXT = GLContext.getFunctionAddress("glMatrixLoadTransposefEXT")) != 0L) & (forwardCompatible || !supported_extensions.contains("OpenGL13") || (this.glMatrixLoadTransposedEXT = GLContext.getFunctionAddress("glMatrixLoadTransposedEXT")) != 0L) & (forwardCompatible || !supported_extensions.contains("OpenGL13") || (this.glMatrixMultTransposefEXT = GLContext.getFunctionAddress("glMatrixMultTransposefEXT")) != 0L) & (forwardCompatible || !supported_extensions.contains("OpenGL13") || (this.glMatrixMultTransposedEXT = GLContext.getFunctionAddress("glMatrixMultTransposedEXT")) != 0L) & (!supported_extensions.contains("OpenGL15") || (this.glNamedBufferDataEXT = GLContext.getFunctionAddress("glNamedBufferDataEXT")) != 0L) & (!supported_extensions.contains("OpenGL15") || (this.glNamedBufferSubDataEXT = GLContext.getFunctionAddress("glNamedBufferSubDataEXT")) != 0L) & (!supported_extensions.contains("OpenGL15") || (this.glMapNamedBufferEXT = GLContext.getFunctionAddress("glMapNamedBufferEXT")) != 0L) & (!supported_extensions.contains("OpenGL15") || (this.glUnmapNamedBufferEXT = GLContext.getFunctionAddress("glUnmapNamedBufferEXT")) != 0L) & (!supported_extensions.contains("OpenGL15") || (this.glGetNamedBufferParameterivEXT = GLContext.getFunctionAddress("glGetNamedBufferParameterivEXT")) != 0L) & (!supported_extensions.contains("OpenGL15") || (this.glGetNamedBufferPointervEXT = GLContext.getFunctionAddress("glGetNamedBufferPointervEXT")) != 0L) & (!supported_extensions.contains("OpenGL15") || (this.glGetNamedBufferSubDataEXT = GLContext.getFunctionAddress("glGetNamedBufferSubDataEXT")) != 0L) & (!supported_extensions.contains("OpenGL20") || (this.glProgramUniform1fEXT = GLContext.getFunctionAddress("glProgramUniform1fEXT")) != 0L) & (!supported_extensions.contains("OpenGL20") || (this.glProgramUniform2fEXT = GLContext.getFunctionAddress("glProgramUniform2fEXT")) != 0L) & (!supported_extensions.contains("OpenGL20") || (this.glProgramUniform3fEXT = GLContext.getFunctionAddress("glProgramUniform3fEXT")) != 0L) & (!supported_extensions.contains("OpenGL20") || (this.glProgramUniform4fEXT = GLContext.getFunctionAddress("glProgramUniform4fEXT")) != 0L) & (!supported_extensions.contains("OpenGL20") || (this.glProgramUniform1iEXT = GLContext.getFunctionAddress("glProgramUniform1iEXT")) != 0L) & (!supported_extensions.contains("OpenGL20") || (this.glProgramUniform2iEXT = GLContext.getFunctionAddress("glProgramUniform2iEXT")) != 0L) & (!supported_extensions.contains("OpenGL20") || (this.glProgramUniform3iEXT = GLContext.getFunctionAddress("glProgramUniform3iEXT")) != 0L) & (!supported_extensions.contains("OpenGL20") || (this.glProgramUniform4iEXT = GLContext.getFunctionAddress("glProgramUniform4iEXT")) != 0L) & (!supported_extensions.contains("OpenGL20") || (this.glProgramUniform1fvEXT = GLContext.getFunctionAddress("glProgramUniform1fvEXT")) != 0L) & (!supported_extensions.contains("OpenGL20") || (this.glProgramUniform2fvEXT = GLContext.getFunctionAddress("glProgramUniform2fvEXT")) != 0L) & (!supported_extensions.contains("OpenGL20") || (this.glProgramUniform3fvEXT = GLContext.getFunctionAddress("glProgramUniform3fvEXT")) != 0L) & (!supported_extensions.contains("OpenGL20") || (this.glProgramUniform4fvEXT = GLContext.getFunctionAddress("glProgramUniform4fvEXT")) != 0L) & (!supported_extensions.contains("OpenGL20") || (this.glProgramUniform1ivEXT = GLContext.getFunctionAddress("glProgramUniform1ivEXT")) != 0L) & (!supported_extensions.contains("OpenGL20") || (this.glProgramUniform2ivEXT = GLContext.getFunctionAddress("glProgramUniform2ivEXT")) != 0L) & (!supported_extensions.contains("OpenGL20") || (this.glProgramUniform3ivEXT = GLContext.getFunctionAddress("glProgramUniform3ivEXT")) != 0L) & (!supported_extensions.contains("OpenGL20") || (this.glProgramUniform4ivEXT = GLContext.getFunctionAddress("glProgramUniform4ivEXT")) != 0L) & (!supported_extensions.contains("OpenGL20") || (this.glProgramUniformMatrix2fvEXT = GLContext.getFunctionAddress("glProgramUniformMatrix2fvEXT")) != 0L) & (!supported_extensions.contains("OpenGL20") || (this.glProgramUniformMatrix3fvEXT = GLContext.getFunctionAddress("glProgramUniformMatrix3fvEXT")) != 0L) & (!supported_extensions.contains("OpenGL20") || (this.glProgramUniformMatrix4fvEXT = GLContext.getFunctionAddress("glProgramUniformMatrix4fvEXT")) != 0L) & (!supported_extensions.contains("OpenGL21") || (this.glProgramUniformMatrix2x3fvEXT = GLContext.getFunctionAddress("glProgramUniformMatrix2x3fvEXT")) != 0L) & (!supported_extensions.contains("OpenGL21") || (this.glProgramUniformMatrix3x2fvEXT = GLContext.getFunctionAddress("glProgramUniformMatrix3x2fvEXT")) != 0L) & (!supported_extensions.contains("OpenGL21") || (this.glProgramUniformMatrix2x4fvEXT = GLContext.getFunctionAddress("glProgramUniformMatrix2x4fvEXT")) != 0L) & (!supported_extensions.contains("OpenGL21") || (this.glProgramUniformMatrix4x2fvEXT = GLContext.getFunctionAddress("glProgramUniformMatrix4x2fvEXT")) != 0L) & (!supported_extensions.contains("OpenGL21") || (this.glProgramUniformMatrix3x4fvEXT = GLContext.getFunctionAddress("glProgramUniformMatrix3x4fvEXT")) != 0L) & (!supported_extensions.contains("OpenGL21") || (this.glProgramUniformMatrix4x3fvEXT = GLContext.getFunctionAddress("glProgramUniformMatrix4x3fvEXT")) != 0L) & (!supported_extensions.contains("GL_EXT_texture_buffer_object") || (this.glTextureBufferEXT = GLContext.getFunctionAddress("glTextureBufferEXT")) != 0L) & (!supported_extensions.contains("GL_EXT_texture_buffer_object") || (this.glMultiTexBufferEXT = GLContext.getFunctionAddress("glMultiTexBufferEXT")) != 0L) & (!supported_extensions.contains("GL_EXT_texture_integer") || (this.glTextureParameterIivEXT = GLContext.getFunctionAddress("glTextureParameterIivEXT")) != 0L) & (!supported_extensions.contains("GL_EXT_texture_integer") || (this.glTextureParameterIuivEXT = GLContext.getFunctionAddress("glTextureParameterIuivEXT")) != 0L) & (!supported_extensions.contains("GL_EXT_texture_integer") || (this.glGetTextureParameterIivEXT = GLContext.getFunctionAddress("glGetTextureParameterIivEXT")) != 0L) & (!supported_extensions.contains("GL_EXT_texture_integer") || (this.glGetTextureParameterIuivEXT = GLContext.getFunctionAddress("glGetTextureParameterIuivEXT")) != 0L) & (!supported_extensions.contains("GL_EXT_texture_integer") || (this.glMultiTexParameterIivEXT = GLContext.getFunctionAddress("glMultiTexParameterIivEXT")) != 0L) & (!supported_extensions.contains("GL_EXT_texture_integer") || (this.glMultiTexParameterIuivEXT = GLContext.getFunctionAddress("glMultiTexParameterIuivEXT")) != 0L) & (!supported_extensions.contains("GL_EXT_texture_integer") || (this.glGetMultiTexParameterIivEXT = GLContext.getFunctionAddress("glGetMultiTexParameterIivEXT")) != 0L) & (!supported_extensions.contains("GL_EXT_texture_integer") || (this.glGetMultiTexParameterIuivEXT = GLContext.getFunctionAddress("glGetMultiTexParameterIuivEXT")) != 0L) & (!supported_extensions.contains("GL_EXT_gpu_shader4") || (this.glProgramUniform1uiEXT = GLContext.getFunctionAddress("glProgramUniform1uiEXT")) != 0L) & (!supported_extensions.contains("GL_EXT_gpu_shader4") || (this.glProgramUniform2uiEXT = GLContext.getFunctionAddress("glProgramUniform2uiEXT")) != 0L) & (!supported_extensions.contains("GL_EXT_gpu_shader4") || (this.glProgramUniform3uiEXT = GLContext.getFunctionAddress("glProgramUniform3uiEXT")) != 0L) & (!supported_extensions.contains("GL_EXT_gpu_shader4") || (this.glProgramUniform4uiEXT = GLContext.getFunctionAddress("glProgramUniform4uiEXT")) != 0L) & (!supported_extensions.contains("GL_EXT_gpu_shader4") || (this.glProgramUniform1uivEXT = GLContext.getFunctionAddress("glProgramUniform1uivEXT")) != 0L) & (!supported_extensions.contains("GL_EXT_gpu_shader4") || (this.glProgramUniform2uivEXT = GLContext.getFunctionAddress("glProgramUniform2uivEXT")) != 0L) & (!supported_extensions.contains("GL_EXT_gpu_shader4") || (this.glProgramUniform3uivEXT = GLContext.getFunctionAddress("glProgramUniform3uivEXT")) != 0L) & (!supported_extensions.contains("GL_EXT_gpu_shader4") || (this.glProgramUniform4uivEXT = GLContext.getFunctionAddress("glProgramUniform4uivEXT")) != 0L) & (!supported_extensions.contains("GL_EXT_gpu_program_parameters") || (this.glNamedProgramLocalParameters4fvEXT = GLContext.getFunctionAddress("glNamedProgramLocalParameters4fvEXT")) != 0L) & (!supported_extensions.contains("GL_NV_gpu_program4") || (this.glNamedProgramLocalParameterI4iEXT = GLContext.getFunctionAddress("glNamedProgramLocalParameterI4iEXT")) != 0L) & (!supported_extensions.contains("GL_NV_gpu_program4") || (this.glNamedProgramLocalParameterI4ivEXT = GLContext.getFunctionAddress("glNamedProgramLocalParameterI4ivEXT")) != 0L) & (!supported_extensions.contains("GL_NV_gpu_program4") || (this.glNamedProgramLocalParametersI4ivEXT = GLContext.getFunctionAddress("glNamedProgramLocalParametersI4ivEXT")) != 0L) & (!supported_extensions.contains("GL_NV_gpu_program4") || (this.glNamedProgramLocalParameterI4uiEXT = GLContext.getFunctionAddress("glNamedProgramLocalParameterI4uiEXT")) != 0L) & (!supported_extensions.contains("GL_NV_gpu_program4") || (this.glNamedProgramLocalParameterI4uivEXT = GLContext.getFunctionAddress("glNamedProgramLocalParameterI4uivEXT")) != 0L) & (!supported_extensions.contains("GL_NV_gpu_program4") || (this.glNamedProgramLocalParametersI4uivEXT = GLContext.getFunctionAddress("glNamedProgramLocalParametersI4uivEXT")) != 0L) & (!supported_extensions.contains("GL_NV_gpu_program4") || (this.glGetNamedProgramLocalParameterIivEXT = GLContext.getFunctionAddress("glGetNamedProgramLocalParameterIivEXT")) != 0L) & (!supported_extensions.contains("GL_NV_gpu_program4") || (this.glGetNamedProgramLocalParameterIuivEXT = GLContext.getFunctionAddress("glGetNamedProgramLocalParameterIuivEXT")) != 0L) & (!supported_extensions.contains("OpenGL30") && !supported_extensions.contains("GL_EXT_framebuffer_object") || (this.glNamedRenderbufferStorageEXT = GLContext.getFunctionAddress("glNamedRenderbufferStorageEXT")) != 0L) & (!supported_extensions.contains("OpenGL30") && !supported_extensions.contains("GL_EXT_framebuffer_object") || (this.glGetNamedRenderbufferParameterivEXT = GLContext.getFunctionAddress("glGetNamedRenderbufferParameterivEXT")) != 0L) & (!supported_extensions.contains("OpenGL30") && !supported_extensions.contains("GL_EXT_framebuffer_multisample") || (this.glNamedRenderbufferStorageMultisampleEXT = GLContext.getFunctionAddress("glNamedRenderbufferStorageMultisampleEXT")) != 0L) & (!supported_extensions.contains("GL_NV_framebuffer_multisample_coverage") || (this.glNamedRenderbufferStorageMultisampleCoverageEXT = GLContext.getFunctionAddress("glNamedRenderbufferStorageMultisampleCoverageEXT")) != 0L) & (!supported_extensions.contains("OpenGL30") && !supported_extensions.contains("GL_EXT_framebuffer_object") || (this.glCheckNamedFramebufferStatusEXT = GLContext.getFunctionAddress("glCheckNamedFramebufferStatusEXT")) != 0L) & (!supported_extensions.contains("OpenGL30") && !supported_extensions.contains("GL_EXT_framebuffer_object") || (this.glNamedFramebufferTexture1DEXT = GLContext.getFunctionAddress("glNamedFramebufferTexture1DEXT")) != 0L) & (!supported_extensions.contains("OpenGL30") && !supported_extensions.contains("GL_EXT_framebuffer_object") || (this.glNamedFramebufferTexture2DEXT = GLContext.getFunctionAddress("glNamedFramebufferTexture2DEXT")) != 0L) & (!supported_extensions.contains("OpenGL30") && !supported_extensions.contains("GL_EXT_framebuffer_object") || (this.glNamedFramebufferTexture3DEXT = GLContext.getFunctionAddress("glNamedFramebufferTexture3DEXT")) != 0L) & (!supported_extensions.contains("OpenGL30") && !supported_extensions.contains("GL_EXT_framebuffer_object") || (this.glNamedFramebufferRenderbufferEXT = GLContext.getFunctionAddress("glNamedFramebufferRenderbufferEXT")) != 0L) & (!supported_extensions.contains("OpenGL30") && !supported_extensions.contains("GL_EXT_framebuffer_object") || (this.glGetNamedFramebufferAttachmentParameterivEXT = GLContext.getFunctionAddress("glGetNamedFramebufferAttachmentParameterivEXT")) != 0L) & (!supported_extensions.contains("OpenGL30") && !supported_extensions.contains("GL_EXT_framebuffer_object") || (this.glGenerateTextureMipmapEXT = GLContext.getFunctionAddress("glGenerateTextureMipmapEXT")) != 0L) & (!supported_extensions.contains("OpenGL30") && !supported_extensions.contains("GL_EXT_framebuffer_object") || (this.glGenerateMultiTexMipmapEXT = GLContext.getFunctionAddress("glGenerateMultiTexMipmapEXT")) != 0L) & (!supported_extensions.contains("OpenGL30") && !supported_extensions.contains("GL_EXT_framebuffer_object") || (this.glFramebufferDrawBufferEXT = GLContext.getFunctionAddress("glFramebufferDrawBufferEXT")) != 0L) & (!supported_extensions.contains("OpenGL30") && !supported_extensions.contains("GL_EXT_framebuffer_object") || (this.glFramebufferDrawBuffersEXT = GLContext.getFunctionAddress("glFramebufferDrawBuffersEXT")) != 0L) & (!supported_extensions.contains("OpenGL30") && !supported_extensions.contains("GL_EXT_framebuffer_object") || (this.glFramebufferReadBufferEXT = GLContext.getFunctionAddress("glFramebufferReadBufferEXT")) != 0L) & (!supported_extensions.contains("OpenGL30") && !supported_extensions.contains("GL_EXT_framebuffer_object") || (this.glGetFramebufferParameterivEXT = GLContext.getFunctionAddress("glGetFramebufferParameterivEXT")) != 0L) & (!supported_extensions.contains("OpenGL31") && !supported_extensions.contains("GL_ARB_copy_buffer") || (this.glNamedCopyBufferSubDataEXT = GLContext.getFunctionAddress("glNamedCopyBufferSubDataEXT")) != 0L) & (!supported_extensions.contains("GL_EXT_geometry_shader4") && !supported_extensions.contains("GL_NV_geometry_program4") || (this.glNamedFramebufferTextureEXT = GLContext.getFunctionAddress("glNamedFramebufferTextureEXT")) != 0L) & (!supported_extensions.contains("GL_EXT_geometry_shader4") && !supported_extensions.contains("GL_NV_geometry_program4") || (this.glNamedFramebufferTextureLayerEXT = GLContext.getFunctionAddress("glNamedFramebufferTextureLayerEXT")) != 0L) & (!supported_extensions.contains("GL_EXT_geometry_shader4") && !supported_extensions.contains("GL_NV_geometry_program4") || (this.glNamedFramebufferTextureFaceEXT = GLContext.getFunctionAddress("glNamedFramebufferTextureFaceEXT")) != 0L) & (!supported_extensions.contains("GL_NV_explicit_multisample") || (this.glTextureRenderbufferEXT = GLContext.getFunctionAddress("glTextureRenderbufferEXT")) != 0L) & (!supported_extensions.contains("GL_NV_explicit_multisample") || (this.glMultiTexRenderbufferEXT = GLContext.getFunctionAddress("glMultiTexRenderbufferEXT")) != 0L) & (forwardCompatible || !supported_extensions.contains("OpenGL30") || (this.glVertexArrayVertexOffsetEXT = GLContext.getFunctionAddress("glVertexArrayVertexOffsetEXT")) != 0L) & (forwardCompatible || !supported_extensions.contains("OpenGL30") || (this.glVertexArrayColorOffsetEXT = GLContext.getFunctionAddress("glVertexArrayColorOffsetEXT")) != 0L) & (forwardCompatible || !supported_extensions.contains("OpenGL30") || (this.glVertexArrayEdgeFlagOffsetEXT = GLContext.getFunctionAddress("glVertexArrayEdgeFlagOffsetEXT")) != 0L) & (!supported_extensions.contains("OpenGL30") || (this.glVertexArrayIndexOffsetEXT = GLContext.getFunctionAddress("glVertexArrayIndexOffsetEXT")) != 0L) & (forwardCompatible || !supported_extensions.contains("OpenGL30") || (this.glVertexArrayNormalOffsetEXT = GLContext.getFunctionAddress("glVertexArrayNormalOffsetEXT")) != 0L) & (forwardCompatible || !supported_extensions.contains("OpenGL30") || (this.glVertexArrayTexCoordOffsetEXT = GLContext.getFunctionAddress("glVertexArrayTexCoordOffsetEXT")) != 0L) & (forwardCompatible || !supported_extensions.contains("OpenGL30") || (this.glVertexArrayMultiTexCoordOffsetEXT = GLContext.getFunctionAddress("glVertexArrayMultiTexCoordOffsetEXT")) != 0L) & (forwardCompatible || !supported_extensions.contains("OpenGL30") || (this.glVertexArrayFogCoordOffsetEXT = GLContext.getFunctionAddress("glVertexArrayFogCoordOffsetEXT")) != 0L) & (forwardCompatible || !supported_extensions.contains("OpenGL30") || (this.glVertexArraySecondaryColorOffsetEXT = GLContext.getFunctionAddress("glVertexArraySecondaryColorOffsetEXT")) != 0L) & (!supported_extensions.contains("OpenGL30") || (this.glVertexArrayVertexAttribOffsetEXT = GLContext.getFunctionAddress("glVertexArrayVertexAttribOffsetEXT")) != 0L) & (!supported_extensions.contains("OpenGL30") || (this.glVertexArrayVertexAttribIOffsetEXT = GLContext.getFunctionAddress("glVertexArrayVertexAttribIOffsetEXT")) != 0L) & (!supported_extensions.contains("OpenGL30") || (this.glEnableVertexArrayEXT = GLContext.getFunctionAddress("glEnableVertexArrayEXT")) != 0L) & (!supported_extensions.contains("OpenGL30") || (this.glDisableVertexArrayEXT = GLContext.getFunctionAddress("glDisableVertexArrayEXT")) != 0L) & (!supported_extensions.contains("OpenGL30") || (this.glEnableVertexArrayAttribEXT = GLContext.getFunctionAddress("glEnableVertexArrayAttribEXT")) != 0L) & (!supported_extensions.contains("OpenGL30") || (this.glDisableVertexArrayAttribEXT = GLContext.getFunctionAddress("glDisableVertexArrayAttribEXT")) != 0L) & (!supported_extensions.contains("OpenGL30") || (this.glGetVertexArrayIntegervEXT = GLContext.getFunctionAddress("glGetVertexArrayIntegervEXT")) != 0L) & (!supported_extensions.contains("OpenGL30") || (this.glGetVertexArrayPointervEXT = GLContext.getFunctionAddress("glGetVertexArrayPointervEXT")) != 0L) & (!supported_extensions.contains("OpenGL30") || (this.glGetVertexArrayIntegeri_vEXT = GLContext.getFunctionAddress("glGetVertexArrayIntegeri_vEXT")) != 0L) & (!supported_extensions.contains("OpenGL30") || (this.glGetVertexArrayPointeri_vEXT = GLContext.getFunctionAddress("glGetVertexArrayPointeri_vEXT")) != 0L) & (!supported_extensions.contains("OpenGL30") || (this.glMapNamedBufferRangeEXT = GLContext.getFunctionAddress("glMapNamedBufferRangeEXT")) != 0L) & (!supported_extensions.contains("OpenGL30") || (this.glFlushMappedNamedBufferRangeEXT = GLContext.getFunctionAddress("glFlushMappedNamedBufferRangeEXT")) != 0L);
    }

    private boolean EXT_draw_buffers2_initNativeFunctionAddresses() {
        this.glColorMaskIndexedEXT = GLContext.getFunctionAddress("glColorMaskIndexedEXT");
        boolean bl = this.glColorMaskIndexedEXT != 0L;
        this.glGetBooleanIndexedvEXT = GLContext.getFunctionAddress("glGetBooleanIndexedvEXT");
        this.glGetIntegerIndexedvEXT = GLContext.getFunctionAddress("glGetIntegerIndexedvEXT");
        this.glEnableIndexedEXT = GLContext.getFunctionAddress("glEnableIndexedEXT");
        this.glDisableIndexedEXT = GLContext.getFunctionAddress("glDisableIndexedEXT");
        this.glIsEnabledIndexedEXT = GLContext.getFunctionAddress("glIsEnabledIndexedEXT");
        return bl & this.glGetBooleanIndexedvEXT != 0L & this.glGetIntegerIndexedvEXT != 0L & this.glEnableIndexedEXT != 0L & this.glDisableIndexedEXT != 0L & this.glIsEnabledIndexedEXT != 0L;
    }

    private boolean EXT_draw_instanced_initNativeFunctionAddresses() {
        this.glDrawArraysInstancedEXT = GLContext.getFunctionAddress("glDrawArraysInstancedEXT");
        boolean bl = this.glDrawArraysInstancedEXT != 0L;
        this.glDrawElementsInstancedEXT = GLContext.getFunctionAddress("glDrawElementsInstancedEXT");
        return bl & this.glDrawElementsInstancedEXT != 0L;
    }

    private boolean EXT_draw_range_elements_initNativeFunctionAddresses() {
        this.glDrawRangeElementsEXT = GLContext.getFunctionAddress("glDrawRangeElementsEXT");
        return this.glDrawRangeElementsEXT != 0L;
    }

    private boolean EXT_fog_coord_initNativeFunctionAddresses() {
        this.glFogCoordfEXT = GLContext.getFunctionAddress("glFogCoordfEXT");
        boolean bl = this.glFogCoordfEXT != 0L;
        this.glFogCoorddEXT = GLContext.getFunctionAddress("glFogCoorddEXT");
        this.glFogCoordPointerEXT = GLContext.getFunctionAddress("glFogCoordPointerEXT");
        return bl & this.glFogCoorddEXT != 0L & this.glFogCoordPointerEXT != 0L;
    }

    private boolean EXT_framebuffer_blit_initNativeFunctionAddresses() {
        this.glBlitFramebufferEXT = GLContext.getFunctionAddress("glBlitFramebufferEXT");
        return this.glBlitFramebufferEXT != 0L;
    }

    private boolean EXT_framebuffer_multisample_initNativeFunctionAddresses() {
        this.glRenderbufferStorageMultisampleEXT = GLContext.getFunctionAddress("glRenderbufferStorageMultisampleEXT");
        return this.glRenderbufferStorageMultisampleEXT != 0L;
    }

    private boolean EXT_framebuffer_object_initNativeFunctionAddresses() {
        this.glIsRenderbufferEXT = GLContext.getFunctionAddress("glIsRenderbufferEXT");
        boolean bl = this.glIsRenderbufferEXT != 0L;
        this.glBindRenderbufferEXT = GLContext.getFunctionAddress("glBindRenderbufferEXT");
        this.glDeleteRenderbuffersEXT = GLContext.getFunctionAddress("glDeleteRenderbuffersEXT");
        this.glGenRenderbuffersEXT = GLContext.getFunctionAddress("glGenRenderbuffersEXT");
        this.glRenderbufferStorageEXT = GLContext.getFunctionAddress("glRenderbufferStorageEXT");
        this.glGetRenderbufferParameterivEXT = GLContext.getFunctionAddress("glGetRenderbufferParameterivEXT");
        this.glIsFramebufferEXT = GLContext.getFunctionAddress("glIsFramebufferEXT");
        this.glBindFramebufferEXT = GLContext.getFunctionAddress("glBindFramebufferEXT");
        this.glDeleteFramebuffersEXT = GLContext.getFunctionAddress("glDeleteFramebuffersEXT");
        this.glGenFramebuffersEXT = GLContext.getFunctionAddress("glGenFramebuffersEXT");
        this.glCheckFramebufferStatusEXT = GLContext.getFunctionAddress("glCheckFramebufferStatusEXT");
        this.glFramebufferTexture1DEXT = GLContext.getFunctionAddress("glFramebufferTexture1DEXT");
        this.glFramebufferTexture2DEXT = GLContext.getFunctionAddress("glFramebufferTexture2DEXT");
        this.glFramebufferTexture3DEXT = GLContext.getFunctionAddress("glFramebufferTexture3DEXT");
        this.glFramebufferRenderbufferEXT = GLContext.getFunctionAddress("glFramebufferRenderbufferEXT");
        this.glGetFramebufferAttachmentParameterivEXT = GLContext.getFunctionAddress("glGetFramebufferAttachmentParameterivEXT");
        this.glGenerateMipmapEXT = GLContext.getFunctionAddress("glGenerateMipmapEXT");
        return bl & this.glBindRenderbufferEXT != 0L & this.glDeleteRenderbuffersEXT != 0L & this.glGenRenderbuffersEXT != 0L & this.glRenderbufferStorageEXT != 0L & this.glGetRenderbufferParameterivEXT != 0L & this.glIsFramebufferEXT != 0L & this.glBindFramebufferEXT != 0L & this.glDeleteFramebuffersEXT != 0L & this.glGenFramebuffersEXT != 0L & this.glCheckFramebufferStatusEXT != 0L & this.glFramebufferTexture1DEXT != 0L & this.glFramebufferTexture2DEXT != 0L & this.glFramebufferTexture3DEXT != 0L & this.glFramebufferRenderbufferEXT != 0L & this.glGetFramebufferAttachmentParameterivEXT != 0L & this.glGenerateMipmapEXT != 0L;
    }

    private boolean EXT_geometry_shader4_initNativeFunctionAddresses() {
        this.glProgramParameteriEXT = GLContext.getFunctionAddress("glProgramParameteriEXT");
        boolean bl = this.glProgramParameteriEXT != 0L;
        this.glFramebufferTextureEXT = GLContext.getFunctionAddress("glFramebufferTextureEXT");
        this.glFramebufferTextureLayerEXT = GLContext.getFunctionAddress("glFramebufferTextureLayerEXT");
        this.glFramebufferTextureFaceEXT = GLContext.getFunctionAddress("glFramebufferTextureFaceEXT");
        return bl & this.glFramebufferTextureEXT != 0L & this.glFramebufferTextureLayerEXT != 0L & this.glFramebufferTextureFaceEXT != 0L;
    }

    private boolean EXT_gpu_program_parameters_initNativeFunctionAddresses() {
        this.glProgramEnvParameters4fvEXT = GLContext.getFunctionAddress("glProgramEnvParameters4fvEXT");
        boolean bl = this.glProgramEnvParameters4fvEXT != 0L;
        this.glProgramLocalParameters4fvEXT = GLContext.getFunctionAddress("glProgramLocalParameters4fvEXT");
        return bl & this.glProgramLocalParameters4fvEXT != 0L;
    }

    private boolean EXT_gpu_shader4_initNativeFunctionAddresses() {
        this.glVertexAttribI1iEXT = GLContext.getFunctionAddress("glVertexAttribI1iEXT");
        boolean bl = this.glVertexAttribI1iEXT != 0L;
        this.glVertexAttribI2iEXT = GLContext.getFunctionAddress("glVertexAttribI2iEXT");
        this.glVertexAttribI3iEXT = GLContext.getFunctionAddress("glVertexAttribI3iEXT");
        this.glVertexAttribI4iEXT = GLContext.getFunctionAddress("glVertexAttribI4iEXT");
        this.glVertexAttribI1uiEXT = GLContext.getFunctionAddress("glVertexAttribI1uiEXT");
        this.glVertexAttribI2uiEXT = GLContext.getFunctionAddress("glVertexAttribI2uiEXT");
        this.glVertexAttribI3uiEXT = GLContext.getFunctionAddress("glVertexAttribI3uiEXT");
        this.glVertexAttribI4uiEXT = GLContext.getFunctionAddress("glVertexAttribI4uiEXT");
        this.glVertexAttribI1ivEXT = GLContext.getFunctionAddress("glVertexAttribI1ivEXT");
        this.glVertexAttribI2ivEXT = GLContext.getFunctionAddress("glVertexAttribI2ivEXT");
        this.glVertexAttribI3ivEXT = GLContext.getFunctionAddress("glVertexAttribI3ivEXT");
        this.glVertexAttribI4ivEXT = GLContext.getFunctionAddress("glVertexAttribI4ivEXT");
        this.glVertexAttribI1uivEXT = GLContext.getFunctionAddress("glVertexAttribI1uivEXT");
        this.glVertexAttribI2uivEXT = GLContext.getFunctionAddress("glVertexAttribI2uivEXT");
        this.glVertexAttribI3uivEXT = GLContext.getFunctionAddress("glVertexAttribI3uivEXT");
        this.glVertexAttribI4uivEXT = GLContext.getFunctionAddress("glVertexAttribI4uivEXT");
        this.glVertexAttribI4bvEXT = GLContext.getFunctionAddress("glVertexAttribI4bvEXT");
        this.glVertexAttribI4svEXT = GLContext.getFunctionAddress("glVertexAttribI4svEXT");
        this.glVertexAttribI4ubvEXT = GLContext.getFunctionAddress("glVertexAttribI4ubvEXT");
        this.glVertexAttribI4usvEXT = GLContext.getFunctionAddress("glVertexAttribI4usvEXT");
        this.glVertexAttribIPointerEXT = GLContext.getFunctionAddress("glVertexAttribIPointerEXT");
        this.glGetVertexAttribIivEXT = GLContext.getFunctionAddress("glGetVertexAttribIivEXT");
        this.glGetVertexAttribIuivEXT = GLContext.getFunctionAddress("glGetVertexAttribIuivEXT");
        this.glUniform1uiEXT = GLContext.getFunctionAddress("glUniform1uiEXT");
        this.glUniform2uiEXT = GLContext.getFunctionAddress("glUniform2uiEXT");
        this.glUniform3uiEXT = GLContext.getFunctionAddress("glUniform3uiEXT");
        this.glUniform4uiEXT = GLContext.getFunctionAddress("glUniform4uiEXT");
        this.glUniform1uivEXT = GLContext.getFunctionAddress("glUniform1uivEXT");
        this.glUniform2uivEXT = GLContext.getFunctionAddress("glUniform2uivEXT");
        this.glUniform3uivEXT = GLContext.getFunctionAddress("glUniform3uivEXT");
        this.glUniform4uivEXT = GLContext.getFunctionAddress("glUniform4uivEXT");
        this.glGetUniformuivEXT = GLContext.getFunctionAddress("glGetUniformuivEXT");
        this.glBindFragDataLocationEXT = GLContext.getFunctionAddress("glBindFragDataLocationEXT");
        this.glGetFragDataLocationEXT = GLContext.getFunctionAddress("glGetFragDataLocationEXT");
        return bl & this.glVertexAttribI2iEXT != 0L & this.glVertexAttribI3iEXT != 0L & this.glVertexAttribI4iEXT != 0L & this.glVertexAttribI1uiEXT != 0L & this.glVertexAttribI2uiEXT != 0L & this.glVertexAttribI3uiEXT != 0L & this.glVertexAttribI4uiEXT != 0L & this.glVertexAttribI1ivEXT != 0L & this.glVertexAttribI2ivEXT != 0L & this.glVertexAttribI3ivEXT != 0L & this.glVertexAttribI4ivEXT != 0L & this.glVertexAttribI1uivEXT != 0L & this.glVertexAttribI2uivEXT != 0L & this.glVertexAttribI3uivEXT != 0L & this.glVertexAttribI4uivEXT != 0L & this.glVertexAttribI4bvEXT != 0L & this.glVertexAttribI4svEXT != 0L & this.glVertexAttribI4ubvEXT != 0L & this.glVertexAttribI4usvEXT != 0L & this.glVertexAttribIPointerEXT != 0L & this.glGetVertexAttribIivEXT != 0L & this.glGetVertexAttribIuivEXT != 0L & this.glUniform1uiEXT != 0L & this.glUniform2uiEXT != 0L & this.glUniform3uiEXT != 0L & this.glUniform4uiEXT != 0L & this.glUniform1uivEXT != 0L & this.glUniform2uivEXT != 0L & this.glUniform3uivEXT != 0L & this.glUniform4uivEXT != 0L & this.glGetUniformuivEXT != 0L & this.glBindFragDataLocationEXT != 0L & this.glGetFragDataLocationEXT != 0L;
    }

    private boolean EXT_multi_draw_arrays_initNativeFunctionAddresses() {
        this.glMultiDrawArraysEXT = GLContext.getFunctionAddress("glMultiDrawArraysEXT");
        return this.glMultiDrawArraysEXT != 0L;
    }

    private boolean EXT_paletted_texture_initNativeFunctionAddresses() {
        this.glColorTableEXT = GLContext.getFunctionAddress("glColorTableEXT");
        boolean bl = this.glColorTableEXT != 0L;
        this.glColorSubTableEXT = GLContext.getFunctionAddress("glColorSubTableEXT");
        this.glGetColorTableEXT = GLContext.getFunctionAddress("glGetColorTableEXT");
        this.glGetColorTableParameterivEXT = GLContext.getFunctionAddress("glGetColorTableParameterivEXT");
        this.glGetColorTableParameterfvEXT = GLContext.getFunctionAddress("glGetColorTableParameterfvEXT");
        return bl & this.glColorSubTableEXT != 0L & this.glGetColorTableEXT != 0L & this.glGetColorTableParameterivEXT != 0L & this.glGetColorTableParameterfvEXT != 0L;
    }

    private boolean EXT_point_parameters_initNativeFunctionAddresses() {
        this.glPointParameterfEXT = GLContext.getFunctionAddress("glPointParameterfEXT");
        boolean bl = this.glPointParameterfEXT != 0L;
        this.glPointParameterfvEXT = GLContext.getFunctionAddress("glPointParameterfvEXT");
        return bl & this.glPointParameterfvEXT != 0L;
    }

    private boolean EXT_provoking_vertex_initNativeFunctionAddresses() {
        this.glProvokingVertexEXT = GLContext.getFunctionAddress("glProvokingVertexEXT");
        return this.glProvokingVertexEXT != 0L;
    }

    private boolean EXT_secondary_color_initNativeFunctionAddresses() {
        this.glSecondaryColor3bEXT = GLContext.getFunctionAddress("glSecondaryColor3bEXT");
        boolean bl = this.glSecondaryColor3bEXT != 0L;
        this.glSecondaryColor3fEXT = GLContext.getFunctionAddress("glSecondaryColor3fEXT");
        this.glSecondaryColor3dEXT = GLContext.getFunctionAddress("glSecondaryColor3dEXT");
        this.glSecondaryColor3ubEXT = GLContext.getFunctionAddress("glSecondaryColor3ubEXT");
        this.glSecondaryColorPointerEXT = GLContext.getFunctionAddress("glSecondaryColorPointerEXT");
        return bl & this.glSecondaryColor3fEXT != 0L & this.glSecondaryColor3dEXT != 0L & this.glSecondaryColor3ubEXT != 0L & this.glSecondaryColorPointerEXT != 0L;
    }

    private boolean EXT_separate_shader_objects_initNativeFunctionAddresses() {
        this.glUseShaderProgramEXT = GLContext.getFunctionAddress("glUseShaderProgramEXT");
        boolean bl = this.glUseShaderProgramEXT != 0L;
        this.glActiveProgramEXT = GLContext.getFunctionAddress("glActiveProgramEXT");
        this.glCreateShaderProgramEXT = GLContext.getFunctionAddress("glCreateShaderProgramEXT");
        return bl & this.glActiveProgramEXT != 0L & this.glCreateShaderProgramEXT != 0L;
    }

    private boolean EXT_shader_image_load_store_initNativeFunctionAddresses() {
        this.glBindImageTextureEXT = GLContext.getFunctionAddress("glBindImageTextureEXT");
        boolean bl = this.glBindImageTextureEXT != 0L;
        this.glMemoryBarrierEXT = GLContext.getFunctionAddress("glMemoryBarrierEXT");
        return bl & this.glMemoryBarrierEXT != 0L;
    }

    private boolean EXT_stencil_clear_tag_initNativeFunctionAddresses() {
        this.glStencilClearTagEXT = GLContext.getFunctionAddress("glStencilClearTagEXT");
        return this.glStencilClearTagEXT != 0L;
    }

    private boolean EXT_stencil_two_side_initNativeFunctionAddresses() {
        this.glActiveStencilFaceEXT = GLContext.getFunctionAddress("glActiveStencilFaceEXT");
        return this.glActiveStencilFaceEXT != 0L;
    }

    private boolean EXT_texture_array_initNativeFunctionAddresses() {
        this.glFramebufferTextureLayerEXT = GLContext.getFunctionAddress("glFramebufferTextureLayerEXT");
        return this.glFramebufferTextureLayerEXT != 0L;
    }

    private boolean EXT_texture_buffer_object_initNativeFunctionAddresses() {
        this.glTexBufferEXT = GLContext.getFunctionAddress("glTexBufferEXT");
        return this.glTexBufferEXT != 0L;
    }

    private boolean EXT_texture_integer_initNativeFunctionAddresses() {
        this.glClearColorIiEXT = GLContext.getFunctionAddress("glClearColorIiEXT");
        boolean bl = this.glClearColorIiEXT != 0L;
        this.glClearColorIuiEXT = GLContext.getFunctionAddress("glClearColorIuiEXT");
        this.glTexParameterIivEXT = GLContext.getFunctionAddress("glTexParameterIivEXT");
        this.glTexParameterIuivEXT = GLContext.getFunctionAddress("glTexParameterIuivEXT");
        this.glGetTexParameterIivEXT = GLContext.getFunctionAddress("glGetTexParameterIivEXT");
        this.glGetTexParameterIuivEXT = GLContext.getFunctionAddress("glGetTexParameterIuivEXT");
        return bl & this.glClearColorIuiEXT != 0L & this.glTexParameterIivEXT != 0L & this.glTexParameterIuivEXT != 0L & this.glGetTexParameterIivEXT != 0L & this.glGetTexParameterIuivEXT != 0L;
    }

    private boolean EXT_timer_query_initNativeFunctionAddresses() {
        this.glGetQueryObjecti64vEXT = GLContext.getFunctionAddress("glGetQueryObjecti64vEXT");
        boolean bl = this.glGetQueryObjecti64vEXT != 0L;
        this.glGetQueryObjectui64vEXT = GLContext.getFunctionAddress("glGetQueryObjectui64vEXT");
        return bl & this.glGetQueryObjectui64vEXT != 0L;
    }

    private boolean EXT_transform_feedback_initNativeFunctionAddresses() {
        this.glBindBufferRangeEXT = GLContext.getFunctionAddress("glBindBufferRangeEXT");
        boolean bl = this.glBindBufferRangeEXT != 0L;
        this.glBindBufferOffsetEXT = GLContext.getFunctionAddress("glBindBufferOffsetEXT");
        this.glBindBufferBaseEXT = GLContext.getFunctionAddress("glBindBufferBaseEXT");
        this.glBeginTransformFeedbackEXT = GLContext.getFunctionAddress("glBeginTransformFeedbackEXT");
        this.glEndTransformFeedbackEXT = GLContext.getFunctionAddress("glEndTransformFeedbackEXT");
        this.glTransformFeedbackVaryingsEXT = GLContext.getFunctionAddress("glTransformFeedbackVaryingsEXT");
        this.glGetTransformFeedbackVaryingEXT = GLContext.getFunctionAddress("glGetTransformFeedbackVaryingEXT");
        return bl & this.glBindBufferOffsetEXT != 0L & this.glBindBufferBaseEXT != 0L & this.glBeginTransformFeedbackEXT != 0L & this.glEndTransformFeedbackEXT != 0L & this.glTransformFeedbackVaryingsEXT != 0L & this.glGetTransformFeedbackVaryingEXT != 0L;
    }

    private boolean EXT_vertex_attrib_64bit_initNativeFunctionAddresses(Set<String> supported_extensions) {
        this.glVertexAttribL1dEXT = GLContext.getFunctionAddress("glVertexAttribL1dEXT");
        boolean bl = this.glVertexAttribL1dEXT != 0L;
        this.glVertexAttribL2dEXT = GLContext.getFunctionAddress("glVertexAttribL2dEXT");
        this.glVertexAttribL3dEXT = GLContext.getFunctionAddress("glVertexAttribL3dEXT");
        this.glVertexAttribL4dEXT = GLContext.getFunctionAddress("glVertexAttribL4dEXT");
        this.glVertexAttribL1dvEXT = GLContext.getFunctionAddress("glVertexAttribL1dvEXT");
        this.glVertexAttribL2dvEXT = GLContext.getFunctionAddress("glVertexAttribL2dvEXT");
        this.glVertexAttribL3dvEXT = GLContext.getFunctionAddress("glVertexAttribL3dvEXT");
        this.glVertexAttribL4dvEXT = GLContext.getFunctionAddress("glVertexAttribL4dvEXT");
        this.glVertexAttribLPointerEXT = GLContext.getFunctionAddress("glVertexAttribLPointerEXT");
        this.glGetVertexAttribLdvEXT = GLContext.getFunctionAddress("glGetVertexAttribLdvEXT");
        return bl & this.glVertexAttribL2dEXT != 0L & this.glVertexAttribL3dEXT != 0L & this.glVertexAttribL4dEXT != 0L & this.glVertexAttribL1dvEXT != 0L & this.glVertexAttribL2dvEXT != 0L & this.glVertexAttribL3dvEXT != 0L & this.glVertexAttribL4dvEXT != 0L & this.glVertexAttribLPointerEXT != 0L & this.glGetVertexAttribLdvEXT != 0L & (!supported_extensions.contains("GL_EXT_direct_state_access") || (this.glVertexArrayVertexAttribLOffsetEXT = GLContext.getFunctionAddress("glVertexArrayVertexAttribLOffsetEXT")) != 0L);
    }

    private boolean EXT_vertex_shader_initNativeFunctionAddresses() {
        this.glBeginVertexShaderEXT = GLContext.getFunctionAddress("glBeginVertexShaderEXT");
        boolean bl = this.glBeginVertexShaderEXT != 0L;
        this.glEndVertexShaderEXT = GLContext.getFunctionAddress("glEndVertexShaderEXT");
        this.glBindVertexShaderEXT = GLContext.getFunctionAddress("glBindVertexShaderEXT");
        this.glGenVertexShadersEXT = GLContext.getFunctionAddress("glGenVertexShadersEXT");
        this.glDeleteVertexShaderEXT = GLContext.getFunctionAddress("glDeleteVertexShaderEXT");
        this.glShaderOp1EXT = GLContext.getFunctionAddress("glShaderOp1EXT");
        this.glShaderOp2EXT = GLContext.getFunctionAddress("glShaderOp2EXT");
        this.glShaderOp3EXT = GLContext.getFunctionAddress("glShaderOp3EXT");
        this.glSwizzleEXT = GLContext.getFunctionAddress("glSwizzleEXT");
        this.glWriteMaskEXT = GLContext.getFunctionAddress("glWriteMaskEXT");
        this.glInsertComponentEXT = GLContext.getFunctionAddress("glInsertComponentEXT");
        this.glExtractComponentEXT = GLContext.getFunctionAddress("glExtractComponentEXT");
        this.glGenSymbolsEXT = GLContext.getFunctionAddress("glGenSymbolsEXT");
        this.glSetInvariantEXT = GLContext.getFunctionAddress("glSetInvariantEXT");
        this.glSetLocalConstantEXT = GLContext.getFunctionAddress("glSetLocalConstantEXT");
        this.glVariantbvEXT = GLContext.getFunctionAddress("glVariantbvEXT");
        this.glVariantsvEXT = GLContext.getFunctionAddress("glVariantsvEXT");
        this.glVariantivEXT = GLContext.getFunctionAddress("glVariantivEXT");
        this.glVariantfvEXT = GLContext.getFunctionAddress("glVariantfvEXT");
        this.glVariantdvEXT = GLContext.getFunctionAddress("glVariantdvEXT");
        this.glVariantubvEXT = GLContext.getFunctionAddress("glVariantubvEXT");
        this.glVariantusvEXT = GLContext.getFunctionAddress("glVariantusvEXT");
        this.glVariantuivEXT = GLContext.getFunctionAddress("glVariantuivEXT");
        this.glVariantPointerEXT = GLContext.getFunctionAddress("glVariantPointerEXT");
        this.glEnableVariantClientStateEXT = GLContext.getFunctionAddress("glEnableVariantClientStateEXT");
        this.glDisableVariantClientStateEXT = GLContext.getFunctionAddress("glDisableVariantClientStateEXT");
        this.glBindLightParameterEXT = GLContext.getFunctionAddress("glBindLightParameterEXT");
        this.glBindMaterialParameterEXT = GLContext.getFunctionAddress("glBindMaterialParameterEXT");
        this.glBindTexGenParameterEXT = GLContext.getFunctionAddress("glBindTexGenParameterEXT");
        this.glBindTextureUnitParameterEXT = GLContext.getFunctionAddress("glBindTextureUnitParameterEXT");
        this.glBindParameterEXT = GLContext.getFunctionAddress("glBindParameterEXT");
        this.glIsVariantEnabledEXT = GLContext.getFunctionAddress("glIsVariantEnabledEXT");
        this.glGetVariantBooleanvEXT = GLContext.getFunctionAddress("glGetVariantBooleanvEXT");
        this.glGetVariantIntegervEXT = GLContext.getFunctionAddress("glGetVariantIntegervEXT");
        this.glGetVariantFloatvEXT = GLContext.getFunctionAddress("glGetVariantFloatvEXT");
        this.glGetVariantPointervEXT = GLContext.getFunctionAddress("glGetVariantPointervEXT");
        this.glGetInvariantBooleanvEXT = GLContext.getFunctionAddress("glGetInvariantBooleanvEXT");
        this.glGetInvariantIntegervEXT = GLContext.getFunctionAddress("glGetInvariantIntegervEXT");
        this.glGetInvariantFloatvEXT = GLContext.getFunctionAddress("glGetInvariantFloatvEXT");
        this.glGetLocalConstantBooleanvEXT = GLContext.getFunctionAddress("glGetLocalConstantBooleanvEXT");
        this.glGetLocalConstantIntegervEXT = GLContext.getFunctionAddress("glGetLocalConstantIntegervEXT");
        this.glGetLocalConstantFloatvEXT = GLContext.getFunctionAddress("glGetLocalConstantFloatvEXT");
        return bl & this.glEndVertexShaderEXT != 0L & this.glBindVertexShaderEXT != 0L & this.glGenVertexShadersEXT != 0L & this.glDeleteVertexShaderEXT != 0L & this.glShaderOp1EXT != 0L & this.glShaderOp2EXT != 0L & this.glShaderOp3EXT != 0L & this.glSwizzleEXT != 0L & this.glWriteMaskEXT != 0L & this.glInsertComponentEXT != 0L & this.glExtractComponentEXT != 0L & this.glGenSymbolsEXT != 0L & this.glSetInvariantEXT != 0L & this.glSetLocalConstantEXT != 0L & this.glVariantbvEXT != 0L & this.glVariantsvEXT != 0L & this.glVariantivEXT != 0L & this.glVariantfvEXT != 0L & this.glVariantdvEXT != 0L & this.glVariantubvEXT != 0L & this.glVariantusvEXT != 0L & this.glVariantuivEXT != 0L & this.glVariantPointerEXT != 0L & this.glEnableVariantClientStateEXT != 0L & this.glDisableVariantClientStateEXT != 0L & this.glBindLightParameterEXT != 0L & this.glBindMaterialParameterEXT != 0L & this.glBindTexGenParameterEXT != 0L & this.glBindTextureUnitParameterEXT != 0L & this.glBindParameterEXT != 0L & this.glIsVariantEnabledEXT != 0L & this.glGetVariantBooleanvEXT != 0L & this.glGetVariantIntegervEXT != 0L & this.glGetVariantFloatvEXT != 0L & this.glGetVariantPointervEXT != 0L & this.glGetInvariantBooleanvEXT != 0L & this.glGetInvariantIntegervEXT != 0L & this.glGetInvariantFloatvEXT != 0L & this.glGetLocalConstantBooleanvEXT != 0L & this.glGetLocalConstantIntegervEXT != 0L & this.glGetLocalConstantFloatvEXT != 0L;
    }

    private boolean EXT_vertex_weighting_initNativeFunctionAddresses() {
        this.glVertexWeightfEXT = GLContext.getFunctionAddress("glVertexWeightfEXT");
        boolean bl = this.glVertexWeightfEXT != 0L;
        this.glVertexWeightPointerEXT = GLContext.getFunctionAddress("glVertexWeightPointerEXT");
        return bl & this.glVertexWeightPointerEXT != 0L;
    }

    private boolean GL11_initNativeFunctionAddresses(boolean forwardCompatible) {
        boolean bl = (forwardCompatible || (this.glAccum = GLContext.getFunctionAddress("glAccum")) != 0L) & (forwardCompatible || (this.glAlphaFunc = GLContext.getFunctionAddress("glAlphaFunc")) != 0L);
        this.glClearColor = GLContext.getFunctionAddress("glClearColor");
        boolean bl2 = bl & this.glClearColor != 0L & (forwardCompatible || (this.glClearAccum = GLContext.getFunctionAddress("glClearAccum")) != 0L);
        this.glClear = GLContext.getFunctionAddress("glClear");
        boolean bl3 = bl2 & this.glClear != 0L & (forwardCompatible || (this.glCallLists = GLContext.getFunctionAddress("glCallLists")) != 0L) & (forwardCompatible || (this.glCallList = GLContext.getFunctionAddress("glCallList")) != 0L);
        this.glBlendFunc = GLContext.getFunctionAddress("glBlendFunc");
        boolean bl4 = bl3 & this.glBlendFunc != 0L & (forwardCompatible || (this.glBitmap = GLContext.getFunctionAddress("glBitmap")) != 0L);
        this.glBindTexture = GLContext.getFunctionAddress("glBindTexture");
        boolean bl5 = bl4 & this.glBindTexture != 0L & (forwardCompatible || (this.glPrioritizeTextures = GLContext.getFunctionAddress("glPrioritizeTextures")) != 0L) & (forwardCompatible || (this.glAreTexturesResident = GLContext.getFunctionAddress("glAreTexturesResident")) != 0L) & (forwardCompatible || (this.glBegin = GLContext.getFunctionAddress("glBegin")) != 0L) & (forwardCompatible || (this.glEnd = GLContext.getFunctionAddress("glEnd")) != 0L);
        this.glArrayElement = GLContext.getFunctionAddress("glArrayElement");
        this.glClearDepth = GLContext.getFunctionAddress("glClearDepth");
        boolean bl6 = bl5 & this.glArrayElement != 0L & this.glClearDepth != 0L & (forwardCompatible || (this.glDeleteLists = GLContext.getFunctionAddress("glDeleteLists")) != 0L);
        this.glDeleteTextures = GLContext.getFunctionAddress("glDeleteTextures");
        this.glCullFace = GLContext.getFunctionAddress("glCullFace");
        this.glCopyTexSubImage2D = GLContext.getFunctionAddress("glCopyTexSubImage2D");
        this.glCopyTexSubImage1D = GLContext.getFunctionAddress("glCopyTexSubImage1D");
        this.glCopyTexImage2D = GLContext.getFunctionAddress("glCopyTexImage2D");
        this.glCopyTexImage1D = GLContext.getFunctionAddress("glCopyTexImage1D");
        this.glCopyPixels = GLContext.getFunctionAddress("glCopyPixels");
        boolean bl7 = bl6 & this.glDeleteTextures != 0L & this.glCullFace != 0L & this.glCopyTexSubImage2D != 0L & this.glCopyTexSubImage1D != 0L & this.glCopyTexImage2D != 0L & this.glCopyTexImage1D != 0L & this.glCopyPixels != 0L & (forwardCompatible || (this.glColorPointer = GLContext.getFunctionAddress("glColorPointer")) != 0L) & (forwardCompatible || (this.glColorMaterial = GLContext.getFunctionAddress("glColorMaterial")) != 0L);
        this.glColorMask = GLContext.getFunctionAddress("glColorMask");
        boolean bl8 = bl7 & this.glColorMask != 0L & (forwardCompatible || (this.glColor3b = GLContext.getFunctionAddress("glColor3b")) != 0L) & (forwardCompatible || (this.glColor3f = GLContext.getFunctionAddress("glColor3f")) != 0L) & (forwardCompatible || (this.glColor3d = GLContext.getFunctionAddress("glColor3d")) != 0L) & (forwardCompatible || (this.glColor3ub = GLContext.getFunctionAddress("glColor3ub")) != 0L) & (forwardCompatible || (this.glColor4b = GLContext.getFunctionAddress("glColor4b")) != 0L) & (forwardCompatible || (this.glColor4f = GLContext.getFunctionAddress("glColor4f")) != 0L) & (forwardCompatible || (this.glColor4d = GLContext.getFunctionAddress("glColor4d")) != 0L) & (forwardCompatible || (this.glColor4ub = GLContext.getFunctionAddress("glColor4ub")) != 0L);
        this.glClipPlane = GLContext.getFunctionAddress("glClipPlane");
        this.glClearStencil = GLContext.getFunctionAddress("glClearStencil");
        boolean bl9 = bl8 & this.glClipPlane != 0L & this.glClearStencil != 0L & (forwardCompatible || (this.glEvalPoint1 = GLContext.getFunctionAddress("glEvalPoint1")) != 0L) & (forwardCompatible || (this.glEvalPoint2 = GLContext.getFunctionAddress("glEvalPoint2")) != 0L) & (forwardCompatible || (this.glEvalMesh1 = GLContext.getFunctionAddress("glEvalMesh1")) != 0L) & (forwardCompatible || (this.glEvalMesh2 = GLContext.getFunctionAddress("glEvalMesh2")) != 0L) & (forwardCompatible || (this.glEvalCoord1f = GLContext.getFunctionAddress("glEvalCoord1f")) != 0L) & (forwardCompatible || (this.glEvalCoord1d = GLContext.getFunctionAddress("glEvalCoord1d")) != 0L) & (forwardCompatible || (this.glEvalCoord2f = GLContext.getFunctionAddress("glEvalCoord2f")) != 0L) & (forwardCompatible || (this.glEvalCoord2d = GLContext.getFunctionAddress("glEvalCoord2d")) != 0L) & (forwardCompatible || (this.glEnableClientState = GLContext.getFunctionAddress("glEnableClientState")) != 0L) & (forwardCompatible || (this.glDisableClientState = GLContext.getFunctionAddress("glDisableClientState")) != 0L);
        this.glEnable = GLContext.getFunctionAddress("glEnable");
        this.glDisable = GLContext.getFunctionAddress("glDisable");
        boolean bl10 = bl9 & this.glEnable != 0L & this.glDisable != 0L & (forwardCompatible || (this.glEdgeFlagPointer = GLContext.getFunctionAddress("glEdgeFlagPointer")) != 0L) & (forwardCompatible || (this.glEdgeFlag = GLContext.getFunctionAddress("glEdgeFlag")) != 0L) & (forwardCompatible || (this.glDrawPixels = GLContext.getFunctionAddress("glDrawPixels")) != 0L);
        this.glDrawElements = GLContext.getFunctionAddress("glDrawElements");
        this.glDrawBuffer = GLContext.getFunctionAddress("glDrawBuffer");
        this.glDrawArrays = GLContext.getFunctionAddress("glDrawArrays");
        this.glDepthRange = GLContext.getFunctionAddress("glDepthRange");
        this.glDepthMask = GLContext.getFunctionAddress("glDepthMask");
        this.glDepthFunc = GLContext.getFunctionAddress("glDepthFunc");
        boolean bl11 = bl10 & this.glDrawElements != 0L & this.glDrawBuffer != 0L & this.glDrawArrays != 0L & this.glDepthRange != 0L & this.glDepthMask != 0L & this.glDepthFunc != 0L & (forwardCompatible || (this.glFeedbackBuffer = GLContext.getFunctionAddress("glFeedbackBuffer")) != 0L) & (forwardCompatible || (this.glGetPixelMapfv = GLContext.getFunctionAddress("glGetPixelMapfv")) != 0L) & (forwardCompatible || (this.glGetPixelMapuiv = GLContext.getFunctionAddress("glGetPixelMapuiv")) != 0L) & (forwardCompatible || (this.glGetPixelMapusv = GLContext.getFunctionAddress("glGetPixelMapusv")) != 0L) & (forwardCompatible || (this.glGetMaterialfv = GLContext.getFunctionAddress("glGetMaterialfv")) != 0L) & (forwardCompatible || (this.glGetMaterialiv = GLContext.getFunctionAddress("glGetMaterialiv")) != 0L) & (forwardCompatible || (this.glGetMapfv = GLContext.getFunctionAddress("glGetMapfv")) != 0L) & (forwardCompatible || (this.glGetMapdv = GLContext.getFunctionAddress("glGetMapdv")) != 0L) & (forwardCompatible || (this.glGetMapiv = GLContext.getFunctionAddress("glGetMapiv")) != 0L) & (forwardCompatible || (this.glGetLightfv = GLContext.getFunctionAddress("glGetLightfv")) != 0L) & (forwardCompatible || (this.glGetLightiv = GLContext.getFunctionAddress("glGetLightiv")) != 0L);
        this.glGetError = GLContext.getFunctionAddress("glGetError");
        this.glGetClipPlane = GLContext.getFunctionAddress("glGetClipPlane");
        this.glGetBooleanv = GLContext.getFunctionAddress("glGetBooleanv");
        this.glGetDoublev = GLContext.getFunctionAddress("glGetDoublev");
        this.glGetFloatv = GLContext.getFunctionAddress("glGetFloatv");
        this.glGetIntegerv = GLContext.getFunctionAddress("glGetIntegerv");
        this.glGenTextures = GLContext.getFunctionAddress("glGenTextures");
        boolean bl12 = bl11 & this.glGetError != 0L & this.glGetClipPlane != 0L & this.glGetBooleanv != 0L & this.glGetDoublev != 0L & this.glGetFloatv != 0L & this.glGetIntegerv != 0L & this.glGenTextures != 0L & (forwardCompatible || (this.glGenLists = GLContext.getFunctionAddress("glGenLists")) != 0L) & (forwardCompatible || (this.glFrustum = GLContext.getFunctionAddress("glFrustum")) != 0L);
        this.glFrontFace = GLContext.getFunctionAddress("glFrontFace");
        boolean bl13 = bl12 & this.glFrontFace != 0L & (forwardCompatible || (this.glFogf = GLContext.getFunctionAddress("glFogf")) != 0L) & (forwardCompatible || (this.glFogi = GLContext.getFunctionAddress("glFogi")) != 0L) & (forwardCompatible || (this.glFogfv = GLContext.getFunctionAddress("glFogfv")) != 0L) & (forwardCompatible || (this.glFogiv = GLContext.getFunctionAddress("glFogiv")) != 0L);
        this.glFlush = GLContext.getFunctionAddress("glFlush");
        this.glFinish = GLContext.getFunctionAddress("glFinish");
        this.glGetPointerv = GLContext.getFunctionAddress("glGetPointerv");
        this.glIsEnabled = GLContext.getFunctionAddress("glIsEnabled");
        this.glInterleavedArrays = GLContext.getFunctionAddress("glInterleavedArrays");
        boolean bl14 = bl13 & this.glFlush != 0L & this.glFinish != 0L & this.glGetPointerv != 0L & this.glIsEnabled != 0L & this.glInterleavedArrays != 0L & (forwardCompatible || (this.glInitNames = GLContext.getFunctionAddress("glInitNames")) != 0L);
        this.glHint = GLContext.getFunctionAddress("glHint");
        this.glGetTexParameterfv = GLContext.getFunctionAddress("glGetTexParameterfv");
        this.glGetTexParameteriv = GLContext.getFunctionAddress("glGetTexParameteriv");
        this.glGetTexLevelParameterfv = GLContext.getFunctionAddress("glGetTexLevelParameterfv");
        this.glGetTexLevelParameteriv = GLContext.getFunctionAddress("glGetTexLevelParameteriv");
        this.glGetTexImage = GLContext.getFunctionAddress("glGetTexImage");
        boolean bl15 = bl14 & this.glHint != 0L & this.glGetTexParameterfv != 0L & this.glGetTexParameteriv != 0L & this.glGetTexLevelParameterfv != 0L & this.glGetTexLevelParameteriv != 0L & this.glGetTexImage != 0L & (forwardCompatible || (this.glGetTexGeniv = GLContext.getFunctionAddress("glGetTexGeniv")) != 0L) & (forwardCompatible || (this.glGetTexGenfv = GLContext.getFunctionAddress("glGetTexGenfv")) != 0L) & (forwardCompatible || (this.glGetTexGendv = GLContext.getFunctionAddress("glGetTexGendv")) != 0L);
        this.glGetTexEnviv = GLContext.getFunctionAddress("glGetTexEnviv");
        this.glGetTexEnvfv = GLContext.getFunctionAddress("glGetTexEnvfv");
        this.glGetString = GLContext.getFunctionAddress("glGetString");
        boolean bl16 = bl15 & this.glGetTexEnviv != 0L & this.glGetTexEnvfv != 0L & this.glGetString != 0L & (forwardCompatible || (this.glGetPolygonStipple = GLContext.getFunctionAddress("glGetPolygonStipple")) != 0L) & (forwardCompatible || (this.glIsList = GLContext.getFunctionAddress("glIsList")) != 0L) & (forwardCompatible || (this.glMaterialf = GLContext.getFunctionAddress("glMaterialf")) != 0L) & (forwardCompatible || (this.glMateriali = GLContext.getFunctionAddress("glMateriali")) != 0L) & (forwardCompatible || (this.glMaterialfv = GLContext.getFunctionAddress("glMaterialfv")) != 0L) & (forwardCompatible || (this.glMaterialiv = GLContext.getFunctionAddress("glMaterialiv")) != 0L) & (forwardCompatible || (this.glMapGrid1f = GLContext.getFunctionAddress("glMapGrid1f")) != 0L) & (forwardCompatible || (this.glMapGrid1d = GLContext.getFunctionAddress("glMapGrid1d")) != 0L) & (forwardCompatible || (this.glMapGrid2f = GLContext.getFunctionAddress("glMapGrid2f")) != 0L) & (forwardCompatible || (this.glMapGrid2d = GLContext.getFunctionAddress("glMapGrid2d")) != 0L) & (forwardCompatible || (this.glMap2f = GLContext.getFunctionAddress("glMap2f")) != 0L) & (forwardCompatible || (this.glMap2d = GLContext.getFunctionAddress("glMap2d")) != 0L) & (forwardCompatible || (this.glMap1f = GLContext.getFunctionAddress("glMap1f")) != 0L) & (forwardCompatible || (this.glMap1d = GLContext.getFunctionAddress("glMap1d")) != 0L);
        this.glLogicOp = GLContext.getFunctionAddress("glLogicOp");
        boolean bl17 = bl16 & this.glLogicOp != 0L & (forwardCompatible || (this.glLoadName = GLContext.getFunctionAddress("glLoadName")) != 0L) & (forwardCompatible || (this.glLoadMatrixf = GLContext.getFunctionAddress("glLoadMatrixf")) != 0L) & (forwardCompatible || (this.glLoadMatrixd = GLContext.getFunctionAddress("glLoadMatrixd")) != 0L) & (forwardCompatible || (this.glLoadIdentity = GLContext.getFunctionAddress("glLoadIdentity")) != 0L) & (forwardCompatible || (this.glListBase = GLContext.getFunctionAddress("glListBase")) != 0L);
        this.glLineWidth = GLContext.getFunctionAddress("glLineWidth");
        boolean bl18 = bl17 & this.glLineWidth != 0L & (forwardCompatible || (this.glLineStipple = GLContext.getFunctionAddress("glLineStipple")) != 0L) & (forwardCompatible || (this.glLightModelf = GLContext.getFunctionAddress("glLightModelf")) != 0L) & (forwardCompatible || (this.glLightModeli = GLContext.getFunctionAddress("glLightModeli")) != 0L) & (forwardCompatible || (this.glLightModelfv = GLContext.getFunctionAddress("glLightModelfv")) != 0L) & (forwardCompatible || (this.glLightModeliv = GLContext.getFunctionAddress("glLightModeliv")) != 0L) & (forwardCompatible || (this.glLightf = GLContext.getFunctionAddress("glLightf")) != 0L) & (forwardCompatible || (this.glLighti = GLContext.getFunctionAddress("glLighti")) != 0L) & (forwardCompatible || (this.glLightfv = GLContext.getFunctionAddress("glLightfv")) != 0L) & (forwardCompatible || (this.glLightiv = GLContext.getFunctionAddress("glLightiv")) != 0L);
        this.glIsTexture = GLContext.getFunctionAddress("glIsTexture");
        boolean bl19 = bl18 & this.glIsTexture != 0L & (forwardCompatible || (this.glMatrixMode = GLContext.getFunctionAddress("glMatrixMode")) != 0L) & (forwardCompatible || (this.glPolygonStipple = GLContext.getFunctionAddress("glPolygonStipple")) != 0L);
        this.glPolygonOffset = GLContext.getFunctionAddress("glPolygonOffset");
        this.glPolygonMode = GLContext.getFunctionAddress("glPolygonMode");
        this.glPointSize = GLContext.getFunctionAddress("glPointSize");
        boolean bl20 = bl19 & this.glPolygonOffset != 0L & this.glPolygonMode != 0L & this.glPointSize != 0L & (forwardCompatible || (this.glPixelZoom = GLContext.getFunctionAddress("glPixelZoom")) != 0L) & (forwardCompatible || (this.glPixelTransferf = GLContext.getFunctionAddress("glPixelTransferf")) != 0L) & (forwardCompatible || (this.glPixelTransferi = GLContext.getFunctionAddress("glPixelTransferi")) != 0L);
        this.glPixelStoref = GLContext.getFunctionAddress("glPixelStoref");
        this.glPixelStorei = GLContext.getFunctionAddress("glPixelStorei");
        boolean bl21 = bl20 & this.glPixelStoref != 0L & this.glPixelStorei != 0L & (forwardCompatible || (this.glPixelMapfv = GLContext.getFunctionAddress("glPixelMapfv")) != 0L) & (forwardCompatible || (this.glPixelMapuiv = GLContext.getFunctionAddress("glPixelMapuiv")) != 0L) & (forwardCompatible || (this.glPixelMapusv = GLContext.getFunctionAddress("glPixelMapusv")) != 0L) & (forwardCompatible || (this.glPassThrough = GLContext.getFunctionAddress("glPassThrough")) != 0L) & (forwardCompatible || (this.glOrtho = GLContext.getFunctionAddress("glOrtho")) != 0L) & (forwardCompatible || (this.glNormalPointer = GLContext.getFunctionAddress("glNormalPointer")) != 0L) & (forwardCompatible || (this.glNormal3b = GLContext.getFunctionAddress("glNormal3b")) != 0L) & (forwardCompatible || (this.glNormal3f = GLContext.getFunctionAddress("glNormal3f")) != 0L) & (forwardCompatible || (this.glNormal3d = GLContext.getFunctionAddress("glNormal3d")) != 0L) & (forwardCompatible || (this.glNormal3i = GLContext.getFunctionAddress("glNormal3i")) != 0L) & (forwardCompatible || (this.glNewList = GLContext.getFunctionAddress("glNewList")) != 0L) & (forwardCompatible || (this.glEndList = GLContext.getFunctionAddress("glEndList")) != 0L) & (forwardCompatible || (this.glMultMatrixf = GLContext.getFunctionAddress("glMultMatrixf")) != 0L) & (forwardCompatible || (this.glMultMatrixd = GLContext.getFunctionAddress("glMultMatrixd")) != 0L);
        this.glShadeModel = GLContext.getFunctionAddress("glShadeModel");
        boolean bl22 = bl21 & this.glShadeModel != 0L & (forwardCompatible || (this.glSelectBuffer = GLContext.getFunctionAddress("glSelectBuffer")) != 0L);
        this.glScissor = GLContext.getFunctionAddress("glScissor");
        boolean bl23 = bl22 & this.glScissor != 0L & (forwardCompatible || (this.glScalef = GLContext.getFunctionAddress("glScalef")) != 0L) & (forwardCompatible || (this.glScaled = GLContext.getFunctionAddress("glScaled")) != 0L) & (forwardCompatible || (this.glRotatef = GLContext.getFunctionAddress("glRotatef")) != 0L) & (forwardCompatible || (this.glRotated = GLContext.getFunctionAddress("glRotated")) != 0L) & (forwardCompatible || (this.glRenderMode = GLContext.getFunctionAddress("glRenderMode")) != 0L) & (forwardCompatible || (this.glRectf = GLContext.getFunctionAddress("glRectf")) != 0L) & (forwardCompatible || (this.glRectd = GLContext.getFunctionAddress("glRectd")) != 0L) & (forwardCompatible || (this.glRecti = GLContext.getFunctionAddress("glRecti")) != 0L);
        this.glReadPixels = GLContext.getFunctionAddress("glReadPixels");
        this.glReadBuffer = GLContext.getFunctionAddress("glReadBuffer");
        boolean bl24 = bl23 & this.glReadPixels != 0L & this.glReadBuffer != 0L & (forwardCompatible || (this.glRasterPos2f = GLContext.getFunctionAddress("glRasterPos2f")) != 0L) & (forwardCompatible || (this.glRasterPos2d = GLContext.getFunctionAddress("glRasterPos2d")) != 0L) & (forwardCompatible || (this.glRasterPos2i = GLContext.getFunctionAddress("glRasterPos2i")) != 0L) & (forwardCompatible || (this.glRasterPos3f = GLContext.getFunctionAddress("glRasterPos3f")) != 0L) & (forwardCompatible || (this.glRasterPos3d = GLContext.getFunctionAddress("glRasterPos3d")) != 0L) & (forwardCompatible || (this.glRasterPos3i = GLContext.getFunctionAddress("glRasterPos3i")) != 0L) & (forwardCompatible || (this.glRasterPos4f = GLContext.getFunctionAddress("glRasterPos4f")) != 0L) & (forwardCompatible || (this.glRasterPos4d = GLContext.getFunctionAddress("glRasterPos4d")) != 0L) & (forwardCompatible || (this.glRasterPos4i = GLContext.getFunctionAddress("glRasterPos4i")) != 0L) & (forwardCompatible || (this.glPushName = GLContext.getFunctionAddress("glPushName")) != 0L) & (forwardCompatible || (this.glPopName = GLContext.getFunctionAddress("glPopName")) != 0L) & (forwardCompatible || (this.glPushMatrix = GLContext.getFunctionAddress("glPushMatrix")) != 0L) & (forwardCompatible || (this.glPopMatrix = GLContext.getFunctionAddress("glPopMatrix")) != 0L) & (forwardCompatible || (this.glPushClientAttrib = GLContext.getFunctionAddress("glPushClientAttrib")) != 0L) & (forwardCompatible || (this.glPopClientAttrib = GLContext.getFunctionAddress("glPopClientAttrib")) != 0L) & (forwardCompatible || (this.glPushAttrib = GLContext.getFunctionAddress("glPushAttrib")) != 0L) & (forwardCompatible || (this.glPopAttrib = GLContext.getFunctionAddress("glPopAttrib")) != 0L);
        this.glStencilFunc = GLContext.getFunctionAddress("glStencilFunc");
        boolean bl25 = bl24 & this.glStencilFunc != 0L & (forwardCompatible || (this.glVertexPointer = GLContext.getFunctionAddress("glVertexPointer")) != 0L) & (forwardCompatible || (this.glVertex2f = GLContext.getFunctionAddress("glVertex2f")) != 0L) & (forwardCompatible || (this.glVertex2d = GLContext.getFunctionAddress("glVertex2d")) != 0L) & (forwardCompatible || (this.glVertex2i = GLContext.getFunctionAddress("glVertex2i")) != 0L) & (forwardCompatible || (this.glVertex3f = GLContext.getFunctionAddress("glVertex3f")) != 0L) & (forwardCompatible || (this.glVertex3d = GLContext.getFunctionAddress("glVertex3d")) != 0L) & (forwardCompatible || (this.glVertex3i = GLContext.getFunctionAddress("glVertex3i")) != 0L) & (forwardCompatible || (this.glVertex4f = GLContext.getFunctionAddress("glVertex4f")) != 0L) & (forwardCompatible || (this.glVertex4d = GLContext.getFunctionAddress("glVertex4d")) != 0L) & (forwardCompatible || (this.glVertex4i = GLContext.getFunctionAddress("glVertex4i")) != 0L) & (forwardCompatible || (this.glTranslatef = GLContext.getFunctionAddress("glTranslatef")) != 0L) & (forwardCompatible || (this.glTranslated = GLContext.getFunctionAddress("glTranslated")) != 0L);
        this.glTexImage1D = GLContext.getFunctionAddress("glTexImage1D");
        this.glTexImage2D = GLContext.getFunctionAddress("glTexImage2D");
        this.glTexSubImage1D = GLContext.getFunctionAddress("glTexSubImage1D");
        this.glTexSubImage2D = GLContext.getFunctionAddress("glTexSubImage2D");
        this.glTexParameterf = GLContext.getFunctionAddress("glTexParameterf");
        this.glTexParameteri = GLContext.getFunctionAddress("glTexParameteri");
        this.glTexParameterfv = GLContext.getFunctionAddress("glTexParameterfv");
        this.glTexParameteriv = GLContext.getFunctionAddress("glTexParameteriv");
        boolean bl26 = bl25 & this.glTexImage1D != 0L & this.glTexImage2D != 0L & this.glTexSubImage1D != 0L & this.glTexSubImage2D != 0L & this.glTexParameterf != 0L & this.glTexParameteri != 0L & this.glTexParameterfv != 0L & this.glTexParameteriv != 0L & (forwardCompatible || (this.glTexGenf = GLContext.getFunctionAddress("glTexGenf")) != 0L) & (forwardCompatible || (this.glTexGend = GLContext.getFunctionAddress("glTexGend")) != 0L) & (forwardCompatible || (this.glTexGenfv = GLContext.getFunctionAddress("glTexGenfv")) != 0L) & (forwardCompatible || (this.glTexGendv = GLContext.getFunctionAddress("glTexGendv")) != 0L) & (forwardCompatible || (this.glTexGeni = GLContext.getFunctionAddress("glTexGeni")) != 0L) & (forwardCompatible || (this.glTexGeniv = GLContext.getFunctionAddress("glTexGeniv")) != 0L);
        this.glTexEnvf = GLContext.getFunctionAddress("glTexEnvf");
        this.glTexEnvi = GLContext.getFunctionAddress("glTexEnvi");
        this.glTexEnvfv = GLContext.getFunctionAddress("glTexEnvfv");
        this.glTexEnviv = GLContext.getFunctionAddress("glTexEnviv");
        boolean bl27 = bl26 & this.glTexEnvf != 0L & this.glTexEnvi != 0L & this.glTexEnvfv != 0L & this.glTexEnviv != 0L & (forwardCompatible || (this.glTexCoordPointer = GLContext.getFunctionAddress("glTexCoordPointer")) != 0L) & (forwardCompatible || (this.glTexCoord1f = GLContext.getFunctionAddress("glTexCoord1f")) != 0L) & (forwardCompatible || (this.glTexCoord1d = GLContext.getFunctionAddress("glTexCoord1d")) != 0L) & (forwardCompatible || (this.glTexCoord2f = GLContext.getFunctionAddress("glTexCoord2f")) != 0L) & (forwardCompatible || (this.glTexCoord2d = GLContext.getFunctionAddress("glTexCoord2d")) != 0L) & (forwardCompatible || (this.glTexCoord3f = GLContext.getFunctionAddress("glTexCoord3f")) != 0L) & (forwardCompatible || (this.glTexCoord3d = GLContext.getFunctionAddress("glTexCoord3d")) != 0L) & (forwardCompatible || (this.glTexCoord4f = GLContext.getFunctionAddress("glTexCoord4f")) != 0L) & (forwardCompatible || (this.glTexCoord4d = GLContext.getFunctionAddress("glTexCoord4d")) != 0L);
        this.glStencilOp = GLContext.getFunctionAddress("glStencilOp");
        this.glStencilMask = GLContext.getFunctionAddress("glStencilMask");
        this.glViewport = GLContext.getFunctionAddress("glViewport");
        return bl27 & this.glStencilOp != 0L & this.glStencilMask != 0L & this.glViewport != 0L;
    }

    private boolean GL12_initNativeFunctionAddresses() {
        this.glDrawRangeElements = GLContext.getFunctionAddress("glDrawRangeElements");
        boolean bl = this.glDrawRangeElements != 0L;
        this.glTexImage3D = GLContext.getFunctionAddress("glTexImage3D");
        this.glTexSubImage3D = GLContext.getFunctionAddress("glTexSubImage3D");
        this.glCopyTexSubImage3D = GLContext.getFunctionAddress("glCopyTexSubImage3D");
        return bl & this.glTexImage3D != 0L & this.glTexSubImage3D != 0L & this.glCopyTexSubImage3D != 0L;
    }

    private boolean GL13_initNativeFunctionAddresses(boolean forwardCompatible) {
        this.glActiveTexture = GLContext.getFunctionAddress("glActiveTexture");
        boolean bl = this.glActiveTexture != 0L & (forwardCompatible || (this.glClientActiveTexture = GLContext.getFunctionAddress("glClientActiveTexture")) != 0L);
        this.glCompressedTexImage1D = GLContext.getFunctionAddress("glCompressedTexImage1D");
        this.glCompressedTexImage2D = GLContext.getFunctionAddress("glCompressedTexImage2D");
        this.glCompressedTexImage3D = GLContext.getFunctionAddress("glCompressedTexImage3D");
        this.glCompressedTexSubImage1D = GLContext.getFunctionAddress("glCompressedTexSubImage1D");
        this.glCompressedTexSubImage2D = GLContext.getFunctionAddress("glCompressedTexSubImage2D");
        this.glCompressedTexSubImage3D = GLContext.getFunctionAddress("glCompressedTexSubImage3D");
        this.glGetCompressedTexImage = GLContext.getFunctionAddress("glGetCompressedTexImage");
        boolean bl2 = bl & this.glCompressedTexImage1D != 0L & this.glCompressedTexImage2D != 0L & this.glCompressedTexImage3D != 0L & this.glCompressedTexSubImage1D != 0L & this.glCompressedTexSubImage2D != 0L & this.glCompressedTexSubImage3D != 0L & this.glGetCompressedTexImage != 0L & (forwardCompatible || (this.glMultiTexCoord1f = GLContext.getFunctionAddress("glMultiTexCoord1f")) != 0L) & (forwardCompatible || (this.glMultiTexCoord1d = GLContext.getFunctionAddress("glMultiTexCoord1d")) != 0L) & (forwardCompatible || (this.glMultiTexCoord2f = GLContext.getFunctionAddress("glMultiTexCoord2f")) != 0L) & (forwardCompatible || (this.glMultiTexCoord2d = GLContext.getFunctionAddress("glMultiTexCoord2d")) != 0L) & (forwardCompatible || (this.glMultiTexCoord3f = GLContext.getFunctionAddress("glMultiTexCoord3f")) != 0L) & (forwardCompatible || (this.glMultiTexCoord3d = GLContext.getFunctionAddress("glMultiTexCoord3d")) != 0L) & (forwardCompatible || (this.glMultiTexCoord4f = GLContext.getFunctionAddress("glMultiTexCoord4f")) != 0L) & (forwardCompatible || (this.glMultiTexCoord4d = GLContext.getFunctionAddress("glMultiTexCoord4d")) != 0L) & (forwardCompatible || (this.glLoadTransposeMatrixf = GLContext.getFunctionAddress("glLoadTransposeMatrixf")) != 0L) & (forwardCompatible || (this.glLoadTransposeMatrixd = GLContext.getFunctionAddress("glLoadTransposeMatrixd")) != 0L) & (forwardCompatible || (this.glMultTransposeMatrixf = GLContext.getFunctionAddress("glMultTransposeMatrixf")) != 0L) & (forwardCompatible || (this.glMultTransposeMatrixd = GLContext.getFunctionAddress("glMultTransposeMatrixd")) != 0L);
        this.glSampleCoverage = GLContext.getFunctionAddress("glSampleCoverage");
        return bl2 & this.glSampleCoverage != 0L;
    }

    private boolean GL14_initNativeFunctionAddresses(boolean forwardCompatible) {
        this.glBlendEquation = GLContext.getFunctionAddress("glBlendEquation");
        boolean bl = this.glBlendEquation != 0L;
        this.glBlendColor = GLContext.getFunctionAddress("glBlendColor");
        boolean bl2 = bl & this.glBlendColor != 0L & (forwardCompatible || (this.glFogCoordf = GLContext.getFunctionAddress("glFogCoordf")) != 0L) & (forwardCompatible || (this.glFogCoordd = GLContext.getFunctionAddress("glFogCoordd")) != 0L) & (forwardCompatible || (this.glFogCoordPointer = GLContext.getFunctionAddress("glFogCoordPointer")) != 0L);
        this.glMultiDrawArrays = GLContext.getFunctionAddress("glMultiDrawArrays");
        this.glPointParameteri = GLContext.getFunctionAddress("glPointParameteri");
        this.glPointParameterf = GLContext.getFunctionAddress("glPointParameterf");
        this.glPointParameteriv = GLContext.getFunctionAddress("glPointParameteriv");
        this.glPointParameterfv = GLContext.getFunctionAddress("glPointParameterfv");
        boolean bl3 = bl2 & this.glMultiDrawArrays != 0L & this.glPointParameteri != 0L & this.glPointParameterf != 0L & this.glPointParameteriv != 0L & this.glPointParameterfv != 0L & (forwardCompatible || (this.glSecondaryColor3b = GLContext.getFunctionAddress("glSecondaryColor3b")) != 0L) & (forwardCompatible || (this.glSecondaryColor3f = GLContext.getFunctionAddress("glSecondaryColor3f")) != 0L) & (forwardCompatible || (this.glSecondaryColor3d = GLContext.getFunctionAddress("glSecondaryColor3d")) != 0L) & (forwardCompatible || (this.glSecondaryColor3ub = GLContext.getFunctionAddress("glSecondaryColor3ub")) != 0L) & (forwardCompatible || (this.glSecondaryColorPointer = GLContext.getFunctionAddress("glSecondaryColorPointer")) != 0L);
        this.glBlendFuncSeparate = GLContext.getFunctionAddress("glBlendFuncSeparate");
        return bl3 & this.glBlendFuncSeparate != 0L & (forwardCompatible || (this.glWindowPos2f = GLContext.getFunctionAddress("glWindowPos2f")) != 0L) & (forwardCompatible || (this.glWindowPos2d = GLContext.getFunctionAddress("glWindowPos2d")) != 0L) & (forwardCompatible || (this.glWindowPos2i = GLContext.getFunctionAddress("glWindowPos2i")) != 0L) & (forwardCompatible || (this.glWindowPos3f = GLContext.getFunctionAddress("glWindowPos3f")) != 0L) & (forwardCompatible || (this.glWindowPos3d = GLContext.getFunctionAddress("glWindowPos3d")) != 0L) & (forwardCompatible || (this.glWindowPos3i = GLContext.getFunctionAddress("glWindowPos3i")) != 0L);
    }

    private boolean GL15_initNativeFunctionAddresses() {
        this.glBindBuffer = GLContext.getFunctionAddress("glBindBuffer");
        boolean bl = this.glBindBuffer != 0L;
        this.glDeleteBuffers = GLContext.getFunctionAddress("glDeleteBuffers");
        this.glGenBuffers = GLContext.getFunctionAddress("glGenBuffers");
        this.glIsBuffer = GLContext.getFunctionAddress("glIsBuffer");
        this.glBufferData = GLContext.getFunctionAddress("glBufferData");
        this.glBufferSubData = GLContext.getFunctionAddress("glBufferSubData");
        this.glGetBufferSubData = GLContext.getFunctionAddress("glGetBufferSubData");
        this.glMapBuffer = GLContext.getFunctionAddress("glMapBuffer");
        this.glUnmapBuffer = GLContext.getFunctionAddress("glUnmapBuffer");
        this.glGetBufferParameteriv = GLContext.getFunctionAddress("glGetBufferParameteriv");
        this.glGetBufferPointerv = GLContext.getFunctionAddress("glGetBufferPointerv");
        this.glGenQueries = GLContext.getFunctionAddress("glGenQueries");
        this.glDeleteQueries = GLContext.getFunctionAddress("glDeleteQueries");
        this.glIsQuery = GLContext.getFunctionAddress("glIsQuery");
        this.glBeginQuery = GLContext.getFunctionAddress("glBeginQuery");
        this.glEndQuery = GLContext.getFunctionAddress("glEndQuery");
        this.glGetQueryiv = GLContext.getFunctionAddress("glGetQueryiv");
        this.glGetQueryObjectiv = GLContext.getFunctionAddress("glGetQueryObjectiv");
        this.glGetQueryObjectuiv = GLContext.getFunctionAddress("glGetQueryObjectuiv");
        return bl & this.glDeleteBuffers != 0L & this.glGenBuffers != 0L & this.glIsBuffer != 0L & this.glBufferData != 0L & this.glBufferSubData != 0L & this.glGetBufferSubData != 0L & this.glMapBuffer != 0L & this.glUnmapBuffer != 0L & this.glGetBufferParameteriv != 0L & this.glGetBufferPointerv != 0L & this.glGenQueries != 0L & this.glDeleteQueries != 0L & this.glIsQuery != 0L & this.glBeginQuery != 0L & this.glEndQuery != 0L & this.glGetQueryiv != 0L & this.glGetQueryObjectiv != 0L & this.glGetQueryObjectuiv != 0L;
    }

    private boolean GL20_initNativeFunctionAddresses() {
        this.glShaderSource = GLContext.getFunctionAddress("glShaderSource");
        boolean bl = this.glShaderSource != 0L;
        this.glCreateShader = GLContext.getFunctionAddress("glCreateShader");
        this.glIsShader = GLContext.getFunctionAddress("glIsShader");
        this.glCompileShader = GLContext.getFunctionAddress("glCompileShader");
        this.glDeleteShader = GLContext.getFunctionAddress("glDeleteShader");
        this.glCreateProgram = GLContext.getFunctionAddress("glCreateProgram");
        this.glIsProgram = GLContext.getFunctionAddress("glIsProgram");
        this.glAttachShader = GLContext.getFunctionAddress("glAttachShader");
        this.glDetachShader = GLContext.getFunctionAddress("glDetachShader");
        this.glLinkProgram = GLContext.getFunctionAddress("glLinkProgram");
        this.glUseProgram = GLContext.getFunctionAddress("glUseProgram");
        this.glValidateProgram = GLContext.getFunctionAddress("glValidateProgram");
        this.glDeleteProgram = GLContext.getFunctionAddress("glDeleteProgram");
        this.glUniform1f = GLContext.getFunctionAddress("glUniform1f");
        this.glUniform2f = GLContext.getFunctionAddress("glUniform2f");
        this.glUniform3f = GLContext.getFunctionAddress("glUniform3f");
        this.glUniform4f = GLContext.getFunctionAddress("glUniform4f");
        this.glUniform1i = GLContext.getFunctionAddress("glUniform1i");
        this.glUniform2i = GLContext.getFunctionAddress("glUniform2i");
        this.glUniform3i = GLContext.getFunctionAddress("glUniform3i");
        this.glUniform4i = GLContext.getFunctionAddress("glUniform4i");
        this.glUniform1fv = GLContext.getFunctionAddress("glUniform1fv");
        this.glUniform2fv = GLContext.getFunctionAddress("glUniform2fv");
        this.glUniform3fv = GLContext.getFunctionAddress("glUniform3fv");
        this.glUniform4fv = GLContext.getFunctionAddress("glUniform4fv");
        this.glUniform1iv = GLContext.getFunctionAddress("glUniform1iv");
        this.glUniform2iv = GLContext.getFunctionAddress("glUniform2iv");
        this.glUniform3iv = GLContext.getFunctionAddress("glUniform3iv");
        this.glUniform4iv = GLContext.getFunctionAddress("glUniform4iv");
        this.glUniformMatrix2fv = GLContext.getFunctionAddress("glUniformMatrix2fv");
        this.glUniformMatrix3fv = GLContext.getFunctionAddress("glUniformMatrix3fv");
        this.glUniformMatrix4fv = GLContext.getFunctionAddress("glUniformMatrix4fv");
        this.glGetShaderiv = GLContext.getFunctionAddress("glGetShaderiv");
        this.glGetProgramiv = GLContext.getFunctionAddress("glGetProgramiv");
        this.glGetShaderInfoLog = GLContext.getFunctionAddress("glGetShaderInfoLog");
        this.glGetProgramInfoLog = GLContext.getFunctionAddress("glGetProgramInfoLog");
        this.glGetAttachedShaders = GLContext.getFunctionAddress("glGetAttachedShaders");
        this.glGetUniformLocation = GLContext.getFunctionAddress("glGetUniformLocation");
        this.glGetActiveUniform = GLContext.getFunctionAddress("glGetActiveUniform");
        this.glGetUniformfv = GLContext.getFunctionAddress("glGetUniformfv");
        this.glGetUniformiv = GLContext.getFunctionAddress("glGetUniformiv");
        this.glGetShaderSource = GLContext.getFunctionAddress("glGetShaderSource");
        this.glVertexAttrib1s = GLContext.getFunctionAddress("glVertexAttrib1s");
        this.glVertexAttrib1f = GLContext.getFunctionAddress("glVertexAttrib1f");
        this.glVertexAttrib1d = GLContext.getFunctionAddress("glVertexAttrib1d");
        this.glVertexAttrib2s = GLContext.getFunctionAddress("glVertexAttrib2s");
        this.glVertexAttrib2f = GLContext.getFunctionAddress("glVertexAttrib2f");
        this.glVertexAttrib2d = GLContext.getFunctionAddress("glVertexAttrib2d");
        this.glVertexAttrib3s = GLContext.getFunctionAddress("glVertexAttrib3s");
        this.glVertexAttrib3f = GLContext.getFunctionAddress("glVertexAttrib3f");
        this.glVertexAttrib3d = GLContext.getFunctionAddress("glVertexAttrib3d");
        this.glVertexAttrib4s = GLContext.getFunctionAddress("glVertexAttrib4s");
        this.glVertexAttrib4f = GLContext.getFunctionAddress("glVertexAttrib4f");
        this.glVertexAttrib4d = GLContext.getFunctionAddress("glVertexAttrib4d");
        this.glVertexAttrib4Nub = GLContext.getFunctionAddress("glVertexAttrib4Nub");
        this.glVertexAttribPointer = GLContext.getFunctionAddress("glVertexAttribPointer");
        this.glEnableVertexAttribArray = GLContext.getFunctionAddress("glEnableVertexAttribArray");
        this.glDisableVertexAttribArray = GLContext.getFunctionAddress("glDisableVertexAttribArray");
        this.glGetVertexAttribfv = GLContext.getFunctionAddress("glGetVertexAttribfv");
        this.glGetVertexAttribdv = GLContext.getFunctionAddress("glGetVertexAttribdv");
        this.glGetVertexAttribiv = GLContext.getFunctionAddress("glGetVertexAttribiv");
        this.glGetVertexAttribPointerv = GLContext.getFunctionAddress("glGetVertexAttribPointerv");
        this.glBindAttribLocation = GLContext.getFunctionAddress("glBindAttribLocation");
        this.glGetActiveAttrib = GLContext.getFunctionAddress("glGetActiveAttrib");
        this.glGetAttribLocation = GLContext.getFunctionAddress("glGetAttribLocation");
        this.glDrawBuffers = GLContext.getFunctionAddress("glDrawBuffers");
        this.glStencilOpSeparate = GLContext.getFunctionAddress("glStencilOpSeparate");
        this.glStencilFuncSeparate = GLContext.getFunctionAddress("glStencilFuncSeparate");
        this.glStencilMaskSeparate = GLContext.getFunctionAddress("glStencilMaskSeparate");
        this.glBlendEquationSeparate = GLContext.getFunctionAddress("glBlendEquationSeparate");
        return bl & this.glCreateShader != 0L & this.glIsShader != 0L & this.glCompileShader != 0L & this.glDeleteShader != 0L & this.glCreateProgram != 0L & this.glIsProgram != 0L & this.glAttachShader != 0L & this.glDetachShader != 0L & this.glLinkProgram != 0L & this.glUseProgram != 0L & this.glValidateProgram != 0L & this.glDeleteProgram != 0L & this.glUniform1f != 0L & this.glUniform2f != 0L & this.glUniform3f != 0L & this.glUniform4f != 0L & this.glUniform1i != 0L & this.glUniform2i != 0L & this.glUniform3i != 0L & this.glUniform4i != 0L & this.glUniform1fv != 0L & this.glUniform2fv != 0L & this.glUniform3fv != 0L & this.glUniform4fv != 0L & this.glUniform1iv != 0L & this.glUniform2iv != 0L & this.glUniform3iv != 0L & this.glUniform4iv != 0L & this.glUniformMatrix2fv != 0L & this.glUniformMatrix3fv != 0L & this.glUniformMatrix4fv != 0L & this.glGetShaderiv != 0L & this.glGetProgramiv != 0L & this.glGetShaderInfoLog != 0L & this.glGetProgramInfoLog != 0L & this.glGetAttachedShaders != 0L & this.glGetUniformLocation != 0L & this.glGetActiveUniform != 0L & this.glGetUniformfv != 0L & this.glGetUniformiv != 0L & this.glGetShaderSource != 0L & this.glVertexAttrib1s != 0L & this.glVertexAttrib1f != 0L & this.glVertexAttrib1d != 0L & this.glVertexAttrib2s != 0L & this.glVertexAttrib2f != 0L & this.glVertexAttrib2d != 0L & this.glVertexAttrib3s != 0L & this.glVertexAttrib3f != 0L & this.glVertexAttrib3d != 0L & this.glVertexAttrib4s != 0L & this.glVertexAttrib4f != 0L & this.glVertexAttrib4d != 0L & this.glVertexAttrib4Nub != 0L & this.glVertexAttribPointer != 0L & this.glEnableVertexAttribArray != 0L & this.glDisableVertexAttribArray != 0L & this.glGetVertexAttribfv != 0L & this.glGetVertexAttribdv != 0L & this.glGetVertexAttribiv != 0L & this.glGetVertexAttribPointerv != 0L & this.glBindAttribLocation != 0L & this.glGetActiveAttrib != 0L & this.glGetAttribLocation != 0L & this.glDrawBuffers != 0L & this.glStencilOpSeparate != 0L & this.glStencilFuncSeparate != 0L & this.glStencilMaskSeparate != 0L & this.glBlendEquationSeparate != 0L;
    }

    private boolean GL21_initNativeFunctionAddresses() {
        this.glUniformMatrix2x3fv = GLContext.getFunctionAddress("glUniformMatrix2x3fv");
        boolean bl = this.glUniformMatrix2x3fv != 0L;
        this.glUniformMatrix3x2fv = GLContext.getFunctionAddress("glUniformMatrix3x2fv");
        this.glUniformMatrix2x4fv = GLContext.getFunctionAddress("glUniformMatrix2x4fv");
        this.glUniformMatrix4x2fv = GLContext.getFunctionAddress("glUniformMatrix4x2fv");
        this.glUniformMatrix3x4fv = GLContext.getFunctionAddress("glUniformMatrix3x4fv");
        this.glUniformMatrix4x3fv = GLContext.getFunctionAddress("glUniformMatrix4x3fv");
        return bl & this.glUniformMatrix3x2fv != 0L & this.glUniformMatrix2x4fv != 0L & this.glUniformMatrix4x2fv != 0L & this.glUniformMatrix3x4fv != 0L & this.glUniformMatrix4x3fv != 0L;
    }

    private boolean GL30_initNativeFunctionAddresses() {
        this.glGetStringi = GLContext.getFunctionAddress("glGetStringi");
        boolean bl = this.glGetStringi != 0L;
        this.glClearBufferfv = GLContext.getFunctionAddress("glClearBufferfv");
        this.glClearBufferiv = GLContext.getFunctionAddress("glClearBufferiv");
        this.glClearBufferuiv = GLContext.getFunctionAddress("glClearBufferuiv");
        this.glClearBufferfi = GLContext.getFunctionAddress("glClearBufferfi");
        this.glVertexAttribI1i = GLContext.getFunctionAddress("glVertexAttribI1i");
        this.glVertexAttribI2i = GLContext.getFunctionAddress("glVertexAttribI2i");
        this.glVertexAttribI3i = GLContext.getFunctionAddress("glVertexAttribI3i");
        this.glVertexAttribI4i = GLContext.getFunctionAddress("glVertexAttribI4i");
        this.glVertexAttribI1ui = GLContext.getFunctionAddress("glVertexAttribI1ui");
        this.glVertexAttribI2ui = GLContext.getFunctionAddress("glVertexAttribI2ui");
        this.glVertexAttribI3ui = GLContext.getFunctionAddress("glVertexAttribI3ui");
        this.glVertexAttribI4ui = GLContext.getFunctionAddress("glVertexAttribI4ui");
        this.glVertexAttribI1iv = GLContext.getFunctionAddress("glVertexAttribI1iv");
        this.glVertexAttribI2iv = GLContext.getFunctionAddress("glVertexAttribI2iv");
        this.glVertexAttribI3iv = GLContext.getFunctionAddress("glVertexAttribI3iv");
        this.glVertexAttribI4iv = GLContext.getFunctionAddress("glVertexAttribI4iv");
        this.glVertexAttribI1uiv = GLContext.getFunctionAddress("glVertexAttribI1uiv");
        this.glVertexAttribI2uiv = GLContext.getFunctionAddress("glVertexAttribI2uiv");
        this.glVertexAttribI3uiv = GLContext.getFunctionAddress("glVertexAttribI3uiv");
        this.glVertexAttribI4uiv = GLContext.getFunctionAddress("glVertexAttribI4uiv");
        this.glVertexAttribI4bv = GLContext.getFunctionAddress("glVertexAttribI4bv");
        this.glVertexAttribI4sv = GLContext.getFunctionAddress("glVertexAttribI4sv");
        this.glVertexAttribI4ubv = GLContext.getFunctionAddress("glVertexAttribI4ubv");
        this.glVertexAttribI4usv = GLContext.getFunctionAddress("glVertexAttribI4usv");
        this.glVertexAttribIPointer = GLContext.getFunctionAddress("glVertexAttribIPointer");
        this.glGetVertexAttribIiv = GLContext.getFunctionAddress("glGetVertexAttribIiv");
        this.glGetVertexAttribIuiv = GLContext.getFunctionAddress("glGetVertexAttribIuiv");
        this.glUniform1ui = GLContext.getFunctionAddress("glUniform1ui");
        this.glUniform2ui = GLContext.getFunctionAddress("glUniform2ui");
        this.glUniform3ui = GLContext.getFunctionAddress("glUniform3ui");
        this.glUniform4ui = GLContext.getFunctionAddress("glUniform4ui");
        this.glUniform1uiv = GLContext.getFunctionAddress("glUniform1uiv");
        this.glUniform2uiv = GLContext.getFunctionAddress("glUniform2uiv");
        this.glUniform3uiv = GLContext.getFunctionAddress("glUniform3uiv");
        this.glUniform4uiv = GLContext.getFunctionAddress("glUniform4uiv");
        this.glGetUniformuiv = GLContext.getFunctionAddress("glGetUniformuiv");
        this.glBindFragDataLocation = GLContext.getFunctionAddress("glBindFragDataLocation");
        this.glGetFragDataLocation = GLContext.getFunctionAddress("glGetFragDataLocation");
        this.glBeginConditionalRender = GLContext.getFunctionAddress("glBeginConditionalRender");
        this.glEndConditionalRender = GLContext.getFunctionAddress("glEndConditionalRender");
        this.glMapBufferRange = GLContext.getFunctionAddress("glMapBufferRange");
        this.glFlushMappedBufferRange = GLContext.getFunctionAddress("glFlushMappedBufferRange");
        this.glClampColor = GLContext.getFunctionAddress("glClampColor");
        this.glIsRenderbuffer = GLContext.getFunctionAddress("glIsRenderbuffer");
        this.glBindRenderbuffer = GLContext.getFunctionAddress("glBindRenderbuffer");
        this.glDeleteRenderbuffers = GLContext.getFunctionAddress("glDeleteRenderbuffers");
        this.glGenRenderbuffers = GLContext.getFunctionAddress("glGenRenderbuffers");
        this.glRenderbufferStorage = GLContext.getFunctionAddress("glRenderbufferStorage");
        this.glGetRenderbufferParameteriv = GLContext.getFunctionAddress("glGetRenderbufferParameteriv");
        this.glIsFramebuffer = GLContext.getFunctionAddress("glIsFramebuffer");
        this.glBindFramebuffer = GLContext.getFunctionAddress("glBindFramebuffer");
        this.glDeleteFramebuffers = GLContext.getFunctionAddress("glDeleteFramebuffers");
        this.glGenFramebuffers = GLContext.getFunctionAddress("glGenFramebuffers");
        this.glCheckFramebufferStatus = GLContext.getFunctionAddress("glCheckFramebufferStatus");
        this.glFramebufferTexture1D = GLContext.getFunctionAddress("glFramebufferTexture1D");
        this.glFramebufferTexture2D = GLContext.getFunctionAddress("glFramebufferTexture2D");
        this.glFramebufferTexture3D = GLContext.getFunctionAddress("glFramebufferTexture3D");
        this.glFramebufferRenderbuffer = GLContext.getFunctionAddress("glFramebufferRenderbuffer");
        this.glGetFramebufferAttachmentParameteriv = GLContext.getFunctionAddress("glGetFramebufferAttachmentParameteriv");
        this.glGenerateMipmap = GLContext.getFunctionAddress("glGenerateMipmap");
        this.glRenderbufferStorageMultisample = GLContext.getFunctionAddress("glRenderbufferStorageMultisample");
        this.glBlitFramebuffer = GLContext.getFunctionAddress("glBlitFramebuffer");
        this.glTexParameterIiv = GLContext.getFunctionAddress("glTexParameterIiv");
        this.glTexParameterIuiv = GLContext.getFunctionAddress("glTexParameterIuiv");
        this.glGetTexParameterIiv = GLContext.getFunctionAddress("glGetTexParameterIiv");
        this.glGetTexParameterIuiv = GLContext.getFunctionAddress("glGetTexParameterIuiv");
        this.glFramebufferTextureLayer = GLContext.getFunctionAddress("glFramebufferTextureLayer");
        this.glColorMaski = GLContext.getFunctionAddress("glColorMaski");
        this.glGetBooleani_v = GLContext.getFunctionAddress("glGetBooleani_v");
        this.glGetIntegeri_v = GLContext.getFunctionAddress("glGetIntegeri_v");
        this.glEnablei = GLContext.getFunctionAddress("glEnablei");
        this.glDisablei = GLContext.getFunctionAddress("glDisablei");
        this.glIsEnabledi = GLContext.getFunctionAddress("glIsEnabledi");
        this.glBindBufferRange = GLContext.getFunctionAddress("glBindBufferRange");
        this.glBindBufferBase = GLContext.getFunctionAddress("glBindBufferBase");
        this.glBeginTransformFeedback = GLContext.getFunctionAddress("glBeginTransformFeedback");
        this.glEndTransformFeedback = GLContext.getFunctionAddress("glEndTransformFeedback");
        this.glTransformFeedbackVaryings = GLContext.getFunctionAddress("glTransformFeedbackVaryings");
        this.glGetTransformFeedbackVarying = GLContext.getFunctionAddress("glGetTransformFeedbackVarying");
        this.glBindVertexArray = GLContext.getFunctionAddress("glBindVertexArray");
        this.glDeleteVertexArrays = GLContext.getFunctionAddress("glDeleteVertexArrays");
        this.glGenVertexArrays = GLContext.getFunctionAddress("glGenVertexArrays");
        this.glIsVertexArray = GLContext.getFunctionAddress("glIsVertexArray");
        return bl & this.glClearBufferfv != 0L & this.glClearBufferiv != 0L & this.glClearBufferuiv != 0L & this.glClearBufferfi != 0L & this.glVertexAttribI1i != 0L & this.glVertexAttribI2i != 0L & this.glVertexAttribI3i != 0L & this.glVertexAttribI4i != 0L & this.glVertexAttribI1ui != 0L & this.glVertexAttribI2ui != 0L & this.glVertexAttribI3ui != 0L & this.glVertexAttribI4ui != 0L & this.glVertexAttribI1iv != 0L & this.glVertexAttribI2iv != 0L & this.glVertexAttribI3iv != 0L & this.glVertexAttribI4iv != 0L & this.glVertexAttribI1uiv != 0L & this.glVertexAttribI2uiv != 0L & this.glVertexAttribI3uiv != 0L & this.glVertexAttribI4uiv != 0L & this.glVertexAttribI4bv != 0L & this.glVertexAttribI4sv != 0L & this.glVertexAttribI4ubv != 0L & this.glVertexAttribI4usv != 0L & this.glVertexAttribIPointer != 0L & this.glGetVertexAttribIiv != 0L & this.glGetVertexAttribIuiv != 0L & this.glUniform1ui != 0L & this.glUniform2ui != 0L & this.glUniform3ui != 0L & this.glUniform4ui != 0L & this.glUniform1uiv != 0L & this.glUniform2uiv != 0L & this.glUniform3uiv != 0L & this.glUniform4uiv != 0L & this.glGetUniformuiv != 0L & this.glBindFragDataLocation != 0L & this.glGetFragDataLocation != 0L & this.glBeginConditionalRender != 0L & this.glEndConditionalRender != 0L & this.glMapBufferRange != 0L & this.glFlushMappedBufferRange != 0L & this.glClampColor != 0L & this.glIsRenderbuffer != 0L & this.glBindRenderbuffer != 0L & this.glDeleteRenderbuffers != 0L & this.glGenRenderbuffers != 0L & this.glRenderbufferStorage != 0L & this.glGetRenderbufferParameteriv != 0L & this.glIsFramebuffer != 0L & this.glBindFramebuffer != 0L & this.glDeleteFramebuffers != 0L & this.glGenFramebuffers != 0L & this.glCheckFramebufferStatus != 0L & this.glFramebufferTexture1D != 0L & this.glFramebufferTexture2D != 0L & this.glFramebufferTexture3D != 0L & this.glFramebufferRenderbuffer != 0L & this.glGetFramebufferAttachmentParameteriv != 0L & this.glGenerateMipmap != 0L & this.glRenderbufferStorageMultisample != 0L & this.glBlitFramebuffer != 0L & this.glTexParameterIiv != 0L & this.glTexParameterIuiv != 0L & this.glGetTexParameterIiv != 0L & this.glGetTexParameterIuiv != 0L & this.glFramebufferTextureLayer != 0L & this.glColorMaski != 0L & this.glGetBooleani_v != 0L & this.glGetIntegeri_v != 0L & this.glEnablei != 0L & this.glDisablei != 0L & this.glIsEnabledi != 0L & this.glBindBufferRange != 0L & this.glBindBufferBase != 0L & this.glBeginTransformFeedback != 0L & this.glEndTransformFeedback != 0L & this.glTransformFeedbackVaryings != 0L & this.glGetTransformFeedbackVarying != 0L & this.glBindVertexArray != 0L & this.glDeleteVertexArrays != 0L & this.glGenVertexArrays != 0L & this.glIsVertexArray != 0L;
    }

    private boolean GL31_initNativeFunctionAddresses() {
        this.glDrawArraysInstanced = GLContext.getFunctionAddress("glDrawArraysInstanced");
        boolean bl = this.glDrawArraysInstanced != 0L;
        this.glDrawElementsInstanced = GLContext.getFunctionAddress("glDrawElementsInstanced");
        this.glCopyBufferSubData = GLContext.getFunctionAddress("glCopyBufferSubData");
        this.glPrimitiveRestartIndex = GLContext.getFunctionAddress("glPrimitiveRestartIndex");
        this.glTexBuffer = GLContext.getFunctionAddress("glTexBuffer");
        this.glGetUniformIndices = GLContext.getFunctionAddress("glGetUniformIndices");
        this.glGetActiveUniformsiv = GLContext.getFunctionAddress("glGetActiveUniformsiv");
        this.glGetActiveUniformName = GLContext.getFunctionAddress("glGetActiveUniformName");
        this.glGetUniformBlockIndex = GLContext.getFunctionAddress("glGetUniformBlockIndex");
        this.glGetActiveUniformBlockiv = GLContext.getFunctionAddress("glGetActiveUniformBlockiv");
        this.glGetActiveUniformBlockName = GLContext.getFunctionAddress("glGetActiveUniformBlockName");
        this.glUniformBlockBinding = GLContext.getFunctionAddress("glUniformBlockBinding");
        return bl & this.glDrawElementsInstanced != 0L & this.glCopyBufferSubData != 0L & this.glPrimitiveRestartIndex != 0L & this.glTexBuffer != 0L & this.glGetUniformIndices != 0L & this.glGetActiveUniformsiv != 0L & this.glGetActiveUniformName != 0L & this.glGetUniformBlockIndex != 0L & this.glGetActiveUniformBlockiv != 0L & this.glGetActiveUniformBlockName != 0L & this.glUniformBlockBinding != 0L;
    }

    private boolean GL32_initNativeFunctionAddresses() {
        this.glGetBufferParameteri64v = GLContext.getFunctionAddress("glGetBufferParameteri64v");
        boolean bl = this.glGetBufferParameteri64v != 0L;
        this.glDrawElementsBaseVertex = GLContext.getFunctionAddress("glDrawElementsBaseVertex");
        this.glDrawRangeElementsBaseVertex = GLContext.getFunctionAddress("glDrawRangeElementsBaseVertex");
        this.glDrawElementsInstancedBaseVertex = GLContext.getFunctionAddress("glDrawElementsInstancedBaseVertex");
        this.glProvokingVertex = GLContext.getFunctionAddress("glProvokingVertex");
        this.glTexImage2DMultisample = GLContext.getFunctionAddress("glTexImage2DMultisample");
        this.glTexImage3DMultisample = GLContext.getFunctionAddress("glTexImage3DMultisample");
        this.glGetMultisamplefv = GLContext.getFunctionAddress("glGetMultisamplefv");
        this.glSampleMaski = GLContext.getFunctionAddress("glSampleMaski");
        this.glFramebufferTexture = GLContext.getFunctionAddress("glFramebufferTexture");
        this.glFenceSync = GLContext.getFunctionAddress("glFenceSync");
        this.glIsSync = GLContext.getFunctionAddress("glIsSync");
        this.glDeleteSync = GLContext.getFunctionAddress("glDeleteSync");
        this.glClientWaitSync = GLContext.getFunctionAddress("glClientWaitSync");
        this.glWaitSync = GLContext.getFunctionAddress("glWaitSync");
        this.glGetInteger64v = GLContext.getFunctionAddress("glGetInteger64v");
        this.glGetInteger64i_v = GLContext.getFunctionAddress("glGetInteger64i_v");
        this.glGetSynciv = GLContext.getFunctionAddress("glGetSynciv");
        return bl & this.glDrawElementsBaseVertex != 0L & this.glDrawRangeElementsBaseVertex != 0L & this.glDrawElementsInstancedBaseVertex != 0L & this.glProvokingVertex != 0L & this.glTexImage2DMultisample != 0L & this.glTexImage3DMultisample != 0L & this.glGetMultisamplefv != 0L & this.glSampleMaski != 0L & this.glFramebufferTexture != 0L & this.glFenceSync != 0L & this.glIsSync != 0L & this.glDeleteSync != 0L & this.glClientWaitSync != 0L & this.glWaitSync != 0L & this.glGetInteger64v != 0L & this.glGetInteger64i_v != 0L & this.glGetSynciv != 0L;
    }

    private boolean GL33_initNativeFunctionAddresses(boolean forwardCompatible) {
        this.glBindFragDataLocationIndexed = GLContext.getFunctionAddress("glBindFragDataLocationIndexed");
        boolean bl = this.glBindFragDataLocationIndexed != 0L;
        this.glGetFragDataIndex = GLContext.getFunctionAddress("glGetFragDataIndex");
        this.glGenSamplers = GLContext.getFunctionAddress("glGenSamplers");
        this.glDeleteSamplers = GLContext.getFunctionAddress("glDeleteSamplers");
        this.glIsSampler = GLContext.getFunctionAddress("glIsSampler");
        this.glBindSampler = GLContext.getFunctionAddress("glBindSampler");
        this.glSamplerParameteri = GLContext.getFunctionAddress("glSamplerParameteri");
        this.glSamplerParameterf = GLContext.getFunctionAddress("glSamplerParameterf");
        this.glSamplerParameteriv = GLContext.getFunctionAddress("glSamplerParameteriv");
        this.glSamplerParameterfv = GLContext.getFunctionAddress("glSamplerParameterfv");
        this.glSamplerParameterIiv = GLContext.getFunctionAddress("glSamplerParameterIiv");
        this.glSamplerParameterIuiv = GLContext.getFunctionAddress("glSamplerParameterIuiv");
        this.glGetSamplerParameteriv = GLContext.getFunctionAddress("glGetSamplerParameteriv");
        this.glGetSamplerParameterfv = GLContext.getFunctionAddress("glGetSamplerParameterfv");
        this.glGetSamplerParameterIiv = GLContext.getFunctionAddress("glGetSamplerParameterIiv");
        this.glGetSamplerParameterIuiv = GLContext.getFunctionAddress("glGetSamplerParameterIuiv");
        this.glQueryCounter = GLContext.getFunctionAddress("glQueryCounter");
        this.glGetQueryObjecti64v = GLContext.getFunctionAddress("glGetQueryObjecti64v");
        this.glGetQueryObjectui64v = GLContext.getFunctionAddress("glGetQueryObjectui64v");
        this.glVertexAttribDivisor = GLContext.getFunctionAddress("glVertexAttribDivisor");
        return bl & this.glGetFragDataIndex != 0L & this.glGenSamplers != 0L & this.glDeleteSamplers != 0L & this.glIsSampler != 0L & this.glBindSampler != 0L & this.glSamplerParameteri != 0L & this.glSamplerParameterf != 0L & this.glSamplerParameteriv != 0L & this.glSamplerParameterfv != 0L & this.glSamplerParameterIiv != 0L & this.glSamplerParameterIuiv != 0L & this.glGetSamplerParameteriv != 0L & this.glGetSamplerParameterfv != 0L & this.glGetSamplerParameterIiv != 0L & this.glGetSamplerParameterIuiv != 0L & this.glQueryCounter != 0L & this.glGetQueryObjecti64v != 0L & this.glGetQueryObjectui64v != 0L & this.glVertexAttribDivisor != 0L & (forwardCompatible || (this.glVertexP2ui = GLContext.getFunctionAddress("glVertexP2ui")) != 0L) & (forwardCompatible || (this.glVertexP3ui = GLContext.getFunctionAddress("glVertexP3ui")) != 0L) & (forwardCompatible || (this.glVertexP4ui = GLContext.getFunctionAddress("glVertexP4ui")) != 0L) & (forwardCompatible || (this.glVertexP2uiv = GLContext.getFunctionAddress("glVertexP2uiv")) != 0L) & (forwardCompatible || (this.glVertexP3uiv = GLContext.getFunctionAddress("glVertexP3uiv")) != 0L) & (forwardCompatible || (this.glVertexP4uiv = GLContext.getFunctionAddress("glVertexP4uiv")) != 0L) & (forwardCompatible || (this.glTexCoordP1ui = GLContext.getFunctionAddress("glTexCoordP1ui")) != 0L) & (forwardCompatible || (this.glTexCoordP2ui = GLContext.getFunctionAddress("glTexCoordP2ui")) != 0L) & (forwardCompatible || (this.glTexCoordP3ui = GLContext.getFunctionAddress("glTexCoordP3ui")) != 0L) & (forwardCompatible || (this.glTexCoordP4ui = GLContext.getFunctionAddress("glTexCoordP4ui")) != 0L) & (forwardCompatible || (this.glTexCoordP1uiv = GLContext.getFunctionAddress("glTexCoordP1uiv")) != 0L) & (forwardCompatible || (this.glTexCoordP2uiv = GLContext.getFunctionAddress("glTexCoordP2uiv")) != 0L) & (forwardCompatible || (this.glTexCoordP3uiv = GLContext.getFunctionAddress("glTexCoordP3uiv")) != 0L) & (forwardCompatible || (this.glTexCoordP4uiv = GLContext.getFunctionAddress("glTexCoordP4uiv")) != 0L) & (forwardCompatible || (this.glMultiTexCoordP1ui = GLContext.getFunctionAddress("glMultiTexCoordP1ui")) != 0L) & (forwardCompatible || (this.glMultiTexCoordP2ui = GLContext.getFunctionAddress("glMultiTexCoordP2ui")) != 0L) & (forwardCompatible || (this.glMultiTexCoordP3ui = GLContext.getFunctionAddress("glMultiTexCoordP3ui")) != 0L) & (forwardCompatible || (this.glMultiTexCoordP4ui = GLContext.getFunctionAddress("glMultiTexCoordP4ui")) != 0L) & (forwardCompatible || (this.glMultiTexCoordP1uiv = GLContext.getFunctionAddress("glMultiTexCoordP1uiv")) != 0L) & (forwardCompatible || (this.glMultiTexCoordP2uiv = GLContext.getFunctionAddress("glMultiTexCoordP2uiv")) != 0L) & (forwardCompatible || (this.glMultiTexCoordP3uiv = GLContext.getFunctionAddress("glMultiTexCoordP3uiv")) != 0L) & (forwardCompatible || (this.glMultiTexCoordP4uiv = GLContext.getFunctionAddress("glMultiTexCoordP4uiv")) != 0L) & (forwardCompatible || (this.glNormalP3ui = GLContext.getFunctionAddress("glNormalP3ui")) != 0L) & (forwardCompatible || (this.glNormalP3uiv = GLContext.getFunctionAddress("glNormalP3uiv")) != 0L) & (forwardCompatible || (this.glColorP3ui = GLContext.getFunctionAddress("glColorP3ui")) != 0L) & (forwardCompatible || (this.glColorP4ui = GLContext.getFunctionAddress("glColorP4ui")) != 0L) & (forwardCompatible || (this.glColorP3uiv = GLContext.getFunctionAddress("glColorP3uiv")) != 0L) & (forwardCompatible || (this.glColorP4uiv = GLContext.getFunctionAddress("glColorP4uiv")) != 0L) & (forwardCompatible || (this.glSecondaryColorP3ui = GLContext.getFunctionAddress("glSecondaryColorP3ui")) != 0L) & (forwardCompatible || (this.glSecondaryColorP3uiv = GLContext.getFunctionAddress("glSecondaryColorP3uiv")) != 0L) & (forwardCompatible || (this.glVertexAttribP1ui = GLContext.getFunctionAddress("glVertexAttribP1ui")) != 0L) & (forwardCompatible || (this.glVertexAttribP2ui = GLContext.getFunctionAddress("glVertexAttribP2ui")) != 0L) & (forwardCompatible || (this.glVertexAttribP3ui = GLContext.getFunctionAddress("glVertexAttribP3ui")) != 0L) & (forwardCompatible || (this.glVertexAttribP4ui = GLContext.getFunctionAddress("glVertexAttribP4ui")) != 0L) & (forwardCompatible || (this.glVertexAttribP1uiv = GLContext.getFunctionAddress("glVertexAttribP1uiv")) != 0L) & (forwardCompatible || (this.glVertexAttribP2uiv = GLContext.getFunctionAddress("glVertexAttribP2uiv")) != 0L) & (forwardCompatible || (this.glVertexAttribP3uiv = GLContext.getFunctionAddress("glVertexAttribP3uiv")) != 0L) & (forwardCompatible || (this.glVertexAttribP4uiv = GLContext.getFunctionAddress("glVertexAttribP4uiv")) != 0L);
    }

    private boolean GL40_initNativeFunctionAddresses() {
        this.glBlendEquationi = GLContext.getFunctionAddress("glBlendEquationi");
        boolean bl = this.glBlendEquationi != 0L;
        this.glBlendEquationSeparatei = GLContext.getFunctionAddress("glBlendEquationSeparatei");
        this.glBlendFunci = GLContext.getFunctionAddress("glBlendFunci");
        this.glBlendFuncSeparatei = GLContext.getFunctionAddress("glBlendFuncSeparatei");
        this.glDrawArraysIndirect = GLContext.getFunctionAddress("glDrawArraysIndirect");
        this.glDrawElementsIndirect = GLContext.getFunctionAddress("glDrawElementsIndirect");
        this.glUniform1d = GLContext.getFunctionAddress("glUniform1d");
        this.glUniform2d = GLContext.getFunctionAddress("glUniform2d");
        this.glUniform3d = GLContext.getFunctionAddress("glUniform3d");
        this.glUniform4d = GLContext.getFunctionAddress("glUniform4d");
        this.glUniform1dv = GLContext.getFunctionAddress("glUniform1dv");
        this.glUniform2dv = GLContext.getFunctionAddress("glUniform2dv");
        this.glUniform3dv = GLContext.getFunctionAddress("glUniform3dv");
        this.glUniform4dv = GLContext.getFunctionAddress("glUniform4dv");
        this.glUniformMatrix2dv = GLContext.getFunctionAddress("glUniformMatrix2dv");
        this.glUniformMatrix3dv = GLContext.getFunctionAddress("glUniformMatrix3dv");
        this.glUniformMatrix4dv = GLContext.getFunctionAddress("glUniformMatrix4dv");
        this.glUniformMatrix2x3dv = GLContext.getFunctionAddress("glUniformMatrix2x3dv");
        this.glUniformMatrix2x4dv = GLContext.getFunctionAddress("glUniformMatrix2x4dv");
        this.glUniformMatrix3x2dv = GLContext.getFunctionAddress("glUniformMatrix3x2dv");
        this.glUniformMatrix3x4dv = GLContext.getFunctionAddress("glUniformMatrix3x4dv");
        this.glUniformMatrix4x2dv = GLContext.getFunctionAddress("glUniformMatrix4x2dv");
        this.glUniformMatrix4x3dv = GLContext.getFunctionAddress("glUniformMatrix4x3dv");
        this.glGetUniformdv = GLContext.getFunctionAddress("glGetUniformdv");
        this.glMinSampleShading = GLContext.getFunctionAddress("glMinSampleShading");
        this.glGetSubroutineUniformLocation = GLContext.getFunctionAddress("glGetSubroutineUniformLocation");
        this.glGetSubroutineIndex = GLContext.getFunctionAddress("glGetSubroutineIndex");
        this.glGetActiveSubroutineUniformiv = GLContext.getFunctionAddress("glGetActiveSubroutineUniformiv");
        this.glGetActiveSubroutineUniformName = GLContext.getFunctionAddress("glGetActiveSubroutineUniformName");
        this.glGetActiveSubroutineName = GLContext.getFunctionAddress("glGetActiveSubroutineName");
        this.glUniformSubroutinesuiv = GLContext.getFunctionAddress("glUniformSubroutinesuiv");
        this.glGetUniformSubroutineuiv = GLContext.getFunctionAddress("glGetUniformSubroutineuiv");
        this.glGetProgramStageiv = GLContext.getFunctionAddress("glGetProgramStageiv");
        this.glPatchParameteri = GLContext.getFunctionAddress("glPatchParameteri");
        this.glPatchParameterfv = GLContext.getFunctionAddress("glPatchParameterfv");
        this.glBindTransformFeedback = GLContext.getFunctionAddress("glBindTransformFeedback");
        this.glDeleteTransformFeedbacks = GLContext.getFunctionAddress("glDeleteTransformFeedbacks");
        this.glGenTransformFeedbacks = GLContext.getFunctionAddress("glGenTransformFeedbacks");
        this.glIsTransformFeedback = GLContext.getFunctionAddress("glIsTransformFeedback");
        this.glPauseTransformFeedback = GLContext.getFunctionAddress("glPauseTransformFeedback");
        this.glResumeTransformFeedback = GLContext.getFunctionAddress("glResumeTransformFeedback");
        this.glDrawTransformFeedback = GLContext.getFunctionAddress("glDrawTransformFeedback");
        this.glDrawTransformFeedbackStream = GLContext.getFunctionAddress("glDrawTransformFeedbackStream");
        this.glBeginQueryIndexed = GLContext.getFunctionAddress("glBeginQueryIndexed");
        this.glEndQueryIndexed = GLContext.getFunctionAddress("glEndQueryIndexed");
        this.glGetQueryIndexediv = GLContext.getFunctionAddress("glGetQueryIndexediv");
        return bl & this.glBlendEquationSeparatei != 0L & this.glBlendFunci != 0L & this.glBlendFuncSeparatei != 0L & this.glDrawArraysIndirect != 0L & this.glDrawElementsIndirect != 0L & this.glUniform1d != 0L & this.glUniform2d != 0L & this.glUniform3d != 0L & this.glUniform4d != 0L & this.glUniform1dv != 0L & this.glUniform2dv != 0L & this.glUniform3dv != 0L & this.glUniform4dv != 0L & this.glUniformMatrix2dv != 0L & this.glUniformMatrix3dv != 0L & this.glUniformMatrix4dv != 0L & this.glUniformMatrix2x3dv != 0L & this.glUniformMatrix2x4dv != 0L & this.glUniformMatrix3x2dv != 0L & this.glUniformMatrix3x4dv != 0L & this.glUniformMatrix4x2dv != 0L & this.glUniformMatrix4x3dv != 0L & this.glGetUniformdv != 0L & this.glMinSampleShading != 0L & this.glGetSubroutineUniformLocation != 0L & this.glGetSubroutineIndex != 0L & this.glGetActiveSubroutineUniformiv != 0L & this.glGetActiveSubroutineUniformName != 0L & this.glGetActiveSubroutineName != 0L & this.glUniformSubroutinesuiv != 0L & this.glGetUniformSubroutineuiv != 0L & this.glGetProgramStageiv != 0L & this.glPatchParameteri != 0L & this.glPatchParameterfv != 0L & this.glBindTransformFeedback != 0L & this.glDeleteTransformFeedbacks != 0L & this.glGenTransformFeedbacks != 0L & this.glIsTransformFeedback != 0L & this.glPauseTransformFeedback != 0L & this.glResumeTransformFeedback != 0L & this.glDrawTransformFeedback != 0L & this.glDrawTransformFeedbackStream != 0L & this.glBeginQueryIndexed != 0L & this.glEndQueryIndexed != 0L & this.glGetQueryIndexediv != 0L;
    }

    private boolean GL41_initNativeFunctionAddresses() {
        this.glReleaseShaderCompiler = GLContext.getFunctionAddress("glReleaseShaderCompiler");
        boolean bl = this.glReleaseShaderCompiler != 0L;
        this.glShaderBinary = GLContext.getFunctionAddress("glShaderBinary");
        this.glGetShaderPrecisionFormat = GLContext.getFunctionAddress("glGetShaderPrecisionFormat");
        this.glDepthRangef = GLContext.getFunctionAddress("glDepthRangef");
        this.glClearDepthf = GLContext.getFunctionAddress("glClearDepthf");
        this.glGetProgramBinary = GLContext.getFunctionAddress("glGetProgramBinary");
        this.glProgramBinary = GLContext.getFunctionAddress("glProgramBinary");
        this.glProgramParameteri = GLContext.getFunctionAddress("glProgramParameteri");
        this.glUseProgramStages = GLContext.getFunctionAddress("glUseProgramStages");
        this.glActiveShaderProgram = GLContext.getFunctionAddress("glActiveShaderProgram");
        this.glCreateShaderProgramv = GLContext.getFunctionAddress("glCreateShaderProgramv");
        this.glBindProgramPipeline = GLContext.getFunctionAddress("glBindProgramPipeline");
        this.glDeleteProgramPipelines = GLContext.getFunctionAddress("glDeleteProgramPipelines");
        this.glGenProgramPipelines = GLContext.getFunctionAddress("glGenProgramPipelines");
        this.glIsProgramPipeline = GLContext.getFunctionAddress("glIsProgramPipeline");
        this.glGetProgramPipelineiv = GLContext.getFunctionAddress("glGetProgramPipelineiv");
        this.glProgramUniform1i = GLContext.getFunctionAddress("glProgramUniform1i");
        this.glProgramUniform2i = GLContext.getFunctionAddress("glProgramUniform2i");
        this.glProgramUniform3i = GLContext.getFunctionAddress("glProgramUniform3i");
        this.glProgramUniform4i = GLContext.getFunctionAddress("glProgramUniform4i");
        this.glProgramUniform1f = GLContext.getFunctionAddress("glProgramUniform1f");
        this.glProgramUniform2f = GLContext.getFunctionAddress("glProgramUniform2f");
        this.glProgramUniform3f = GLContext.getFunctionAddress("glProgramUniform3f");
        this.glProgramUniform4f = GLContext.getFunctionAddress("glProgramUniform4f");
        this.glProgramUniform1d = GLContext.getFunctionAddress("glProgramUniform1d");
        this.glProgramUniform2d = GLContext.getFunctionAddress("glProgramUniform2d");
        this.glProgramUniform3d = GLContext.getFunctionAddress("glProgramUniform3d");
        this.glProgramUniform4d = GLContext.getFunctionAddress("glProgramUniform4d");
        this.glProgramUniform1iv = GLContext.getFunctionAddress("glProgramUniform1iv");
        this.glProgramUniform2iv = GLContext.getFunctionAddress("glProgramUniform2iv");
        this.glProgramUniform3iv = GLContext.getFunctionAddress("glProgramUniform3iv");
        this.glProgramUniform4iv = GLContext.getFunctionAddress("glProgramUniform4iv");
        this.glProgramUniform1fv = GLContext.getFunctionAddress("glProgramUniform1fv");
        this.glProgramUniform2fv = GLContext.getFunctionAddress("glProgramUniform2fv");
        this.glProgramUniform3fv = GLContext.getFunctionAddress("glProgramUniform3fv");
        this.glProgramUniform4fv = GLContext.getFunctionAddress("glProgramUniform4fv");
        this.glProgramUniform1dv = GLContext.getFunctionAddress("glProgramUniform1dv");
        this.glProgramUniform2dv = GLContext.getFunctionAddress("glProgramUniform2dv");
        this.glProgramUniform3dv = GLContext.getFunctionAddress("glProgramUniform3dv");
        this.glProgramUniform4dv = GLContext.getFunctionAddress("glProgramUniform4dv");
        this.glProgramUniform1ui = GLContext.getFunctionAddress("glProgramUniform1ui");
        this.glProgramUniform2ui = GLContext.getFunctionAddress("glProgramUniform2ui");
        this.glProgramUniform3ui = GLContext.getFunctionAddress("glProgramUniform3ui");
        this.glProgramUniform4ui = GLContext.getFunctionAddress("glProgramUniform4ui");
        this.glProgramUniform1uiv = GLContext.getFunctionAddress("glProgramUniform1uiv");
        this.glProgramUniform2uiv = GLContext.getFunctionAddress("glProgramUniform2uiv");
        this.glProgramUniform3uiv = GLContext.getFunctionAddress("glProgramUniform3uiv");
        this.glProgramUniform4uiv = GLContext.getFunctionAddress("glProgramUniform4uiv");
        this.glProgramUniformMatrix2fv = GLContext.getFunctionAddress("glProgramUniformMatrix2fv");
        this.glProgramUniformMatrix3fv = GLContext.getFunctionAddress("glProgramUniformMatrix3fv");
        this.glProgramUniformMatrix4fv = GLContext.getFunctionAddress("glProgramUniformMatrix4fv");
        this.glProgramUniformMatrix2dv = GLContext.getFunctionAddress("glProgramUniformMatrix2dv");
        this.glProgramUniformMatrix3dv = GLContext.getFunctionAddress("glProgramUniformMatrix3dv");
        this.glProgramUniformMatrix4dv = GLContext.getFunctionAddress("glProgramUniformMatrix4dv");
        this.glProgramUniformMatrix2x3fv = GLContext.getFunctionAddress("glProgramUniformMatrix2x3fv");
        this.glProgramUniformMatrix3x2fv = GLContext.getFunctionAddress("glProgramUniformMatrix3x2fv");
        this.glProgramUniformMatrix2x4fv = GLContext.getFunctionAddress("glProgramUniformMatrix2x4fv");
        this.glProgramUniformMatrix4x2fv = GLContext.getFunctionAddress("glProgramUniformMatrix4x2fv");
        this.glProgramUniformMatrix3x4fv = GLContext.getFunctionAddress("glProgramUniformMatrix3x4fv");
        this.glProgramUniformMatrix4x3fv = GLContext.getFunctionAddress("glProgramUniformMatrix4x3fv");
        this.glProgramUniformMatrix2x3dv = GLContext.getFunctionAddress("glProgramUniformMatrix2x3dv");
        this.glProgramUniformMatrix3x2dv = GLContext.getFunctionAddress("glProgramUniformMatrix3x2dv");
        this.glProgramUniformMatrix2x4dv = GLContext.getFunctionAddress("glProgramUniformMatrix2x4dv");
        this.glProgramUniformMatrix4x2dv = GLContext.getFunctionAddress("glProgramUniformMatrix4x2dv");
        this.glProgramUniformMatrix3x4dv = GLContext.getFunctionAddress("glProgramUniformMatrix3x4dv");
        this.glProgramUniformMatrix4x3dv = GLContext.getFunctionAddress("glProgramUniformMatrix4x3dv");
        this.glValidateProgramPipeline = GLContext.getFunctionAddress("glValidateProgramPipeline");
        this.glGetProgramPipelineInfoLog = GLContext.getFunctionAddress("glGetProgramPipelineInfoLog");
        this.glVertexAttribL1d = GLContext.getFunctionAddress("glVertexAttribL1d");
        this.glVertexAttribL2d = GLContext.getFunctionAddress("glVertexAttribL2d");
        this.glVertexAttribL3d = GLContext.getFunctionAddress("glVertexAttribL3d");
        this.glVertexAttribL4d = GLContext.getFunctionAddress("glVertexAttribL4d");
        this.glVertexAttribL1dv = GLContext.getFunctionAddress("glVertexAttribL1dv");
        this.glVertexAttribL2dv = GLContext.getFunctionAddress("glVertexAttribL2dv");
        this.glVertexAttribL3dv = GLContext.getFunctionAddress("glVertexAttribL3dv");
        this.glVertexAttribL4dv = GLContext.getFunctionAddress("glVertexAttribL4dv");
        this.glVertexAttribLPointer = GLContext.getFunctionAddress("glVertexAttribLPointer");
        this.glGetVertexAttribLdv = GLContext.getFunctionAddress("glGetVertexAttribLdv");
        this.glViewportArrayv = GLContext.getFunctionAddress("glViewportArrayv");
        this.glViewportIndexedf = GLContext.getFunctionAddress("glViewportIndexedf");
        this.glViewportIndexedfv = GLContext.getFunctionAddress("glViewportIndexedfv");
        this.glScissorArrayv = GLContext.getFunctionAddress("glScissorArrayv");
        this.glScissorIndexed = GLContext.getFunctionAddress("glScissorIndexed");
        this.glScissorIndexedv = GLContext.getFunctionAddress("glScissorIndexedv");
        this.glDepthRangeArrayv = GLContext.getFunctionAddress("glDepthRangeArrayv");
        this.glDepthRangeIndexed = GLContext.getFunctionAddress("glDepthRangeIndexed");
        this.glGetFloati_v = GLContext.getFunctionAddress("glGetFloati_v");
        this.glGetDoublei_v = GLContext.getFunctionAddress("glGetDoublei_v");
        return bl & this.glShaderBinary != 0L & this.glGetShaderPrecisionFormat != 0L & this.glDepthRangef != 0L & this.glClearDepthf != 0L & this.glGetProgramBinary != 0L & this.glProgramBinary != 0L & this.glProgramParameteri != 0L & this.glUseProgramStages != 0L & this.glActiveShaderProgram != 0L & this.glCreateShaderProgramv != 0L & this.glBindProgramPipeline != 0L & this.glDeleteProgramPipelines != 0L & this.glGenProgramPipelines != 0L & this.glIsProgramPipeline != 0L & this.glGetProgramPipelineiv != 0L & this.glProgramUniform1i != 0L & this.glProgramUniform2i != 0L & this.glProgramUniform3i != 0L & this.glProgramUniform4i != 0L & this.glProgramUniform1f != 0L & this.glProgramUniform2f != 0L & this.glProgramUniform3f != 0L & this.glProgramUniform4f != 0L & this.glProgramUniform1d != 0L & this.glProgramUniform2d != 0L & this.glProgramUniform3d != 0L & this.glProgramUniform4d != 0L & this.glProgramUniform1iv != 0L & this.glProgramUniform2iv != 0L & this.glProgramUniform3iv != 0L & this.glProgramUniform4iv != 0L & this.glProgramUniform1fv != 0L & this.glProgramUniform2fv != 0L & this.glProgramUniform3fv != 0L & this.glProgramUniform4fv != 0L & this.glProgramUniform1dv != 0L & this.glProgramUniform2dv != 0L & this.glProgramUniform3dv != 0L & this.glProgramUniform4dv != 0L & this.glProgramUniform1ui != 0L & this.glProgramUniform2ui != 0L & this.glProgramUniform3ui != 0L & this.glProgramUniform4ui != 0L & this.glProgramUniform1uiv != 0L & this.glProgramUniform2uiv != 0L & this.glProgramUniform3uiv != 0L & this.glProgramUniform4uiv != 0L & this.glProgramUniformMatrix2fv != 0L & this.glProgramUniformMatrix3fv != 0L & this.glProgramUniformMatrix4fv != 0L & this.glProgramUniformMatrix2dv != 0L & this.glProgramUniformMatrix3dv != 0L & this.glProgramUniformMatrix4dv != 0L & this.glProgramUniformMatrix2x3fv != 0L & this.glProgramUniformMatrix3x2fv != 0L & this.glProgramUniformMatrix2x4fv != 0L & this.glProgramUniformMatrix4x2fv != 0L & this.glProgramUniformMatrix3x4fv != 0L & this.glProgramUniformMatrix4x3fv != 0L & this.glProgramUniformMatrix2x3dv != 0L & this.glProgramUniformMatrix3x2dv != 0L & this.glProgramUniformMatrix2x4dv != 0L & this.glProgramUniformMatrix4x2dv != 0L & this.glProgramUniformMatrix3x4dv != 0L & this.glProgramUniformMatrix4x3dv != 0L & this.glValidateProgramPipeline != 0L & this.glGetProgramPipelineInfoLog != 0L & this.glVertexAttribL1d != 0L & this.glVertexAttribL2d != 0L & this.glVertexAttribL3d != 0L & this.glVertexAttribL4d != 0L & this.glVertexAttribL1dv != 0L & this.glVertexAttribL2dv != 0L & this.glVertexAttribL3dv != 0L & this.glVertexAttribL4dv != 0L & this.glVertexAttribLPointer != 0L & this.glGetVertexAttribLdv != 0L & this.glViewportArrayv != 0L & this.glViewportIndexedf != 0L & this.glViewportIndexedfv != 0L & this.glScissorArrayv != 0L & this.glScissorIndexed != 0L & this.glScissorIndexedv != 0L & this.glDepthRangeArrayv != 0L & this.glDepthRangeIndexed != 0L & this.glGetFloati_v != 0L & this.glGetDoublei_v != 0L;
    }

    private boolean GL42_initNativeFunctionAddresses() {
        this.glGetActiveAtomicCounterBufferiv = GLContext.getFunctionAddress("glGetActiveAtomicCounterBufferiv");
        boolean bl = this.glGetActiveAtomicCounterBufferiv != 0L;
        this.glTexStorage1D = GLContext.getFunctionAddress("glTexStorage1D");
        this.glTexStorage2D = GLContext.getFunctionAddress("glTexStorage2D");
        this.glTexStorage3D = GLContext.getFunctionAddress("glTexStorage3D");
        this.glDrawTransformFeedbackInstanced = GLContext.getFunctionAddress("glDrawTransformFeedbackInstanced");
        this.glDrawTransformFeedbackStreamInstanced = GLContext.getFunctionAddress("glDrawTransformFeedbackStreamInstanced");
        this.glDrawArraysInstancedBaseInstance = GLContext.getFunctionAddress("glDrawArraysInstancedBaseInstance");
        this.glDrawElementsInstancedBaseInstance = GLContext.getFunctionAddress("glDrawElementsInstancedBaseInstance");
        this.glDrawElementsInstancedBaseVertexBaseInstance = GLContext.getFunctionAddress("glDrawElementsInstancedBaseVertexBaseInstance");
        this.glBindImageTexture = GLContext.getFunctionAddress("glBindImageTexture");
        this.glMemoryBarrier = GLContext.getFunctionAddress("glMemoryBarrier");
        this.glGetInternalformativ = GLContext.getFunctionAddress("glGetInternalformativ");
        return bl & this.glTexStorage1D != 0L & this.glTexStorage2D != 0L & this.glTexStorage3D != 0L & this.glDrawTransformFeedbackInstanced != 0L & this.glDrawTransformFeedbackStreamInstanced != 0L & this.glDrawArraysInstancedBaseInstance != 0L & this.glDrawElementsInstancedBaseInstance != 0L & this.glDrawElementsInstancedBaseVertexBaseInstance != 0L & this.glBindImageTexture != 0L & this.glMemoryBarrier != 0L & this.glGetInternalformativ != 0L;
    }

    private boolean GL43_initNativeFunctionAddresses() {
        this.glClearBufferData = GLContext.getFunctionAddress("glClearBufferData");
        boolean bl = this.glClearBufferData != 0L;
        this.glClearBufferSubData = GLContext.getFunctionAddress("glClearBufferSubData");
        this.glDispatchCompute = GLContext.getFunctionAddress("glDispatchCompute");
        this.glDispatchComputeIndirect = GLContext.getFunctionAddress("glDispatchComputeIndirect");
        this.glCopyImageSubData = GLContext.getFunctionAddress("glCopyImageSubData");
        this.glDebugMessageControl = GLContext.getFunctionAddress("glDebugMessageControl");
        this.glDebugMessageInsert = GLContext.getFunctionAddress("glDebugMessageInsert");
        this.glDebugMessageCallback = GLContext.getFunctionAddress("glDebugMessageCallback");
        this.glGetDebugMessageLog = GLContext.getFunctionAddress("glGetDebugMessageLog");
        this.glPushDebugGroup = GLContext.getFunctionAddress("glPushDebugGroup");
        this.glPopDebugGroup = GLContext.getFunctionAddress("glPopDebugGroup");
        this.glObjectLabel = GLContext.getFunctionAddress("glObjectLabel");
        this.glGetObjectLabel = GLContext.getFunctionAddress("glGetObjectLabel");
        this.glObjectPtrLabel = GLContext.getFunctionAddress("glObjectPtrLabel");
        this.glGetObjectPtrLabel = GLContext.getFunctionAddress("glGetObjectPtrLabel");
        this.glFramebufferParameteri = GLContext.getFunctionAddress("glFramebufferParameteri");
        this.glGetFramebufferParameteriv = GLContext.getFunctionAddress("glGetFramebufferParameteriv");
        this.glGetInternalformati64v = GLContext.getFunctionAddress("glGetInternalformati64v");
        this.glInvalidateTexSubImage = GLContext.getFunctionAddress("glInvalidateTexSubImage");
        this.glInvalidateTexImage = GLContext.getFunctionAddress("glInvalidateTexImage");
        this.glInvalidateBufferSubData = GLContext.getFunctionAddress("glInvalidateBufferSubData");
        this.glInvalidateBufferData = GLContext.getFunctionAddress("glInvalidateBufferData");
        this.glInvalidateFramebuffer = GLContext.getFunctionAddress("glInvalidateFramebuffer");
        this.glInvalidateSubFramebuffer = GLContext.getFunctionAddress("glInvalidateSubFramebuffer");
        this.glMultiDrawArraysIndirect = GLContext.getFunctionAddress("glMultiDrawArraysIndirect");
        this.glMultiDrawElementsIndirect = GLContext.getFunctionAddress("glMultiDrawElementsIndirect");
        this.glGetProgramInterfaceiv = GLContext.getFunctionAddress("glGetProgramInterfaceiv");
        this.glGetProgramResourceIndex = GLContext.getFunctionAddress("glGetProgramResourceIndex");
        this.glGetProgramResourceName = GLContext.getFunctionAddress("glGetProgramResourceName");
        this.glGetProgramResourceiv = GLContext.getFunctionAddress("glGetProgramResourceiv");
        this.glGetProgramResourceLocation = GLContext.getFunctionAddress("glGetProgramResourceLocation");
        this.glGetProgramResourceLocationIndex = GLContext.getFunctionAddress("glGetProgramResourceLocationIndex");
        this.glShaderStorageBlockBinding = GLContext.getFunctionAddress("glShaderStorageBlockBinding");
        this.glTexBufferRange = GLContext.getFunctionAddress("glTexBufferRange");
        this.glTexStorage2DMultisample = GLContext.getFunctionAddress("glTexStorage2DMultisample");
        this.glTexStorage3DMultisample = GLContext.getFunctionAddress("glTexStorage3DMultisample");
        this.glTextureView = GLContext.getFunctionAddress("glTextureView");
        this.glBindVertexBuffer = GLContext.getFunctionAddress("glBindVertexBuffer");
        this.glVertexAttribFormat = GLContext.getFunctionAddress("glVertexAttribFormat");
        this.glVertexAttribIFormat = GLContext.getFunctionAddress("glVertexAttribIFormat");
        this.glVertexAttribLFormat = GLContext.getFunctionAddress("glVertexAttribLFormat");
        this.glVertexAttribBinding = GLContext.getFunctionAddress("glVertexAttribBinding");
        this.glVertexBindingDivisor = GLContext.getFunctionAddress("glVertexBindingDivisor");
        return bl & this.glClearBufferSubData != 0L & this.glDispatchCompute != 0L & this.glDispatchComputeIndirect != 0L & this.glCopyImageSubData != 0L & this.glDebugMessageControl != 0L & this.glDebugMessageInsert != 0L & this.glDebugMessageCallback != 0L & this.glGetDebugMessageLog != 0L & this.glPushDebugGroup != 0L & this.glPopDebugGroup != 0L & this.glObjectLabel != 0L & this.glGetObjectLabel != 0L & this.glObjectPtrLabel != 0L & this.glGetObjectPtrLabel != 0L & this.glFramebufferParameteri != 0L & this.glGetFramebufferParameteriv != 0L & this.glGetInternalformati64v != 0L & this.glInvalidateTexSubImage != 0L & this.glInvalidateTexImage != 0L & this.glInvalidateBufferSubData != 0L & this.glInvalidateBufferData != 0L & this.glInvalidateFramebuffer != 0L & this.glInvalidateSubFramebuffer != 0L & this.glMultiDrawArraysIndirect != 0L & this.glMultiDrawElementsIndirect != 0L & this.glGetProgramInterfaceiv != 0L & this.glGetProgramResourceIndex != 0L & this.glGetProgramResourceName != 0L & this.glGetProgramResourceiv != 0L & this.glGetProgramResourceLocation != 0L & this.glGetProgramResourceLocationIndex != 0L & this.glShaderStorageBlockBinding != 0L & this.glTexBufferRange != 0L & this.glTexStorage2DMultisample != 0L & this.glTexStorage3DMultisample != 0L & this.glTextureView != 0L & this.glBindVertexBuffer != 0L & this.glVertexAttribFormat != 0L & this.glVertexAttribIFormat != 0L & this.glVertexAttribLFormat != 0L & this.glVertexAttribBinding != 0L & this.glVertexBindingDivisor != 0L;
    }

    private boolean GL44_initNativeFunctionAddresses() {
        this.glBufferStorage = GLContext.getFunctionAddress("glBufferStorage");
        boolean bl = this.glBufferStorage != 0L;
        this.glClearTexImage = GLContext.getFunctionAddress("glClearTexImage");
        this.glClearTexSubImage = GLContext.getFunctionAddress("glClearTexSubImage");
        this.glBindBuffersBase = GLContext.getFunctionAddress("glBindBuffersBase");
        this.glBindBuffersRange = GLContext.getFunctionAddress("glBindBuffersRange");
        this.glBindTextures = GLContext.getFunctionAddress("glBindTextures");
        this.glBindSamplers = GLContext.getFunctionAddress("glBindSamplers");
        this.glBindImageTextures = GLContext.getFunctionAddress("glBindImageTextures");
        this.glBindVertexBuffers = GLContext.getFunctionAddress("glBindVertexBuffers");
        return bl & this.glClearTexImage != 0L & this.glClearTexSubImage != 0L & this.glBindBuffersBase != 0L & this.glBindBuffersRange != 0L & this.glBindTextures != 0L & this.glBindSamplers != 0L & this.glBindImageTextures != 0L & this.glBindVertexBuffers != 0L;
    }

    private boolean GL45_initNativeFunctionAddresses() {
        this.glClipControl = GLContext.getFunctionAddress("glClipControl");
        boolean bl = this.glClipControl != 0L;
        this.glCreateTransformFeedbacks = GLContext.getFunctionAddress("glCreateTransformFeedbacks");
        this.glTransformFeedbackBufferBase = GLContext.getFunctionAddress("glTransformFeedbackBufferBase");
        this.glTransformFeedbackBufferRange = GLContext.getFunctionAddress("glTransformFeedbackBufferRange");
        this.glGetTransformFeedbackiv = GLContext.getFunctionAddress("glGetTransformFeedbackiv");
        this.glGetTransformFeedbacki_v = GLContext.getFunctionAddress("glGetTransformFeedbacki_v");
        this.glGetTransformFeedbacki64_v = GLContext.getFunctionAddress("glGetTransformFeedbacki64_v");
        this.glCreateBuffers = GLContext.getFunctionAddress("glCreateBuffers");
        this.glNamedBufferStorage = GLContext.getFunctionAddress("glNamedBufferStorage");
        this.glNamedBufferData = GLContext.getFunctionAddress("glNamedBufferData");
        this.glNamedBufferSubData = GLContext.getFunctionAddress("glNamedBufferSubData");
        this.glCopyNamedBufferSubData = GLContext.getFunctionAddress("glCopyNamedBufferSubData");
        this.glClearNamedBufferData = GLContext.getFunctionAddress("glClearNamedBufferData");
        this.glClearNamedBufferSubData = GLContext.getFunctionAddress("glClearNamedBufferSubData");
        this.glMapNamedBuffer = GLContext.getFunctionAddress("glMapNamedBuffer");
        this.glMapNamedBufferRange = GLContext.getFunctionAddress("glMapNamedBufferRange");
        this.glUnmapNamedBuffer = GLContext.getFunctionAddress("glUnmapNamedBuffer");
        this.glFlushMappedNamedBufferRange = GLContext.getFunctionAddress("glFlushMappedNamedBufferRange");
        this.glGetNamedBufferParameteriv = GLContext.getFunctionAddress("glGetNamedBufferParameteriv");
        this.glGetNamedBufferParameteri64v = GLContext.getFunctionAddress("glGetNamedBufferParameteri64v");
        this.glGetNamedBufferPointerv = GLContext.getFunctionAddress("glGetNamedBufferPointerv");
        this.glGetNamedBufferSubData = GLContext.getFunctionAddress("glGetNamedBufferSubData");
        this.glCreateFramebuffers = GLContext.getFunctionAddress("glCreateFramebuffers");
        this.glNamedFramebufferRenderbuffer = GLContext.getFunctionAddress("glNamedFramebufferRenderbuffer");
        this.glNamedFramebufferParameteri = GLContext.getFunctionAddress("glNamedFramebufferParameteri");
        this.glNamedFramebufferTexture = GLContext.getFunctionAddress("glNamedFramebufferTexture");
        this.glNamedFramebufferTextureLayer = GLContext.getFunctionAddress("glNamedFramebufferTextureLayer");
        this.glNamedFramebufferDrawBuffer = GLContext.getFunctionAddress("glNamedFramebufferDrawBuffer");
        this.glNamedFramebufferDrawBuffers = GLContext.getFunctionAddress("glNamedFramebufferDrawBuffers");
        this.glNamedFramebufferReadBuffer = GLContext.getFunctionAddress("glNamedFramebufferReadBuffer");
        this.glInvalidateNamedFramebufferData = GLContext.getFunctionAddress("glInvalidateNamedFramebufferData");
        this.glInvalidateNamedFramebufferSubData = GLContext.getFunctionAddress("glInvalidateNamedFramebufferSubData");
        this.glClearNamedFramebufferiv = GLContext.getFunctionAddress("glClearNamedFramebufferiv");
        this.glClearNamedFramebufferuiv = GLContext.getFunctionAddress("glClearNamedFramebufferuiv");
        this.glClearNamedFramebufferfv = GLContext.getFunctionAddress("glClearNamedFramebufferfv");
        this.glClearNamedFramebufferfi = GLContext.getFunctionAddress("glClearNamedFramebufferfi");
        this.glBlitNamedFramebuffer = GLContext.getFunctionAddress("glBlitNamedFramebuffer");
        this.glCheckNamedFramebufferStatus = GLContext.getFunctionAddress("glCheckNamedFramebufferStatus");
        this.glGetNamedFramebufferParameteriv = GLContext.getFunctionAddress("glGetNamedFramebufferParameteriv");
        this.glGetNamedFramebufferAttachmentParameteriv = GLContext.getFunctionAddress("glGetNamedFramebufferAttachmentParameteriv");
        this.glCreateRenderbuffers = GLContext.getFunctionAddress("glCreateRenderbuffers");
        this.glNamedRenderbufferStorage = GLContext.getFunctionAddress("glNamedRenderbufferStorage");
        this.glNamedRenderbufferStorageMultisample = GLContext.getFunctionAddress("glNamedRenderbufferStorageMultisample");
        this.glGetNamedRenderbufferParameteriv = GLContext.getFunctionAddress("glGetNamedRenderbufferParameteriv");
        this.glCreateTextures = GLContext.getFunctionAddress("glCreateTextures");
        this.glTextureBuffer = GLContext.getFunctionAddress("glTextureBuffer");
        this.glTextureBufferRange = GLContext.getFunctionAddress("glTextureBufferRange");
        this.glTextureStorage1D = GLContext.getFunctionAddress("glTextureStorage1D");
        this.glTextureStorage2D = GLContext.getFunctionAddress("glTextureStorage2D");
        this.glTextureStorage3D = GLContext.getFunctionAddress("glTextureStorage3D");
        this.glTextureStorage2DMultisample = GLContext.getFunctionAddress("glTextureStorage2DMultisample");
        this.glTextureStorage3DMultisample = GLContext.getFunctionAddress("glTextureStorage3DMultisample");
        this.glTextureSubImage1D = GLContext.getFunctionAddress("glTextureSubImage1D");
        this.glTextureSubImage2D = GLContext.getFunctionAddress("glTextureSubImage2D");
        this.glTextureSubImage3D = GLContext.getFunctionAddress("glTextureSubImage3D");
        this.glCompressedTextureSubImage1D = GLContext.getFunctionAddress("glCompressedTextureSubImage1D");
        this.glCompressedTextureSubImage2D = GLContext.getFunctionAddress("glCompressedTextureSubImage2D");
        this.glCompressedTextureSubImage3D = GLContext.getFunctionAddress("glCompressedTextureSubImage3D");
        this.glCopyTextureSubImage1D = GLContext.getFunctionAddress("glCopyTextureSubImage1D");
        this.glCopyTextureSubImage2D = GLContext.getFunctionAddress("glCopyTextureSubImage2D");
        this.glCopyTextureSubImage3D = GLContext.getFunctionAddress("glCopyTextureSubImage3D");
        this.glTextureParameterf = GLContext.getFunctionAddress("glTextureParameterf");
        this.glTextureParameterfv = GLContext.getFunctionAddress("glTextureParameterfv");
        this.glTextureParameteri = GLContext.getFunctionAddress("glTextureParameteri");
        this.glTextureParameterIiv = GLContext.getFunctionAddress("glTextureParameterIiv");
        this.glTextureParameterIuiv = GLContext.getFunctionAddress("glTextureParameterIuiv");
        this.glTextureParameteriv = GLContext.getFunctionAddress("glTextureParameteriv");
        this.glGenerateTextureMipmap = GLContext.getFunctionAddress("glGenerateTextureMipmap");
        this.glBindTextureUnit = GLContext.getFunctionAddress("glBindTextureUnit");
        this.glGetTextureImage = GLContext.getFunctionAddress("glGetTextureImage");
        this.glGetCompressedTextureImage = GLContext.getFunctionAddress("glGetCompressedTextureImage");
        this.glGetTextureLevelParameterfv = GLContext.getFunctionAddress("glGetTextureLevelParameterfv");
        this.glGetTextureLevelParameteriv = GLContext.getFunctionAddress("glGetTextureLevelParameteriv");
        this.glGetTextureParameterfv = GLContext.getFunctionAddress("glGetTextureParameterfv");
        this.glGetTextureParameterIiv = GLContext.getFunctionAddress("glGetTextureParameterIiv");
        this.glGetTextureParameterIuiv = GLContext.getFunctionAddress("glGetTextureParameterIuiv");
        this.glGetTextureParameteriv = GLContext.getFunctionAddress("glGetTextureParameteriv");
        this.glCreateVertexArrays = GLContext.getFunctionAddress("glCreateVertexArrays");
        this.glDisableVertexArrayAttrib = GLContext.getFunctionAddress("glDisableVertexArrayAttrib");
        this.glEnableVertexArrayAttrib = GLContext.getFunctionAddress("glEnableVertexArrayAttrib");
        this.glVertexArrayElementBuffer = GLContext.getFunctionAddress("glVertexArrayElementBuffer");
        this.glVertexArrayVertexBuffer = GLContext.getFunctionAddress("glVertexArrayVertexBuffer");
        this.glVertexArrayVertexBuffers = GLContext.getFunctionAddress("glVertexArrayVertexBuffers");
        this.glVertexArrayAttribFormat = GLContext.getFunctionAddress("glVertexArrayAttribFormat");
        this.glVertexArrayAttribIFormat = GLContext.getFunctionAddress("glVertexArrayAttribIFormat");
        this.glVertexArrayAttribLFormat = GLContext.getFunctionAddress("glVertexArrayAttribLFormat");
        this.glVertexArrayAttribBinding = GLContext.getFunctionAddress("glVertexArrayAttribBinding");
        this.glVertexArrayBindingDivisor = GLContext.getFunctionAddress("glVertexArrayBindingDivisor");
        this.glGetVertexArrayiv = GLContext.getFunctionAddress("glGetVertexArrayiv");
        this.glGetVertexArrayIndexediv = GLContext.getFunctionAddress("glGetVertexArrayIndexediv");
        this.glGetVertexArrayIndexed64iv = GLContext.getFunctionAddress("glGetVertexArrayIndexed64iv");
        this.glCreateSamplers = GLContext.getFunctionAddress("glCreateSamplers");
        this.glCreateProgramPipelines = GLContext.getFunctionAddress("glCreateProgramPipelines");
        this.glCreateQueries = GLContext.getFunctionAddress("glCreateQueries");
        this.glMemoryBarrierByRegion = GLContext.getFunctionAddress("glMemoryBarrierByRegion");
        this.glGetTextureSubImage = GLContext.getFunctionAddress("glGetTextureSubImage");
        this.glGetCompressedTextureSubImage = GLContext.getFunctionAddress("glGetCompressedTextureSubImage");
        this.glTextureBarrier = GLContext.getFunctionAddress("glTextureBarrier");
        this.glGetGraphicsResetStatus = GLContext.getFunctionAddress("glGetGraphicsResetStatus");
        this.glReadnPixels = GLContext.getFunctionAddress("glReadnPixels");
        this.glGetnUniformfv = GLContext.getFunctionAddress("glGetnUniformfv");
        this.glGetnUniformiv = GLContext.getFunctionAddress("glGetnUniformiv");
        this.glGetnUniformuiv = GLContext.getFunctionAddress("glGetnUniformuiv");
        return bl & this.glCreateTransformFeedbacks != 0L & this.glTransformFeedbackBufferBase != 0L & this.glTransformFeedbackBufferRange != 0L & this.glGetTransformFeedbackiv != 0L & this.glGetTransformFeedbacki_v != 0L & this.glGetTransformFeedbacki64_v != 0L & this.glCreateBuffers != 0L & this.glNamedBufferStorage != 0L & this.glNamedBufferData != 0L & this.glNamedBufferSubData != 0L & this.glCopyNamedBufferSubData != 0L & this.glClearNamedBufferData != 0L & this.glClearNamedBufferSubData != 0L & this.glMapNamedBuffer != 0L & this.glMapNamedBufferRange != 0L & this.glUnmapNamedBuffer != 0L & this.glFlushMappedNamedBufferRange != 0L & this.glGetNamedBufferParameteriv != 0L & this.glGetNamedBufferParameteri64v != 0L & this.glGetNamedBufferPointerv != 0L & this.glGetNamedBufferSubData != 0L & this.glCreateFramebuffers != 0L & this.glNamedFramebufferRenderbuffer != 0L & this.glNamedFramebufferParameteri != 0L & this.glNamedFramebufferTexture != 0L & this.glNamedFramebufferTextureLayer != 0L & this.glNamedFramebufferDrawBuffer != 0L & this.glNamedFramebufferDrawBuffers != 0L & this.glNamedFramebufferReadBuffer != 0L & this.glInvalidateNamedFramebufferData != 0L & this.glInvalidateNamedFramebufferSubData != 0L & this.glClearNamedFramebufferiv != 0L & this.glClearNamedFramebufferuiv != 0L & this.glClearNamedFramebufferfv != 0L & this.glClearNamedFramebufferfi != 0L & this.glBlitNamedFramebuffer != 0L & this.glCheckNamedFramebufferStatus != 0L & this.glGetNamedFramebufferParameteriv != 0L & this.glGetNamedFramebufferAttachmentParameteriv != 0L & this.glCreateRenderbuffers != 0L & this.glNamedRenderbufferStorage != 0L & this.glNamedRenderbufferStorageMultisample != 0L & this.glGetNamedRenderbufferParameteriv != 0L & this.glCreateTextures != 0L & this.glTextureBuffer != 0L & this.glTextureBufferRange != 0L & this.glTextureStorage1D != 0L & this.glTextureStorage2D != 0L & this.glTextureStorage3D != 0L & this.glTextureStorage2DMultisample != 0L & this.glTextureStorage3DMultisample != 0L & this.glTextureSubImage1D != 0L & this.glTextureSubImage2D != 0L & this.glTextureSubImage3D != 0L & this.glCompressedTextureSubImage1D != 0L & this.glCompressedTextureSubImage2D != 0L & this.glCompressedTextureSubImage3D != 0L & this.glCopyTextureSubImage1D != 0L & this.glCopyTextureSubImage2D != 0L & this.glCopyTextureSubImage3D != 0L & this.glTextureParameterf != 0L & this.glTextureParameterfv != 0L & this.glTextureParameteri != 0L & this.glTextureParameterIiv != 0L & this.glTextureParameterIuiv != 0L & this.glTextureParameteriv != 0L & this.glGenerateTextureMipmap != 0L & this.glBindTextureUnit != 0L & this.glGetTextureImage != 0L & this.glGetCompressedTextureImage != 0L & this.glGetTextureLevelParameterfv != 0L & this.glGetTextureLevelParameteriv != 0L & this.glGetTextureParameterfv != 0L & this.glGetTextureParameterIiv != 0L & this.glGetTextureParameterIuiv != 0L & this.glGetTextureParameteriv != 0L & this.glCreateVertexArrays != 0L & this.glDisableVertexArrayAttrib != 0L & this.glEnableVertexArrayAttrib != 0L & this.glVertexArrayElementBuffer != 0L & this.glVertexArrayVertexBuffer != 0L & this.glVertexArrayVertexBuffers != 0L & this.glVertexArrayAttribFormat != 0L & this.glVertexArrayAttribIFormat != 0L & this.glVertexArrayAttribLFormat != 0L & this.glVertexArrayAttribBinding != 0L & this.glVertexArrayBindingDivisor != 0L & this.glGetVertexArrayiv != 0L & this.glGetVertexArrayIndexediv != 0L & this.glGetVertexArrayIndexed64iv != 0L & this.glCreateSamplers != 0L & this.glCreateProgramPipelines != 0L & this.glCreateQueries != 0L & this.glMemoryBarrierByRegion != 0L & this.glGetTextureSubImage != 0L & this.glGetCompressedTextureSubImage != 0L & this.glTextureBarrier != 0L & this.glGetGraphicsResetStatus != 0L & this.glReadnPixels != 0L & this.glGetnUniformfv != 0L & this.glGetnUniformiv != 0L & this.glGetnUniformuiv != 0L;
    }

    private boolean GREMEDY_frame_terminator_initNativeFunctionAddresses() {
        this.glFrameTerminatorGREMEDY = GLContext.getFunctionAddress("glFrameTerminatorGREMEDY");
        return this.glFrameTerminatorGREMEDY != 0L;
    }

    private boolean GREMEDY_string_marker_initNativeFunctionAddresses() {
        this.glStringMarkerGREMEDY = GLContext.getFunctionAddress("glStringMarkerGREMEDY");
        return this.glStringMarkerGREMEDY != 0L;
    }

    private boolean INTEL_map_texture_initNativeFunctionAddresses() {
        this.glMapTexture2DINTEL = GLContext.getFunctionAddress("glMapTexture2DINTEL");
        boolean bl = this.glMapTexture2DINTEL != 0L;
        this.glUnmapTexture2DINTEL = GLContext.getFunctionAddress("glUnmapTexture2DINTEL");
        this.glSyncTextureINTEL = GLContext.getFunctionAddress("glSyncTextureINTEL");
        return bl & this.glUnmapTexture2DINTEL != 0L & this.glSyncTextureINTEL != 0L;
    }

    private boolean KHR_debug_initNativeFunctionAddresses() {
        this.glDebugMessageControl = GLContext.getFunctionAddress("glDebugMessageControl");
        boolean bl = this.glDebugMessageControl != 0L;
        this.glDebugMessageInsert = GLContext.getFunctionAddress("glDebugMessageInsert");
        this.glDebugMessageCallback = GLContext.getFunctionAddress("glDebugMessageCallback");
        this.glGetDebugMessageLog = GLContext.getFunctionAddress("glGetDebugMessageLog");
        this.glPushDebugGroup = GLContext.getFunctionAddress("glPushDebugGroup");
        this.glPopDebugGroup = GLContext.getFunctionAddress("glPopDebugGroup");
        this.glObjectLabel = GLContext.getFunctionAddress("glObjectLabel");
        this.glGetObjectLabel = GLContext.getFunctionAddress("glGetObjectLabel");
        this.glObjectPtrLabel = GLContext.getFunctionAddress("glObjectPtrLabel");
        this.glGetObjectPtrLabel = GLContext.getFunctionAddress("glGetObjectPtrLabel");
        return bl & this.glDebugMessageInsert != 0L & this.glDebugMessageCallback != 0L & this.glGetDebugMessageLog != 0L & this.glPushDebugGroup != 0L & this.glPopDebugGroup != 0L & this.glObjectLabel != 0L & this.glGetObjectLabel != 0L & this.glObjectPtrLabel != 0L & this.glGetObjectPtrLabel != 0L;
    }

    private boolean KHR_robustness_initNativeFunctionAddresses() {
        this.glGetGraphicsResetStatus = GLContext.getFunctionAddress("glGetGraphicsResetStatus");
        boolean bl = this.glGetGraphicsResetStatus != 0L;
        this.glReadnPixels = GLContext.getFunctionAddress("glReadnPixels");
        this.glGetnUniformfv = GLContext.getFunctionAddress("glGetnUniformfv");
        this.glGetnUniformiv = GLContext.getFunctionAddress("glGetnUniformiv");
        this.glGetnUniformuiv = GLContext.getFunctionAddress("glGetnUniformuiv");
        return bl & this.glReadnPixels != 0L & this.glGetnUniformfv != 0L & this.glGetnUniformiv != 0L & this.glGetnUniformuiv != 0L;
    }

    private boolean NV_bindless_multi_draw_indirect_initNativeFunctionAddresses() {
        this.glMultiDrawArraysIndirectBindlessNV = GLContext.getFunctionAddress("glMultiDrawArraysIndirectBindlessNV");
        boolean bl = this.glMultiDrawArraysIndirectBindlessNV != 0L;
        this.glMultiDrawElementsIndirectBindlessNV = GLContext.getFunctionAddress("glMultiDrawElementsIndirectBindlessNV");
        return bl & this.glMultiDrawElementsIndirectBindlessNV != 0L;
    }

    private boolean NV_bindless_texture_initNativeFunctionAddresses() {
        this.glGetTextureHandleNV = GLContext.getFunctionAddress("glGetTextureHandleNV");
        boolean bl = this.glGetTextureHandleNV != 0L;
        this.glGetTextureSamplerHandleNV = GLContext.getFunctionAddress("glGetTextureSamplerHandleNV");
        this.glMakeTextureHandleResidentNV = GLContext.getFunctionAddress("glMakeTextureHandleResidentNV");
        this.glMakeTextureHandleNonResidentNV = GLContext.getFunctionAddress("glMakeTextureHandleNonResidentNV");
        this.glGetImageHandleNV = GLContext.getFunctionAddress("glGetImageHandleNV");
        this.glMakeImageHandleResidentNV = GLContext.getFunctionAddress("glMakeImageHandleResidentNV");
        this.glMakeImageHandleNonResidentNV = GLContext.getFunctionAddress("glMakeImageHandleNonResidentNV");
        this.glUniformHandleui64NV = GLContext.getFunctionAddress("glUniformHandleui64NV");
        this.glUniformHandleui64vNV = GLContext.getFunctionAddress("glUniformHandleui64vNV");
        this.glProgramUniformHandleui64NV = GLContext.getFunctionAddress("glProgramUniformHandleui64NV");
        this.glProgramUniformHandleui64vNV = GLContext.getFunctionAddress("glProgramUniformHandleui64vNV");
        this.glIsTextureHandleResidentNV = GLContext.getFunctionAddress("glIsTextureHandleResidentNV");
        this.glIsImageHandleResidentNV = GLContext.getFunctionAddress("glIsImageHandleResidentNV");
        return bl & this.glGetTextureSamplerHandleNV != 0L & this.glMakeTextureHandleResidentNV != 0L & this.glMakeTextureHandleNonResidentNV != 0L & this.glGetImageHandleNV != 0L & this.glMakeImageHandleResidentNV != 0L & this.glMakeImageHandleNonResidentNV != 0L & this.glUniformHandleui64NV != 0L & this.glUniformHandleui64vNV != 0L & this.glProgramUniformHandleui64NV != 0L & this.glProgramUniformHandleui64vNV != 0L & this.glIsTextureHandleResidentNV != 0L & this.glIsImageHandleResidentNV != 0L;
    }

    private boolean NV_blend_equation_advanced_initNativeFunctionAddresses() {
        this.glBlendParameteriNV = GLContext.getFunctionAddress("glBlendParameteriNV");
        boolean bl = this.glBlendParameteriNV != 0L;
        this.glBlendBarrierNV = GLContext.getFunctionAddress("glBlendBarrierNV");
        return bl & this.glBlendBarrierNV != 0L;
    }

    private boolean NV_conditional_render_initNativeFunctionAddresses() {
        this.glBeginConditionalRenderNV = GLContext.getFunctionAddress("glBeginConditionalRenderNV");
        boolean bl = this.glBeginConditionalRenderNV != 0L;
        this.glEndConditionalRenderNV = GLContext.getFunctionAddress("glEndConditionalRenderNV");
        return bl & this.glEndConditionalRenderNV != 0L;
    }

    private boolean NV_copy_image_initNativeFunctionAddresses() {
        this.glCopyImageSubDataNV = GLContext.getFunctionAddress("glCopyImageSubDataNV");
        return this.glCopyImageSubDataNV != 0L;
    }

    private boolean NV_depth_buffer_float_initNativeFunctionAddresses() {
        this.glDepthRangedNV = GLContext.getFunctionAddress("glDepthRangedNV");
        boolean bl = this.glDepthRangedNV != 0L;
        this.glClearDepthdNV = GLContext.getFunctionAddress("glClearDepthdNV");
        this.glDepthBoundsdNV = GLContext.getFunctionAddress("glDepthBoundsdNV");
        return bl & this.glClearDepthdNV != 0L & this.glDepthBoundsdNV != 0L;
    }

    private boolean NV_draw_texture_initNativeFunctionAddresses() {
        this.glDrawTextureNV = GLContext.getFunctionAddress("glDrawTextureNV");
        return this.glDrawTextureNV != 0L;
    }

    private boolean NV_evaluators_initNativeFunctionAddresses() {
        this.glGetMapControlPointsNV = GLContext.getFunctionAddress("glGetMapControlPointsNV");
        boolean bl = this.glGetMapControlPointsNV != 0L;
        this.glMapControlPointsNV = GLContext.getFunctionAddress("glMapControlPointsNV");
        this.glMapParameterfvNV = GLContext.getFunctionAddress("glMapParameterfvNV");
        this.glMapParameterivNV = GLContext.getFunctionAddress("glMapParameterivNV");
        this.glGetMapParameterfvNV = GLContext.getFunctionAddress("glGetMapParameterfvNV");
        this.glGetMapParameterivNV = GLContext.getFunctionAddress("glGetMapParameterivNV");
        this.glGetMapAttribParameterfvNV = GLContext.getFunctionAddress("glGetMapAttribParameterfvNV");
        this.glGetMapAttribParameterivNV = GLContext.getFunctionAddress("glGetMapAttribParameterivNV");
        this.glEvalMapsNV = GLContext.getFunctionAddress("glEvalMapsNV");
        return bl & this.glMapControlPointsNV != 0L & this.glMapParameterfvNV != 0L & this.glMapParameterivNV != 0L & this.glGetMapParameterfvNV != 0L & this.glGetMapParameterivNV != 0L & this.glGetMapAttribParameterfvNV != 0L & this.glGetMapAttribParameterivNV != 0L & this.glEvalMapsNV != 0L;
    }

    private boolean NV_explicit_multisample_initNativeFunctionAddresses() {
        this.glGetBooleanIndexedvEXT = GLContext.getFunctionAddress("glGetBooleanIndexedvEXT");
        boolean bl = this.glGetBooleanIndexedvEXT != 0L;
        this.glGetIntegerIndexedvEXT = GLContext.getFunctionAddress("glGetIntegerIndexedvEXT");
        this.glGetMultisamplefvNV = GLContext.getFunctionAddress("glGetMultisamplefvNV");
        this.glSampleMaskIndexedNV = GLContext.getFunctionAddress("glSampleMaskIndexedNV");
        this.glTexRenderbufferNV = GLContext.getFunctionAddress("glTexRenderbufferNV");
        return bl & this.glGetIntegerIndexedvEXT != 0L & this.glGetMultisamplefvNV != 0L & this.glSampleMaskIndexedNV != 0L & this.glTexRenderbufferNV != 0L;
    }

    private boolean NV_fence_initNativeFunctionAddresses() {
        this.glGenFencesNV = GLContext.getFunctionAddress("glGenFencesNV");
        boolean bl = this.glGenFencesNV != 0L;
        this.glDeleteFencesNV = GLContext.getFunctionAddress("glDeleteFencesNV");
        this.glSetFenceNV = GLContext.getFunctionAddress("glSetFenceNV");
        this.glTestFenceNV = GLContext.getFunctionAddress("glTestFenceNV");
        this.glFinishFenceNV = GLContext.getFunctionAddress("glFinishFenceNV");
        this.glIsFenceNV = GLContext.getFunctionAddress("glIsFenceNV");
        this.glGetFenceivNV = GLContext.getFunctionAddress("glGetFenceivNV");
        return bl & this.glDeleteFencesNV != 0L & this.glSetFenceNV != 0L & this.glTestFenceNV != 0L & this.glFinishFenceNV != 0L & this.glIsFenceNV != 0L & this.glGetFenceivNV != 0L;
    }

    private boolean NV_fragment_program_initNativeFunctionAddresses() {
        this.glProgramNamedParameter4fNV = GLContext.getFunctionAddress("glProgramNamedParameter4fNV");
        boolean bl = this.glProgramNamedParameter4fNV != 0L;
        this.glProgramNamedParameter4dNV = GLContext.getFunctionAddress("glProgramNamedParameter4dNV");
        this.glGetProgramNamedParameterfvNV = GLContext.getFunctionAddress("glGetProgramNamedParameterfvNV");
        this.glGetProgramNamedParameterdvNV = GLContext.getFunctionAddress("glGetProgramNamedParameterdvNV");
        return bl & this.glProgramNamedParameter4dNV != 0L & this.glGetProgramNamedParameterfvNV != 0L & this.glGetProgramNamedParameterdvNV != 0L;
    }

    private boolean NV_framebuffer_multisample_coverage_initNativeFunctionAddresses() {
        this.glRenderbufferStorageMultisampleCoverageNV = GLContext.getFunctionAddress("glRenderbufferStorageMultisampleCoverageNV");
        return this.glRenderbufferStorageMultisampleCoverageNV != 0L;
    }

    private boolean NV_geometry_program4_initNativeFunctionAddresses() {
        this.glProgramVertexLimitNV = GLContext.getFunctionAddress("glProgramVertexLimitNV");
        boolean bl = this.glProgramVertexLimitNV != 0L;
        this.glFramebufferTextureEXT = GLContext.getFunctionAddress("glFramebufferTextureEXT");
        this.glFramebufferTextureLayerEXT = GLContext.getFunctionAddress("glFramebufferTextureLayerEXT");
        this.glFramebufferTextureFaceEXT = GLContext.getFunctionAddress("glFramebufferTextureFaceEXT");
        return bl & this.glFramebufferTextureEXT != 0L & this.glFramebufferTextureLayerEXT != 0L & this.glFramebufferTextureFaceEXT != 0L;
    }

    private boolean NV_gpu_program4_initNativeFunctionAddresses() {
        this.glProgramLocalParameterI4iNV = GLContext.getFunctionAddress("glProgramLocalParameterI4iNV");
        boolean bl = this.glProgramLocalParameterI4iNV != 0L;
        this.glProgramLocalParameterI4ivNV = GLContext.getFunctionAddress("glProgramLocalParameterI4ivNV");
        this.glProgramLocalParametersI4ivNV = GLContext.getFunctionAddress("glProgramLocalParametersI4ivNV");
        this.glProgramLocalParameterI4uiNV = GLContext.getFunctionAddress("glProgramLocalParameterI4uiNV");
        this.glProgramLocalParameterI4uivNV = GLContext.getFunctionAddress("glProgramLocalParameterI4uivNV");
        this.glProgramLocalParametersI4uivNV = GLContext.getFunctionAddress("glProgramLocalParametersI4uivNV");
        this.glProgramEnvParameterI4iNV = GLContext.getFunctionAddress("glProgramEnvParameterI4iNV");
        this.glProgramEnvParameterI4ivNV = GLContext.getFunctionAddress("glProgramEnvParameterI4ivNV");
        this.glProgramEnvParametersI4ivNV = GLContext.getFunctionAddress("glProgramEnvParametersI4ivNV");
        this.glProgramEnvParameterI4uiNV = GLContext.getFunctionAddress("glProgramEnvParameterI4uiNV");
        this.glProgramEnvParameterI4uivNV = GLContext.getFunctionAddress("glProgramEnvParameterI4uivNV");
        this.glProgramEnvParametersI4uivNV = GLContext.getFunctionAddress("glProgramEnvParametersI4uivNV");
        this.glGetProgramLocalParameterIivNV = GLContext.getFunctionAddress("glGetProgramLocalParameterIivNV");
        this.glGetProgramLocalParameterIuivNV = GLContext.getFunctionAddress("glGetProgramLocalParameterIuivNV");
        this.glGetProgramEnvParameterIivNV = GLContext.getFunctionAddress("glGetProgramEnvParameterIivNV");
        this.glGetProgramEnvParameterIuivNV = GLContext.getFunctionAddress("glGetProgramEnvParameterIuivNV");
        return bl & this.glProgramLocalParameterI4ivNV != 0L & this.glProgramLocalParametersI4ivNV != 0L & this.glProgramLocalParameterI4uiNV != 0L & this.glProgramLocalParameterI4uivNV != 0L & this.glProgramLocalParametersI4uivNV != 0L & this.glProgramEnvParameterI4iNV != 0L & this.glProgramEnvParameterI4ivNV != 0L & this.glProgramEnvParametersI4ivNV != 0L & this.glProgramEnvParameterI4uiNV != 0L & this.glProgramEnvParameterI4uivNV != 0L & this.glProgramEnvParametersI4uivNV != 0L & this.glGetProgramLocalParameterIivNV != 0L & this.glGetProgramLocalParameterIuivNV != 0L & this.glGetProgramEnvParameterIivNV != 0L & this.glGetProgramEnvParameterIuivNV != 0L;
    }

    private boolean NV_gpu_shader5_initNativeFunctionAddresses(Set<String> supported_extensions) {
        this.glUniform1i64NV = GLContext.getFunctionAddress("glUniform1i64NV");
        boolean bl = this.glUniform1i64NV != 0L;
        this.glUniform2i64NV = GLContext.getFunctionAddress("glUniform2i64NV");
        this.glUniform3i64NV = GLContext.getFunctionAddress("glUniform3i64NV");
        this.glUniform4i64NV = GLContext.getFunctionAddress("glUniform4i64NV");
        this.glUniform1i64vNV = GLContext.getFunctionAddress("glUniform1i64vNV");
        this.glUniform2i64vNV = GLContext.getFunctionAddress("glUniform2i64vNV");
        this.glUniform3i64vNV = GLContext.getFunctionAddress("glUniform3i64vNV");
        this.glUniform4i64vNV = GLContext.getFunctionAddress("glUniform4i64vNV");
        this.glUniform1ui64NV = GLContext.getFunctionAddress("glUniform1ui64NV");
        this.glUniform2ui64NV = GLContext.getFunctionAddress("glUniform2ui64NV");
        this.glUniform3ui64NV = GLContext.getFunctionAddress("glUniform3ui64NV");
        this.glUniform4ui64NV = GLContext.getFunctionAddress("glUniform4ui64NV");
        this.glUniform1ui64vNV = GLContext.getFunctionAddress("glUniform1ui64vNV");
        this.glUniform2ui64vNV = GLContext.getFunctionAddress("glUniform2ui64vNV");
        this.glUniform3ui64vNV = GLContext.getFunctionAddress("glUniform3ui64vNV");
        this.glUniform4ui64vNV = GLContext.getFunctionAddress("glUniform4ui64vNV");
        this.glGetUniformi64vNV = GLContext.getFunctionAddress("glGetUniformi64vNV");
        this.glGetUniformui64vNV = GLContext.getFunctionAddress("glGetUniformui64vNV");
        return bl & this.glUniform2i64NV != 0L & this.glUniform3i64NV != 0L & this.glUniform4i64NV != 0L & this.glUniform1i64vNV != 0L & this.glUniform2i64vNV != 0L & this.glUniform3i64vNV != 0L & this.glUniform4i64vNV != 0L & this.glUniform1ui64NV != 0L & this.glUniform2ui64NV != 0L & this.glUniform3ui64NV != 0L & this.glUniform4ui64NV != 0L & this.glUniform1ui64vNV != 0L & this.glUniform2ui64vNV != 0L & this.glUniform3ui64vNV != 0L & this.glUniform4ui64vNV != 0L & this.glGetUniformi64vNV != 0L & this.glGetUniformui64vNV != 0L & (!supported_extensions.contains("GL_EXT_direct_state_access") || (this.glProgramUniform1i64NV = GLContext.getFunctionAddress("glProgramUniform1i64NV")) != 0L) & (!supported_extensions.contains("GL_EXT_direct_state_access") || (this.glProgramUniform2i64NV = GLContext.getFunctionAddress("glProgramUniform2i64NV")) != 0L) & (!supported_extensions.contains("GL_EXT_direct_state_access") || (this.glProgramUniform3i64NV = GLContext.getFunctionAddress("glProgramUniform3i64NV")) != 0L) & (!supported_extensions.contains("GL_EXT_direct_state_access") || (this.glProgramUniform4i64NV = GLContext.getFunctionAddress("glProgramUniform4i64NV")) != 0L) & (!supported_extensions.contains("GL_EXT_direct_state_access") || (this.glProgramUniform1i64vNV = GLContext.getFunctionAddress("glProgramUniform1i64vNV")) != 0L) & (!supported_extensions.contains("GL_EXT_direct_state_access") || (this.glProgramUniform2i64vNV = GLContext.getFunctionAddress("glProgramUniform2i64vNV")) != 0L) & (!supported_extensions.contains("GL_EXT_direct_state_access") || (this.glProgramUniform3i64vNV = GLContext.getFunctionAddress("glProgramUniform3i64vNV")) != 0L) & (!supported_extensions.contains("GL_EXT_direct_state_access") || (this.glProgramUniform4i64vNV = GLContext.getFunctionAddress("glProgramUniform4i64vNV")) != 0L) & (!supported_extensions.contains("GL_EXT_direct_state_access") || (this.glProgramUniform1ui64NV = GLContext.getFunctionAddress("glProgramUniform1ui64NV")) != 0L) & (!supported_extensions.contains("GL_EXT_direct_state_access") || (this.glProgramUniform2ui64NV = GLContext.getFunctionAddress("glProgramUniform2ui64NV")) != 0L) & (!supported_extensions.contains("GL_EXT_direct_state_access") || (this.glProgramUniform3ui64NV = GLContext.getFunctionAddress("glProgramUniform3ui64NV")) != 0L) & (!supported_extensions.contains("GL_EXT_direct_state_access") || (this.glProgramUniform4ui64NV = GLContext.getFunctionAddress("glProgramUniform4ui64NV")) != 0L) & (!supported_extensions.contains("GL_EXT_direct_state_access") || (this.glProgramUniform1ui64vNV = GLContext.getFunctionAddress("glProgramUniform1ui64vNV")) != 0L) & (!supported_extensions.contains("GL_EXT_direct_state_access") || (this.glProgramUniform2ui64vNV = GLContext.getFunctionAddress("glProgramUniform2ui64vNV")) != 0L) & (!supported_extensions.contains("GL_EXT_direct_state_access") || (this.glProgramUniform3ui64vNV = GLContext.getFunctionAddress("glProgramUniform3ui64vNV")) != 0L) & (!supported_extensions.contains("GL_EXT_direct_state_access") || (this.glProgramUniform4ui64vNV = GLContext.getFunctionAddress("glProgramUniform4ui64vNV")) != 0L);
    }

    private boolean NV_half_float_initNativeFunctionAddresses(Set<String> supported_extensions) {
        this.glVertex2hNV = GLContext.getFunctionAddress("glVertex2hNV");
        boolean bl = this.glVertex2hNV != 0L;
        this.glVertex3hNV = GLContext.getFunctionAddress("glVertex3hNV");
        this.glVertex4hNV = GLContext.getFunctionAddress("glVertex4hNV");
        this.glNormal3hNV = GLContext.getFunctionAddress("glNormal3hNV");
        this.glColor3hNV = GLContext.getFunctionAddress("glColor3hNV");
        this.glColor4hNV = GLContext.getFunctionAddress("glColor4hNV");
        this.glTexCoord1hNV = GLContext.getFunctionAddress("glTexCoord1hNV");
        this.glTexCoord2hNV = GLContext.getFunctionAddress("glTexCoord2hNV");
        this.glTexCoord3hNV = GLContext.getFunctionAddress("glTexCoord3hNV");
        this.glTexCoord4hNV = GLContext.getFunctionAddress("glTexCoord4hNV");
        this.glMultiTexCoord1hNV = GLContext.getFunctionAddress("glMultiTexCoord1hNV");
        this.glMultiTexCoord2hNV = GLContext.getFunctionAddress("glMultiTexCoord2hNV");
        this.glMultiTexCoord3hNV = GLContext.getFunctionAddress("glMultiTexCoord3hNV");
        this.glMultiTexCoord4hNV = GLContext.getFunctionAddress("glMultiTexCoord4hNV");
        return bl & this.glVertex3hNV != 0L & this.glVertex4hNV != 0L & this.glNormal3hNV != 0L & this.glColor3hNV != 0L & this.glColor4hNV != 0L & this.glTexCoord1hNV != 0L & this.glTexCoord2hNV != 0L & this.glTexCoord3hNV != 0L & this.glTexCoord4hNV != 0L & this.glMultiTexCoord1hNV != 0L & this.glMultiTexCoord2hNV != 0L & this.glMultiTexCoord3hNV != 0L & this.glMultiTexCoord4hNV != 0L & (!supported_extensions.contains("GL_EXT_fog_coord") || (this.glFogCoordhNV = GLContext.getFunctionAddress("glFogCoordhNV")) != 0L) & (!supported_extensions.contains("GL_EXT_secondary_color") || (this.glSecondaryColor3hNV = GLContext.getFunctionAddress("glSecondaryColor3hNV")) != 0L) & (!supported_extensions.contains("GL_EXT_vertex_weighting") || (this.glVertexWeighthNV = GLContext.getFunctionAddress("glVertexWeighthNV")) != 0L) & (!supported_extensions.contains("GL_NV_vertex_program") || (this.glVertexAttrib1hNV = GLContext.getFunctionAddress("glVertexAttrib1hNV")) != 0L) & (!supported_extensions.contains("GL_NV_vertex_program") || (this.glVertexAttrib2hNV = GLContext.getFunctionAddress("glVertexAttrib2hNV")) != 0L) & (!supported_extensions.contains("GL_NV_vertex_program") || (this.glVertexAttrib3hNV = GLContext.getFunctionAddress("glVertexAttrib3hNV")) != 0L) & (!supported_extensions.contains("GL_NV_vertex_program") || (this.glVertexAttrib4hNV = GLContext.getFunctionAddress("glVertexAttrib4hNV")) != 0L) & (!supported_extensions.contains("GL_NV_vertex_program") || (this.glVertexAttribs1hvNV = GLContext.getFunctionAddress("glVertexAttribs1hvNV")) != 0L) & (!supported_extensions.contains("GL_NV_vertex_program") || (this.glVertexAttribs2hvNV = GLContext.getFunctionAddress("glVertexAttribs2hvNV")) != 0L) & (!supported_extensions.contains("GL_NV_vertex_program") || (this.glVertexAttribs3hvNV = GLContext.getFunctionAddress("glVertexAttribs3hvNV")) != 0L) & (!supported_extensions.contains("GL_NV_vertex_program") || (this.glVertexAttribs4hvNV = GLContext.getFunctionAddress("glVertexAttribs4hvNV")) != 0L);
    }

    private boolean NV_occlusion_query_initNativeFunctionAddresses() {
        this.glGenOcclusionQueriesNV = GLContext.getFunctionAddress("glGenOcclusionQueriesNV");
        boolean bl = this.glGenOcclusionQueriesNV != 0L;
        this.glDeleteOcclusionQueriesNV = GLContext.getFunctionAddress("glDeleteOcclusionQueriesNV");
        this.glIsOcclusionQueryNV = GLContext.getFunctionAddress("glIsOcclusionQueryNV");
        this.glBeginOcclusionQueryNV = GLContext.getFunctionAddress("glBeginOcclusionQueryNV");
        this.glEndOcclusionQueryNV = GLContext.getFunctionAddress("glEndOcclusionQueryNV");
        this.glGetOcclusionQueryuivNV = GLContext.getFunctionAddress("glGetOcclusionQueryuivNV");
        this.glGetOcclusionQueryivNV = GLContext.getFunctionAddress("glGetOcclusionQueryivNV");
        return bl & this.glDeleteOcclusionQueriesNV != 0L & this.glIsOcclusionQueryNV != 0L & this.glBeginOcclusionQueryNV != 0L & this.glEndOcclusionQueryNV != 0L & this.glGetOcclusionQueryuivNV != 0L & this.glGetOcclusionQueryivNV != 0L;
    }

    private boolean NV_parameter_buffer_object_initNativeFunctionAddresses() {
        this.glProgramBufferParametersfvNV = GLContext.getFunctionAddress("glProgramBufferParametersfvNV");
        boolean bl = this.glProgramBufferParametersfvNV != 0L;
        this.glProgramBufferParametersIivNV = GLContext.getFunctionAddress("glProgramBufferParametersIivNV");
        this.glProgramBufferParametersIuivNV = GLContext.getFunctionAddress("glProgramBufferParametersIuivNV");
        return bl & this.glProgramBufferParametersIivNV != 0L & this.glProgramBufferParametersIuivNV != 0L;
    }

    private boolean NV_path_rendering_initNativeFunctionAddresses() {
        this.glPathCommandsNV = GLContext.getFunctionAddress("glPathCommandsNV");
        boolean bl = this.glPathCommandsNV != 0L;
        this.glPathCoordsNV = GLContext.getFunctionAddress("glPathCoordsNV");
        this.glPathSubCommandsNV = GLContext.getFunctionAddress("glPathSubCommandsNV");
        this.glPathSubCoordsNV = GLContext.getFunctionAddress("glPathSubCoordsNV");
        this.glPathStringNV = GLContext.getFunctionAddress("glPathStringNV");
        this.glPathGlyphsNV = GLContext.getFunctionAddress("glPathGlyphsNV");
        this.glPathGlyphRangeNV = GLContext.getFunctionAddress("glPathGlyphRangeNV");
        this.glWeightPathsNV = GLContext.getFunctionAddress("glWeightPathsNV");
        this.glCopyPathNV = GLContext.getFunctionAddress("glCopyPathNV");
        this.glInterpolatePathsNV = GLContext.getFunctionAddress("glInterpolatePathsNV");
        this.glTransformPathNV = GLContext.getFunctionAddress("glTransformPathNV");
        this.glPathParameterivNV = GLContext.getFunctionAddress("glPathParameterivNV");
        this.glPathParameteriNV = GLContext.getFunctionAddress("glPathParameteriNV");
        this.glPathParameterfvNV = GLContext.getFunctionAddress("glPathParameterfvNV");
        this.glPathParameterfNV = GLContext.getFunctionAddress("glPathParameterfNV");
        this.glPathDashArrayNV = GLContext.getFunctionAddress("glPathDashArrayNV");
        this.glGenPathsNV = GLContext.getFunctionAddress("glGenPathsNV");
        this.glDeletePathsNV = GLContext.getFunctionAddress("glDeletePathsNV");
        this.glIsPathNV = GLContext.getFunctionAddress("glIsPathNV");
        this.glPathStencilFuncNV = GLContext.getFunctionAddress("glPathStencilFuncNV");
        this.glPathStencilDepthOffsetNV = GLContext.getFunctionAddress("glPathStencilDepthOffsetNV");
        this.glStencilFillPathNV = GLContext.getFunctionAddress("glStencilFillPathNV");
        this.glStencilStrokePathNV = GLContext.getFunctionAddress("glStencilStrokePathNV");
        this.glStencilFillPathInstancedNV = GLContext.getFunctionAddress("glStencilFillPathInstancedNV");
        this.glStencilStrokePathInstancedNV = GLContext.getFunctionAddress("glStencilStrokePathInstancedNV");
        this.glPathCoverDepthFuncNV = GLContext.getFunctionAddress("glPathCoverDepthFuncNV");
        this.glPathColorGenNV = GLContext.getFunctionAddress("glPathColorGenNV");
        this.glPathTexGenNV = GLContext.getFunctionAddress("glPathTexGenNV");
        this.glPathFogGenNV = GLContext.getFunctionAddress("glPathFogGenNV");
        this.glCoverFillPathNV = GLContext.getFunctionAddress("glCoverFillPathNV");
        this.glCoverStrokePathNV = GLContext.getFunctionAddress("glCoverStrokePathNV");
        this.glCoverFillPathInstancedNV = GLContext.getFunctionAddress("glCoverFillPathInstancedNV");
        this.glCoverStrokePathInstancedNV = GLContext.getFunctionAddress("glCoverStrokePathInstancedNV");
        this.glGetPathParameterivNV = GLContext.getFunctionAddress("glGetPathParameterivNV");
        this.glGetPathParameterfvNV = GLContext.getFunctionAddress("glGetPathParameterfvNV");
        this.glGetPathCommandsNV = GLContext.getFunctionAddress("glGetPathCommandsNV");
        this.glGetPathCoordsNV = GLContext.getFunctionAddress("glGetPathCoordsNV");
        this.glGetPathDashArrayNV = GLContext.getFunctionAddress("glGetPathDashArrayNV");
        this.glGetPathMetricsNV = GLContext.getFunctionAddress("glGetPathMetricsNV");
        this.glGetPathMetricRangeNV = GLContext.getFunctionAddress("glGetPathMetricRangeNV");
        this.glGetPathSpacingNV = GLContext.getFunctionAddress("glGetPathSpacingNV");
        this.glGetPathColorGenivNV = GLContext.getFunctionAddress("glGetPathColorGenivNV");
        this.glGetPathColorGenfvNV = GLContext.getFunctionAddress("glGetPathColorGenfvNV");
        this.glGetPathTexGenivNV = GLContext.getFunctionAddress("glGetPathTexGenivNV");
        this.glGetPathTexGenfvNV = GLContext.getFunctionAddress("glGetPathTexGenfvNV");
        this.glIsPointInFillPathNV = GLContext.getFunctionAddress("glIsPointInFillPathNV");
        this.glIsPointInStrokePathNV = GLContext.getFunctionAddress("glIsPointInStrokePathNV");
        this.glGetPathLengthNV = GLContext.getFunctionAddress("glGetPathLengthNV");
        this.glPointAlongPathNV = GLContext.getFunctionAddress("glPointAlongPathNV");
        return bl & this.glPathCoordsNV != 0L & this.glPathSubCommandsNV != 0L & this.glPathSubCoordsNV != 0L & this.glPathStringNV != 0L & this.glPathGlyphsNV != 0L & this.glPathGlyphRangeNV != 0L & this.glWeightPathsNV != 0L & this.glCopyPathNV != 0L & this.glInterpolatePathsNV != 0L & this.glTransformPathNV != 0L & this.glPathParameterivNV != 0L & this.glPathParameteriNV != 0L & this.glPathParameterfvNV != 0L & this.glPathParameterfNV != 0L & this.glPathDashArrayNV != 0L & this.glGenPathsNV != 0L & this.glDeletePathsNV != 0L & this.glIsPathNV != 0L & this.glPathStencilFuncNV != 0L & this.glPathStencilDepthOffsetNV != 0L & this.glStencilFillPathNV != 0L & this.glStencilStrokePathNV != 0L & this.glStencilFillPathInstancedNV != 0L & this.glStencilStrokePathInstancedNV != 0L & this.glPathCoverDepthFuncNV != 0L & this.glPathColorGenNV != 0L & this.glPathTexGenNV != 0L & this.glPathFogGenNV != 0L & this.glCoverFillPathNV != 0L & this.glCoverStrokePathNV != 0L & this.glCoverFillPathInstancedNV != 0L & this.glCoverStrokePathInstancedNV != 0L & this.glGetPathParameterivNV != 0L & this.glGetPathParameterfvNV != 0L & this.glGetPathCommandsNV != 0L & this.glGetPathCoordsNV != 0L & this.glGetPathDashArrayNV != 0L & this.glGetPathMetricsNV != 0L & this.glGetPathMetricRangeNV != 0L & this.glGetPathSpacingNV != 0L & this.glGetPathColorGenivNV != 0L & this.glGetPathColorGenfvNV != 0L & this.glGetPathTexGenivNV != 0L & this.glGetPathTexGenfvNV != 0L & this.glIsPointInFillPathNV != 0L & this.glIsPointInStrokePathNV != 0L & this.glGetPathLengthNV != 0L & this.glPointAlongPathNV != 0L;
    }

    private boolean NV_pixel_data_range_initNativeFunctionAddresses() {
        this.glPixelDataRangeNV = GLContext.getFunctionAddress("glPixelDataRangeNV");
        boolean bl = this.glPixelDataRangeNV != 0L;
        this.glFlushPixelDataRangeNV = GLContext.getFunctionAddress("glFlushPixelDataRangeNV");
        return bl & this.glFlushPixelDataRangeNV != 0L;
    }

    private boolean NV_point_sprite_initNativeFunctionAddresses() {
        this.glPointParameteriNV = GLContext.getFunctionAddress("glPointParameteriNV");
        boolean bl = this.glPointParameteriNV != 0L;
        this.glPointParameterivNV = GLContext.getFunctionAddress("glPointParameterivNV");
        return bl & this.glPointParameterivNV != 0L;
    }

    private boolean NV_present_video_initNativeFunctionAddresses() {
        this.glPresentFrameKeyedNV = GLContext.getFunctionAddress("glPresentFrameKeyedNV");
        boolean bl = this.glPresentFrameKeyedNV != 0L;
        this.glPresentFrameDualFillNV = GLContext.getFunctionAddress("glPresentFrameDualFillNV");
        this.glGetVideoivNV = GLContext.getFunctionAddress("glGetVideoivNV");
        this.glGetVideouivNV = GLContext.getFunctionAddress("glGetVideouivNV");
        this.glGetVideoi64vNV = GLContext.getFunctionAddress("glGetVideoi64vNV");
        this.glGetVideoui64vNV = GLContext.getFunctionAddress("glGetVideoui64vNV");
        return bl & this.glPresentFrameDualFillNV != 0L & this.glGetVideoivNV != 0L & this.glGetVideouivNV != 0L & this.glGetVideoi64vNV != 0L & this.glGetVideoui64vNV != 0L;
    }

    private boolean NV_primitive_restart_initNativeFunctionAddresses() {
        this.glPrimitiveRestartNV = GLContext.getFunctionAddress("glPrimitiveRestartNV");
        boolean bl = this.glPrimitiveRestartNV != 0L;
        this.glPrimitiveRestartIndexNV = GLContext.getFunctionAddress("glPrimitiveRestartIndexNV");
        return bl & this.glPrimitiveRestartIndexNV != 0L;
    }

    private boolean NV_program_initNativeFunctionAddresses() {
        this.glLoadProgramNV = GLContext.getFunctionAddress("glLoadProgramNV");
        boolean bl = this.glLoadProgramNV != 0L;
        this.glBindProgramNV = GLContext.getFunctionAddress("glBindProgramNV");
        this.glDeleteProgramsNV = GLContext.getFunctionAddress("glDeleteProgramsNV");
        this.glGenProgramsNV = GLContext.getFunctionAddress("glGenProgramsNV");
        this.glGetProgramivNV = GLContext.getFunctionAddress("glGetProgramivNV");
        this.glGetProgramStringNV = GLContext.getFunctionAddress("glGetProgramStringNV");
        this.glIsProgramNV = GLContext.getFunctionAddress("glIsProgramNV");
        this.glAreProgramsResidentNV = GLContext.getFunctionAddress("glAreProgramsResidentNV");
        this.glRequestResidentProgramsNV = GLContext.getFunctionAddress("glRequestResidentProgramsNV");
        return bl & this.glBindProgramNV != 0L & this.glDeleteProgramsNV != 0L & this.glGenProgramsNV != 0L & this.glGetProgramivNV != 0L & this.glGetProgramStringNV != 0L & this.glIsProgramNV != 0L & this.glAreProgramsResidentNV != 0L & this.glRequestResidentProgramsNV != 0L;
    }

    private boolean NV_register_combiners_initNativeFunctionAddresses() {
        this.glCombinerParameterfNV = GLContext.getFunctionAddress("glCombinerParameterfNV");
        boolean bl = this.glCombinerParameterfNV != 0L;
        this.glCombinerParameterfvNV = GLContext.getFunctionAddress("glCombinerParameterfvNV");
        this.glCombinerParameteriNV = GLContext.getFunctionAddress("glCombinerParameteriNV");
        this.glCombinerParameterivNV = GLContext.getFunctionAddress("glCombinerParameterivNV");
        this.glCombinerInputNV = GLContext.getFunctionAddress("glCombinerInputNV");
        this.glCombinerOutputNV = GLContext.getFunctionAddress("glCombinerOutputNV");
        this.glFinalCombinerInputNV = GLContext.getFunctionAddress("glFinalCombinerInputNV");
        this.glGetCombinerInputParameterfvNV = GLContext.getFunctionAddress("glGetCombinerInputParameterfvNV");
        this.glGetCombinerInputParameterivNV = GLContext.getFunctionAddress("glGetCombinerInputParameterivNV");
        this.glGetCombinerOutputParameterfvNV = GLContext.getFunctionAddress("glGetCombinerOutputParameterfvNV");
        this.glGetCombinerOutputParameterivNV = GLContext.getFunctionAddress("glGetCombinerOutputParameterivNV");
        this.glGetFinalCombinerInputParameterfvNV = GLContext.getFunctionAddress("glGetFinalCombinerInputParameterfvNV");
        this.glGetFinalCombinerInputParameterivNV = GLContext.getFunctionAddress("glGetFinalCombinerInputParameterivNV");
        return bl & this.glCombinerParameterfvNV != 0L & this.glCombinerParameteriNV != 0L & this.glCombinerParameterivNV != 0L & this.glCombinerInputNV != 0L & this.glCombinerOutputNV != 0L & this.glFinalCombinerInputNV != 0L & this.glGetCombinerInputParameterfvNV != 0L & this.glGetCombinerInputParameterivNV != 0L & this.glGetCombinerOutputParameterfvNV != 0L & this.glGetCombinerOutputParameterivNV != 0L & this.glGetFinalCombinerInputParameterfvNV != 0L & this.glGetFinalCombinerInputParameterivNV != 0L;
    }

    private boolean NV_register_combiners2_initNativeFunctionAddresses() {
        this.glCombinerStageParameterfvNV = GLContext.getFunctionAddress("glCombinerStageParameterfvNV");
        boolean bl = this.glCombinerStageParameterfvNV != 0L;
        this.glGetCombinerStageParameterfvNV = GLContext.getFunctionAddress("glGetCombinerStageParameterfvNV");
        return bl & this.glGetCombinerStageParameterfvNV != 0L;
    }

    private boolean NV_shader_buffer_load_initNativeFunctionAddresses() {
        this.glMakeBufferResidentNV = GLContext.getFunctionAddress("glMakeBufferResidentNV");
        boolean bl = this.glMakeBufferResidentNV != 0L;
        this.glMakeBufferNonResidentNV = GLContext.getFunctionAddress("glMakeBufferNonResidentNV");
        this.glIsBufferResidentNV = GLContext.getFunctionAddress("glIsBufferResidentNV");
        this.glMakeNamedBufferResidentNV = GLContext.getFunctionAddress("glMakeNamedBufferResidentNV");
        this.glMakeNamedBufferNonResidentNV = GLContext.getFunctionAddress("glMakeNamedBufferNonResidentNV");
        this.glIsNamedBufferResidentNV = GLContext.getFunctionAddress("glIsNamedBufferResidentNV");
        this.glGetBufferParameterui64vNV = GLContext.getFunctionAddress("glGetBufferParameterui64vNV");
        this.glGetNamedBufferParameterui64vNV = GLContext.getFunctionAddress("glGetNamedBufferParameterui64vNV");
        this.glGetIntegerui64vNV = GLContext.getFunctionAddress("glGetIntegerui64vNV");
        this.glUniformui64NV = GLContext.getFunctionAddress("glUniformui64NV");
        this.glUniformui64vNV = GLContext.getFunctionAddress("glUniformui64vNV");
        this.glGetUniformui64vNV = GLContext.getFunctionAddress("glGetUniformui64vNV");
        this.glProgramUniformui64NV = GLContext.getFunctionAddress("glProgramUniformui64NV");
        this.glProgramUniformui64vNV = GLContext.getFunctionAddress("glProgramUniformui64vNV");
        return bl & this.glMakeBufferNonResidentNV != 0L & this.glIsBufferResidentNV != 0L & this.glMakeNamedBufferResidentNV != 0L & this.glMakeNamedBufferNonResidentNV != 0L & this.glIsNamedBufferResidentNV != 0L & this.glGetBufferParameterui64vNV != 0L & this.glGetNamedBufferParameterui64vNV != 0L & this.glGetIntegerui64vNV != 0L & this.glUniformui64NV != 0L & this.glUniformui64vNV != 0L & this.glGetUniformui64vNV != 0L & this.glProgramUniformui64NV != 0L & this.glProgramUniformui64vNV != 0L;
    }

    private boolean NV_texture_barrier_initNativeFunctionAddresses() {
        this.glTextureBarrierNV = GLContext.getFunctionAddress("glTextureBarrierNV");
        return this.glTextureBarrierNV != 0L;
    }

    private boolean NV_texture_multisample_initNativeFunctionAddresses() {
        this.glTexImage2DMultisampleCoverageNV = GLContext.getFunctionAddress("glTexImage2DMultisampleCoverageNV");
        boolean bl = this.glTexImage2DMultisampleCoverageNV != 0L;
        this.glTexImage3DMultisampleCoverageNV = GLContext.getFunctionAddress("glTexImage3DMultisampleCoverageNV");
        this.glTextureImage2DMultisampleNV = GLContext.getFunctionAddress("glTextureImage2DMultisampleNV");
        this.glTextureImage3DMultisampleNV = GLContext.getFunctionAddress("glTextureImage3DMultisampleNV");
        this.glTextureImage2DMultisampleCoverageNV = GLContext.getFunctionAddress("glTextureImage2DMultisampleCoverageNV");
        this.glTextureImage3DMultisampleCoverageNV = GLContext.getFunctionAddress("glTextureImage3DMultisampleCoverageNV");
        return bl & this.glTexImage3DMultisampleCoverageNV != 0L & this.glTextureImage2DMultisampleNV != 0L & this.glTextureImage3DMultisampleNV != 0L & this.glTextureImage2DMultisampleCoverageNV != 0L & this.glTextureImage3DMultisampleCoverageNV != 0L;
    }

    private boolean NV_transform_feedback_initNativeFunctionAddresses() {
        this.glBindBufferRangeNV = GLContext.getFunctionAddress("glBindBufferRangeNV");
        boolean bl = this.glBindBufferRangeNV != 0L;
        this.glBindBufferOffsetNV = GLContext.getFunctionAddress("glBindBufferOffsetNV");
        this.glBindBufferBaseNV = GLContext.getFunctionAddress("glBindBufferBaseNV");
        this.glTransformFeedbackAttribsNV = GLContext.getFunctionAddress("glTransformFeedbackAttribsNV");
        this.glTransformFeedbackVaryingsNV = GLContext.getFunctionAddress("glTransformFeedbackVaryingsNV");
        this.glBeginTransformFeedbackNV = GLContext.getFunctionAddress("glBeginTransformFeedbackNV");
        this.glEndTransformFeedbackNV = GLContext.getFunctionAddress("glEndTransformFeedbackNV");
        this.glGetVaryingLocationNV = GLContext.getFunctionAddress("glGetVaryingLocationNV");
        this.glGetActiveVaryingNV = GLContext.getFunctionAddress("glGetActiveVaryingNV");
        this.glActiveVaryingNV = GLContext.getFunctionAddress("glActiveVaryingNV");
        this.glGetTransformFeedbackVaryingNV = GLContext.getFunctionAddress("glGetTransformFeedbackVaryingNV");
        return bl & this.glBindBufferOffsetNV != 0L & this.glBindBufferBaseNV != 0L & this.glTransformFeedbackAttribsNV != 0L & this.glTransformFeedbackVaryingsNV != 0L & this.glBeginTransformFeedbackNV != 0L & this.glEndTransformFeedbackNV != 0L & this.glGetVaryingLocationNV != 0L & this.glGetActiveVaryingNV != 0L & this.glActiveVaryingNV != 0L & this.glGetTransformFeedbackVaryingNV != 0L;
    }

    private boolean NV_transform_feedback2_initNativeFunctionAddresses() {
        this.glBindTransformFeedbackNV = GLContext.getFunctionAddress("glBindTransformFeedbackNV");
        boolean bl = this.glBindTransformFeedbackNV != 0L;
        this.glDeleteTransformFeedbacksNV = GLContext.getFunctionAddress("glDeleteTransformFeedbacksNV");
        this.glGenTransformFeedbacksNV = GLContext.getFunctionAddress("glGenTransformFeedbacksNV");
        this.glIsTransformFeedbackNV = GLContext.getFunctionAddress("glIsTransformFeedbackNV");
        this.glPauseTransformFeedbackNV = GLContext.getFunctionAddress("glPauseTransformFeedbackNV");
        this.glResumeTransformFeedbackNV = GLContext.getFunctionAddress("glResumeTransformFeedbackNV");
        this.glDrawTransformFeedbackNV = GLContext.getFunctionAddress("glDrawTransformFeedbackNV");
        return bl & this.glDeleteTransformFeedbacksNV != 0L & this.glGenTransformFeedbacksNV != 0L & this.glIsTransformFeedbackNV != 0L & this.glPauseTransformFeedbackNV != 0L & this.glResumeTransformFeedbackNV != 0L & this.glDrawTransformFeedbackNV != 0L;
    }

    private boolean NV_vertex_array_range_initNativeFunctionAddresses() {
        this.glVertexArrayRangeNV = GLContext.getFunctionAddress("glVertexArrayRangeNV");
        boolean bl = this.glVertexArrayRangeNV != 0L;
        this.glFlushVertexArrayRangeNV = GLContext.getFunctionAddress("glFlushVertexArrayRangeNV");
        this.glAllocateMemoryNV = GLContext.getPlatformSpecificFunctionAddress("gl", new String[]{"Windows", "Linux"}, new String[]{"wgl", "glX"}, "glAllocateMemoryNV");
        this.glFreeMemoryNV = GLContext.getPlatformSpecificFunctionAddress("gl", new String[]{"Windows", "Linux"}, new String[]{"wgl", "glX"}, "glFreeMemoryNV");
        return bl & this.glFlushVertexArrayRangeNV != 0L & this.glAllocateMemoryNV != 0L & this.glFreeMemoryNV != 0L;
    }

    private boolean NV_vertex_attrib_integer_64bit_initNativeFunctionAddresses(Set<String> supported_extensions) {
        this.glVertexAttribL1i64NV = GLContext.getFunctionAddress("glVertexAttribL1i64NV");
        boolean bl = this.glVertexAttribL1i64NV != 0L;
        this.glVertexAttribL2i64NV = GLContext.getFunctionAddress("glVertexAttribL2i64NV");
        this.glVertexAttribL3i64NV = GLContext.getFunctionAddress("glVertexAttribL3i64NV");
        this.glVertexAttribL4i64NV = GLContext.getFunctionAddress("glVertexAttribL4i64NV");
        this.glVertexAttribL1i64vNV = GLContext.getFunctionAddress("glVertexAttribL1i64vNV");
        this.glVertexAttribL2i64vNV = GLContext.getFunctionAddress("glVertexAttribL2i64vNV");
        this.glVertexAttribL3i64vNV = GLContext.getFunctionAddress("glVertexAttribL3i64vNV");
        this.glVertexAttribL4i64vNV = GLContext.getFunctionAddress("glVertexAttribL4i64vNV");
        this.glVertexAttribL1ui64NV = GLContext.getFunctionAddress("glVertexAttribL1ui64NV");
        this.glVertexAttribL2ui64NV = GLContext.getFunctionAddress("glVertexAttribL2ui64NV");
        this.glVertexAttribL3ui64NV = GLContext.getFunctionAddress("glVertexAttribL3ui64NV");
        this.glVertexAttribL4ui64NV = GLContext.getFunctionAddress("glVertexAttribL4ui64NV");
        this.glVertexAttribL1ui64vNV = GLContext.getFunctionAddress("glVertexAttribL1ui64vNV");
        this.glVertexAttribL2ui64vNV = GLContext.getFunctionAddress("glVertexAttribL2ui64vNV");
        this.glVertexAttribL3ui64vNV = GLContext.getFunctionAddress("glVertexAttribL3ui64vNV");
        this.glVertexAttribL4ui64vNV = GLContext.getFunctionAddress("glVertexAttribL4ui64vNV");
        this.glGetVertexAttribLi64vNV = GLContext.getFunctionAddress("glGetVertexAttribLi64vNV");
        this.glGetVertexAttribLui64vNV = GLContext.getFunctionAddress("glGetVertexAttribLui64vNV");
        return bl & this.glVertexAttribL2i64NV != 0L & this.glVertexAttribL3i64NV != 0L & this.glVertexAttribL4i64NV != 0L & this.glVertexAttribL1i64vNV != 0L & this.glVertexAttribL2i64vNV != 0L & this.glVertexAttribL3i64vNV != 0L & this.glVertexAttribL4i64vNV != 0L & this.glVertexAttribL1ui64NV != 0L & this.glVertexAttribL2ui64NV != 0L & this.glVertexAttribL3ui64NV != 0L & this.glVertexAttribL4ui64NV != 0L & this.glVertexAttribL1ui64vNV != 0L & this.glVertexAttribL2ui64vNV != 0L & this.glVertexAttribL3ui64vNV != 0L & this.glVertexAttribL4ui64vNV != 0L & this.glGetVertexAttribLi64vNV != 0L & this.glGetVertexAttribLui64vNV != 0L & (!supported_extensions.contains("GL_NV_vertex_buffer_unified_memory") || (this.glVertexAttribLFormatNV = GLContext.getFunctionAddress("glVertexAttribLFormatNV")) != 0L);
    }

    private boolean NV_vertex_buffer_unified_memory_initNativeFunctionAddresses() {
        this.glBufferAddressRangeNV = GLContext.getFunctionAddress("glBufferAddressRangeNV");
        boolean bl = this.glBufferAddressRangeNV != 0L;
        this.glVertexFormatNV = GLContext.getFunctionAddress("glVertexFormatNV");
        this.glNormalFormatNV = GLContext.getFunctionAddress("glNormalFormatNV");
        this.glColorFormatNV = GLContext.getFunctionAddress("glColorFormatNV");
        this.glIndexFormatNV = GLContext.getFunctionAddress("glIndexFormatNV");
        this.glTexCoordFormatNV = GLContext.getFunctionAddress("glTexCoordFormatNV");
        this.glEdgeFlagFormatNV = GLContext.getFunctionAddress("glEdgeFlagFormatNV");
        this.glSecondaryColorFormatNV = GLContext.getFunctionAddress("glSecondaryColorFormatNV");
        this.glFogCoordFormatNV = GLContext.getFunctionAddress("glFogCoordFormatNV");
        this.glVertexAttribFormatNV = GLContext.getFunctionAddress("glVertexAttribFormatNV");
        this.glVertexAttribIFormatNV = GLContext.getFunctionAddress("glVertexAttribIFormatNV");
        this.glGetIntegerui64i_vNV = GLContext.getFunctionAddress("glGetIntegerui64i_vNV");
        return bl & this.glVertexFormatNV != 0L & this.glNormalFormatNV != 0L & this.glColorFormatNV != 0L & this.glIndexFormatNV != 0L & this.glTexCoordFormatNV != 0L & this.glEdgeFlagFormatNV != 0L & this.glSecondaryColorFormatNV != 0L & this.glFogCoordFormatNV != 0L & this.glVertexAttribFormatNV != 0L & this.glVertexAttribIFormatNV != 0L & this.glGetIntegerui64i_vNV != 0L;
    }

    private boolean NV_vertex_program_initNativeFunctionAddresses() {
        this.glExecuteProgramNV = GLContext.getFunctionAddress("glExecuteProgramNV");
        boolean bl = this.glExecuteProgramNV != 0L;
        this.glGetProgramParameterfvNV = GLContext.getFunctionAddress("glGetProgramParameterfvNV");
        this.glGetProgramParameterdvNV = GLContext.getFunctionAddress("glGetProgramParameterdvNV");
        this.glGetTrackMatrixivNV = GLContext.getFunctionAddress("glGetTrackMatrixivNV");
        this.glGetVertexAttribfvNV = GLContext.getFunctionAddress("glGetVertexAttribfvNV");
        this.glGetVertexAttribdvNV = GLContext.getFunctionAddress("glGetVertexAttribdvNV");
        this.glGetVertexAttribivNV = GLContext.getFunctionAddress("glGetVertexAttribivNV");
        this.glGetVertexAttribPointervNV = GLContext.getFunctionAddress("glGetVertexAttribPointervNV");
        this.glProgramParameter4fNV = GLContext.getFunctionAddress("glProgramParameter4fNV");
        this.glProgramParameter4dNV = GLContext.getFunctionAddress("glProgramParameter4dNV");
        this.glProgramParameters4fvNV = GLContext.getFunctionAddress("glProgramParameters4fvNV");
        this.glProgramParameters4dvNV = GLContext.getFunctionAddress("glProgramParameters4dvNV");
        this.glTrackMatrixNV = GLContext.getFunctionAddress("glTrackMatrixNV");
        this.glVertexAttribPointerNV = GLContext.getFunctionAddress("glVertexAttribPointerNV");
        this.glVertexAttrib1sNV = GLContext.getFunctionAddress("glVertexAttrib1sNV");
        this.glVertexAttrib1fNV = GLContext.getFunctionAddress("glVertexAttrib1fNV");
        this.glVertexAttrib1dNV = GLContext.getFunctionAddress("glVertexAttrib1dNV");
        this.glVertexAttrib2sNV = GLContext.getFunctionAddress("glVertexAttrib2sNV");
        this.glVertexAttrib2fNV = GLContext.getFunctionAddress("glVertexAttrib2fNV");
        this.glVertexAttrib2dNV = GLContext.getFunctionAddress("glVertexAttrib2dNV");
        this.glVertexAttrib3sNV = GLContext.getFunctionAddress("glVertexAttrib3sNV");
        this.glVertexAttrib3fNV = GLContext.getFunctionAddress("glVertexAttrib3fNV");
        this.glVertexAttrib3dNV = GLContext.getFunctionAddress("glVertexAttrib3dNV");
        this.glVertexAttrib4sNV = GLContext.getFunctionAddress("glVertexAttrib4sNV");
        this.glVertexAttrib4fNV = GLContext.getFunctionAddress("glVertexAttrib4fNV");
        this.glVertexAttrib4dNV = GLContext.getFunctionAddress("glVertexAttrib4dNV");
        this.glVertexAttrib4ubNV = GLContext.getFunctionAddress("glVertexAttrib4ubNV");
        this.glVertexAttribs1svNV = GLContext.getFunctionAddress("glVertexAttribs1svNV");
        this.glVertexAttribs1fvNV = GLContext.getFunctionAddress("glVertexAttribs1fvNV");
        this.glVertexAttribs1dvNV = GLContext.getFunctionAddress("glVertexAttribs1dvNV");
        this.glVertexAttribs2svNV = GLContext.getFunctionAddress("glVertexAttribs2svNV");
        this.glVertexAttribs2fvNV = GLContext.getFunctionAddress("glVertexAttribs2fvNV");
        this.glVertexAttribs2dvNV = GLContext.getFunctionAddress("glVertexAttribs2dvNV");
        this.glVertexAttribs3svNV = GLContext.getFunctionAddress("glVertexAttribs3svNV");
        this.glVertexAttribs3fvNV = GLContext.getFunctionAddress("glVertexAttribs3fvNV");
        this.glVertexAttribs3dvNV = GLContext.getFunctionAddress("glVertexAttribs3dvNV");
        this.glVertexAttribs4svNV = GLContext.getFunctionAddress("glVertexAttribs4svNV");
        this.glVertexAttribs4fvNV = GLContext.getFunctionAddress("glVertexAttribs4fvNV");
        this.glVertexAttribs4dvNV = GLContext.getFunctionAddress("glVertexAttribs4dvNV");
        return bl & this.glGetProgramParameterfvNV != 0L & this.glGetProgramParameterdvNV != 0L & this.glGetTrackMatrixivNV != 0L & this.glGetVertexAttribfvNV != 0L & this.glGetVertexAttribdvNV != 0L & this.glGetVertexAttribivNV != 0L & this.glGetVertexAttribPointervNV != 0L & this.glProgramParameter4fNV != 0L & this.glProgramParameter4dNV != 0L & this.glProgramParameters4fvNV != 0L & this.glProgramParameters4dvNV != 0L & this.glTrackMatrixNV != 0L & this.glVertexAttribPointerNV != 0L & this.glVertexAttrib1sNV != 0L & this.glVertexAttrib1fNV != 0L & this.glVertexAttrib1dNV != 0L & this.glVertexAttrib2sNV != 0L & this.glVertexAttrib2fNV != 0L & this.glVertexAttrib2dNV != 0L & this.glVertexAttrib3sNV != 0L & this.glVertexAttrib3fNV != 0L & this.glVertexAttrib3dNV != 0L & this.glVertexAttrib4sNV != 0L & this.glVertexAttrib4fNV != 0L & this.glVertexAttrib4dNV != 0L & this.glVertexAttrib4ubNV != 0L & this.glVertexAttribs1svNV != 0L & this.glVertexAttribs1fvNV != 0L & this.glVertexAttribs1dvNV != 0L & this.glVertexAttribs2svNV != 0L & this.glVertexAttribs2fvNV != 0L & this.glVertexAttribs2dvNV != 0L & this.glVertexAttribs3svNV != 0L & this.glVertexAttribs3fvNV != 0L & this.glVertexAttribs3dvNV != 0L & this.glVertexAttribs4svNV != 0L & this.glVertexAttribs4fvNV != 0L & this.glVertexAttribs4dvNV != 0L;
    }

    private boolean NV_video_capture_initNativeFunctionAddresses() {
        this.glBeginVideoCaptureNV = GLContext.getFunctionAddress("glBeginVideoCaptureNV");
        boolean bl = this.glBeginVideoCaptureNV != 0L;
        this.glBindVideoCaptureStreamBufferNV = GLContext.getFunctionAddress("glBindVideoCaptureStreamBufferNV");
        this.glBindVideoCaptureStreamTextureNV = GLContext.getFunctionAddress("glBindVideoCaptureStreamTextureNV");
        this.glEndVideoCaptureNV = GLContext.getFunctionAddress("glEndVideoCaptureNV");
        this.glGetVideoCaptureivNV = GLContext.getFunctionAddress("glGetVideoCaptureivNV");
        this.glGetVideoCaptureStreamivNV = GLContext.getFunctionAddress("glGetVideoCaptureStreamivNV");
        this.glGetVideoCaptureStreamfvNV = GLContext.getFunctionAddress("glGetVideoCaptureStreamfvNV");
        this.glGetVideoCaptureStreamdvNV = GLContext.getFunctionAddress("glGetVideoCaptureStreamdvNV");
        this.glVideoCaptureNV = GLContext.getFunctionAddress("glVideoCaptureNV");
        this.glVideoCaptureStreamParameterivNV = GLContext.getFunctionAddress("glVideoCaptureStreamParameterivNV");
        this.glVideoCaptureStreamParameterfvNV = GLContext.getFunctionAddress("glVideoCaptureStreamParameterfvNV");
        this.glVideoCaptureStreamParameterdvNV = GLContext.getFunctionAddress("glVideoCaptureStreamParameterdvNV");
        return bl & this.glBindVideoCaptureStreamBufferNV != 0L & this.glBindVideoCaptureStreamTextureNV != 0L & this.glEndVideoCaptureNV != 0L & this.glGetVideoCaptureivNV != 0L & this.glGetVideoCaptureStreamivNV != 0L & this.glGetVideoCaptureStreamfvNV != 0L & this.glGetVideoCaptureStreamdvNV != 0L & this.glVideoCaptureNV != 0L & this.glVideoCaptureStreamParameterivNV != 0L & this.glVideoCaptureStreamParameterfvNV != 0L & this.glVideoCaptureStreamParameterdvNV != 0L;
    }

    private static void remove(Set supported_extensions, String extension) {
        LWJGLUtil.log(extension + " was reported as available but an entry point is missing");
        supported_extensions.remove(extension);
    }

    private Set<String> initAllStubs(boolean forwardCompatible) throws LWJGLException {
        this.glGetError = GLContext.getFunctionAddress("glGetError");
        this.glGetString = GLContext.getFunctionAddress("glGetString");
        this.glGetIntegerv = GLContext.getFunctionAddress("glGetIntegerv");
        this.glGetStringi = GLContext.getFunctionAddress("glGetStringi");
        GLContext.setCapabilities(this);
        HashSet<String> supported_extensions = new HashSet<String>(256);
        int profileMask = GLContext.getSupportedExtensions(supported_extensions);
        if (supported_extensions.contains("OpenGL31") && !supported_extensions.contains("GL_ARB_compatibility") && (profileMask & 2) == 0) {
            forwardCompatible = true;
        }
        if (!this.GL11_initNativeFunctionAddresses(forwardCompatible)) {
            throw new LWJGLException("GL11 not supported");
        }
        if (supported_extensions.contains("GL_ARB_fragment_program")) {
            supported_extensions.add("GL_ARB_program");
        }
        if (supported_extensions.contains("GL_ARB_pixel_buffer_object")) {
            supported_extensions.add("GL_ARB_buffer_object");
        }
        if (supported_extensions.contains("GL_ARB_vertex_buffer_object")) {
            supported_extensions.add("GL_ARB_buffer_object");
        }
        if (supported_extensions.contains("GL_ARB_vertex_program")) {
            supported_extensions.add("GL_ARB_program");
        }
        if (supported_extensions.contains("GL_EXT_pixel_buffer_object")) {
            supported_extensions.add("GL_ARB_buffer_object");
        }
        if (supported_extensions.contains("GL_NV_fragment_program")) {
            supported_extensions.add("GL_NV_program");
        }
        if (supported_extensions.contains("GL_NV_vertex_program")) {
            supported_extensions.add("GL_NV_program");
        }
        if ((supported_extensions.contains("GL_AMD_debug_output") || supported_extensions.contains("GL_AMDX_debug_output")) && !this.AMD_debug_output_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "GL_AMDX_debug_output");
            ContextCapabilities.remove(supported_extensions, "GL_AMD_debug_output");
        }
        if (supported_extensions.contains("GL_AMD_draw_buffers_blend") && !this.AMD_draw_buffers_blend_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "GL_AMD_draw_buffers_blend");
        }
        if (supported_extensions.contains("GL_AMD_interleaved_elements") && !this.AMD_interleaved_elements_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "GL_AMD_interleaved_elements");
        }
        if (supported_extensions.contains("GL_AMD_multi_draw_indirect") && !this.AMD_multi_draw_indirect_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "GL_AMD_multi_draw_indirect");
        }
        if (supported_extensions.contains("GL_AMD_name_gen_delete") && !this.AMD_name_gen_delete_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "GL_AMD_name_gen_delete");
        }
        if (supported_extensions.contains("GL_AMD_performance_monitor") && !this.AMD_performance_monitor_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "GL_AMD_performance_monitor");
        }
        if (supported_extensions.contains("GL_AMD_sample_positions") && !this.AMD_sample_positions_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "GL_AMD_sample_positions");
        }
        if (supported_extensions.contains("GL_AMD_sparse_texture") && !this.AMD_sparse_texture_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "GL_AMD_sparse_texture");
        }
        if (supported_extensions.contains("GL_AMD_stencil_operation_extended") && !this.AMD_stencil_operation_extended_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "GL_AMD_stencil_operation_extended");
        }
        if (supported_extensions.contains("GL_AMD_vertex_shader_tessellator") && !this.AMD_vertex_shader_tessellator_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "GL_AMD_vertex_shader_tessellator");
        }
        if (supported_extensions.contains("GL_APPLE_element_array") && !this.APPLE_element_array_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "GL_APPLE_element_array");
        }
        if (supported_extensions.contains("GL_APPLE_fence") && !this.APPLE_fence_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "GL_APPLE_fence");
        }
        if (supported_extensions.contains("GL_APPLE_flush_buffer_range") && !this.APPLE_flush_buffer_range_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "GL_APPLE_flush_buffer_range");
        }
        if (supported_extensions.contains("GL_APPLE_object_purgeable") && !this.APPLE_object_purgeable_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "GL_APPLE_object_purgeable");
        }
        if (supported_extensions.contains("GL_APPLE_texture_range") && !this.APPLE_texture_range_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "GL_APPLE_texture_range");
        }
        if (supported_extensions.contains("GL_APPLE_vertex_array_object") && !this.APPLE_vertex_array_object_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "GL_APPLE_vertex_array_object");
        }
        if (supported_extensions.contains("GL_APPLE_vertex_array_range") && !this.APPLE_vertex_array_range_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "GL_APPLE_vertex_array_range");
        }
        if (supported_extensions.contains("GL_APPLE_vertex_program_evaluators") && !this.APPLE_vertex_program_evaluators_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "GL_APPLE_vertex_program_evaluators");
        }
        if (supported_extensions.contains("GL_ARB_ES2_compatibility") && !this.ARB_ES2_compatibility_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "GL_ARB_ES2_compatibility");
        }
        if (supported_extensions.contains("GL_ARB_ES3_1_compatibility") && !this.ARB_ES3_1_compatibility_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "GL_ARB_ES3_1_compatibility");
        }
        if (supported_extensions.contains("GL_ARB_base_instance") && !this.ARB_base_instance_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "GL_ARB_base_instance");
        }
        if (supported_extensions.contains("GL_ARB_bindless_texture") && !this.ARB_bindless_texture_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "GL_ARB_bindless_texture");
        }
        if (supported_extensions.contains("GL_ARB_blend_func_extended") && !this.ARB_blend_func_extended_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "GL_ARB_blend_func_extended");
        }
        if (supported_extensions.contains("GL_ARB_buffer_object") && !this.ARB_buffer_object_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "GL_ARB_buffer_object");
        }
        if (supported_extensions.contains("GL_ARB_buffer_storage") && !this.ARB_buffer_storage_initNativeFunctionAddresses(supported_extensions)) {
            ContextCapabilities.remove(supported_extensions, "GL_ARB_buffer_storage");
        }
        if (supported_extensions.contains("GL_ARB_cl_event") && !this.ARB_cl_event_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "GL_ARB_cl_event");
        }
        if (supported_extensions.contains("GL_ARB_clear_buffer_object") && !this.ARB_clear_buffer_object_initNativeFunctionAddresses(supported_extensions)) {
            ContextCapabilities.remove(supported_extensions, "GL_ARB_clear_buffer_object");
        }
        if (supported_extensions.contains("GL_ARB_clear_texture") && !this.ARB_clear_texture_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "GL_ARB_clear_texture");
        }
        if (supported_extensions.contains("GL_ARB_clip_control") && !this.ARB_clip_control_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "GL_ARB_clip_control");
        }
        if (supported_extensions.contains("GL_ARB_color_buffer_float") && !this.ARB_color_buffer_float_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "GL_ARB_color_buffer_float");
        }
        if (supported_extensions.contains("GL_ARB_compute_shader") && !this.ARB_compute_shader_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "GL_ARB_compute_shader");
        }
        if (supported_extensions.contains("GL_ARB_compute_variable_group_size") && !this.ARB_compute_variable_group_size_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "GL_ARB_compute_variable_group_size");
        }
        if (supported_extensions.contains("GL_ARB_copy_buffer") && !this.ARB_copy_buffer_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "GL_ARB_copy_buffer");
        }
        if (supported_extensions.contains("GL_ARB_copy_image") && !this.ARB_copy_image_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "GL_ARB_copy_image");
        }
        if (supported_extensions.contains("GL_ARB_debug_output") && !this.ARB_debug_output_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "GL_ARB_debug_output");
        }
        if (supported_extensions.contains("GL_ARB_direct_state_access") && !this.ARB_direct_state_access_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "GL_ARB_direct_state_access");
        }
        if (supported_extensions.contains("GL_ARB_draw_buffers") && !this.ARB_draw_buffers_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "GL_ARB_draw_buffers");
        }
        if (supported_extensions.contains("GL_ARB_draw_buffers_blend") && !this.ARB_draw_buffers_blend_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "GL_ARB_draw_buffers_blend");
        }
        if (supported_extensions.contains("GL_ARB_draw_elements_base_vertex") && !this.ARB_draw_elements_base_vertex_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "GL_ARB_draw_elements_base_vertex");
        }
        if (supported_extensions.contains("GL_ARB_draw_indirect") && !this.ARB_draw_indirect_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "GL_ARB_draw_indirect");
        }
        if (supported_extensions.contains("GL_ARB_draw_instanced") && !this.ARB_draw_instanced_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "GL_ARB_draw_instanced");
        }
        if (supported_extensions.contains("GL_ARB_framebuffer_no_attachments") && !this.ARB_framebuffer_no_attachments_initNativeFunctionAddresses(supported_extensions)) {
            ContextCapabilities.remove(supported_extensions, "GL_ARB_framebuffer_no_attachments");
        }
        if (supported_extensions.contains("GL_ARB_framebuffer_object") && !this.ARB_framebuffer_object_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "GL_ARB_framebuffer_object");
        }
        if (supported_extensions.contains("GL_ARB_geometry_shader4") && !this.ARB_geometry_shader4_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "GL_ARB_geometry_shader4");
        }
        if (supported_extensions.contains("GL_ARB_get_program_binary") && !this.ARB_get_program_binary_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "GL_ARB_get_program_binary");
        }
        if (supported_extensions.contains("GL_ARB_get_texture_sub_image") && !this.ARB_get_texture_sub_image_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "GL_ARB_get_texture_sub_image");
        }
        if (supported_extensions.contains("GL_ARB_gpu_shader_fp64") && !this.ARB_gpu_shader_fp64_initNativeFunctionAddresses(supported_extensions)) {
            ContextCapabilities.remove(supported_extensions, "GL_ARB_gpu_shader_fp64");
        }
        if (supported_extensions.contains("GL_ARB_imaging") && !this.ARB_imaging_initNativeFunctionAddresses(forwardCompatible)) {
            ContextCapabilities.remove(supported_extensions, "GL_ARB_imaging");
        }
        if (supported_extensions.contains("GL_ARB_indirect_parameters") && !this.ARB_indirect_parameters_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "GL_ARB_indirect_parameters");
        }
        if (supported_extensions.contains("GL_ARB_instanced_arrays") && !this.ARB_instanced_arrays_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "GL_ARB_instanced_arrays");
        }
        if (supported_extensions.contains("GL_ARB_internalformat_query") && !this.ARB_internalformat_query_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "GL_ARB_internalformat_query");
        }
        if (supported_extensions.contains("GL_ARB_internalformat_query2") && !this.ARB_internalformat_query2_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "GL_ARB_internalformat_query2");
        }
        if (supported_extensions.contains("GL_ARB_invalidate_subdata") && !this.ARB_invalidate_subdata_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "GL_ARB_invalidate_subdata");
        }
        if (supported_extensions.contains("GL_ARB_map_buffer_range") && !this.ARB_map_buffer_range_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "GL_ARB_map_buffer_range");
        }
        if (supported_extensions.contains("GL_ARB_matrix_palette") && !this.ARB_matrix_palette_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "GL_ARB_matrix_palette");
        }
        if (supported_extensions.contains("GL_ARB_multi_bind") && !this.ARB_multi_bind_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "GL_ARB_multi_bind");
        }
        if (supported_extensions.contains("GL_ARB_multi_draw_indirect") && !this.ARB_multi_draw_indirect_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "GL_ARB_multi_draw_indirect");
        }
        if (supported_extensions.contains("GL_ARB_multisample") && !this.ARB_multisample_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "GL_ARB_multisample");
        }
        if (supported_extensions.contains("GL_ARB_multitexture") && !this.ARB_multitexture_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "GL_ARB_multitexture");
        }
        if (supported_extensions.contains("GL_ARB_occlusion_query") && !this.ARB_occlusion_query_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "GL_ARB_occlusion_query");
        }
        if (supported_extensions.contains("GL_ARB_point_parameters") && !this.ARB_point_parameters_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "GL_ARB_point_parameters");
        }
        if (supported_extensions.contains("GL_ARB_program") && !this.ARB_program_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "GL_ARB_program");
        }
        if (supported_extensions.contains("GL_ARB_program_interface_query") && !this.ARB_program_interface_query_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "GL_ARB_program_interface_query");
        }
        if (supported_extensions.contains("GL_ARB_provoking_vertex") && !this.ARB_provoking_vertex_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "GL_ARB_provoking_vertex");
        }
        if (supported_extensions.contains("GL_ARB_robustness") && !this.ARB_robustness_initNativeFunctionAddresses(forwardCompatible, supported_extensions)) {
            ContextCapabilities.remove(supported_extensions, "GL_ARB_robustness");
        }
        if (supported_extensions.contains("GL_ARB_sample_shading") && !this.ARB_sample_shading_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "GL_ARB_sample_shading");
        }
        if (supported_extensions.contains("GL_ARB_sampler_objects") && !this.ARB_sampler_objects_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "GL_ARB_sampler_objects");
        }
        if (supported_extensions.contains("GL_ARB_separate_shader_objects") && !this.ARB_separate_shader_objects_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "GL_ARB_separate_shader_objects");
        }
        if (supported_extensions.contains("GL_ARB_shader_atomic_counters") && !this.ARB_shader_atomic_counters_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "GL_ARB_shader_atomic_counters");
        }
        if (supported_extensions.contains("GL_ARB_shader_image_load_store") && !this.ARB_shader_image_load_store_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "GL_ARB_shader_image_load_store");
        }
        if (supported_extensions.contains("GL_ARB_shader_objects") && !this.ARB_shader_objects_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "GL_ARB_shader_objects");
        }
        if (supported_extensions.contains("GL_ARB_shader_storage_buffer_object") && !this.ARB_shader_storage_buffer_object_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "GL_ARB_shader_storage_buffer_object");
        }
        if (supported_extensions.contains("GL_ARB_shader_subroutine") && !this.ARB_shader_subroutine_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "GL_ARB_shader_subroutine");
        }
        if (supported_extensions.contains("GL_ARB_shading_language_include") && !this.ARB_shading_language_include_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "GL_ARB_shading_language_include");
        }
        if (supported_extensions.contains("GL_ARB_sparse_buffer") && !this.ARB_sparse_buffer_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "GL_ARB_sparse_buffer");
        }
        if (supported_extensions.contains("GL_ARB_sparse_texture") && !this.ARB_sparse_texture_initNativeFunctionAddresses(supported_extensions)) {
            ContextCapabilities.remove(supported_extensions, "GL_ARB_sparse_texture");
        }
        if (supported_extensions.contains("GL_ARB_sync") && !this.ARB_sync_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "GL_ARB_sync");
        }
        if (supported_extensions.contains("GL_ARB_tessellation_shader") && !this.ARB_tessellation_shader_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "GL_ARB_tessellation_shader");
        }
        if (supported_extensions.contains("GL_ARB_texture_barrier") && !this.ARB_texture_barrier_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "GL_ARB_texture_barrier");
        }
        if (supported_extensions.contains("GL_ARB_texture_buffer_object") && !this.ARB_texture_buffer_object_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "GL_ARB_texture_buffer_object");
        }
        if (supported_extensions.contains("GL_ARB_texture_buffer_range") && !this.ARB_texture_buffer_range_initNativeFunctionAddresses(supported_extensions)) {
            ContextCapabilities.remove(supported_extensions, "GL_ARB_texture_buffer_range");
        }
        if (supported_extensions.contains("GL_ARB_texture_compression") && !this.ARB_texture_compression_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "GL_ARB_texture_compression");
        }
        if (supported_extensions.contains("GL_ARB_texture_multisample") && !this.ARB_texture_multisample_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "GL_ARB_texture_multisample");
        }
        if ((supported_extensions.contains("GL_ARB_texture_storage") || supported_extensions.contains("GL_EXT_texture_storage")) && !this.ARB_texture_storage_initNativeFunctionAddresses(supported_extensions)) {
            ContextCapabilities.remove(supported_extensions, "GL_EXT_texture_storage");
            ContextCapabilities.remove(supported_extensions, "GL_ARB_texture_storage");
        }
        if (supported_extensions.contains("GL_ARB_texture_storage_multisample") && !this.ARB_texture_storage_multisample_initNativeFunctionAddresses(supported_extensions)) {
            ContextCapabilities.remove(supported_extensions, "GL_ARB_texture_storage_multisample");
        }
        if (supported_extensions.contains("GL_ARB_texture_view") && !this.ARB_texture_view_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "GL_ARB_texture_view");
        }
        if (supported_extensions.contains("GL_ARB_timer_query") && !this.ARB_timer_query_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "GL_ARB_timer_query");
        }
        if (supported_extensions.contains("GL_ARB_transform_feedback2") && !this.ARB_transform_feedback2_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "GL_ARB_transform_feedback2");
        }
        if (supported_extensions.contains("GL_ARB_transform_feedback3") && !this.ARB_transform_feedback3_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "GL_ARB_transform_feedback3");
        }
        if (supported_extensions.contains("GL_ARB_transform_feedback_instanced") && !this.ARB_transform_feedback_instanced_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "GL_ARB_transform_feedback_instanced");
        }
        if (supported_extensions.contains("GL_ARB_transpose_matrix") && !this.ARB_transpose_matrix_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "GL_ARB_transpose_matrix");
        }
        if (supported_extensions.contains("GL_ARB_uniform_buffer_object") && !this.ARB_uniform_buffer_object_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "GL_ARB_uniform_buffer_object");
        }
        if (supported_extensions.contains("GL_ARB_vertex_array_object") && !this.ARB_vertex_array_object_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "GL_ARB_vertex_array_object");
        }
        if (supported_extensions.contains("GL_ARB_vertex_attrib_64bit") && !this.ARB_vertex_attrib_64bit_initNativeFunctionAddresses(supported_extensions)) {
            ContextCapabilities.remove(supported_extensions, "GL_ARB_vertex_attrib_64bit");
        }
        if (supported_extensions.contains("GL_ARB_vertex_attrib_binding") && !this.ARB_vertex_attrib_binding_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "GL_ARB_vertex_attrib_binding");
        }
        if (supported_extensions.contains("GL_ARB_vertex_blend") && !this.ARB_vertex_blend_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "GL_ARB_vertex_blend");
        }
        if (supported_extensions.contains("GL_ARB_vertex_program") && !this.ARB_vertex_program_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "GL_ARB_vertex_program");
        }
        if (supported_extensions.contains("GL_ARB_vertex_shader") && !this.ARB_vertex_shader_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "GL_ARB_vertex_shader");
        }
        if (supported_extensions.contains("GL_ARB_vertex_type_2_10_10_10_rev") && !this.ARB_vertex_type_2_10_10_10_rev_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "GL_ARB_vertex_type_2_10_10_10_rev");
        }
        if (supported_extensions.contains("GL_ARB_viewport_array") && !this.ARB_viewport_array_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "GL_ARB_viewport_array");
        }
        if (supported_extensions.contains("GL_ARB_window_pos") && !this.ARB_window_pos_initNativeFunctionAddresses(forwardCompatible)) {
            ContextCapabilities.remove(supported_extensions, "GL_ARB_window_pos");
        }
        if (supported_extensions.contains("GL_ATI_draw_buffers") && !this.ATI_draw_buffers_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "GL_ATI_draw_buffers");
        }
        if (supported_extensions.contains("GL_ATI_element_array") && !this.ATI_element_array_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "GL_ATI_element_array");
        }
        if (supported_extensions.contains("GL_ATI_envmap_bumpmap") && !this.ATI_envmap_bumpmap_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "GL_ATI_envmap_bumpmap");
        }
        if (supported_extensions.contains("GL_ATI_fragment_shader") && !this.ATI_fragment_shader_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "GL_ATI_fragment_shader");
        }
        if (supported_extensions.contains("GL_ATI_map_object_buffer") && !this.ATI_map_object_buffer_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "GL_ATI_map_object_buffer");
        }
        if (supported_extensions.contains("GL_ATI_pn_triangles") && !this.ATI_pn_triangles_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "GL_ATI_pn_triangles");
        }
        if (supported_extensions.contains("GL_ATI_separate_stencil") && !this.ATI_separate_stencil_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "GL_ATI_separate_stencil");
        }
        if (supported_extensions.contains("GL_ATI_vertex_array_object") && !this.ATI_vertex_array_object_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "GL_ATI_vertex_array_object");
        }
        if (supported_extensions.contains("GL_ATI_vertex_attrib_array_object") && !this.ATI_vertex_attrib_array_object_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "GL_ATI_vertex_attrib_array_object");
        }
        if (supported_extensions.contains("GL_ATI_vertex_streams") && !this.ATI_vertex_streams_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "GL_ATI_vertex_streams");
        }
        if (supported_extensions.contains("GL_EXT_bindable_uniform") && !this.EXT_bindable_uniform_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "GL_EXT_bindable_uniform");
        }
        if (supported_extensions.contains("GL_EXT_blend_color") && !this.EXT_blend_color_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "GL_EXT_blend_color");
        }
        if (supported_extensions.contains("GL_EXT_blend_equation_separate") && !this.EXT_blend_equation_separate_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "GL_EXT_blend_equation_separate");
        }
        if (supported_extensions.contains("GL_EXT_blend_func_separate") && !this.EXT_blend_func_separate_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "GL_EXT_blend_func_separate");
        }
        if (supported_extensions.contains("GL_EXT_blend_minmax") && !this.EXT_blend_minmax_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "GL_EXT_blend_minmax");
        }
        if (supported_extensions.contains("GL_EXT_compiled_vertex_array") && !this.EXT_compiled_vertex_array_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "GL_EXT_compiled_vertex_array");
        }
        if (supported_extensions.contains("GL_EXT_depth_bounds_test") && !this.EXT_depth_bounds_test_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "GL_EXT_depth_bounds_test");
        }
        supported_extensions.add("GL_EXT_direct_state_access");
        if (supported_extensions.contains("GL_EXT_direct_state_access") && !this.EXT_direct_state_access_initNativeFunctionAddresses(forwardCompatible, supported_extensions)) {
            ContextCapabilities.remove(supported_extensions, "GL_EXT_direct_state_access");
        }
        if (supported_extensions.contains("GL_EXT_draw_buffers2") && !this.EXT_draw_buffers2_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "GL_EXT_draw_buffers2");
        }
        if (supported_extensions.contains("GL_EXT_draw_instanced") && !this.EXT_draw_instanced_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "GL_EXT_draw_instanced");
        }
        if (supported_extensions.contains("GL_EXT_draw_range_elements") && !this.EXT_draw_range_elements_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "GL_EXT_draw_range_elements");
        }
        if (supported_extensions.contains("GL_EXT_fog_coord") && !this.EXT_fog_coord_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "GL_EXT_fog_coord");
        }
        if (supported_extensions.contains("GL_EXT_framebuffer_blit") && !this.EXT_framebuffer_blit_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "GL_EXT_framebuffer_blit");
        }
        if (supported_extensions.contains("GL_EXT_framebuffer_multisample") && !this.EXT_framebuffer_multisample_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "GL_EXT_framebuffer_multisample");
        }
        if (supported_extensions.contains("GL_EXT_framebuffer_object") && !this.EXT_framebuffer_object_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "GL_EXT_framebuffer_object");
        }
        if (supported_extensions.contains("GL_EXT_geometry_shader4") && !this.EXT_geometry_shader4_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "GL_EXT_geometry_shader4");
        }
        if (supported_extensions.contains("GL_EXT_gpu_program_parameters") && !this.EXT_gpu_program_parameters_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "GL_EXT_gpu_program_parameters");
        }
        if (supported_extensions.contains("GL_EXT_gpu_shader4") && !this.EXT_gpu_shader4_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "GL_EXT_gpu_shader4");
        }
        if (supported_extensions.contains("GL_EXT_multi_draw_arrays") && !this.EXT_multi_draw_arrays_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "GL_EXT_multi_draw_arrays");
        }
        if (supported_extensions.contains("GL_EXT_paletted_texture") && !this.EXT_paletted_texture_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "GL_EXT_paletted_texture");
        }
        if (supported_extensions.contains("GL_EXT_point_parameters") && !this.EXT_point_parameters_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "GL_EXT_point_parameters");
        }
        if (supported_extensions.contains("GL_EXT_provoking_vertex") && !this.EXT_provoking_vertex_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "GL_EXT_provoking_vertex");
        }
        if (supported_extensions.contains("GL_EXT_secondary_color") && !this.EXT_secondary_color_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "GL_EXT_secondary_color");
        }
        if (supported_extensions.contains("GL_EXT_separate_shader_objects") && !this.EXT_separate_shader_objects_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "GL_EXT_separate_shader_objects");
        }
        if (supported_extensions.contains("GL_EXT_shader_image_load_store") && !this.EXT_shader_image_load_store_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "GL_EXT_shader_image_load_store");
        }
        if (supported_extensions.contains("GL_EXT_stencil_clear_tag") && !this.EXT_stencil_clear_tag_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "GL_EXT_stencil_clear_tag");
        }
        if (supported_extensions.contains("GL_EXT_stencil_two_side") && !this.EXT_stencil_two_side_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "GL_EXT_stencil_two_side");
        }
        if (supported_extensions.contains("GL_EXT_texture_array") && !this.EXT_texture_array_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "GL_EXT_texture_array");
        }
        if (supported_extensions.contains("GL_EXT_texture_buffer_object") && !this.EXT_texture_buffer_object_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "GL_EXT_texture_buffer_object");
        }
        if (supported_extensions.contains("GL_EXT_texture_integer") && !this.EXT_texture_integer_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "GL_EXT_texture_integer");
        }
        if (supported_extensions.contains("GL_EXT_timer_query") && !this.EXT_timer_query_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "GL_EXT_timer_query");
        }
        if (supported_extensions.contains("GL_EXT_transform_feedback") && !this.EXT_transform_feedback_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "GL_EXT_transform_feedback");
        }
        if (supported_extensions.contains("GL_EXT_vertex_attrib_64bit") && !this.EXT_vertex_attrib_64bit_initNativeFunctionAddresses(supported_extensions)) {
            ContextCapabilities.remove(supported_extensions, "GL_EXT_vertex_attrib_64bit");
        }
        if (supported_extensions.contains("GL_EXT_vertex_shader") && !this.EXT_vertex_shader_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "GL_EXT_vertex_shader");
        }
        if (supported_extensions.contains("GL_EXT_vertex_weighting") && !this.EXT_vertex_weighting_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "GL_EXT_vertex_weighting");
        }
        if (supported_extensions.contains("OpenGL12") && !this.GL12_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "OpenGL12");
        }
        if (supported_extensions.contains("OpenGL13") && !this.GL13_initNativeFunctionAddresses(forwardCompatible)) {
            ContextCapabilities.remove(supported_extensions, "OpenGL13");
        }
        if (supported_extensions.contains("OpenGL14") && !this.GL14_initNativeFunctionAddresses(forwardCompatible)) {
            ContextCapabilities.remove(supported_extensions, "OpenGL14");
        }
        if (supported_extensions.contains("OpenGL15") && !this.GL15_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "OpenGL15");
        }
        if (supported_extensions.contains("OpenGL20") && !this.GL20_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "OpenGL20");
        }
        if (supported_extensions.contains("OpenGL21") && !this.GL21_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "OpenGL21");
        }
        if (supported_extensions.contains("OpenGL30") && !this.GL30_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "OpenGL30");
        }
        if (supported_extensions.contains("OpenGL31") && !this.GL31_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "OpenGL31");
        }
        if (supported_extensions.contains("OpenGL32") && !this.GL32_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "OpenGL32");
        }
        if (supported_extensions.contains("OpenGL33") && !this.GL33_initNativeFunctionAddresses(forwardCompatible)) {
            ContextCapabilities.remove(supported_extensions, "OpenGL33");
        }
        if (supported_extensions.contains("OpenGL40") && !this.GL40_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "OpenGL40");
        }
        if (supported_extensions.contains("OpenGL41") && !this.GL41_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "OpenGL41");
        }
        if (supported_extensions.contains("OpenGL42") && !this.GL42_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "OpenGL42");
        }
        if (supported_extensions.contains("OpenGL43") && !this.GL43_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "OpenGL43");
        }
        if (supported_extensions.contains("OpenGL44") && !this.GL44_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "OpenGL44");
        }
        if (supported_extensions.contains("OpenGL45") && !this.GL45_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "OpenGL45");
        }
        if (supported_extensions.contains("GL_GREMEDY_frame_terminator") && !this.GREMEDY_frame_terminator_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "GL_GREMEDY_frame_terminator");
        }
        if (supported_extensions.contains("GL_GREMEDY_string_marker") && !this.GREMEDY_string_marker_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "GL_GREMEDY_string_marker");
        }
        if (supported_extensions.contains("GL_INTEL_map_texture") && !this.INTEL_map_texture_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "GL_INTEL_map_texture");
        }
        if (supported_extensions.contains("GL_KHR_debug") && !this.KHR_debug_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "GL_KHR_debug");
        }
        if (supported_extensions.contains("GL_KHR_robustness") && !this.KHR_robustness_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "GL_KHR_robustness");
        }
        if (supported_extensions.contains("GL_NV_bindless_multi_draw_indirect") && !this.NV_bindless_multi_draw_indirect_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "GL_NV_bindless_multi_draw_indirect");
        }
        if (supported_extensions.contains("GL_NV_bindless_texture") && !this.NV_bindless_texture_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "GL_NV_bindless_texture");
        }
        if (supported_extensions.contains("GL_NV_blend_equation_advanced") && !this.NV_blend_equation_advanced_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "GL_NV_blend_equation_advanced");
        }
        if (supported_extensions.contains("GL_NV_conditional_render") && !this.NV_conditional_render_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "GL_NV_conditional_render");
        }
        if (supported_extensions.contains("GL_NV_copy_image") && !this.NV_copy_image_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "GL_NV_copy_image");
        }
        if (supported_extensions.contains("GL_NV_depth_buffer_float") && !this.NV_depth_buffer_float_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "GL_NV_depth_buffer_float");
        }
        if (supported_extensions.contains("GL_NV_draw_texture") && !this.NV_draw_texture_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "GL_NV_draw_texture");
        }
        if (supported_extensions.contains("GL_NV_evaluators") && !this.NV_evaluators_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "GL_NV_evaluators");
        }
        if (supported_extensions.contains("GL_NV_explicit_multisample") && !this.NV_explicit_multisample_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "GL_NV_explicit_multisample");
        }
        if (supported_extensions.contains("GL_NV_fence") && !this.NV_fence_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "GL_NV_fence");
        }
        if (supported_extensions.contains("GL_NV_fragment_program") && !this.NV_fragment_program_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "GL_NV_fragment_program");
        }
        if (supported_extensions.contains("GL_NV_framebuffer_multisample_coverage") && !this.NV_framebuffer_multisample_coverage_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "GL_NV_framebuffer_multisample_coverage");
        }
        if (supported_extensions.contains("GL_NV_geometry_program4") && !this.NV_geometry_program4_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "GL_NV_geometry_program4");
        }
        if (supported_extensions.contains("GL_NV_gpu_program4") && !this.NV_gpu_program4_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "GL_NV_gpu_program4");
        }
        if (supported_extensions.contains("GL_NV_gpu_shader5") && !this.NV_gpu_shader5_initNativeFunctionAddresses(supported_extensions)) {
            ContextCapabilities.remove(supported_extensions, "GL_NV_gpu_shader5");
        }
        if (supported_extensions.contains("GL_NV_half_float") && !this.NV_half_float_initNativeFunctionAddresses(supported_extensions)) {
            ContextCapabilities.remove(supported_extensions, "GL_NV_half_float");
        }
        if (supported_extensions.contains("GL_NV_occlusion_query") && !this.NV_occlusion_query_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "GL_NV_occlusion_query");
        }
        if (supported_extensions.contains("GL_NV_parameter_buffer_object") && !this.NV_parameter_buffer_object_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "GL_NV_parameter_buffer_object");
        }
        if (supported_extensions.contains("GL_NV_path_rendering") && !this.NV_path_rendering_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "GL_NV_path_rendering");
        }
        if (supported_extensions.contains("GL_NV_pixel_data_range") && !this.NV_pixel_data_range_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "GL_NV_pixel_data_range");
        }
        if (supported_extensions.contains("GL_NV_point_sprite") && !this.NV_point_sprite_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "GL_NV_point_sprite");
        }
        if (supported_extensions.contains("GL_NV_present_video") && !this.NV_present_video_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "GL_NV_present_video");
        }
        supported_extensions.add("GL_NV_primitive_restart");
        if (supported_extensions.contains("GL_NV_primitive_restart") && !this.NV_primitive_restart_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "GL_NV_primitive_restart");
        }
        if (supported_extensions.contains("GL_NV_program") && !this.NV_program_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "GL_NV_program");
        }
        if (supported_extensions.contains("GL_NV_register_combiners") && !this.NV_register_combiners_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "GL_NV_register_combiners");
        }
        if (supported_extensions.contains("GL_NV_register_combiners2") && !this.NV_register_combiners2_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "GL_NV_register_combiners2");
        }
        if (supported_extensions.contains("GL_NV_shader_buffer_load") && !this.NV_shader_buffer_load_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "GL_NV_shader_buffer_load");
        }
        if (supported_extensions.contains("GL_NV_texture_barrier") && !this.NV_texture_barrier_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "GL_NV_texture_barrier");
        }
        if (supported_extensions.contains("GL_NV_texture_multisample") && !this.NV_texture_multisample_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "GL_NV_texture_multisample");
        }
        if (supported_extensions.contains("GL_NV_transform_feedback") && !this.NV_transform_feedback_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "GL_NV_transform_feedback");
        }
        if (supported_extensions.contains("GL_NV_transform_feedback2") && !this.NV_transform_feedback2_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "GL_NV_transform_feedback2");
        }
        if (supported_extensions.contains("GL_NV_vertex_array_range") && !this.NV_vertex_array_range_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "GL_NV_vertex_array_range");
        }
        if (supported_extensions.contains("GL_NV_vertex_attrib_integer_64bit") && !this.NV_vertex_attrib_integer_64bit_initNativeFunctionAddresses(supported_extensions)) {
            ContextCapabilities.remove(supported_extensions, "GL_NV_vertex_attrib_integer_64bit");
        }
        if (supported_extensions.contains("GL_NV_vertex_buffer_unified_memory") && !this.NV_vertex_buffer_unified_memory_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "GL_NV_vertex_buffer_unified_memory");
        }
        if (supported_extensions.contains("GL_NV_vertex_program") && !this.NV_vertex_program_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "GL_NV_vertex_program");
        }
        if (supported_extensions.contains("GL_NV_video_capture") && !this.NV_video_capture_initNativeFunctionAddresses()) {
            ContextCapabilities.remove(supported_extensions, "GL_NV_video_capture");
        }
        return supported_extensions;
    }

    static void unloadAllStubs() {
    }

    ContextCapabilities(boolean forwardCompatible) throws LWJGLException {
        Set<String> supported_extensions = this.initAllStubs(forwardCompatible);
        this.GL_AMD_blend_minmax_factor = supported_extensions.contains("GL_AMD_blend_minmax_factor");
        this.GL_AMD_conservative_depth = supported_extensions.contains("GL_AMD_conservative_depth");
        this.GL_AMD_debug_output = supported_extensions.contains("GL_AMD_debug_output") || supported_extensions.contains("GL_AMDX_debug_output");
        this.GL_AMD_depth_clamp_separate = supported_extensions.contains("GL_AMD_depth_clamp_separate");
        this.GL_AMD_draw_buffers_blend = supported_extensions.contains("GL_AMD_draw_buffers_blend");
        this.GL_AMD_interleaved_elements = supported_extensions.contains("GL_AMD_interleaved_elements");
        this.GL_AMD_multi_draw_indirect = supported_extensions.contains("GL_AMD_multi_draw_indirect");
        this.GL_AMD_name_gen_delete = supported_extensions.contains("GL_AMD_name_gen_delete");
        this.GL_AMD_performance_monitor = supported_extensions.contains("GL_AMD_performance_monitor");
        this.GL_AMD_pinned_memory = supported_extensions.contains("GL_AMD_pinned_memory");
        this.GL_AMD_query_buffer_object = supported_extensions.contains("GL_AMD_query_buffer_object");
        this.GL_AMD_sample_positions = supported_extensions.contains("GL_AMD_sample_positions");
        this.GL_AMD_seamless_cubemap_per_texture = supported_extensions.contains("GL_AMD_seamless_cubemap_per_texture");
        this.GL_AMD_shader_atomic_counter_ops = supported_extensions.contains("GL_AMD_shader_atomic_counter_ops");
        this.GL_AMD_shader_stencil_export = supported_extensions.contains("GL_AMD_shader_stencil_export");
        this.GL_AMD_shader_trinary_minmax = supported_extensions.contains("GL_AMD_shader_trinary_minmax");
        this.GL_AMD_sparse_texture = supported_extensions.contains("GL_AMD_sparse_texture");
        this.GL_AMD_stencil_operation_extended = supported_extensions.contains("GL_AMD_stencil_operation_extended");
        this.GL_AMD_texture_texture4 = supported_extensions.contains("GL_AMD_texture_texture4");
        this.GL_AMD_transform_feedback3_lines_triangles = supported_extensions.contains("GL_AMD_transform_feedback3_lines_triangles");
        this.GL_AMD_vertex_shader_layer = supported_extensions.contains("GL_AMD_vertex_shader_layer");
        this.GL_AMD_vertex_shader_tessellator = supported_extensions.contains("GL_AMD_vertex_shader_tessellator");
        this.GL_AMD_vertex_shader_viewport_index = supported_extensions.contains("GL_AMD_vertex_shader_viewport_index");
        this.GL_APPLE_aux_depth_stencil = supported_extensions.contains("GL_APPLE_aux_depth_stencil");
        this.GL_APPLE_client_storage = supported_extensions.contains("GL_APPLE_client_storage");
        this.GL_APPLE_element_array = supported_extensions.contains("GL_APPLE_element_array");
        this.GL_APPLE_fence = supported_extensions.contains("GL_APPLE_fence");
        this.GL_APPLE_float_pixels = supported_extensions.contains("GL_APPLE_float_pixels");
        this.GL_APPLE_flush_buffer_range = supported_extensions.contains("GL_APPLE_flush_buffer_range");
        this.GL_APPLE_object_purgeable = supported_extensions.contains("GL_APPLE_object_purgeable");
        this.GL_APPLE_packed_pixels = supported_extensions.contains("GL_APPLE_packed_pixels");
        this.GL_APPLE_rgb_422 = supported_extensions.contains("GL_APPLE_rgb_422");
        this.GL_APPLE_row_bytes = supported_extensions.contains("GL_APPLE_row_bytes");
        this.GL_APPLE_texture_range = supported_extensions.contains("GL_APPLE_texture_range");
        this.GL_APPLE_vertex_array_object = supported_extensions.contains("GL_APPLE_vertex_array_object");
        this.GL_APPLE_vertex_array_range = supported_extensions.contains("GL_APPLE_vertex_array_range");
        this.GL_APPLE_vertex_program_evaluators = supported_extensions.contains("GL_APPLE_vertex_program_evaluators");
        this.GL_APPLE_ycbcr_422 = supported_extensions.contains("GL_APPLE_ycbcr_422");
        this.GL_ARB_ES2_compatibility = supported_extensions.contains("GL_ARB_ES2_compatibility");
        this.GL_ARB_ES3_1_compatibility = supported_extensions.contains("GL_ARB_ES3_1_compatibility");
        this.GL_ARB_ES3_compatibility = supported_extensions.contains("GL_ARB_ES3_compatibility");
        this.GL_ARB_arrays_of_arrays = supported_extensions.contains("GL_ARB_arrays_of_arrays");
        this.GL_ARB_base_instance = supported_extensions.contains("GL_ARB_base_instance");
        this.GL_ARB_bindless_texture = supported_extensions.contains("GL_ARB_bindless_texture");
        this.GL_ARB_blend_func_extended = supported_extensions.contains("GL_ARB_blend_func_extended");
        this.GL_ARB_buffer_storage = supported_extensions.contains("GL_ARB_buffer_storage");
        this.GL_ARB_cl_event = supported_extensions.contains("GL_ARB_cl_event");
        this.GL_ARB_clear_buffer_object = supported_extensions.contains("GL_ARB_clear_buffer_object");
        this.GL_ARB_clear_texture = supported_extensions.contains("GL_ARB_clear_texture");
        this.GL_ARB_clip_control = supported_extensions.contains("GL_ARB_clip_control");
        this.GL_ARB_color_buffer_float = supported_extensions.contains("GL_ARB_color_buffer_float");
        this.GL_ARB_compatibility = supported_extensions.contains("GL_ARB_compatibility");
        this.GL_ARB_compressed_texture_pixel_storage = supported_extensions.contains("GL_ARB_compressed_texture_pixel_storage");
        this.GL_ARB_compute_shader = supported_extensions.contains("GL_ARB_compute_shader");
        this.GL_ARB_compute_variable_group_size = supported_extensions.contains("GL_ARB_compute_variable_group_size");
        this.GL_ARB_conditional_render_inverted = supported_extensions.contains("GL_ARB_conditional_render_inverted");
        this.GL_ARB_conservative_depth = supported_extensions.contains("GL_ARB_conservative_depth");
        this.GL_ARB_copy_buffer = supported_extensions.contains("GL_ARB_copy_buffer");
        this.GL_ARB_copy_image = supported_extensions.contains("GL_ARB_copy_image");
        this.GL_ARB_cull_distance = supported_extensions.contains("GL_ARB_cull_distance");
        this.GL_ARB_debug_output = supported_extensions.contains("GL_ARB_debug_output");
        this.GL_ARB_depth_buffer_float = supported_extensions.contains("GL_ARB_depth_buffer_float");
        this.GL_ARB_depth_clamp = supported_extensions.contains("GL_ARB_depth_clamp");
        this.GL_ARB_depth_texture = supported_extensions.contains("GL_ARB_depth_texture");
        this.GL_ARB_derivative_control = supported_extensions.contains("GL_ARB_derivative_control");
        this.GL_ARB_direct_state_access = supported_extensions.contains("GL_ARB_direct_state_access");
        this.GL_ARB_draw_buffers = supported_extensions.contains("GL_ARB_draw_buffers");
        this.GL_ARB_draw_buffers_blend = supported_extensions.contains("GL_ARB_draw_buffers_blend");
        this.GL_ARB_draw_elements_base_vertex = supported_extensions.contains("GL_ARB_draw_elements_base_vertex");
        this.GL_ARB_draw_indirect = supported_extensions.contains("GL_ARB_draw_indirect");
        this.GL_ARB_draw_instanced = supported_extensions.contains("GL_ARB_draw_instanced");
        this.GL_ARB_enhanced_layouts = supported_extensions.contains("GL_ARB_enhanced_layouts");
        this.GL_ARB_explicit_attrib_location = supported_extensions.contains("GL_ARB_explicit_attrib_location");
        this.GL_ARB_explicit_uniform_location = supported_extensions.contains("GL_ARB_explicit_uniform_location");
        this.GL_ARB_fragment_coord_conventions = supported_extensions.contains("GL_ARB_fragment_coord_conventions");
        this.GL_ARB_fragment_layer_viewport = supported_extensions.contains("GL_ARB_fragment_layer_viewport");
        this.GL_ARB_fragment_program = supported_extensions.contains("GL_ARB_fragment_program") && supported_extensions.contains("GL_ARB_program");
        this.GL_ARB_fragment_program_shadow = supported_extensions.contains("GL_ARB_fragment_program_shadow");
        this.GL_ARB_fragment_shader = supported_extensions.contains("GL_ARB_fragment_shader");
        this.GL_ARB_framebuffer_no_attachments = supported_extensions.contains("GL_ARB_framebuffer_no_attachments");
        this.GL_ARB_framebuffer_object = supported_extensions.contains("GL_ARB_framebuffer_object");
        this.GL_ARB_framebuffer_sRGB = supported_extensions.contains("GL_ARB_framebuffer_sRGB");
        this.GL_ARB_geometry_shader4 = supported_extensions.contains("GL_ARB_geometry_shader4");
        this.GL_ARB_get_program_binary = supported_extensions.contains("GL_ARB_get_program_binary");
        this.GL_ARB_get_texture_sub_image = supported_extensions.contains("GL_ARB_get_texture_sub_image");
        this.GL_ARB_gpu_shader5 = supported_extensions.contains("GL_ARB_gpu_shader5");
        this.GL_ARB_gpu_shader_fp64 = supported_extensions.contains("GL_ARB_gpu_shader_fp64");
        this.GL_ARB_half_float_pixel = supported_extensions.contains("GL_ARB_half_float_pixel");
        this.GL_ARB_half_float_vertex = supported_extensions.contains("GL_ARB_half_float_vertex");
        this.GL_ARB_imaging = supported_extensions.contains("GL_ARB_imaging");
        this.GL_ARB_indirect_parameters = supported_extensions.contains("GL_ARB_indirect_parameters");
        this.GL_ARB_instanced_arrays = supported_extensions.contains("GL_ARB_instanced_arrays");
        this.GL_ARB_internalformat_query = supported_extensions.contains("GL_ARB_internalformat_query");
        this.GL_ARB_internalformat_query2 = supported_extensions.contains("GL_ARB_internalformat_query2");
        this.GL_ARB_invalidate_subdata = supported_extensions.contains("GL_ARB_invalidate_subdata");
        this.GL_ARB_map_buffer_alignment = supported_extensions.contains("GL_ARB_map_buffer_alignment");
        this.GL_ARB_map_buffer_range = supported_extensions.contains("GL_ARB_map_buffer_range");
        this.GL_ARB_matrix_palette = supported_extensions.contains("GL_ARB_matrix_palette");
        this.GL_ARB_multi_bind = supported_extensions.contains("GL_ARB_multi_bind");
        this.GL_ARB_multi_draw_indirect = supported_extensions.contains("GL_ARB_multi_draw_indirect");
        this.GL_ARB_multisample = supported_extensions.contains("GL_ARB_multisample");
        this.GL_ARB_multitexture = supported_extensions.contains("GL_ARB_multitexture");
        this.GL_ARB_occlusion_query = supported_extensions.contains("GL_ARB_occlusion_query");
        this.GL_ARB_occlusion_query2 = supported_extensions.contains("GL_ARB_occlusion_query2");
        this.GL_ARB_pipeline_statistics_query = supported_extensions.contains("GL_ARB_pipeline_statistics_query");
        this.GL_ARB_pixel_buffer_object = supported_extensions.contains("GL_ARB_pixel_buffer_object") && supported_extensions.contains("GL_ARB_buffer_object");
        this.GL_ARB_point_parameters = supported_extensions.contains("GL_ARB_point_parameters");
        this.GL_ARB_point_sprite = supported_extensions.contains("GL_ARB_point_sprite");
        this.GL_ARB_program_interface_query = supported_extensions.contains("GL_ARB_program_interface_query");
        this.GL_ARB_provoking_vertex = supported_extensions.contains("GL_ARB_provoking_vertex");
        this.GL_ARB_query_buffer_object = supported_extensions.contains("GL_ARB_query_buffer_object");
        this.GL_ARB_robust_buffer_access_behavior = supported_extensions.contains("GL_ARB_robust_buffer_access_behavior");
        this.GL_ARB_robustness = supported_extensions.contains("GL_ARB_robustness");
        this.GL_ARB_robustness_isolation = supported_extensions.contains("GL_ARB_robustness_isolation");
        this.GL_ARB_sample_shading = supported_extensions.contains("GL_ARB_sample_shading");
        this.GL_ARB_sampler_objects = supported_extensions.contains("GL_ARB_sampler_objects");
        this.GL_ARB_seamless_cube_map = supported_extensions.contains("GL_ARB_seamless_cube_map");
        this.GL_ARB_seamless_cubemap_per_texture = supported_extensions.contains("GL_ARB_seamless_cubemap_per_texture");
        this.GL_ARB_separate_shader_objects = supported_extensions.contains("GL_ARB_separate_shader_objects");
        this.GL_ARB_shader_atomic_counters = supported_extensions.contains("GL_ARB_shader_atomic_counters");
        this.GL_ARB_shader_bit_encoding = supported_extensions.contains("GL_ARB_shader_bit_encoding");
        this.GL_ARB_shader_draw_parameters = supported_extensions.contains("GL_ARB_shader_draw_parameters");
        this.GL_ARB_shader_group_vote = supported_extensions.contains("GL_ARB_shader_group_vote");
        this.GL_ARB_shader_image_load_store = supported_extensions.contains("GL_ARB_shader_image_load_store");
        this.GL_ARB_shader_image_size = supported_extensions.contains("GL_ARB_shader_image_size");
        this.GL_ARB_shader_objects = supported_extensions.contains("GL_ARB_shader_objects");
        this.GL_ARB_shader_precision = supported_extensions.contains("GL_ARB_shader_precision");
        this.GL_ARB_shader_stencil_export = supported_extensions.contains("GL_ARB_shader_stencil_export");
        this.GL_ARB_shader_storage_buffer_object = supported_extensions.contains("GL_ARB_shader_storage_buffer_object");
        this.GL_ARB_shader_subroutine = supported_extensions.contains("GL_ARB_shader_subroutine");
        this.GL_ARB_shader_texture_image_samples = supported_extensions.contains("GL_ARB_shader_texture_image_samples");
        this.GL_ARB_shader_texture_lod = supported_extensions.contains("GL_ARB_shader_texture_lod");
        this.GL_ARB_shading_language_100 = supported_extensions.contains("GL_ARB_shading_language_100");
        this.GL_ARB_shading_language_420pack = supported_extensions.contains("GL_ARB_shading_language_420pack");
        this.GL_ARB_shading_language_include = supported_extensions.contains("GL_ARB_shading_language_include");
        this.GL_ARB_shading_language_packing = supported_extensions.contains("GL_ARB_shading_language_packing");
        this.GL_ARB_shadow = supported_extensions.contains("GL_ARB_shadow");
        this.GL_ARB_shadow_ambient = supported_extensions.contains("GL_ARB_shadow_ambient");
        this.GL_ARB_sparse_buffer = supported_extensions.contains("GL_ARB_sparse_buffer");
        this.GL_ARB_sparse_texture = supported_extensions.contains("GL_ARB_sparse_texture");
        this.GL_ARB_stencil_texturing = supported_extensions.contains("GL_ARB_stencil_texturing");
        this.GL_ARB_sync = supported_extensions.contains("GL_ARB_sync");
        this.GL_ARB_tessellation_shader = supported_extensions.contains("GL_ARB_tessellation_shader");
        this.GL_ARB_texture_barrier = supported_extensions.contains("GL_ARB_texture_barrier");
        this.GL_ARB_texture_border_clamp = supported_extensions.contains("GL_ARB_texture_border_clamp");
        this.GL_ARB_texture_buffer_object = supported_extensions.contains("GL_ARB_texture_buffer_object");
        this.GL_ARB_texture_buffer_object_rgb32 = supported_extensions.contains("GL_ARB_texture_buffer_object_rgb32") || supported_extensions.contains("GL_EXT_texture_buffer_object_rgb32");
        this.GL_ARB_texture_buffer_range = supported_extensions.contains("GL_ARB_texture_buffer_range");
        this.GL_ARB_texture_compression = supported_extensions.contains("GL_ARB_texture_compression");
        this.GL_ARB_texture_compression_bptc = supported_extensions.contains("GL_ARB_texture_compression_bptc") || supported_extensions.contains("GL_EXT_texture_compression_bptc");
        this.GL_ARB_texture_compression_rgtc = supported_extensions.contains("GL_ARB_texture_compression_rgtc");
        this.GL_ARB_texture_cube_map = supported_extensions.contains("GL_ARB_texture_cube_map");
        this.GL_ARB_texture_cube_map_array = supported_extensions.contains("GL_ARB_texture_cube_map_array");
        this.GL_ARB_texture_env_add = supported_extensions.contains("GL_ARB_texture_env_add");
        this.GL_ARB_texture_env_combine = supported_extensions.contains("GL_ARB_texture_env_combine");
        this.GL_ARB_texture_env_crossbar = supported_extensions.contains("GL_ARB_texture_env_crossbar");
        this.GL_ARB_texture_env_dot3 = supported_extensions.contains("GL_ARB_texture_env_dot3");
        this.GL_ARB_texture_float = supported_extensions.contains("GL_ARB_texture_float");
        this.GL_ARB_texture_gather = supported_extensions.contains("GL_ARB_texture_gather");
        this.GL_ARB_texture_mirror_clamp_to_edge = supported_extensions.contains("GL_ARB_texture_mirror_clamp_to_edge");
        this.GL_ARB_texture_mirrored_repeat = supported_extensions.contains("GL_ARB_texture_mirrored_repeat");
        this.GL_ARB_texture_multisample = supported_extensions.contains("GL_ARB_texture_multisample");
        this.GL_ARB_texture_non_power_of_two = supported_extensions.contains("GL_ARB_texture_non_power_of_two");
        this.GL_ARB_texture_query_levels = supported_extensions.contains("GL_ARB_texture_query_levels");
        this.GL_ARB_texture_query_lod = supported_extensions.contains("GL_ARB_texture_query_lod");
        this.GL_ARB_texture_rectangle = supported_extensions.contains("GL_ARB_texture_rectangle");
        this.GL_ARB_texture_rg = supported_extensions.contains("GL_ARB_texture_rg");
        this.GL_ARB_texture_rgb10_a2ui = supported_extensions.contains("GL_ARB_texture_rgb10_a2ui");
        this.GL_ARB_texture_stencil8 = supported_extensions.contains("GL_ARB_texture_stencil8");
        this.GL_ARB_texture_storage = supported_extensions.contains("GL_ARB_texture_storage") || supported_extensions.contains("GL_EXT_texture_storage");
        this.GL_ARB_texture_storage_multisample = supported_extensions.contains("GL_ARB_texture_storage_multisample");
        this.GL_ARB_texture_swizzle = supported_extensions.contains("GL_ARB_texture_swizzle");
        this.GL_ARB_texture_view = supported_extensions.contains("GL_ARB_texture_view");
        this.GL_ARB_timer_query = supported_extensions.contains("GL_ARB_timer_query");
        this.GL_ARB_transform_feedback2 = supported_extensions.contains("GL_ARB_transform_feedback2");
        this.GL_ARB_transform_feedback3 = supported_extensions.contains("GL_ARB_transform_feedback3");
        this.GL_ARB_transform_feedback_instanced = supported_extensions.contains("GL_ARB_transform_feedback_instanced");
        this.GL_ARB_transform_feedback_overflow_query = supported_extensions.contains("GL_ARB_transform_feedback_overflow_query");
        this.GL_ARB_transpose_matrix = supported_extensions.contains("GL_ARB_transpose_matrix");
        this.GL_ARB_uniform_buffer_object = supported_extensions.contains("GL_ARB_uniform_buffer_object");
        this.GL_ARB_vertex_array_bgra = supported_extensions.contains("GL_ARB_vertex_array_bgra");
        this.GL_ARB_vertex_array_object = supported_extensions.contains("GL_ARB_vertex_array_object");
        this.GL_ARB_vertex_attrib_64bit = supported_extensions.contains("GL_ARB_vertex_attrib_64bit");
        this.GL_ARB_vertex_attrib_binding = supported_extensions.contains("GL_ARB_vertex_attrib_binding");
        this.GL_ARB_vertex_blend = supported_extensions.contains("GL_ARB_vertex_blend");
        this.GL_ARB_vertex_buffer_object = supported_extensions.contains("GL_ARB_vertex_buffer_object") && supported_extensions.contains("GL_ARB_buffer_object");
        this.GL_ARB_vertex_program = supported_extensions.contains("GL_ARB_vertex_program") && supported_extensions.contains("GL_ARB_program");
        this.GL_ARB_vertex_shader = supported_extensions.contains("GL_ARB_vertex_shader");
        this.GL_ARB_vertex_type_10f_11f_11f_rev = supported_extensions.contains("GL_ARB_vertex_type_10f_11f_11f_rev");
        this.GL_ARB_vertex_type_2_10_10_10_rev = supported_extensions.contains("GL_ARB_vertex_type_2_10_10_10_rev");
        this.GL_ARB_viewport_array = supported_extensions.contains("GL_ARB_viewport_array");
        this.GL_ARB_window_pos = supported_extensions.contains("GL_ARB_window_pos");
        this.GL_ATI_draw_buffers = supported_extensions.contains("GL_ATI_draw_buffers");
        this.GL_ATI_element_array = supported_extensions.contains("GL_ATI_element_array");
        this.GL_ATI_envmap_bumpmap = supported_extensions.contains("GL_ATI_envmap_bumpmap");
        this.GL_ATI_fragment_shader = supported_extensions.contains("GL_ATI_fragment_shader");
        this.GL_ATI_map_object_buffer = supported_extensions.contains("GL_ATI_map_object_buffer");
        this.GL_ATI_meminfo = supported_extensions.contains("GL_ATI_meminfo");
        this.GL_ATI_pn_triangles = supported_extensions.contains("GL_ATI_pn_triangles");
        this.GL_ATI_separate_stencil = supported_extensions.contains("GL_ATI_separate_stencil");
        this.GL_ATI_shader_texture_lod = supported_extensions.contains("GL_ATI_shader_texture_lod");
        this.GL_ATI_text_fragment_shader = supported_extensions.contains("GL_ATI_text_fragment_shader");
        this.GL_ATI_texture_compression_3dc = supported_extensions.contains("GL_ATI_texture_compression_3dc");
        this.GL_ATI_texture_env_combine3 = supported_extensions.contains("GL_ATI_texture_env_combine3");
        this.GL_ATI_texture_float = supported_extensions.contains("GL_ATI_texture_float");
        this.GL_ATI_texture_mirror_once = supported_extensions.contains("GL_ATI_texture_mirror_once");
        this.GL_ATI_vertex_array_object = supported_extensions.contains("GL_ATI_vertex_array_object");
        this.GL_ATI_vertex_attrib_array_object = supported_extensions.contains("GL_ATI_vertex_attrib_array_object");
        this.GL_ATI_vertex_streams = supported_extensions.contains("GL_ATI_vertex_streams");
        this.GL_EXT_Cg_shader = supported_extensions.contains("GL_EXT_Cg_shader");
        this.GL_EXT_abgr = supported_extensions.contains("GL_EXT_abgr");
        this.GL_EXT_bgra = supported_extensions.contains("GL_EXT_bgra");
        this.GL_EXT_bindable_uniform = supported_extensions.contains("GL_EXT_bindable_uniform");
        this.GL_EXT_blend_color = supported_extensions.contains("GL_EXT_blend_color");
        this.GL_EXT_blend_equation_separate = supported_extensions.contains("GL_EXT_blend_equation_separate");
        this.GL_EXT_blend_func_separate = supported_extensions.contains("GL_EXT_blend_func_separate");
        this.GL_EXT_blend_minmax = supported_extensions.contains("GL_EXT_blend_minmax");
        this.GL_EXT_blend_subtract = supported_extensions.contains("GL_EXT_blend_subtract");
        this.GL_EXT_compiled_vertex_array = supported_extensions.contains("GL_EXT_compiled_vertex_array");
        this.GL_EXT_depth_bounds_test = supported_extensions.contains("GL_EXT_depth_bounds_test");
        this.GL_EXT_direct_state_access = supported_extensions.contains("GL_EXT_direct_state_access");
        this.GL_EXT_draw_buffers2 = supported_extensions.contains("GL_EXT_draw_buffers2");
        this.GL_EXT_draw_instanced = supported_extensions.contains("GL_EXT_draw_instanced");
        this.GL_EXT_draw_range_elements = supported_extensions.contains("GL_EXT_draw_range_elements");
        this.GL_EXT_fog_coord = supported_extensions.contains("GL_EXT_fog_coord");
        this.GL_EXT_framebuffer_blit = supported_extensions.contains("GL_EXT_framebuffer_blit");
        this.GL_EXT_framebuffer_multisample = supported_extensions.contains("GL_EXT_framebuffer_multisample");
        this.GL_EXT_framebuffer_multisample_blit_scaled = supported_extensions.contains("GL_EXT_framebuffer_multisample_blit_scaled");
        this.GL_EXT_framebuffer_object = supported_extensions.contains("GL_EXT_framebuffer_object");
        this.GL_EXT_framebuffer_sRGB = supported_extensions.contains("GL_EXT_framebuffer_sRGB");
        this.GL_EXT_geometry_shader4 = supported_extensions.contains("GL_EXT_geometry_shader4");
        this.GL_EXT_gpu_program_parameters = supported_extensions.contains("GL_EXT_gpu_program_parameters");
        this.GL_EXT_gpu_shader4 = supported_extensions.contains("GL_EXT_gpu_shader4");
        this.GL_EXT_multi_draw_arrays = supported_extensions.contains("GL_EXT_multi_draw_arrays");
        this.GL_EXT_packed_depth_stencil = supported_extensions.contains("GL_EXT_packed_depth_stencil");
        this.GL_EXT_packed_float = supported_extensions.contains("GL_EXT_packed_float");
        this.GL_EXT_packed_pixels = supported_extensions.contains("GL_EXT_packed_pixels");
        this.GL_EXT_paletted_texture = supported_extensions.contains("GL_EXT_paletted_texture");
        this.GL_EXT_pixel_buffer_object = supported_extensions.contains("GL_EXT_pixel_buffer_object") && supported_extensions.contains("GL_ARB_buffer_object");
        this.GL_EXT_point_parameters = supported_extensions.contains("GL_EXT_point_parameters");
        this.GL_EXT_provoking_vertex = supported_extensions.contains("GL_EXT_provoking_vertex");
        this.GL_EXT_rescale_normal = supported_extensions.contains("GL_EXT_rescale_normal");
        this.GL_EXT_secondary_color = supported_extensions.contains("GL_EXT_secondary_color");
        this.GL_EXT_separate_shader_objects = supported_extensions.contains("GL_EXT_separate_shader_objects");
        this.GL_EXT_separate_specular_color = supported_extensions.contains("GL_EXT_separate_specular_color");
        this.GL_EXT_shader_image_load_store = supported_extensions.contains("GL_EXT_shader_image_load_store");
        this.GL_EXT_shadow_funcs = supported_extensions.contains("GL_EXT_shadow_funcs");
        this.GL_EXT_shared_texture_palette = supported_extensions.contains("GL_EXT_shared_texture_palette");
        this.GL_EXT_stencil_clear_tag = supported_extensions.contains("GL_EXT_stencil_clear_tag");
        this.GL_EXT_stencil_two_side = supported_extensions.contains("GL_EXT_stencil_two_side");
        this.GL_EXT_stencil_wrap = supported_extensions.contains("GL_EXT_stencil_wrap");
        this.GL_EXT_texture_3d = supported_extensions.contains("GL_EXT_texture_3d");
        this.GL_EXT_texture_array = supported_extensions.contains("GL_EXT_texture_array");
        this.GL_EXT_texture_buffer_object = supported_extensions.contains("GL_EXT_texture_buffer_object");
        this.GL_EXT_texture_compression_latc = supported_extensions.contains("GL_EXT_texture_compression_latc");
        this.GL_EXT_texture_compression_rgtc = supported_extensions.contains("GL_EXT_texture_compression_rgtc");
        this.GL_EXT_texture_compression_s3tc = supported_extensions.contains("GL_EXT_texture_compression_s3tc");
        this.GL_EXT_texture_env_combine = supported_extensions.contains("GL_EXT_texture_env_combine");
        this.GL_EXT_texture_env_dot3 = supported_extensions.contains("GL_EXT_texture_env_dot3");
        this.GL_EXT_texture_filter_anisotropic = supported_extensions.contains("GL_EXT_texture_filter_anisotropic");
        this.GL_EXT_texture_integer = supported_extensions.contains("GL_EXT_texture_integer");
        this.GL_EXT_texture_lod_bias = supported_extensions.contains("GL_EXT_texture_lod_bias");
        this.GL_EXT_texture_mirror_clamp = supported_extensions.contains("GL_EXT_texture_mirror_clamp");
        this.GL_EXT_texture_rectangle = supported_extensions.contains("GL_EXT_texture_rectangle");
        this.GL_EXT_texture_sRGB = supported_extensions.contains("GL_EXT_texture_sRGB");
        this.GL_EXT_texture_sRGB_decode = supported_extensions.contains("GL_EXT_texture_sRGB_decode");
        this.GL_EXT_texture_shared_exponent = supported_extensions.contains("GL_EXT_texture_shared_exponent");
        this.GL_EXT_texture_snorm = supported_extensions.contains("GL_EXT_texture_snorm");
        this.GL_EXT_texture_swizzle = supported_extensions.contains("GL_EXT_texture_swizzle");
        this.GL_EXT_timer_query = supported_extensions.contains("GL_EXT_timer_query");
        this.GL_EXT_transform_feedback = supported_extensions.contains("GL_EXT_transform_feedback");
        this.GL_EXT_vertex_array_bgra = supported_extensions.contains("GL_EXT_vertex_array_bgra");
        this.GL_EXT_vertex_attrib_64bit = supported_extensions.contains("GL_EXT_vertex_attrib_64bit");
        this.GL_EXT_vertex_shader = supported_extensions.contains("GL_EXT_vertex_shader");
        this.GL_EXT_vertex_weighting = supported_extensions.contains("GL_EXT_vertex_weighting");
        this.OpenGL11 = supported_extensions.contains("OpenGL11");
        this.OpenGL12 = supported_extensions.contains("OpenGL12");
        this.OpenGL13 = supported_extensions.contains("OpenGL13");
        this.OpenGL14 = supported_extensions.contains("OpenGL14");
        this.OpenGL15 = supported_extensions.contains("OpenGL15");
        this.OpenGL20 = supported_extensions.contains("OpenGL20");
        this.OpenGL21 = supported_extensions.contains("OpenGL21");
        this.OpenGL30 = supported_extensions.contains("OpenGL30");
        this.OpenGL31 = supported_extensions.contains("OpenGL31");
        this.OpenGL32 = supported_extensions.contains("OpenGL32");
        this.OpenGL33 = supported_extensions.contains("OpenGL33");
        this.OpenGL40 = supported_extensions.contains("OpenGL40");
        this.OpenGL41 = supported_extensions.contains("OpenGL41");
        this.OpenGL42 = supported_extensions.contains("OpenGL42");
        this.OpenGL43 = supported_extensions.contains("OpenGL43");
        this.OpenGL44 = supported_extensions.contains("OpenGL44");
        this.OpenGL45 = supported_extensions.contains("OpenGL45");
        this.GL_GREMEDY_frame_terminator = supported_extensions.contains("GL_GREMEDY_frame_terminator");
        this.GL_GREMEDY_string_marker = supported_extensions.contains("GL_GREMEDY_string_marker");
        this.GL_HP_occlusion_test = supported_extensions.contains("GL_HP_occlusion_test");
        this.GL_IBM_rasterpos_clip = supported_extensions.contains("GL_IBM_rasterpos_clip");
        this.GL_INTEL_map_texture = supported_extensions.contains("GL_INTEL_map_texture");
        this.GL_KHR_context_flush_control = supported_extensions.contains("GL_KHR_context_flush_control");
        this.GL_KHR_debug = supported_extensions.contains("GL_KHR_debug");
        this.GL_KHR_robust_buffer_access_behavior = supported_extensions.contains("GL_KHR_robust_buffer_access_behavior");
        this.GL_KHR_robustness = supported_extensions.contains("GL_KHR_robustness");
        this.GL_KHR_texture_compression_astc_ldr = supported_extensions.contains("GL_KHR_texture_compression_astc_ldr");
        this.GL_NVX_gpu_memory_info = supported_extensions.contains("GL_NVX_gpu_memory_info");
        this.GL_NV_bindless_multi_draw_indirect = supported_extensions.contains("GL_NV_bindless_multi_draw_indirect");
        this.GL_NV_bindless_texture = supported_extensions.contains("GL_NV_bindless_texture");
        this.GL_NV_blend_equation_advanced = supported_extensions.contains("GL_NV_blend_equation_advanced");
        this.GL_NV_blend_square = supported_extensions.contains("GL_NV_blend_square");
        this.GL_NV_compute_program5 = supported_extensions.contains("GL_NV_compute_program5");
        this.GL_NV_conditional_render = supported_extensions.contains("GL_NV_conditional_render");
        this.GL_NV_copy_depth_to_color = supported_extensions.contains("GL_NV_copy_depth_to_color");
        this.GL_NV_copy_image = supported_extensions.contains("GL_NV_copy_image");
        this.GL_NV_deep_texture3D = supported_extensions.contains("GL_NV_deep_texture3D");
        this.GL_NV_depth_buffer_float = supported_extensions.contains("GL_NV_depth_buffer_float");
        this.GL_NV_depth_clamp = supported_extensions.contains("GL_NV_depth_clamp");
        this.GL_NV_draw_texture = supported_extensions.contains("GL_NV_draw_texture");
        this.GL_NV_evaluators = supported_extensions.contains("GL_NV_evaluators");
        this.GL_NV_explicit_multisample = supported_extensions.contains("GL_NV_explicit_multisample");
        this.GL_NV_fence = supported_extensions.contains("GL_NV_fence");
        this.GL_NV_float_buffer = supported_extensions.contains("GL_NV_float_buffer");
        this.GL_NV_fog_distance = supported_extensions.contains("GL_NV_fog_distance");
        this.GL_NV_fragment_program = supported_extensions.contains("GL_NV_fragment_program") && supported_extensions.contains("GL_NV_program");
        this.GL_NV_fragment_program2 = supported_extensions.contains("GL_NV_fragment_program2");
        this.GL_NV_fragment_program4 = supported_extensions.contains("GL_NV_fragment_program4");
        this.GL_NV_fragment_program_option = supported_extensions.contains("GL_NV_fragment_program_option");
        this.GL_NV_framebuffer_multisample_coverage = supported_extensions.contains("GL_NV_framebuffer_multisample_coverage");
        this.GL_NV_geometry_program4 = supported_extensions.contains("GL_NV_geometry_program4");
        this.GL_NV_geometry_shader4 = supported_extensions.contains("GL_NV_geometry_shader4");
        this.GL_NV_gpu_program4 = supported_extensions.contains("GL_NV_gpu_program4");
        this.GL_NV_gpu_program5 = supported_extensions.contains("GL_NV_gpu_program5");
        this.GL_NV_gpu_program5_mem_extended = supported_extensions.contains("GL_NV_gpu_program5_mem_extended");
        this.GL_NV_gpu_shader5 = supported_extensions.contains("GL_NV_gpu_shader5");
        this.GL_NV_half_float = supported_extensions.contains("GL_NV_half_float");
        this.GL_NV_light_max_exponent = supported_extensions.contains("GL_NV_light_max_exponent");
        this.GL_NV_multisample_coverage = supported_extensions.contains("GL_NV_multisample_coverage");
        this.GL_NV_multisample_filter_hint = supported_extensions.contains("GL_NV_multisample_filter_hint");
        this.GL_NV_occlusion_query = supported_extensions.contains("GL_NV_occlusion_query");
        this.GL_NV_packed_depth_stencil = supported_extensions.contains("GL_NV_packed_depth_stencil");
        this.GL_NV_parameter_buffer_object = supported_extensions.contains("GL_NV_parameter_buffer_object");
        this.GL_NV_parameter_buffer_object2 = supported_extensions.contains("GL_NV_parameter_buffer_object2");
        this.GL_NV_path_rendering = supported_extensions.contains("GL_NV_path_rendering");
        this.GL_NV_pixel_data_range = supported_extensions.contains("GL_NV_pixel_data_range");
        this.GL_NV_point_sprite = supported_extensions.contains("GL_NV_point_sprite");
        this.GL_NV_present_video = supported_extensions.contains("GL_NV_present_video");
        this.GL_NV_primitive_restart = supported_extensions.contains("GL_NV_primitive_restart");
        this.GL_NV_register_combiners = supported_extensions.contains("GL_NV_register_combiners");
        this.GL_NV_register_combiners2 = supported_extensions.contains("GL_NV_register_combiners2");
        this.GL_NV_shader_atomic_counters = supported_extensions.contains("GL_NV_shader_atomic_counters");
        this.GL_NV_shader_atomic_float = supported_extensions.contains("GL_NV_shader_atomic_float");
        this.GL_NV_shader_buffer_load = supported_extensions.contains("GL_NV_shader_buffer_load");
        this.GL_NV_shader_buffer_store = supported_extensions.contains("GL_NV_shader_buffer_store");
        this.GL_NV_shader_storage_buffer_object = supported_extensions.contains("GL_NV_shader_storage_buffer_object");
        this.GL_NV_tessellation_program5 = supported_extensions.contains("GL_NV_tessellation_program5");
        this.GL_NV_texgen_reflection = supported_extensions.contains("GL_NV_texgen_reflection");
        this.GL_NV_texture_barrier = supported_extensions.contains("GL_NV_texture_barrier");
        this.GL_NV_texture_compression_vtc = supported_extensions.contains("GL_NV_texture_compression_vtc");
        this.GL_NV_texture_env_combine4 = supported_extensions.contains("GL_NV_texture_env_combine4");
        this.GL_NV_texture_expand_normal = supported_extensions.contains("GL_NV_texture_expand_normal");
        this.GL_NV_texture_multisample = supported_extensions.contains("GL_NV_texture_multisample");
        this.GL_NV_texture_rectangle = supported_extensions.contains("GL_NV_texture_rectangle");
        this.GL_NV_texture_shader = supported_extensions.contains("GL_NV_texture_shader");
        this.GL_NV_texture_shader2 = supported_extensions.contains("GL_NV_texture_shader2");
        this.GL_NV_texture_shader3 = supported_extensions.contains("GL_NV_texture_shader3");
        this.GL_NV_transform_feedback = supported_extensions.contains("GL_NV_transform_feedback");
        this.GL_NV_transform_feedback2 = supported_extensions.contains("GL_NV_transform_feedback2");
        this.GL_NV_vertex_array_range = supported_extensions.contains("GL_NV_vertex_array_range");
        this.GL_NV_vertex_array_range2 = supported_extensions.contains("GL_NV_vertex_array_range2");
        this.GL_NV_vertex_attrib_integer_64bit = supported_extensions.contains("GL_NV_vertex_attrib_integer_64bit");
        this.GL_NV_vertex_buffer_unified_memory = supported_extensions.contains("GL_NV_vertex_buffer_unified_memory");
        this.GL_NV_vertex_program = supported_extensions.contains("GL_NV_vertex_program") && supported_extensions.contains("GL_NV_program");
        this.GL_NV_vertex_program1_1 = supported_extensions.contains("GL_NV_vertex_program1_1");
        this.GL_NV_vertex_program2 = supported_extensions.contains("GL_NV_vertex_program2");
        this.GL_NV_vertex_program2_option = supported_extensions.contains("GL_NV_vertex_program2_option");
        this.GL_NV_vertex_program3 = supported_extensions.contains("GL_NV_vertex_program3");
        this.GL_NV_vertex_program4 = supported_extensions.contains("GL_NV_vertex_program4");
        this.GL_NV_video_capture = supported_extensions.contains("GL_NV_video_capture");
        this.GL_SGIS_generate_mipmap = supported_extensions.contains("GL_SGIS_generate_mipmap");
        this.GL_SGIS_texture_lod = supported_extensions.contains("GL_SGIS_texture_lod");
        this.GL_SUN_slice_accum = supported_extensions.contains("GL_SUN_slice_accum");
        this.tracker.init();
    }
}

