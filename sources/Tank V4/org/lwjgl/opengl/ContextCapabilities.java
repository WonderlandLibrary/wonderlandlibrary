package org.lwjgl.opengl;

import java.util.HashSet;
import java.util.Set;
import org.lwjgl.LWJGLException;
import org.lwjgl.LWJGLUtil;

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
   public final boolean GL_ARB_ES3_compatibility;
   public final boolean GL_ARB_arrays_of_arrays;
   public final boolean GL_ARB_base_instance;
   public final boolean GL_ARB_bindless_texture;
   public final boolean GL_ARB_blend_func_extended;
   public final boolean GL_ARB_buffer_storage;
   public final boolean GL_ARB_cl_event;
   public final boolean GL_ARB_clear_buffer_object;
   public final boolean GL_ARB_clear_texture;
   public final boolean GL_ARB_color_buffer_float;
   public final boolean GL_ARB_compatibility;
   public final boolean GL_ARB_compressed_texture_pixel_storage;
   public final boolean GL_ARB_compute_shader;
   public final boolean GL_ARB_compute_variable_group_size;
   public final boolean GL_ARB_conservative_depth;
   public final boolean GL_ARB_copy_buffer;
   public final boolean GL_ARB_copy_image;
   public final boolean GL_ARB_debug_output;
   public final boolean GL_ARB_depth_buffer_float;
   public final boolean GL_ARB_depth_clamp;
   public final boolean GL_ARB_depth_texture;
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
   public final boolean GL_ARB_shader_texture_lod;
   public final boolean GL_ARB_shading_language_100;
   public final boolean GL_ARB_shading_language_420pack;
   public final boolean GL_ARB_shading_language_include;
   public final boolean GL_ARB_shading_language_packing;
   public final boolean GL_ARB_shadow;
   public final boolean GL_ARB_shadow_ambient;
   public final boolean GL_ARB_sparse_texture;
   public final boolean GL_ARB_stencil_texturing;
   public final boolean GL_ARB_sync;
   public final boolean GL_ARB_tessellation_shader;
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
   public final boolean GL_EXT_abgr;
   public final boolean GL_EXT_bgra;
   public final boolean GL_EXT_bindable_uniform;
   public final boolean GL_EXT_blend_color;
   public final boolean GL_EXT_blend_equation_separate;
   public final boolean GL_EXT_blend_func_separate;
   public final boolean GL_EXT_blend_minmax;
   public final boolean GL_EXT_blend_subtract;
   public final boolean GL_EXT_Cg_shader;
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
   public final boolean GL_GREMEDY_frame_terminator;
   public final boolean GL_GREMEDY_string_marker;
   public final boolean GL_HP_occlusion_test;
   public final boolean GL_IBM_rasterpos_clip;
   public final boolean GL_INTEL_map_texture;
   public final boolean GL_KHR_debug;
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

   private static void remove(Set var0, String var1) {
      LWJGLUtil.log(var1 + " was reported as available but an entry point is missing");
      var0.remove(var1);
   }

   private Set initAllStubs(boolean var1) throws LWJGLException {
      this.glGetError = GLContext.getFunctionAddress("glGetError");
      this.glGetString = GLContext.getFunctionAddress("glGetString");
      this.glGetIntegerv = GLContext.getFunctionAddress("glGetIntegerv");
      this.glGetStringi = GLContext.getFunctionAddress("glGetStringi");
      GLContext.setCapabilities(this);
      HashSet var2 = new HashSet(256);
      int var3 = GLContext.getSupportedExtensions(var2);
      if (var2.contains("OpenGL31") && !var2.contains("GL_ARB_compatibility") && (var3 & 2) == 0) {
         var1 = true;
      }

      if (!var1) {
         throw new LWJGLException("GL11 not supported");
      } else {
         if (var2.contains("GL_ARB_fragment_program")) {
            var2.add("GL_ARB_program");
         }

         if (var2.contains("GL_ARB_pixel_buffer_object")) {
            var2.add("GL_ARB_buffer_object");
         }

         if (var2.contains("GL_ARB_vertex_buffer_object")) {
            var2.add("GL_ARB_buffer_object");
         }

         if (var2.contains("GL_ARB_vertex_program")) {
            var2.add("GL_ARB_program");
         }

         if (var2.contains("GL_EXT_pixel_buffer_object")) {
            var2.add("GL_ARB_buffer_object");
         }

         if (var2.contains("GL_NV_fragment_program")) {
            var2.add("GL_NV_program");
         }

         if (var2.contains("GL_NV_vertex_program")) {
            var2.add("GL_NV_program");
         }

         if ((var2.contains("GL_AMD_debug_output") || var2.contains("GL_AMDX_debug_output")) && this != false) {
            remove(var2, "GL_AMDX_debug_output");
            remove(var2, "GL_AMD_debug_output");
         }

         if (var2.contains("GL_AMD_draw_buffers_blend") && this != false) {
            remove(var2, "GL_AMD_draw_buffers_blend");
         }

         if (var2.contains("GL_AMD_interleaved_elements") && this != false) {
            remove(var2, "GL_AMD_interleaved_elements");
         }

         if (var2.contains("GL_AMD_multi_draw_indirect") && this != false) {
            remove(var2, "GL_AMD_multi_draw_indirect");
         }

         if (var2.contains("GL_AMD_name_gen_delete") && this != false) {
            remove(var2, "GL_AMD_name_gen_delete");
         }

         if (var2.contains("GL_AMD_performance_monitor") && this != false) {
            remove(var2, "GL_AMD_performance_monitor");
         }

         if (var2.contains("GL_AMD_sample_positions") && this != false) {
            remove(var2, "GL_AMD_sample_positions");
         }

         if (var2.contains("GL_AMD_sparse_texture") && this != false) {
            remove(var2, "GL_AMD_sparse_texture");
         }

         if (var2.contains("GL_AMD_stencil_operation_extended") && this != false) {
            remove(var2, "GL_AMD_stencil_operation_extended");
         }

         if (var2.contains("GL_AMD_vertex_shader_tessellator") && this != false) {
            remove(var2, "GL_AMD_vertex_shader_tessellator");
         }

         if (var2.contains("GL_APPLE_element_array") && this != false) {
            remove(var2, "GL_APPLE_element_array");
         }

         if (var2.contains("GL_APPLE_fence") && this != false) {
            remove(var2, "GL_APPLE_fence");
         }

         if (var2.contains("GL_APPLE_flush_buffer_range") && this != false) {
            remove(var2, "GL_APPLE_flush_buffer_range");
         }

         if (var2.contains("GL_APPLE_object_purgeable") && this != false) {
            remove(var2, "GL_APPLE_object_purgeable");
         }

         if (var2.contains("GL_APPLE_texture_range") && this != false) {
            remove(var2, "GL_APPLE_texture_range");
         }

         if (var2.contains("GL_APPLE_vertex_array_object") && this != false) {
            remove(var2, "GL_APPLE_vertex_array_object");
         }

         if (var2.contains("GL_APPLE_vertex_array_range") && this != false) {
            remove(var2, "GL_APPLE_vertex_array_range");
         }

         if (var2.contains("GL_APPLE_vertex_program_evaluators") && this != false) {
            remove(var2, "GL_APPLE_vertex_program_evaluators");
         }

         if (var2.contains("GL_ARB_ES2_compatibility") && this != false) {
            remove(var2, "GL_ARB_ES2_compatibility");
         }

         if (var2.contains("GL_ARB_base_instance") && this != false) {
            remove(var2, "GL_ARB_base_instance");
         }

         if (var2.contains("GL_ARB_bindless_texture") && this != false) {
            remove(var2, "GL_ARB_bindless_texture");
         }

         if (var2.contains("GL_ARB_blend_func_extended") && this != false) {
            remove(var2, "GL_ARB_blend_func_extended");
         }

         if (var2.contains("GL_ARB_buffer_object") && this != false) {
            remove(var2, "GL_ARB_buffer_object");
         }

         if (var2.contains("GL_ARB_buffer_storage") && var2 != false) {
            remove(var2, "GL_ARB_buffer_storage");
         }

         if (var2.contains("GL_ARB_cl_event") && this != false) {
            remove(var2, "GL_ARB_cl_event");
         }

         if (var2.contains("GL_ARB_clear_buffer_object") && var2 != false) {
            remove(var2, "GL_ARB_clear_buffer_object");
         }

         if (var2.contains("GL_ARB_clear_texture") && this != false) {
            remove(var2, "GL_ARB_clear_texture");
         }

         if (var2.contains("GL_ARB_color_buffer_float") && this != false) {
            remove(var2, "GL_ARB_color_buffer_float");
         }

         if (var2.contains("GL_ARB_compute_shader") && this != false) {
            remove(var2, "GL_ARB_compute_shader");
         }

         if (var2.contains("GL_ARB_compute_variable_group_size") && this != false) {
            remove(var2, "GL_ARB_compute_variable_group_size");
         }

         if (var2.contains("GL_ARB_copy_buffer") && this != false) {
            remove(var2, "GL_ARB_copy_buffer");
         }

         if (var2.contains("GL_ARB_copy_image") && this != false) {
            remove(var2, "GL_ARB_copy_image");
         }

         if (var2.contains("GL_ARB_debug_output") && this != false) {
            remove(var2, "GL_ARB_debug_output");
         }

         if (var2.contains("GL_ARB_draw_buffers") && this != false) {
            remove(var2, "GL_ARB_draw_buffers");
         }

         if (var2.contains("GL_ARB_draw_buffers_blend") && this != false) {
            remove(var2, "GL_ARB_draw_buffers_blend");
         }

         if (var2.contains("GL_ARB_draw_elements_base_vertex") && this != false) {
            remove(var2, "GL_ARB_draw_elements_base_vertex");
         }

         if (var2.contains("GL_ARB_draw_indirect") && this != false) {
            remove(var2, "GL_ARB_draw_indirect");
         }

         if (var2.contains("GL_ARB_draw_instanced") && this != false) {
            remove(var2, "GL_ARB_draw_instanced");
         }

         if (var2.contains("GL_ARB_framebuffer_no_attachments") && var2 != false) {
            remove(var2, "GL_ARB_framebuffer_no_attachments");
         }

         if (var2.contains("GL_ARB_framebuffer_object") && this != false) {
            remove(var2, "GL_ARB_framebuffer_object");
         }

         if (var2.contains("GL_ARB_geometry_shader4") && this != false) {
            remove(var2, "GL_ARB_geometry_shader4");
         }

         if (var2.contains("GL_ARB_get_program_binary") && this != false) {
            remove(var2, "GL_ARB_get_program_binary");
         }

         if (var2.contains("GL_ARB_gpu_shader_fp64") && var2 != false) {
            remove(var2, "GL_ARB_gpu_shader_fp64");
         }

         if (var2.contains("GL_ARB_imaging") && !var1) {
            remove(var2, "GL_ARB_imaging");
         }

         if (var2.contains("GL_ARB_indirect_parameters") && this != false) {
            remove(var2, "GL_ARB_indirect_parameters");
         }

         if (var2.contains("GL_ARB_instanced_arrays") && this != false) {
            remove(var2, "GL_ARB_instanced_arrays");
         }

         if (var2.contains("GL_ARB_internalformat_query") && this != false) {
            remove(var2, "GL_ARB_internalformat_query");
         }

         if (var2.contains("GL_ARB_internalformat_query2") && this != false) {
            remove(var2, "GL_ARB_internalformat_query2");
         }

         if (var2.contains("GL_ARB_invalidate_subdata") && this != false) {
            remove(var2, "GL_ARB_invalidate_subdata");
         }

         if (var2.contains("GL_ARB_map_buffer_range") && this != false) {
            remove(var2, "GL_ARB_map_buffer_range");
         }

         if (var2.contains("GL_ARB_matrix_palette") && this != false) {
            remove(var2, "GL_ARB_matrix_palette");
         }

         if (var2.contains("GL_ARB_multi_bind") && this != false) {
            remove(var2, "GL_ARB_multi_bind");
         }

         if (var2.contains("GL_ARB_multi_draw_indirect") && this != false) {
            remove(var2, "GL_ARB_multi_draw_indirect");
         }

         if (var2.contains("GL_ARB_multisample") && this != false) {
            remove(var2, "GL_ARB_multisample");
         }

         if (var2.contains("GL_ARB_multitexture") && this != false) {
            remove(var2, "GL_ARB_multitexture");
         }

         if (var2.contains("GL_ARB_occlusion_query") && this != false) {
            remove(var2, "GL_ARB_occlusion_query");
         }

         if (var2.contains("GL_ARB_point_parameters") && this != false) {
            remove(var2, "GL_ARB_point_parameters");
         }

         if (var2.contains("GL_ARB_program") && this != false) {
            remove(var2, "GL_ARB_program");
         }

         if (var2.contains("GL_ARB_program_interface_query") && this != false) {
            remove(var2, "GL_ARB_program_interface_query");
         }

         if (var2.contains("GL_ARB_provoking_vertex") && this != false) {
            remove(var2, "GL_ARB_provoking_vertex");
         }

         if (var2.contains("GL_ARB_robustness") && var2 != false) {
            remove(var2, "GL_ARB_robustness");
         }

         if (var2.contains("GL_ARB_sample_shading") && this != false) {
            remove(var2, "GL_ARB_sample_shading");
         }

         if (var2.contains("GL_ARB_sampler_objects") && this != false) {
            remove(var2, "GL_ARB_sampler_objects");
         }

         if (var2.contains("GL_ARB_separate_shader_objects") && this != false) {
            remove(var2, "GL_ARB_separate_shader_objects");
         }

         if (var2.contains("GL_ARB_shader_atomic_counters") && this != false) {
            remove(var2, "GL_ARB_shader_atomic_counters");
         }

         if (var2.contains("GL_ARB_shader_image_load_store") && this != false) {
            remove(var2, "GL_ARB_shader_image_load_store");
         }

         if (var2.contains("GL_ARB_shader_objects") && this != false) {
            remove(var2, "GL_ARB_shader_objects");
         }

         if (var2.contains("GL_ARB_shader_storage_buffer_object") && this != false) {
            remove(var2, "GL_ARB_shader_storage_buffer_object");
         }

         if (var2.contains("GL_ARB_shader_subroutine") && this != false) {
            remove(var2, "GL_ARB_shader_subroutine");
         }

         if (var2.contains("GL_ARB_shading_language_include") && this != false) {
            remove(var2, "GL_ARB_shading_language_include");
         }

         if (var2.contains("GL_ARB_sparse_texture") && var2 != false) {
            remove(var2, "GL_ARB_sparse_texture");
         }

         if (var2.contains("GL_ARB_sync") && this != false) {
            remove(var2, "GL_ARB_sync");
         }

         if (var2.contains("GL_ARB_tessellation_shader") && this != false) {
            remove(var2, "GL_ARB_tessellation_shader");
         }

         if (var2.contains("GL_ARB_texture_buffer_object") && this != false) {
            remove(var2, "GL_ARB_texture_buffer_object");
         }

         if (var2.contains("GL_ARB_texture_buffer_range") && var2 != false) {
            remove(var2, "GL_ARB_texture_buffer_range");
         }

         if (var2.contains("GL_ARB_texture_compression") && this != false) {
            remove(var2, "GL_ARB_texture_compression");
         }

         if (var2.contains("GL_ARB_texture_multisample") && this != false) {
            remove(var2, "GL_ARB_texture_multisample");
         }

         if ((var2.contains("GL_ARB_texture_storage") || var2.contains("GL_EXT_texture_storage")) && var2 != false) {
            remove(var2, "GL_EXT_texture_storage");
            remove(var2, "GL_ARB_texture_storage");
         }

         if (var2.contains("GL_ARB_texture_storage_multisample") && var2 != false) {
            remove(var2, "GL_ARB_texture_storage_multisample");
         }

         if (var2.contains("GL_ARB_texture_view") && this != false) {
            remove(var2, "GL_ARB_texture_view");
         }

         if (var2.contains("GL_ARB_timer_query") && this != false) {
            remove(var2, "GL_ARB_timer_query");
         }

         if (var2.contains("GL_ARB_transform_feedback2") && this != false) {
            remove(var2, "GL_ARB_transform_feedback2");
         }

         if (var2.contains("GL_ARB_transform_feedback3") && this != false) {
            remove(var2, "GL_ARB_transform_feedback3");
         }

         if (var2.contains("GL_ARB_transform_feedback_instanced") && this != false) {
            remove(var2, "GL_ARB_transform_feedback_instanced");
         }

         if (var2.contains("GL_ARB_transpose_matrix") && this != false) {
            remove(var2, "GL_ARB_transpose_matrix");
         }

         if (var2.contains("GL_ARB_uniform_buffer_object") && this != false) {
            remove(var2, "GL_ARB_uniform_buffer_object");
         }

         if (var2.contains("GL_ARB_vertex_array_object") && this != false) {
            remove(var2, "GL_ARB_vertex_array_object");
         }

         if (var2.contains("GL_ARB_vertex_attrib_64bit") && var2 != false) {
            remove(var2, "GL_ARB_vertex_attrib_64bit");
         }

         if (var2.contains("GL_ARB_vertex_attrib_binding") && this != false) {
            remove(var2, "GL_ARB_vertex_attrib_binding");
         }

         if (var2.contains("GL_ARB_vertex_blend") && this != false) {
            remove(var2, "GL_ARB_vertex_blend");
         }

         if (var2.contains("GL_ARB_vertex_program") && this != false) {
            remove(var2, "GL_ARB_vertex_program");
         }

         if (var2.contains("GL_ARB_vertex_shader") && this != false) {
            remove(var2, "GL_ARB_vertex_shader");
         }

         if (var2.contains("GL_ARB_vertex_type_2_10_10_10_rev") && this != false) {
            remove(var2, "GL_ARB_vertex_type_2_10_10_10_rev");
         }

         if (var2.contains("GL_ARB_viewport_array") && this != false) {
            remove(var2, "GL_ARB_viewport_array");
         }

         if (var2.contains("GL_ARB_window_pos") && !var1) {
            remove(var2, "GL_ARB_window_pos");
         }

         if (var2.contains("GL_ATI_draw_buffers") && this != false) {
            remove(var2, "GL_ATI_draw_buffers");
         }

         if (var2.contains("GL_ATI_element_array") && this != false) {
            remove(var2, "GL_ATI_element_array");
         }

         if (var2.contains("GL_ATI_envmap_bumpmap") && this != false) {
            remove(var2, "GL_ATI_envmap_bumpmap");
         }

         if (var2.contains("GL_ATI_fragment_shader") && this != false) {
            remove(var2, "GL_ATI_fragment_shader");
         }

         if (var2.contains("GL_ATI_map_object_buffer") && this != false) {
            remove(var2, "GL_ATI_map_object_buffer");
         }

         if (var2.contains("GL_ATI_pn_triangles") && this != false) {
            remove(var2, "GL_ATI_pn_triangles");
         }

         if (var2.contains("GL_ATI_separate_stencil") && this != false) {
            remove(var2, "GL_ATI_separate_stencil");
         }

         if (var2.contains("GL_ATI_vertex_array_object") && this != false) {
            remove(var2, "GL_ATI_vertex_array_object");
         }

         if (var2.contains("GL_ATI_vertex_attrib_array_object") && this != false) {
            remove(var2, "GL_ATI_vertex_attrib_array_object");
         }

         if (var2.contains("GL_ATI_vertex_streams") && this != false) {
            remove(var2, "GL_ATI_vertex_streams");
         }

         if (var2.contains("GL_EXT_bindable_uniform") && this != false) {
            remove(var2, "GL_EXT_bindable_uniform");
         }

         if (var2.contains("GL_EXT_blend_color") && this != false) {
            remove(var2, "GL_EXT_blend_color");
         }

         if (var2.contains("GL_EXT_blend_equation_separate") && this != false) {
            remove(var2, "GL_EXT_blend_equation_separate");
         }

         if (var2.contains("GL_EXT_blend_func_separate") && this != false) {
            remove(var2, "GL_EXT_blend_func_separate");
         }

         if (var2.contains("GL_EXT_blend_minmax") && this != false) {
            remove(var2, "GL_EXT_blend_minmax");
         }

         if (var2.contains("GL_EXT_compiled_vertex_array") && this != false) {
            remove(var2, "GL_EXT_compiled_vertex_array");
         }

         if (var2.contains("GL_EXT_depth_bounds_test") && this != false) {
            remove(var2, "GL_EXT_depth_bounds_test");
         }

         var2.add("GL_EXT_direct_state_access");
         if (var2.contains("GL_EXT_direct_state_access") && var2 == false) {
            remove(var2, "GL_EXT_direct_state_access");
         }

         if (var2.contains("GL_EXT_draw_buffers2") && this != false) {
            remove(var2, "GL_EXT_draw_buffers2");
         }

         if (var2.contains("GL_EXT_draw_instanced") && this != false) {
            remove(var2, "GL_EXT_draw_instanced");
         }

         if (var2.contains("GL_EXT_draw_range_elements") && this != false) {
            remove(var2, "GL_EXT_draw_range_elements");
         }

         if (var2.contains("GL_EXT_fog_coord") && this != false) {
            remove(var2, "GL_EXT_fog_coord");
         }

         if (var2.contains("GL_EXT_framebuffer_blit") && this != false) {
            remove(var2, "GL_EXT_framebuffer_blit");
         }

         if (var2.contains("GL_EXT_framebuffer_multisample") && this != false) {
            remove(var2, "GL_EXT_framebuffer_multisample");
         }

         if (var2.contains("GL_EXT_framebuffer_object") && this != false) {
            remove(var2, "GL_EXT_framebuffer_object");
         }

         if (var2.contains("GL_EXT_geometry_shader4") && this != false) {
            remove(var2, "GL_EXT_geometry_shader4");
         }

         if (var2.contains("GL_EXT_gpu_program_parameters") && this != false) {
            remove(var2, "GL_EXT_gpu_program_parameters");
         }

         if (var2.contains("GL_EXT_gpu_shader4") && this != false) {
            remove(var2, "GL_EXT_gpu_shader4");
         }

         if (var2.contains("GL_EXT_multi_draw_arrays") && this != false) {
            remove(var2, "GL_EXT_multi_draw_arrays");
         }

         if (var2.contains("GL_EXT_paletted_texture") && this != false) {
            remove(var2, "GL_EXT_paletted_texture");
         }

         if (var2.contains("GL_EXT_point_parameters") && this != false) {
            remove(var2, "GL_EXT_point_parameters");
         }

         if (var2.contains("GL_EXT_provoking_vertex") && this != false) {
            remove(var2, "GL_EXT_provoking_vertex");
         }

         if (var2.contains("GL_EXT_secondary_color") && this != false) {
            remove(var2, "GL_EXT_secondary_color");
         }

         if (var2.contains("GL_EXT_separate_shader_objects") && this != false) {
            remove(var2, "GL_EXT_separate_shader_objects");
         }

         if (var2.contains("GL_EXT_shader_image_load_store") && this != false) {
            remove(var2, "GL_EXT_shader_image_load_store");
         }

         if (var2.contains("GL_EXT_stencil_clear_tag") && this != false) {
            remove(var2, "GL_EXT_stencil_clear_tag");
         }

         if (var2.contains("GL_EXT_stencil_two_side") && this != false) {
            remove(var2, "GL_EXT_stencil_two_side");
         }

         if (var2.contains("GL_EXT_texture_array") && this != false) {
            remove(var2, "GL_EXT_texture_array");
         }

         if (var2.contains("GL_EXT_texture_buffer_object") && this != false) {
            remove(var2, "GL_EXT_texture_buffer_object");
         }

         if (var2.contains("GL_EXT_texture_integer") && this != false) {
            remove(var2, "GL_EXT_texture_integer");
         }

         if (var2.contains("GL_EXT_timer_query") && this != false) {
            remove(var2, "GL_EXT_timer_query");
         }

         if (var2.contains("GL_EXT_transform_feedback") && this != false) {
            remove(var2, "GL_EXT_transform_feedback");
         }

         if (var2.contains("GL_EXT_vertex_attrib_64bit") && var2 != false) {
            remove(var2, "GL_EXT_vertex_attrib_64bit");
         }

         if (var2.contains("GL_EXT_vertex_shader") && this != false) {
            remove(var2, "GL_EXT_vertex_shader");
         }

         if (var2.contains("GL_EXT_vertex_weighting") && this != false) {
            remove(var2, "GL_EXT_vertex_weighting");
         }

         if (var2.contains("OpenGL12") && this != false) {
            remove(var2, "OpenGL12");
         }

         if (var2.contains("OpenGL13") && var1) {
            remove(var2, "OpenGL13");
         }

         if (var2.contains("OpenGL14") && var1) {
            remove(var2, "OpenGL14");
         }

         if (var2.contains("OpenGL15") && this != false) {
            remove(var2, "OpenGL15");
         }

         if (var2.contains("OpenGL20") && this != false) {
            remove(var2, "OpenGL20");
         }

         if (var2.contains("OpenGL21") && this != false) {
            remove(var2, "OpenGL21");
         }

         if (var2.contains("OpenGL30") && this != false) {
            remove(var2, "OpenGL30");
         }

         if (var2.contains("OpenGL31") && this != false) {
            remove(var2, "OpenGL31");
         }

         if (var2.contains("OpenGL32") && this != false) {
            remove(var2, "OpenGL32");
         }

         if (var2.contains("OpenGL33") && var1) {
            remove(var2, "OpenGL33");
         }

         if (var2.contains("OpenGL40") && this != false) {
            remove(var2, "OpenGL40");
         }

         if (var2.contains("OpenGL41") && this != false) {
            remove(var2, "OpenGL41");
         }

         if (var2.contains("OpenGL42") && this == false) {
            remove(var2, "OpenGL42");
         }

         if (var2.contains("OpenGL43") && this != false) {
            remove(var2, "OpenGL43");
         }

         if (var2.contains("OpenGL44") && this != false) {
            remove(var2, "OpenGL44");
         }

         if (var2.contains("GL_GREMEDY_frame_terminator") && this != false) {
            remove(var2, "GL_GREMEDY_frame_terminator");
         }

         if (var2.contains("GL_GREMEDY_string_marker") && this != false) {
            remove(var2, "GL_GREMEDY_string_marker");
         }

         if (var2.contains("GL_INTEL_map_texture") && this != false) {
            remove(var2, "GL_INTEL_map_texture");
         }

         if (var2.contains("GL_KHR_debug") && this != false) {
            remove(var2, "GL_KHR_debug");
         }

         if (var2.contains("GL_NV_bindless_multi_draw_indirect") && this != false) {
            remove(var2, "GL_NV_bindless_multi_draw_indirect");
         }

         if (var2.contains("GL_NV_bindless_texture") && this != false) {
            remove(var2, "GL_NV_bindless_texture");
         }

         if (var2.contains("GL_NV_blend_equation_advanced") && this != false) {
            remove(var2, "GL_NV_blend_equation_advanced");
         }

         if (var2.contains("GL_NV_conditional_render") && this != false) {
            remove(var2, "GL_NV_conditional_render");
         }

         if (var2.contains("GL_NV_copy_image") && this != false) {
            remove(var2, "GL_NV_copy_image");
         }

         if (var2.contains("GL_NV_depth_buffer_float") && this != false) {
            remove(var2, "GL_NV_depth_buffer_float");
         }

         if (var2.contains("GL_NV_draw_texture") && this != false) {
            remove(var2, "GL_NV_draw_texture");
         }

         if (var2.contains("GL_NV_evaluators") && this != false) {
            remove(var2, "GL_NV_evaluators");
         }

         if (var2.contains("GL_NV_explicit_multisample") && this != false) {
            remove(var2, "GL_NV_explicit_multisample");
         }

         if (var2.contains("GL_NV_fence") && this != false) {
            remove(var2, "GL_NV_fence");
         }

         if (var2.contains("GL_NV_fragment_program") && this != false) {
            remove(var2, "GL_NV_fragment_program");
         }

         if (var2.contains("GL_NV_framebuffer_multisample_coverage") && this != false) {
            remove(var2, "GL_NV_framebuffer_multisample_coverage");
         }

         if (var2.contains("GL_NV_geometry_program4") && this != false) {
            remove(var2, "GL_NV_geometry_program4");
         }

         if (var2.contains("GL_NV_gpu_program4") && this != false) {
            remove(var2, "GL_NV_gpu_program4");
         }

         if (var2.contains("GL_NV_gpu_shader5") && var2 != false) {
            remove(var2, "GL_NV_gpu_shader5");
         }

         if (var2.contains("GL_NV_half_float") && this != false) {
            remove(var2, "GL_NV_half_float");
         }

         if (var2.contains("GL_NV_occlusion_query") && this != false) {
            remove(var2, "GL_NV_occlusion_query");
         }

         if (var2.contains("GL_NV_parameter_buffer_object") && this != false) {
            remove(var2, "GL_NV_parameter_buffer_object");
         }

         if (var2.contains("GL_NV_path_rendering") && this != false) {
            remove(var2, "GL_NV_path_rendering");
         }

         if (var2.contains("GL_NV_pixel_data_range") && this != false) {
            remove(var2, "GL_NV_pixel_data_range");
         }

         if (var2.contains("GL_NV_point_sprite") && this != false) {
            remove(var2, "GL_NV_point_sprite");
         }

         if (var2.contains("GL_NV_present_video") && this != false) {
            remove(var2, "GL_NV_present_video");
         }

         var2.add("GL_NV_primitive_restart");
         if (var2.contains("GL_NV_primitive_restart") && this != false) {
            remove(var2, "GL_NV_primitive_restart");
         }

         if (var2.contains("GL_NV_program") && this != false) {
            remove(var2, "GL_NV_program");
         }

         if (var2.contains("GL_NV_register_combiners") && this != false) {
            remove(var2, "GL_NV_register_combiners");
         }

         if (var2.contains("GL_NV_register_combiners2") && this != false) {
            remove(var2, "GL_NV_register_combiners2");
         }

         if (var2.contains("GL_NV_shader_buffer_load") && this != false) {
            remove(var2, "GL_NV_shader_buffer_load");
         }

         if (var2.contains("GL_NV_texture_barrier") && this != false) {
            remove(var2, "GL_NV_texture_barrier");
         }

         if (var2.contains("GL_NV_texture_multisample") && this != false) {
            remove(var2, "GL_NV_texture_multisample");
         }

         if (var2.contains("GL_NV_transform_feedback") && this != false) {
            remove(var2, "GL_NV_transform_feedback");
         }

         if (var2.contains("GL_NV_transform_feedback2") && this != false) {
            remove(var2, "GL_NV_transform_feedback2");
         }

         if (var2.contains("GL_NV_vertex_array_range") && this != false) {
            remove(var2, "GL_NV_vertex_array_range");
         }

         if (var2.contains("GL_NV_vertex_attrib_integer_64bit") && var2 != false) {
            remove(var2, "GL_NV_vertex_attrib_integer_64bit");
         }

         if (var2.contains("GL_NV_vertex_buffer_unified_memory") && this != false) {
            remove(var2, "GL_NV_vertex_buffer_unified_memory");
         }

         if (var2.contains("GL_NV_vertex_program") && this != false) {
            remove(var2, "GL_NV_vertex_program");
         }

         if (var2.contains("GL_NV_video_capture") && this != false) {
            remove(var2, "GL_NV_video_capture");
         }

         return var2;
      }
   }

   static void unloadAllStubs() {
   }

   ContextCapabilities(boolean var1) throws LWJGLException {
      Set var2 = this.initAllStubs(var1);
      this.GL_AMD_blend_minmax_factor = var2.contains("GL_AMD_blend_minmax_factor");
      this.GL_AMD_conservative_depth = var2.contains("GL_AMD_conservative_depth");
      this.GL_AMD_debug_output = var2.contains("GL_AMD_debug_output") || var2.contains("GL_AMDX_debug_output");
      this.GL_AMD_depth_clamp_separate = var2.contains("GL_AMD_depth_clamp_separate");
      this.GL_AMD_draw_buffers_blend = var2.contains("GL_AMD_draw_buffers_blend");
      this.GL_AMD_interleaved_elements = var2.contains("GL_AMD_interleaved_elements");
      this.GL_AMD_multi_draw_indirect = var2.contains("GL_AMD_multi_draw_indirect");
      this.GL_AMD_name_gen_delete = var2.contains("GL_AMD_name_gen_delete");
      this.GL_AMD_performance_monitor = var2.contains("GL_AMD_performance_monitor");
      this.GL_AMD_pinned_memory = var2.contains("GL_AMD_pinned_memory");
      this.GL_AMD_query_buffer_object = var2.contains("GL_AMD_query_buffer_object");
      this.GL_AMD_sample_positions = var2.contains("GL_AMD_sample_positions");
      this.GL_AMD_seamless_cubemap_per_texture = var2.contains("GL_AMD_seamless_cubemap_per_texture");
      this.GL_AMD_shader_atomic_counter_ops = var2.contains("GL_AMD_shader_atomic_counter_ops");
      this.GL_AMD_shader_stencil_export = var2.contains("GL_AMD_shader_stencil_export");
      this.GL_AMD_shader_trinary_minmax = var2.contains("GL_AMD_shader_trinary_minmax");
      this.GL_AMD_sparse_texture = var2.contains("GL_AMD_sparse_texture");
      this.GL_AMD_stencil_operation_extended = var2.contains("GL_AMD_stencil_operation_extended");
      this.GL_AMD_texture_texture4 = var2.contains("GL_AMD_texture_texture4");
      this.GL_AMD_transform_feedback3_lines_triangles = var2.contains("GL_AMD_transform_feedback3_lines_triangles");
      this.GL_AMD_vertex_shader_layer = var2.contains("GL_AMD_vertex_shader_layer");
      this.GL_AMD_vertex_shader_tessellator = var2.contains("GL_AMD_vertex_shader_tessellator");
      this.GL_AMD_vertex_shader_viewport_index = var2.contains("GL_AMD_vertex_shader_viewport_index");
      this.GL_APPLE_aux_depth_stencil = var2.contains("GL_APPLE_aux_depth_stencil");
      this.GL_APPLE_client_storage = var2.contains("GL_APPLE_client_storage");
      this.GL_APPLE_element_array = var2.contains("GL_APPLE_element_array");
      this.GL_APPLE_fence = var2.contains("GL_APPLE_fence");
      this.GL_APPLE_float_pixels = var2.contains("GL_APPLE_float_pixels");
      this.GL_APPLE_flush_buffer_range = var2.contains("GL_APPLE_flush_buffer_range");
      this.GL_APPLE_object_purgeable = var2.contains("GL_APPLE_object_purgeable");
      this.GL_APPLE_packed_pixels = var2.contains("GL_APPLE_packed_pixels");
      this.GL_APPLE_rgb_422 = var2.contains("GL_APPLE_rgb_422");
      this.GL_APPLE_row_bytes = var2.contains("GL_APPLE_row_bytes");
      this.GL_APPLE_texture_range = var2.contains("GL_APPLE_texture_range");
      this.GL_APPLE_vertex_array_object = var2.contains("GL_APPLE_vertex_array_object");
      this.GL_APPLE_vertex_array_range = var2.contains("GL_APPLE_vertex_array_range");
      this.GL_APPLE_vertex_program_evaluators = var2.contains("GL_APPLE_vertex_program_evaluators");
      this.GL_APPLE_ycbcr_422 = var2.contains("GL_APPLE_ycbcr_422");
      this.GL_ARB_ES2_compatibility = var2.contains("GL_ARB_ES2_compatibility");
      this.GL_ARB_ES3_compatibility = var2.contains("GL_ARB_ES3_compatibility");
      this.GL_ARB_arrays_of_arrays = var2.contains("GL_ARB_arrays_of_arrays");
      this.GL_ARB_base_instance = var2.contains("GL_ARB_base_instance");
      this.GL_ARB_bindless_texture = var2.contains("GL_ARB_bindless_texture");
      this.GL_ARB_blend_func_extended = var2.contains("GL_ARB_blend_func_extended");
      this.GL_ARB_buffer_storage = var2.contains("GL_ARB_buffer_storage");
      this.GL_ARB_cl_event = var2.contains("GL_ARB_cl_event");
      this.GL_ARB_clear_buffer_object = var2.contains("GL_ARB_clear_buffer_object");
      this.GL_ARB_clear_texture = var2.contains("GL_ARB_clear_texture");
      this.GL_ARB_color_buffer_float = var2.contains("GL_ARB_color_buffer_float");
      this.GL_ARB_compatibility = var2.contains("GL_ARB_compatibility");
      this.GL_ARB_compressed_texture_pixel_storage = var2.contains("GL_ARB_compressed_texture_pixel_storage");
      this.GL_ARB_compute_shader = var2.contains("GL_ARB_compute_shader");
      this.GL_ARB_compute_variable_group_size = var2.contains("GL_ARB_compute_variable_group_size");
      this.GL_ARB_conservative_depth = var2.contains("GL_ARB_conservative_depth");
      this.GL_ARB_copy_buffer = var2.contains("GL_ARB_copy_buffer");
      this.GL_ARB_copy_image = var2.contains("GL_ARB_copy_image");
      this.GL_ARB_debug_output = var2.contains("GL_ARB_debug_output");
      this.GL_ARB_depth_buffer_float = var2.contains("GL_ARB_depth_buffer_float");
      this.GL_ARB_depth_clamp = var2.contains("GL_ARB_depth_clamp");
      this.GL_ARB_depth_texture = var2.contains("GL_ARB_depth_texture");
      this.GL_ARB_draw_buffers = var2.contains("GL_ARB_draw_buffers");
      this.GL_ARB_draw_buffers_blend = var2.contains("GL_ARB_draw_buffers_blend");
      this.GL_ARB_draw_elements_base_vertex = var2.contains("GL_ARB_draw_elements_base_vertex");
      this.GL_ARB_draw_indirect = var2.contains("GL_ARB_draw_indirect");
      this.GL_ARB_draw_instanced = var2.contains("GL_ARB_draw_instanced");
      this.GL_ARB_enhanced_layouts = var2.contains("GL_ARB_enhanced_layouts");
      this.GL_ARB_explicit_attrib_location = var2.contains("GL_ARB_explicit_attrib_location");
      this.GL_ARB_explicit_uniform_location = var2.contains("GL_ARB_explicit_uniform_location");
      this.GL_ARB_fragment_coord_conventions = var2.contains("GL_ARB_fragment_coord_conventions");
      this.GL_ARB_fragment_layer_viewport = var2.contains("GL_ARB_fragment_layer_viewport");
      this.GL_ARB_fragment_program = var2.contains("GL_ARB_fragment_program") && var2.contains("GL_ARB_program");
      this.GL_ARB_fragment_program_shadow = var2.contains("GL_ARB_fragment_program_shadow");
      this.GL_ARB_fragment_shader = var2.contains("GL_ARB_fragment_shader");
      this.GL_ARB_framebuffer_no_attachments = var2.contains("GL_ARB_framebuffer_no_attachments");
      this.GL_ARB_framebuffer_object = var2.contains("GL_ARB_framebuffer_object");
      this.GL_ARB_framebuffer_sRGB = var2.contains("GL_ARB_framebuffer_sRGB");
      this.GL_ARB_geometry_shader4 = var2.contains("GL_ARB_geometry_shader4");
      this.GL_ARB_get_program_binary = var2.contains("GL_ARB_get_program_binary");
      this.GL_ARB_gpu_shader5 = var2.contains("GL_ARB_gpu_shader5");
      this.GL_ARB_gpu_shader_fp64 = var2.contains("GL_ARB_gpu_shader_fp64");
      this.GL_ARB_half_float_pixel = var2.contains("GL_ARB_half_float_pixel");
      this.GL_ARB_half_float_vertex = var2.contains("GL_ARB_half_float_vertex");
      this.GL_ARB_imaging = var2.contains("GL_ARB_imaging");
      this.GL_ARB_indirect_parameters = var2.contains("GL_ARB_indirect_parameters");
      this.GL_ARB_instanced_arrays = var2.contains("GL_ARB_instanced_arrays");
      this.GL_ARB_internalformat_query = var2.contains("GL_ARB_internalformat_query");
      this.GL_ARB_internalformat_query2 = var2.contains("GL_ARB_internalformat_query2");
      this.GL_ARB_invalidate_subdata = var2.contains("GL_ARB_invalidate_subdata");
      this.GL_ARB_map_buffer_alignment = var2.contains("GL_ARB_map_buffer_alignment");
      this.GL_ARB_map_buffer_range = var2.contains("GL_ARB_map_buffer_range");
      this.GL_ARB_matrix_palette = var2.contains("GL_ARB_matrix_palette");
      this.GL_ARB_multi_bind = var2.contains("GL_ARB_multi_bind");
      this.GL_ARB_multi_draw_indirect = var2.contains("GL_ARB_multi_draw_indirect");
      this.GL_ARB_multisample = var2.contains("GL_ARB_multisample");
      this.GL_ARB_multitexture = var2.contains("GL_ARB_multitexture");
      this.GL_ARB_occlusion_query = var2.contains("GL_ARB_occlusion_query");
      this.GL_ARB_occlusion_query2 = var2.contains("GL_ARB_occlusion_query2");
      this.GL_ARB_pixel_buffer_object = var2.contains("GL_ARB_pixel_buffer_object") && var2.contains("GL_ARB_buffer_object");
      this.GL_ARB_point_parameters = var2.contains("GL_ARB_point_parameters");
      this.GL_ARB_point_sprite = var2.contains("GL_ARB_point_sprite");
      this.GL_ARB_program_interface_query = var2.contains("GL_ARB_program_interface_query");
      this.GL_ARB_provoking_vertex = var2.contains("GL_ARB_provoking_vertex");
      this.GL_ARB_query_buffer_object = var2.contains("GL_ARB_query_buffer_object");
      this.GL_ARB_robust_buffer_access_behavior = var2.contains("GL_ARB_robust_buffer_access_behavior");
      this.GL_ARB_robustness = var2.contains("GL_ARB_robustness");
      this.GL_ARB_robustness_isolation = var2.contains("GL_ARB_robustness_isolation");
      this.GL_ARB_sample_shading = var2.contains("GL_ARB_sample_shading");
      this.GL_ARB_sampler_objects = var2.contains("GL_ARB_sampler_objects");
      this.GL_ARB_seamless_cube_map = var2.contains("GL_ARB_seamless_cube_map");
      this.GL_ARB_seamless_cubemap_per_texture = var2.contains("GL_ARB_seamless_cubemap_per_texture");
      this.GL_ARB_separate_shader_objects = var2.contains("GL_ARB_separate_shader_objects");
      this.GL_ARB_shader_atomic_counters = var2.contains("GL_ARB_shader_atomic_counters");
      this.GL_ARB_shader_bit_encoding = var2.contains("GL_ARB_shader_bit_encoding");
      this.GL_ARB_shader_draw_parameters = var2.contains("GL_ARB_shader_draw_parameters");
      this.GL_ARB_shader_group_vote = var2.contains("GL_ARB_shader_group_vote");
      this.GL_ARB_shader_image_load_store = var2.contains("GL_ARB_shader_image_load_store");
      this.GL_ARB_shader_image_size = var2.contains("GL_ARB_shader_image_size");
      this.GL_ARB_shader_objects = var2.contains("GL_ARB_shader_objects");
      this.GL_ARB_shader_precision = var2.contains("GL_ARB_shader_precision");
      this.GL_ARB_shader_stencil_export = var2.contains("GL_ARB_shader_stencil_export");
      this.GL_ARB_shader_storage_buffer_object = var2.contains("GL_ARB_shader_storage_buffer_object");
      this.GL_ARB_shader_subroutine = var2.contains("GL_ARB_shader_subroutine");
      this.GL_ARB_shader_texture_lod = var2.contains("GL_ARB_shader_texture_lod");
      this.GL_ARB_shading_language_100 = var2.contains("GL_ARB_shading_language_100");
      this.GL_ARB_shading_language_420pack = var2.contains("GL_ARB_shading_language_420pack");
      this.GL_ARB_shading_language_include = var2.contains("GL_ARB_shading_language_include");
      this.GL_ARB_shading_language_packing = var2.contains("GL_ARB_shading_language_packing");
      this.GL_ARB_shadow = var2.contains("GL_ARB_shadow");
      this.GL_ARB_shadow_ambient = var2.contains("GL_ARB_shadow_ambient");
      this.GL_ARB_sparse_texture = var2.contains("GL_ARB_sparse_texture");
      this.GL_ARB_stencil_texturing = var2.contains("GL_ARB_stencil_texturing");
      this.GL_ARB_sync = var2.contains("GL_ARB_sync");
      this.GL_ARB_tessellation_shader = var2.contains("GL_ARB_tessellation_shader");
      this.GL_ARB_texture_border_clamp = var2.contains("GL_ARB_texture_border_clamp");
      this.GL_ARB_texture_buffer_object = var2.contains("GL_ARB_texture_buffer_object");
      this.GL_ARB_texture_buffer_object_rgb32 = var2.contains("GL_ARB_texture_buffer_object_rgb32") || var2.contains("GL_EXT_texture_buffer_object_rgb32");
      this.GL_ARB_texture_buffer_range = var2.contains("GL_ARB_texture_buffer_range");
      this.GL_ARB_texture_compression = var2.contains("GL_ARB_texture_compression");
      this.GL_ARB_texture_compression_bptc = var2.contains("GL_ARB_texture_compression_bptc") || var2.contains("GL_EXT_texture_compression_bptc");
      this.GL_ARB_texture_compression_rgtc = var2.contains("GL_ARB_texture_compression_rgtc");
      this.GL_ARB_texture_cube_map = var2.contains("GL_ARB_texture_cube_map");
      this.GL_ARB_texture_cube_map_array = var2.contains("GL_ARB_texture_cube_map_array");
      this.GL_ARB_texture_env_add = var2.contains("GL_ARB_texture_env_add");
      this.GL_ARB_texture_env_combine = var2.contains("GL_ARB_texture_env_combine");
      this.GL_ARB_texture_env_crossbar = var2.contains("GL_ARB_texture_env_crossbar");
      this.GL_ARB_texture_env_dot3 = var2.contains("GL_ARB_texture_env_dot3");
      this.GL_ARB_texture_float = var2.contains("GL_ARB_texture_float");
      this.GL_ARB_texture_gather = var2.contains("GL_ARB_texture_gather");
      this.GL_ARB_texture_mirror_clamp_to_edge = var2.contains("GL_ARB_texture_mirror_clamp_to_edge");
      this.GL_ARB_texture_mirrored_repeat = var2.contains("GL_ARB_texture_mirrored_repeat");
      this.GL_ARB_texture_multisample = var2.contains("GL_ARB_texture_multisample");
      this.GL_ARB_texture_non_power_of_two = var2.contains("GL_ARB_texture_non_power_of_two");
      this.GL_ARB_texture_query_levels = var2.contains("GL_ARB_texture_query_levels");
      this.GL_ARB_texture_query_lod = var2.contains("GL_ARB_texture_query_lod");
      this.GL_ARB_texture_rectangle = var2.contains("GL_ARB_texture_rectangle");
      this.GL_ARB_texture_rg = var2.contains("GL_ARB_texture_rg");
      this.GL_ARB_texture_rgb10_a2ui = var2.contains("GL_ARB_texture_rgb10_a2ui");
      this.GL_ARB_texture_stencil8 = var2.contains("GL_ARB_texture_stencil8");
      this.GL_ARB_texture_storage = var2.contains("GL_ARB_texture_storage") || var2.contains("GL_EXT_texture_storage");
      this.GL_ARB_texture_storage_multisample = var2.contains("GL_ARB_texture_storage_multisample");
      this.GL_ARB_texture_swizzle = var2.contains("GL_ARB_texture_swizzle");
      this.GL_ARB_texture_view = var2.contains("GL_ARB_texture_view");
      this.GL_ARB_timer_query = var2.contains("GL_ARB_timer_query");
      this.GL_ARB_transform_feedback2 = var2.contains("GL_ARB_transform_feedback2");
      this.GL_ARB_transform_feedback3 = var2.contains("GL_ARB_transform_feedback3");
      this.GL_ARB_transform_feedback_instanced = var2.contains("GL_ARB_transform_feedback_instanced");
      this.GL_ARB_transpose_matrix = var2.contains("GL_ARB_transpose_matrix");
      this.GL_ARB_uniform_buffer_object = var2.contains("GL_ARB_uniform_buffer_object");
      this.GL_ARB_vertex_array_bgra = var2.contains("GL_ARB_vertex_array_bgra");
      this.GL_ARB_vertex_array_object = var2.contains("GL_ARB_vertex_array_object");
      this.GL_ARB_vertex_attrib_64bit = var2.contains("GL_ARB_vertex_attrib_64bit");
      this.GL_ARB_vertex_attrib_binding = var2.contains("GL_ARB_vertex_attrib_binding");
      this.GL_ARB_vertex_blend = var2.contains("GL_ARB_vertex_blend");
      this.GL_ARB_vertex_buffer_object = var2.contains("GL_ARB_vertex_buffer_object") && var2.contains("GL_ARB_buffer_object");
      this.GL_ARB_vertex_program = var2.contains("GL_ARB_vertex_program") && var2.contains("GL_ARB_program");
      this.GL_ARB_vertex_shader = var2.contains("GL_ARB_vertex_shader");
      this.GL_ARB_vertex_type_10f_11f_11f_rev = var2.contains("GL_ARB_vertex_type_10f_11f_11f_rev");
      this.GL_ARB_vertex_type_2_10_10_10_rev = var2.contains("GL_ARB_vertex_type_2_10_10_10_rev");
      this.GL_ARB_viewport_array = var2.contains("GL_ARB_viewport_array");
      this.GL_ARB_window_pos = var2.contains("GL_ARB_window_pos");
      this.GL_ATI_draw_buffers = var2.contains("GL_ATI_draw_buffers");
      this.GL_ATI_element_array = var2.contains("GL_ATI_element_array");
      this.GL_ATI_envmap_bumpmap = var2.contains("GL_ATI_envmap_bumpmap");
      this.GL_ATI_fragment_shader = var2.contains("GL_ATI_fragment_shader");
      this.GL_ATI_map_object_buffer = var2.contains("GL_ATI_map_object_buffer");
      this.GL_ATI_meminfo = var2.contains("GL_ATI_meminfo");
      this.GL_ATI_pn_triangles = var2.contains("GL_ATI_pn_triangles");
      this.GL_ATI_separate_stencil = var2.contains("GL_ATI_separate_stencil");
      this.GL_ATI_shader_texture_lod = var2.contains("GL_ATI_shader_texture_lod");
      this.GL_ATI_text_fragment_shader = var2.contains("GL_ATI_text_fragment_shader");
      this.GL_ATI_texture_compression_3dc = var2.contains("GL_ATI_texture_compression_3dc");
      this.GL_ATI_texture_env_combine3 = var2.contains("GL_ATI_texture_env_combine3");
      this.GL_ATI_texture_float = var2.contains("GL_ATI_texture_float");
      this.GL_ATI_texture_mirror_once = var2.contains("GL_ATI_texture_mirror_once");
      this.GL_ATI_vertex_array_object = var2.contains("GL_ATI_vertex_array_object");
      this.GL_ATI_vertex_attrib_array_object = var2.contains("GL_ATI_vertex_attrib_array_object");
      this.GL_ATI_vertex_streams = var2.contains("GL_ATI_vertex_streams");
      this.GL_EXT_abgr = var2.contains("GL_EXT_abgr");
      this.GL_EXT_bgra = var2.contains("GL_EXT_bgra");
      this.GL_EXT_bindable_uniform = var2.contains("GL_EXT_bindable_uniform");
      this.GL_EXT_blend_color = var2.contains("GL_EXT_blend_color");
      this.GL_EXT_blend_equation_separate = var2.contains("GL_EXT_blend_equation_separate");
      this.GL_EXT_blend_func_separate = var2.contains("GL_EXT_blend_func_separate");
      this.GL_EXT_blend_minmax = var2.contains("GL_EXT_blend_minmax");
      this.GL_EXT_blend_subtract = var2.contains("GL_EXT_blend_subtract");
      this.GL_EXT_Cg_shader = var2.contains("GL_EXT_Cg_shader");
      this.GL_EXT_compiled_vertex_array = var2.contains("GL_EXT_compiled_vertex_array");
      this.GL_EXT_depth_bounds_test = var2.contains("GL_EXT_depth_bounds_test");
      this.GL_EXT_direct_state_access = var2.contains("GL_EXT_direct_state_access");
      this.GL_EXT_draw_buffers2 = var2.contains("GL_EXT_draw_buffers2");
      this.GL_EXT_draw_instanced = var2.contains("GL_EXT_draw_instanced");
      this.GL_EXT_draw_range_elements = var2.contains("GL_EXT_draw_range_elements");
      this.GL_EXT_fog_coord = var2.contains("GL_EXT_fog_coord");
      this.GL_EXT_framebuffer_blit = var2.contains("GL_EXT_framebuffer_blit");
      this.GL_EXT_framebuffer_multisample = var2.contains("GL_EXT_framebuffer_multisample");
      this.GL_EXT_framebuffer_multisample_blit_scaled = var2.contains("GL_EXT_framebuffer_multisample_blit_scaled");
      this.GL_EXT_framebuffer_object = var2.contains("GL_EXT_framebuffer_object");
      this.GL_EXT_framebuffer_sRGB = var2.contains("GL_EXT_framebuffer_sRGB");
      this.GL_EXT_geometry_shader4 = var2.contains("GL_EXT_geometry_shader4");
      this.GL_EXT_gpu_program_parameters = var2.contains("GL_EXT_gpu_program_parameters");
      this.GL_EXT_gpu_shader4 = var2.contains("GL_EXT_gpu_shader4");
      this.GL_EXT_multi_draw_arrays = var2.contains("GL_EXT_multi_draw_arrays");
      this.GL_EXT_packed_depth_stencil = var2.contains("GL_EXT_packed_depth_stencil");
      this.GL_EXT_packed_float = var2.contains("GL_EXT_packed_float");
      this.GL_EXT_packed_pixels = var2.contains("GL_EXT_packed_pixels");
      this.GL_EXT_paletted_texture = var2.contains("GL_EXT_paletted_texture");
      this.GL_EXT_pixel_buffer_object = var2.contains("GL_EXT_pixel_buffer_object") && var2.contains("GL_ARB_buffer_object");
      this.GL_EXT_point_parameters = var2.contains("GL_EXT_point_parameters");
      this.GL_EXT_provoking_vertex = var2.contains("GL_EXT_provoking_vertex");
      this.GL_EXT_rescale_normal = var2.contains("GL_EXT_rescale_normal");
      this.GL_EXT_secondary_color = var2.contains("GL_EXT_secondary_color");
      this.GL_EXT_separate_shader_objects = var2.contains("GL_EXT_separate_shader_objects");
      this.GL_EXT_separate_specular_color = var2.contains("GL_EXT_separate_specular_color");
      this.GL_EXT_shader_image_load_store = var2.contains("GL_EXT_shader_image_load_store");
      this.GL_EXT_shadow_funcs = var2.contains("GL_EXT_shadow_funcs");
      this.GL_EXT_shared_texture_palette = var2.contains("GL_EXT_shared_texture_palette");
      this.GL_EXT_stencil_clear_tag = var2.contains("GL_EXT_stencil_clear_tag");
      this.GL_EXT_stencil_two_side = var2.contains("GL_EXT_stencil_two_side");
      this.GL_EXT_stencil_wrap = var2.contains("GL_EXT_stencil_wrap");
      this.GL_EXT_texture_3d = var2.contains("GL_EXT_texture_3d");
      this.GL_EXT_texture_array = var2.contains("GL_EXT_texture_array");
      this.GL_EXT_texture_buffer_object = var2.contains("GL_EXT_texture_buffer_object");
      this.GL_EXT_texture_compression_latc = var2.contains("GL_EXT_texture_compression_latc");
      this.GL_EXT_texture_compression_rgtc = var2.contains("GL_EXT_texture_compression_rgtc");
      this.GL_EXT_texture_compression_s3tc = var2.contains("GL_EXT_texture_compression_s3tc");
      this.GL_EXT_texture_env_combine = var2.contains("GL_EXT_texture_env_combine");
      this.GL_EXT_texture_env_dot3 = var2.contains("GL_EXT_texture_env_dot3");
      this.GL_EXT_texture_filter_anisotropic = var2.contains("GL_EXT_texture_filter_anisotropic");
      this.GL_EXT_texture_integer = var2.contains("GL_EXT_texture_integer");
      this.GL_EXT_texture_lod_bias = var2.contains("GL_EXT_texture_lod_bias");
      this.GL_EXT_texture_mirror_clamp = var2.contains("GL_EXT_texture_mirror_clamp");
      this.GL_EXT_texture_rectangle = var2.contains("GL_EXT_texture_rectangle");
      this.GL_EXT_texture_sRGB = var2.contains("GL_EXT_texture_sRGB");
      this.GL_EXT_texture_sRGB_decode = var2.contains("GL_EXT_texture_sRGB_decode");
      this.GL_EXT_texture_shared_exponent = var2.contains("GL_EXT_texture_shared_exponent");
      this.GL_EXT_texture_snorm = var2.contains("GL_EXT_texture_snorm");
      this.GL_EXT_texture_swizzle = var2.contains("GL_EXT_texture_swizzle");
      this.GL_EXT_timer_query = var2.contains("GL_EXT_timer_query");
      this.GL_EXT_transform_feedback = var2.contains("GL_EXT_transform_feedback");
      this.GL_EXT_vertex_array_bgra = var2.contains("GL_EXT_vertex_array_bgra");
      this.GL_EXT_vertex_attrib_64bit = var2.contains("GL_EXT_vertex_attrib_64bit");
      this.GL_EXT_vertex_shader = var2.contains("GL_EXT_vertex_shader");
      this.GL_EXT_vertex_weighting = var2.contains("GL_EXT_vertex_weighting");
      this.OpenGL11 = var2.contains("OpenGL11");
      this.OpenGL12 = var2.contains("OpenGL12");
      this.OpenGL13 = var2.contains("OpenGL13");
      this.OpenGL14 = var2.contains("OpenGL14");
      this.OpenGL15 = var2.contains("OpenGL15");
      this.OpenGL20 = var2.contains("OpenGL20");
      this.OpenGL21 = var2.contains("OpenGL21");
      this.OpenGL30 = var2.contains("OpenGL30");
      this.OpenGL31 = var2.contains("OpenGL31");
      this.OpenGL32 = var2.contains("OpenGL32");
      this.OpenGL33 = var2.contains("OpenGL33");
      this.OpenGL40 = var2.contains("OpenGL40");
      this.OpenGL41 = var2.contains("OpenGL41");
      this.OpenGL42 = var2.contains("OpenGL42");
      this.OpenGL43 = var2.contains("OpenGL43");
      this.OpenGL44 = var2.contains("OpenGL44");
      this.GL_GREMEDY_frame_terminator = var2.contains("GL_GREMEDY_frame_terminator");
      this.GL_GREMEDY_string_marker = var2.contains("GL_GREMEDY_string_marker");
      this.GL_HP_occlusion_test = var2.contains("GL_HP_occlusion_test");
      this.GL_IBM_rasterpos_clip = var2.contains("GL_IBM_rasterpos_clip");
      this.GL_INTEL_map_texture = var2.contains("GL_INTEL_map_texture");
      this.GL_KHR_debug = var2.contains("GL_KHR_debug");
      this.GL_KHR_texture_compression_astc_ldr = var2.contains("GL_KHR_texture_compression_astc_ldr");
      this.GL_NVX_gpu_memory_info = var2.contains("GL_NVX_gpu_memory_info");
      this.GL_NV_bindless_multi_draw_indirect = var2.contains("GL_NV_bindless_multi_draw_indirect");
      this.GL_NV_bindless_texture = var2.contains("GL_NV_bindless_texture");
      this.GL_NV_blend_equation_advanced = var2.contains("GL_NV_blend_equation_advanced");
      this.GL_NV_blend_square = var2.contains("GL_NV_blend_square");
      this.GL_NV_compute_program5 = var2.contains("GL_NV_compute_program5");
      this.GL_NV_conditional_render = var2.contains("GL_NV_conditional_render");
      this.GL_NV_copy_depth_to_color = var2.contains("GL_NV_copy_depth_to_color");
      this.GL_NV_copy_image = var2.contains("GL_NV_copy_image");
      this.GL_NV_deep_texture3D = var2.contains("GL_NV_deep_texture3D");
      this.GL_NV_depth_buffer_float = var2.contains("GL_NV_depth_buffer_float");
      this.GL_NV_depth_clamp = var2.contains("GL_NV_depth_clamp");
      this.GL_NV_draw_texture = var2.contains("GL_NV_draw_texture");
      this.GL_NV_evaluators = var2.contains("GL_NV_evaluators");
      this.GL_NV_explicit_multisample = var2.contains("GL_NV_explicit_multisample");
      this.GL_NV_fence = var2.contains("GL_NV_fence");
      this.GL_NV_float_buffer = var2.contains("GL_NV_float_buffer");
      this.GL_NV_fog_distance = var2.contains("GL_NV_fog_distance");
      this.GL_NV_fragment_program = var2.contains("GL_NV_fragment_program") && var2.contains("GL_NV_program");
      this.GL_NV_fragment_program2 = var2.contains("GL_NV_fragment_program2");
      this.GL_NV_fragment_program4 = var2.contains("GL_NV_fragment_program4");
      this.GL_NV_fragment_program_option = var2.contains("GL_NV_fragment_program_option");
      this.GL_NV_framebuffer_multisample_coverage = var2.contains("GL_NV_framebuffer_multisample_coverage");
      this.GL_NV_geometry_program4 = var2.contains("GL_NV_geometry_program4");
      this.GL_NV_geometry_shader4 = var2.contains("GL_NV_geometry_shader4");
      this.GL_NV_gpu_program4 = var2.contains("GL_NV_gpu_program4");
      this.GL_NV_gpu_program5 = var2.contains("GL_NV_gpu_program5");
      this.GL_NV_gpu_program5_mem_extended = var2.contains("GL_NV_gpu_program5_mem_extended");
      this.GL_NV_gpu_shader5 = var2.contains("GL_NV_gpu_shader5");
      this.GL_NV_half_float = var2.contains("GL_NV_half_float");
      this.GL_NV_light_max_exponent = var2.contains("GL_NV_light_max_exponent");
      this.GL_NV_multisample_coverage = var2.contains("GL_NV_multisample_coverage");
      this.GL_NV_multisample_filter_hint = var2.contains("GL_NV_multisample_filter_hint");
      this.GL_NV_occlusion_query = var2.contains("GL_NV_occlusion_query");
      this.GL_NV_packed_depth_stencil = var2.contains("GL_NV_packed_depth_stencil");
      this.GL_NV_parameter_buffer_object = var2.contains("GL_NV_parameter_buffer_object");
      this.GL_NV_parameter_buffer_object2 = var2.contains("GL_NV_parameter_buffer_object2");
      this.GL_NV_path_rendering = var2.contains("GL_NV_path_rendering");
      this.GL_NV_pixel_data_range = var2.contains("GL_NV_pixel_data_range");
      this.GL_NV_point_sprite = var2.contains("GL_NV_point_sprite");
      this.GL_NV_present_video = var2.contains("GL_NV_present_video");
      this.GL_NV_primitive_restart = var2.contains("GL_NV_primitive_restart");
      this.GL_NV_register_combiners = var2.contains("GL_NV_register_combiners");
      this.GL_NV_register_combiners2 = var2.contains("GL_NV_register_combiners2");
      this.GL_NV_shader_atomic_counters = var2.contains("GL_NV_shader_atomic_counters");
      this.GL_NV_shader_atomic_float = var2.contains("GL_NV_shader_atomic_float");
      this.GL_NV_shader_buffer_load = var2.contains("GL_NV_shader_buffer_load");
      this.GL_NV_shader_buffer_store = var2.contains("GL_NV_shader_buffer_store");
      this.GL_NV_shader_storage_buffer_object = var2.contains("GL_NV_shader_storage_buffer_object");
      this.GL_NV_tessellation_program5 = var2.contains("GL_NV_tessellation_program5");
      this.GL_NV_texgen_reflection = var2.contains("GL_NV_texgen_reflection");
      this.GL_NV_texture_barrier = var2.contains("GL_NV_texture_barrier");
      this.GL_NV_texture_compression_vtc = var2.contains("GL_NV_texture_compression_vtc");
      this.GL_NV_texture_env_combine4 = var2.contains("GL_NV_texture_env_combine4");
      this.GL_NV_texture_expand_normal = var2.contains("GL_NV_texture_expand_normal");
      this.GL_NV_texture_multisample = var2.contains("GL_NV_texture_multisample");
      this.GL_NV_texture_rectangle = var2.contains("GL_NV_texture_rectangle");
      this.GL_NV_texture_shader = var2.contains("GL_NV_texture_shader");
      this.GL_NV_texture_shader2 = var2.contains("GL_NV_texture_shader2");
      this.GL_NV_texture_shader3 = var2.contains("GL_NV_texture_shader3");
      this.GL_NV_transform_feedback = var2.contains("GL_NV_transform_feedback");
      this.GL_NV_transform_feedback2 = var2.contains("GL_NV_transform_feedback2");
      this.GL_NV_vertex_array_range = var2.contains("GL_NV_vertex_array_range");
      this.GL_NV_vertex_array_range2 = var2.contains("GL_NV_vertex_array_range2");
      this.GL_NV_vertex_attrib_integer_64bit = var2.contains("GL_NV_vertex_attrib_integer_64bit");
      this.GL_NV_vertex_buffer_unified_memory = var2.contains("GL_NV_vertex_buffer_unified_memory");
      this.GL_NV_vertex_program = var2.contains("GL_NV_vertex_program") && var2.contains("GL_NV_program");
      this.GL_NV_vertex_program1_1 = var2.contains("GL_NV_vertex_program1_1");
      this.GL_NV_vertex_program2 = var2.contains("GL_NV_vertex_program2");
      this.GL_NV_vertex_program2_option = var2.contains("GL_NV_vertex_program2_option");
      this.GL_NV_vertex_program3 = var2.contains("GL_NV_vertex_program3");
      this.GL_NV_vertex_program4 = var2.contains("GL_NV_vertex_program4");
      this.GL_NV_video_capture = var2.contains("GL_NV_video_capture");
      this.GL_SGIS_generate_mipmap = var2.contains("GL_SGIS_generate_mipmap");
      this.GL_SGIS_texture_lod = var2.contains("GL_SGIS_texture_lod");
      this.GL_SUN_slice_accum = var2.contains("GL_SUN_slice_accum");
      this.tracker.init();
   }
}
