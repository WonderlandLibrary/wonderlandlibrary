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
    return (((this.glDebugMessageEnableAMD = GLContext.getFunctionAddress(new String[] { "glDebugMessageEnableAMD", "glDebugMessageEnableAMDX" })) != 0L)) & (((this.glDebugMessageInsertAMD = GLContext.getFunctionAddress(new String[] { "glDebugMessageInsertAMD", "glDebugMessageInsertAMDX" })) != 0L)) & (((this.glDebugMessageCallbackAMD = GLContext.getFunctionAddress(new String[] { "glDebugMessageCallbackAMD", "glDebugMessageCallbackAMDX" })) != 0L) ? 1 : 0) & (((this.glGetDebugMessageLogAMD = GLContext.getFunctionAddress(new String[] { "glGetDebugMessageLogAMD", "glGetDebugMessageLogAMDX" })) != 0L) ? 1 : 0);
  }
  
  private boolean AMD_draw_buffers_blend_initNativeFunctionAddresses() {
    return (((this.glBlendFuncIndexedAMD = GLContext.getFunctionAddress("glBlendFuncIndexedAMD")) != 0L)) & (((this.glBlendFuncSeparateIndexedAMD = GLContext.getFunctionAddress("glBlendFuncSeparateIndexedAMD")) != 0L)) & (((this.glBlendEquationIndexedAMD = GLContext.getFunctionAddress("glBlendEquationIndexedAMD")) != 0L) ? 1 : 0) & (((this.glBlendEquationSeparateIndexedAMD = GLContext.getFunctionAddress("glBlendEquationSeparateIndexedAMD")) != 0L) ? 1 : 0);
  }
  
  private boolean AMD_interleaved_elements_initNativeFunctionAddresses() {
    return ((this.glVertexAttribParameteriAMD = GLContext.getFunctionAddress("glVertexAttribParameteriAMD")) != 0L);
  }
  
  private boolean AMD_multi_draw_indirect_initNativeFunctionAddresses() {
    return (((this.glMultiDrawArraysIndirectAMD = GLContext.getFunctionAddress("glMultiDrawArraysIndirectAMD")) != 0L)) & (((this.glMultiDrawElementsIndirectAMD = GLContext.getFunctionAddress("glMultiDrawElementsIndirectAMD")) != 0L));
  }
  
  private boolean AMD_name_gen_delete_initNativeFunctionAddresses() {
    return (((this.glGenNamesAMD = GLContext.getFunctionAddress("glGenNamesAMD")) != 0L)) & (((this.glDeleteNamesAMD = GLContext.getFunctionAddress("glDeleteNamesAMD")) != 0L)) & (((this.glIsNameAMD = GLContext.getFunctionAddress("glIsNameAMD")) != 0L) ? 1 : 0);
  }
  
  private boolean AMD_performance_monitor_initNativeFunctionAddresses() {
    return (((this.glGetPerfMonitorGroupsAMD = GLContext.getFunctionAddress("glGetPerfMonitorGroupsAMD")) != 0L)) & (((this.glGetPerfMonitorCountersAMD = GLContext.getFunctionAddress("glGetPerfMonitorCountersAMD")) != 0L)) & (((this.glGetPerfMonitorGroupStringAMD = GLContext.getFunctionAddress("glGetPerfMonitorGroupStringAMD")) != 0L) ? 1 : 0) & (((this.glGetPerfMonitorCounterStringAMD = GLContext.getFunctionAddress("glGetPerfMonitorCounterStringAMD")) != 0L) ? 1 : 0) & (((this.glGetPerfMonitorCounterInfoAMD = GLContext.getFunctionAddress("glGetPerfMonitorCounterInfoAMD")) != 0L) ? 1 : 0) & (((this.glGenPerfMonitorsAMD = GLContext.getFunctionAddress("glGenPerfMonitorsAMD")) != 0L) ? 1 : 0) & (((this.glDeletePerfMonitorsAMD = GLContext.getFunctionAddress("glDeletePerfMonitorsAMD")) != 0L) ? 1 : 0) & (((this.glSelectPerfMonitorCountersAMD = GLContext.getFunctionAddress("glSelectPerfMonitorCountersAMD")) != 0L) ? 1 : 0) & (((this.glBeginPerfMonitorAMD = GLContext.getFunctionAddress("glBeginPerfMonitorAMD")) != 0L) ? 1 : 0) & (((this.glEndPerfMonitorAMD = GLContext.getFunctionAddress("glEndPerfMonitorAMD")) != 0L) ? 1 : 0) & (((this.glGetPerfMonitorCounterDataAMD = GLContext.getFunctionAddress("glGetPerfMonitorCounterDataAMD")) != 0L) ? 1 : 0);
  }
  
  private boolean AMD_sample_positions_initNativeFunctionAddresses() {
    return ((this.glSetMultisamplefvAMD = GLContext.getFunctionAddress("glSetMultisamplefvAMD")) != 0L);
  }
  
  private boolean AMD_sparse_texture_initNativeFunctionAddresses() {
    return (((this.glTexStorageSparseAMD = GLContext.getFunctionAddress("glTexStorageSparseAMD")) != 0L)) & (((this.glTextureStorageSparseAMD = GLContext.getFunctionAddress("glTextureStorageSparseAMD")) != 0L));
  }
  
  private boolean AMD_stencil_operation_extended_initNativeFunctionAddresses() {
    return ((this.glStencilOpValueAMD = GLContext.getFunctionAddress("glStencilOpValueAMD")) != 0L);
  }
  
  private boolean AMD_vertex_shader_tessellator_initNativeFunctionAddresses() {
    return (((this.glTessellationFactorAMD = GLContext.getFunctionAddress("glTessellationFactorAMD")) != 0L)) & (((this.glTessellationModeAMD = GLContext.getFunctionAddress("glTessellationModeAMD")) != 0L));
  }
  
  private boolean APPLE_element_array_initNativeFunctionAddresses() {
    return (((this.glElementPointerAPPLE = GLContext.getFunctionAddress("glElementPointerAPPLE")) != 0L)) & (((this.glDrawElementArrayAPPLE = GLContext.getFunctionAddress("glDrawElementArrayAPPLE")) != 0L)) & (((this.glDrawRangeElementArrayAPPLE = GLContext.getFunctionAddress("glDrawRangeElementArrayAPPLE")) != 0L) ? 1 : 0) & (((this.glMultiDrawElementArrayAPPLE = GLContext.getFunctionAddress("glMultiDrawElementArrayAPPLE")) != 0L) ? 1 : 0) & (((this.glMultiDrawRangeElementArrayAPPLE = GLContext.getFunctionAddress("glMultiDrawRangeElementArrayAPPLE")) != 0L) ? 1 : 0);
  }
  
  private boolean APPLE_fence_initNativeFunctionAddresses() {
    return (((this.glGenFencesAPPLE = GLContext.getFunctionAddress("glGenFencesAPPLE")) != 0L)) & (((this.glDeleteFencesAPPLE = GLContext.getFunctionAddress("glDeleteFencesAPPLE")) != 0L)) & (((this.glSetFenceAPPLE = GLContext.getFunctionAddress("glSetFenceAPPLE")) != 0L) ? 1 : 0) & (((this.glIsFenceAPPLE = GLContext.getFunctionAddress("glIsFenceAPPLE")) != 0L) ? 1 : 0) & (((this.glTestFenceAPPLE = GLContext.getFunctionAddress("glTestFenceAPPLE")) != 0L) ? 1 : 0) & (((this.glFinishFenceAPPLE = GLContext.getFunctionAddress("glFinishFenceAPPLE")) != 0L) ? 1 : 0) & (((this.glTestObjectAPPLE = GLContext.getFunctionAddress("glTestObjectAPPLE")) != 0L) ? 1 : 0) & (((this.glFinishObjectAPPLE = GLContext.getFunctionAddress("glFinishObjectAPPLE")) != 0L) ? 1 : 0);
  }
  
  private boolean APPLE_flush_buffer_range_initNativeFunctionAddresses() {
    return (((this.glBufferParameteriAPPLE = GLContext.getFunctionAddress("glBufferParameteriAPPLE")) != 0L)) & (((this.glFlushMappedBufferRangeAPPLE = GLContext.getFunctionAddress("glFlushMappedBufferRangeAPPLE")) != 0L));
  }
  
  private boolean APPLE_object_purgeable_initNativeFunctionAddresses() {
    return (((this.glObjectPurgeableAPPLE = GLContext.getFunctionAddress("glObjectPurgeableAPPLE")) != 0L)) & (((this.glObjectUnpurgeableAPPLE = GLContext.getFunctionAddress("glObjectUnpurgeableAPPLE")) != 0L)) & (((this.glGetObjectParameterivAPPLE = GLContext.getFunctionAddress("glGetObjectParameterivAPPLE")) != 0L) ? 1 : 0);
  }
  
  private boolean APPLE_texture_range_initNativeFunctionAddresses() {
    return (((this.glTextureRangeAPPLE = GLContext.getFunctionAddress("glTextureRangeAPPLE")) != 0L)) & (((this.glGetTexParameterPointervAPPLE = GLContext.getFunctionAddress("glGetTexParameterPointervAPPLE")) != 0L));
  }
  
  private boolean APPLE_vertex_array_object_initNativeFunctionAddresses() {
    return (((this.glBindVertexArrayAPPLE = GLContext.getFunctionAddress("glBindVertexArrayAPPLE")) != 0L)) & (((this.glDeleteVertexArraysAPPLE = GLContext.getFunctionAddress("glDeleteVertexArraysAPPLE")) != 0L)) & (((this.glGenVertexArraysAPPLE = GLContext.getFunctionAddress("glGenVertexArraysAPPLE")) != 0L) ? 1 : 0) & (((this.glIsVertexArrayAPPLE = GLContext.getFunctionAddress("glIsVertexArrayAPPLE")) != 0L) ? 1 : 0);
  }
  
  private boolean APPLE_vertex_array_range_initNativeFunctionAddresses() {
    return (((this.glVertexArrayRangeAPPLE = GLContext.getFunctionAddress("glVertexArrayRangeAPPLE")) != 0L)) & (((this.glFlushVertexArrayRangeAPPLE = GLContext.getFunctionAddress("glFlushVertexArrayRangeAPPLE")) != 0L)) & (((this.glVertexArrayParameteriAPPLE = GLContext.getFunctionAddress("glVertexArrayParameteriAPPLE")) != 0L) ? 1 : 0);
  }
  
  private boolean APPLE_vertex_program_evaluators_initNativeFunctionAddresses() {
    return (((this.glEnableVertexAttribAPPLE = GLContext.getFunctionAddress("glEnableVertexAttribAPPLE")) != 0L)) & (((this.glDisableVertexAttribAPPLE = GLContext.getFunctionAddress("glDisableVertexAttribAPPLE")) != 0L)) & (((this.glIsVertexAttribEnabledAPPLE = GLContext.getFunctionAddress("glIsVertexAttribEnabledAPPLE")) != 0L) ? 1 : 0) & (((this.glMapVertexAttrib1dAPPLE = GLContext.getFunctionAddress("glMapVertexAttrib1dAPPLE")) != 0L) ? 1 : 0) & (((this.glMapVertexAttrib1fAPPLE = GLContext.getFunctionAddress("glMapVertexAttrib1fAPPLE")) != 0L) ? 1 : 0) & (((this.glMapVertexAttrib2dAPPLE = GLContext.getFunctionAddress("glMapVertexAttrib2dAPPLE")) != 0L) ? 1 : 0) & (((this.glMapVertexAttrib2fAPPLE = GLContext.getFunctionAddress("glMapVertexAttrib2fAPPLE")) != 0L) ? 1 : 0);
  }
  
  private boolean ARB_ES2_compatibility_initNativeFunctionAddresses() {
    return (((this.glReleaseShaderCompiler = GLContext.getFunctionAddress("glReleaseShaderCompiler")) != 0L)) & (((this.glShaderBinary = GLContext.getFunctionAddress("glShaderBinary")) != 0L)) & (((this.glGetShaderPrecisionFormat = GLContext.getFunctionAddress("glGetShaderPrecisionFormat")) != 0L) ? 1 : 0) & (((this.glDepthRangef = GLContext.getFunctionAddress("glDepthRangef")) != 0L) ? 1 : 0) & (((this.glClearDepthf = GLContext.getFunctionAddress("glClearDepthf")) != 0L) ? 1 : 0);
  }
  
  private boolean ARB_ES3_1_compatibility_initNativeFunctionAddresses() {
    return ((this.glMemoryBarrierByRegion = GLContext.getFunctionAddress("glMemoryBarrierByRegion")) != 0L);
  }
  
  private boolean ARB_base_instance_initNativeFunctionAddresses() {
    return (((this.glDrawArraysInstancedBaseInstance = GLContext.getFunctionAddress("glDrawArraysInstancedBaseInstance")) != 0L)) & (((this.glDrawElementsInstancedBaseInstance = GLContext.getFunctionAddress("glDrawElementsInstancedBaseInstance")) != 0L)) & (((this.glDrawElementsInstancedBaseVertexBaseInstance = GLContext.getFunctionAddress("glDrawElementsInstancedBaseVertexBaseInstance")) != 0L) ? 1 : 0);
  }
  
  private boolean ARB_bindless_texture_initNativeFunctionAddresses() {
    return (((this.glGetTextureHandleARB = GLContext.getFunctionAddress("glGetTextureHandleARB")) != 0L)) & (((this.glGetTextureSamplerHandleARB = GLContext.getFunctionAddress("glGetTextureSamplerHandleARB")) != 0L)) & (((this.glMakeTextureHandleResidentARB = GLContext.getFunctionAddress("glMakeTextureHandleResidentARB")) != 0L) ? 1 : 0) & (((this.glMakeTextureHandleNonResidentARB = GLContext.getFunctionAddress("glMakeTextureHandleNonResidentARB")) != 0L) ? 1 : 0) & (((this.glGetImageHandleARB = GLContext.getFunctionAddress("glGetImageHandleARB")) != 0L) ? 1 : 0) & (((this.glMakeImageHandleResidentARB = GLContext.getFunctionAddress("glMakeImageHandleResidentARB")) != 0L) ? 1 : 0) & (((this.glMakeImageHandleNonResidentARB = GLContext.getFunctionAddress("glMakeImageHandleNonResidentARB")) != 0L) ? 1 : 0) & (((this.glUniformHandleui64ARB = GLContext.getFunctionAddress("glUniformHandleui64ARB")) != 0L) ? 1 : 0) & (((this.glUniformHandleui64vARB = GLContext.getFunctionAddress("glUniformHandleui64vARB")) != 0L) ? 1 : 0) & (((this.glProgramUniformHandleui64ARB = GLContext.getFunctionAddress("glProgramUniformHandleui64ARB")) != 0L) ? 1 : 0) & (((this.glProgramUniformHandleui64vARB = GLContext.getFunctionAddress("glProgramUniformHandleui64vARB")) != 0L) ? 1 : 0) & (((this.glIsTextureHandleResidentARB = GLContext.getFunctionAddress("glIsTextureHandleResidentARB")) != 0L) ? 1 : 0) & (((this.glIsImageHandleResidentARB = GLContext.getFunctionAddress("glIsImageHandleResidentARB")) != 0L) ? 1 : 0) & (((this.glVertexAttribL1ui64ARB = GLContext.getFunctionAddress("glVertexAttribL1ui64ARB")) != 0L) ? 1 : 0) & (((this.glVertexAttribL1ui64vARB = GLContext.getFunctionAddress("glVertexAttribL1ui64vARB")) != 0L) ? 1 : 0) & (((this.glGetVertexAttribLui64vARB = GLContext.getFunctionAddress("glGetVertexAttribLui64vARB")) != 0L) ? 1 : 0);
  }
  
  private boolean ARB_blend_func_extended_initNativeFunctionAddresses() {
    return (((this.glBindFragDataLocationIndexed = GLContext.getFunctionAddress("glBindFragDataLocationIndexed")) != 0L)) & (((this.glGetFragDataIndex = GLContext.getFunctionAddress("glGetFragDataIndex")) != 0L));
  }
  
  private boolean ARB_buffer_object_initNativeFunctionAddresses() {
    return (((this.glBindBufferARB = GLContext.getFunctionAddress("glBindBufferARB")) != 0L)) & (((this.glDeleteBuffersARB = GLContext.getFunctionAddress("glDeleteBuffersARB")) != 0L)) & (((this.glGenBuffersARB = GLContext.getFunctionAddress("glGenBuffersARB")) != 0L) ? 1 : 0) & (((this.glIsBufferARB = GLContext.getFunctionAddress("glIsBufferARB")) != 0L) ? 1 : 0) & (((this.glBufferDataARB = GLContext.getFunctionAddress("glBufferDataARB")) != 0L) ? 1 : 0) & (((this.glBufferSubDataARB = GLContext.getFunctionAddress("glBufferSubDataARB")) != 0L) ? 1 : 0) & (((this.glGetBufferSubDataARB = GLContext.getFunctionAddress("glGetBufferSubDataARB")) != 0L) ? 1 : 0) & (((this.glMapBufferARB = GLContext.getFunctionAddress("glMapBufferARB")) != 0L) ? 1 : 0) & (((this.glUnmapBufferARB = GLContext.getFunctionAddress("glUnmapBufferARB")) != 0L) ? 1 : 0) & (((this.glGetBufferParameterivARB = GLContext.getFunctionAddress("glGetBufferParameterivARB")) != 0L) ? 1 : 0) & (((this.glGetBufferPointervARB = GLContext.getFunctionAddress("glGetBufferPointervARB")) != 0L) ? 1 : 0);
  }
  
  private boolean ARB_buffer_storage_initNativeFunctionAddresses(Set<String> supported_extensions) {
    return (((this.glBufferStorage = GLContext.getFunctionAddress("glBufferStorage")) != 0L)) & ((!supported_extensions.contains("GL_EXT_direct_state_access") || (this.glNamedBufferStorageEXT = GLContext.getFunctionAddress("glNamedBufferStorageEXT")) != 0L));
  }
  
  private boolean ARB_cl_event_initNativeFunctionAddresses() {
    return ((this.glCreateSyncFromCLeventARB = GLContext.getFunctionAddress("glCreateSyncFromCLeventARB")) != 0L);
  }
  
  private boolean ARB_clear_buffer_object_initNativeFunctionAddresses(Set<String> supported_extensions) {
    return (((this.glClearBufferData = GLContext.getFunctionAddress("glClearBufferData")) != 0L)) & (((this.glClearBufferSubData = GLContext.getFunctionAddress("glClearBufferSubData")) != 0L)) & ((!supported_extensions.contains("GL_EXT_direct_state_access") || (this.glClearNamedBufferDataEXT = GLContext.getFunctionAddress("glClearNamedBufferDataEXT")) != 0L) ? 1 : 0) & ((!supported_extensions.contains("GL_EXT_direct_state_access") || (this.glClearNamedBufferSubDataEXT = GLContext.getFunctionAddress("glClearNamedBufferSubDataEXT")) != 0L) ? 1 : 0);
  }
  
  private boolean ARB_clear_texture_initNativeFunctionAddresses() {
    return (((this.glClearTexImage = GLContext.getFunctionAddress("glClearTexImage")) != 0L)) & (((this.glClearTexSubImage = GLContext.getFunctionAddress("glClearTexSubImage")) != 0L));
  }
  
  private boolean ARB_clip_control_initNativeFunctionAddresses() {
    return ((this.glClipControl = GLContext.getFunctionAddress("glClipControl")) != 0L);
  }
  
  private boolean ARB_color_buffer_float_initNativeFunctionAddresses() {
    return ((this.glClampColorARB = GLContext.getFunctionAddress("glClampColorARB")) != 0L);
  }
  
  private boolean ARB_compute_shader_initNativeFunctionAddresses() {
    return (((this.glDispatchCompute = GLContext.getFunctionAddress("glDispatchCompute")) != 0L)) & (((this.glDispatchComputeIndirect = GLContext.getFunctionAddress("glDispatchComputeIndirect")) != 0L));
  }
  
  private boolean ARB_compute_variable_group_size_initNativeFunctionAddresses() {
    return ((this.glDispatchComputeGroupSizeARB = GLContext.getFunctionAddress("glDispatchComputeGroupSizeARB")) != 0L);
  }
  
  private boolean ARB_copy_buffer_initNativeFunctionAddresses() {
    return ((this.glCopyBufferSubData = GLContext.getFunctionAddress("glCopyBufferSubData")) != 0L);
  }
  
  private boolean ARB_copy_image_initNativeFunctionAddresses() {
    return ((this.glCopyImageSubData = GLContext.getFunctionAddress("glCopyImageSubData")) != 0L);
  }
  
  private boolean ARB_debug_output_initNativeFunctionAddresses() {
    return (((this.glDebugMessageControlARB = GLContext.getFunctionAddress("glDebugMessageControlARB")) != 0L)) & (((this.glDebugMessageInsertARB = GLContext.getFunctionAddress("glDebugMessageInsertARB")) != 0L)) & (((this.glDebugMessageCallbackARB = GLContext.getFunctionAddress("glDebugMessageCallbackARB")) != 0L) ? 1 : 0) & (((this.glGetDebugMessageLogARB = GLContext.getFunctionAddress("glGetDebugMessageLogARB")) != 0L) ? 1 : 0);
  }
  
  private boolean ARB_direct_state_access_initNativeFunctionAddresses() {
    return (((this.glCreateTransformFeedbacks = GLContext.getFunctionAddress("glCreateTransformFeedbacks")) != 0L)) & (((this.glTransformFeedbackBufferBase = GLContext.getFunctionAddress("glTransformFeedbackBufferBase")) != 0L)) & (((this.glTransformFeedbackBufferRange = GLContext.getFunctionAddress("glTransformFeedbackBufferRange")) != 0L) ? 1 : 0) & (((this.glGetTransformFeedbackiv = GLContext.getFunctionAddress("glGetTransformFeedbackiv")) != 0L) ? 1 : 0) & (((this.glGetTransformFeedbacki_v = GLContext.getFunctionAddress("glGetTransformFeedbacki_v")) != 0L) ? 1 : 0) & (((this.glGetTransformFeedbacki64_v = GLContext.getFunctionAddress("glGetTransformFeedbacki64_v")) != 0L) ? 1 : 0) & (((this.glCreateBuffers = GLContext.getFunctionAddress("glCreateBuffers")) != 0L) ? 1 : 0) & (((this.glNamedBufferStorage = GLContext.getFunctionAddress("glNamedBufferStorage")) != 0L) ? 1 : 0) & (((this.glNamedBufferData = GLContext.getFunctionAddress("glNamedBufferData")) != 0L) ? 1 : 0) & (((this.glNamedBufferSubData = GLContext.getFunctionAddress("glNamedBufferSubData")) != 0L) ? 1 : 0) & (((this.glCopyNamedBufferSubData = GLContext.getFunctionAddress("glCopyNamedBufferSubData")) != 0L) ? 1 : 0) & (((this.glClearNamedBufferData = GLContext.getFunctionAddress("glClearNamedBufferData")) != 0L) ? 1 : 0) & (((this.glClearNamedBufferSubData = GLContext.getFunctionAddress("glClearNamedBufferSubData")) != 0L) ? 1 : 0) & (((this.glMapNamedBuffer = GLContext.getFunctionAddress("glMapNamedBuffer")) != 0L) ? 1 : 0) & (((this.glMapNamedBufferRange = GLContext.getFunctionAddress("glMapNamedBufferRange")) != 0L) ? 1 : 0) & (((this.glUnmapNamedBuffer = GLContext.getFunctionAddress("glUnmapNamedBuffer")) != 0L) ? 1 : 0) & (((this.glFlushMappedNamedBufferRange = GLContext.getFunctionAddress("glFlushMappedNamedBufferRange")) != 0L) ? 1 : 0) & (((this.glGetNamedBufferParameteriv = GLContext.getFunctionAddress("glGetNamedBufferParameteriv")) != 0L) ? 1 : 0) & (((this.glGetNamedBufferParameteri64v = GLContext.getFunctionAddress("glGetNamedBufferParameteri64v")) != 0L) ? 1 : 0) & (((this.glGetNamedBufferPointerv = GLContext.getFunctionAddress("glGetNamedBufferPointerv")) != 0L) ? 1 : 0) & (((this.glGetNamedBufferSubData = GLContext.getFunctionAddress("glGetNamedBufferSubData")) != 0L) ? 1 : 0) & (((this.glCreateFramebuffers = GLContext.getFunctionAddress("glCreateFramebuffers")) != 0L) ? 1 : 0) & (((this.glNamedFramebufferRenderbuffer = GLContext.getFunctionAddress("glNamedFramebufferRenderbuffer")) != 0L) ? 1 : 0) & (((this.glNamedFramebufferParameteri = GLContext.getFunctionAddress("glNamedFramebufferParameteri")) != 0L) ? 1 : 0) & (((this.glNamedFramebufferTexture = GLContext.getFunctionAddress("glNamedFramebufferTexture")) != 0L) ? 1 : 0) & (((this.glNamedFramebufferTextureLayer = GLContext.getFunctionAddress("glNamedFramebufferTextureLayer")) != 0L) ? 1 : 0) & (((this.glNamedFramebufferDrawBuffer = GLContext.getFunctionAddress("glNamedFramebufferDrawBuffer")) != 0L) ? 1 : 0) & (((this.glNamedFramebufferDrawBuffers = GLContext.getFunctionAddress("glNamedFramebufferDrawBuffers")) != 0L) ? 1 : 0) & (((this.glNamedFramebufferReadBuffer = GLContext.getFunctionAddress("glNamedFramebufferReadBuffer")) != 0L) ? 1 : 0) & (((this.glInvalidateNamedFramebufferData = GLContext.getFunctionAddress("glInvalidateNamedFramebufferData")) != 0L) ? 1 : 0) & (((this.glInvalidateNamedFramebufferSubData = GLContext.getFunctionAddress("glInvalidateNamedFramebufferSubData")) != 0L) ? 1 : 0) & (((this.glClearNamedFramebufferiv = GLContext.getFunctionAddress("glClearNamedFramebufferiv")) != 0L) ? 1 : 0) & (((this.glClearNamedFramebufferuiv = GLContext.getFunctionAddress("glClearNamedFramebufferuiv")) != 0L) ? 1 : 0) & (((this.glClearNamedFramebufferfv = GLContext.getFunctionAddress("glClearNamedFramebufferfv")) != 0L) ? 1 : 0) & (((this.glClearNamedFramebufferfi = GLContext.getFunctionAddress("glClearNamedFramebufferfi")) != 0L) ? 1 : 0) & (((this.glBlitNamedFramebuffer = GLContext.getFunctionAddress("glBlitNamedFramebuffer")) != 0L) ? 1 : 0) & (((this.glCheckNamedFramebufferStatus = GLContext.getFunctionAddress("glCheckNamedFramebufferStatus")) != 0L) ? 1 : 0) & (((this.glGetNamedFramebufferParameteriv = GLContext.getFunctionAddress("glGetNamedFramebufferParameteriv")) != 0L) ? 1 : 0) & (((this.glGetNamedFramebufferAttachmentParameteriv = GLContext.getFunctionAddress("glGetNamedFramebufferAttachmentParameteriv")) != 0L) ? 1 : 0) & (((this.glCreateRenderbuffers = GLContext.getFunctionAddress("glCreateRenderbuffers")) != 0L) ? 1 : 0) & (((this.glNamedRenderbufferStorage = GLContext.getFunctionAddress("glNamedRenderbufferStorage")) != 0L) ? 1 : 0) & (((this.glNamedRenderbufferStorageMultisample = GLContext.getFunctionAddress("glNamedRenderbufferStorageMultisample")) != 0L) ? 1 : 0) & (((this.glGetNamedRenderbufferParameteriv = GLContext.getFunctionAddress("glGetNamedRenderbufferParameteriv")) != 0L) ? 1 : 0) & (((this.glCreateTextures = GLContext.getFunctionAddress("glCreateTextures")) != 0L) ? 1 : 0) & (((this.glTextureBuffer = GLContext.getFunctionAddress("glTextureBuffer")) != 0L) ? 1 : 0) & (((this.glTextureBufferRange = GLContext.getFunctionAddress("glTextureBufferRange")) != 0L) ? 1 : 0) & (((this.glTextureStorage1D = GLContext.getFunctionAddress("glTextureStorage1D")) != 0L) ? 1 : 0) & (((this.glTextureStorage2D = GLContext.getFunctionAddress("glTextureStorage2D")) != 0L) ? 1 : 0) & (((this.glTextureStorage3D = GLContext.getFunctionAddress("glTextureStorage3D")) != 0L) ? 1 : 0) & (((this.glTextureStorage2DMultisample = GLContext.getFunctionAddress("glTextureStorage2DMultisample")) != 0L) ? 1 : 0) & (((this.glTextureStorage3DMultisample = GLContext.getFunctionAddress("glTextureStorage3DMultisample")) != 0L) ? 1 : 0) & (((this.glTextureSubImage1D = GLContext.getFunctionAddress("glTextureSubImage1D")) != 0L) ? 1 : 0) & (((this.glTextureSubImage2D = GLContext.getFunctionAddress("glTextureSubImage2D")) != 0L) ? 1 : 0) & (((this.glTextureSubImage3D = GLContext.getFunctionAddress("glTextureSubImage3D")) != 0L) ? 1 : 0) & (((this.glCompressedTextureSubImage1D = GLContext.getFunctionAddress("glCompressedTextureSubImage1D")) != 0L) ? 1 : 0) & (((this.glCompressedTextureSubImage2D = GLContext.getFunctionAddress("glCompressedTextureSubImage2D")) != 0L) ? 1 : 0) & (((this.glCompressedTextureSubImage3D = GLContext.getFunctionAddress("glCompressedTextureSubImage3D")) != 0L) ? 1 : 0) & (((this.glCopyTextureSubImage1D = GLContext.getFunctionAddress("glCopyTextureSubImage1D")) != 0L) ? 1 : 0) & (((this.glCopyTextureSubImage2D = GLContext.getFunctionAddress("glCopyTextureSubImage2D")) != 0L) ? 1 : 0) & (((this.glCopyTextureSubImage3D = GLContext.getFunctionAddress("glCopyTextureSubImage3D")) != 0L) ? 1 : 0) & (((this.glTextureParameterf = GLContext.getFunctionAddress("glTextureParameterf")) != 0L) ? 1 : 0) & (((this.glTextureParameterfv = GLContext.getFunctionAddress("glTextureParameterfv")) != 0L) ? 1 : 0) & (((this.glTextureParameteri = GLContext.getFunctionAddress("glTextureParameteri")) != 0L) ? 1 : 0) & (((this.glTextureParameterIiv = GLContext.getFunctionAddress("glTextureParameterIiv")) != 0L) ? 1 : 0) & (((this.glTextureParameterIuiv = GLContext.getFunctionAddress("glTextureParameterIuiv")) != 0L) ? 1 : 0) & (((this.glTextureParameteriv = GLContext.getFunctionAddress("glTextureParameteriv")) != 0L) ? 1 : 0) & (((this.glGenerateTextureMipmap = GLContext.getFunctionAddress("glGenerateTextureMipmap")) != 0L) ? 1 : 0) & (((this.glBindTextureUnit = GLContext.getFunctionAddress("glBindTextureUnit")) != 0L) ? 1 : 0) & (((this.glGetTextureImage = GLContext.getFunctionAddress("glGetTextureImage")) != 0L) ? 1 : 0) & (((this.glGetCompressedTextureImage = GLContext.getFunctionAddress("glGetCompressedTextureImage")) != 0L) ? 1 : 0) & (((this.glGetTextureLevelParameterfv = GLContext.getFunctionAddress("glGetTextureLevelParameterfv")) != 0L) ? 1 : 0) & (((this.glGetTextureLevelParameteriv = GLContext.getFunctionAddress("glGetTextureLevelParameteriv")) != 0L) ? 1 : 0) & (((this.glGetTextureParameterfv = GLContext.getFunctionAddress("glGetTextureParameterfv")) != 0L) ? 1 : 0) & (((this.glGetTextureParameterIiv = GLContext.getFunctionAddress("glGetTextureParameterIiv")) != 0L) ? 1 : 0) & (((this.glGetTextureParameterIuiv = GLContext.getFunctionAddress("glGetTextureParameterIuiv")) != 0L) ? 1 : 0) & (((this.glGetTextureParameteriv = GLContext.getFunctionAddress("glGetTextureParameteriv")) != 0L) ? 1 : 0) & (((this.glCreateVertexArrays = GLContext.getFunctionAddress("glCreateVertexArrays")) != 0L) ? 1 : 0) & (((this.glDisableVertexArrayAttrib = GLContext.getFunctionAddress("glDisableVertexArrayAttrib")) != 0L) ? 1 : 0) & (((this.glEnableVertexArrayAttrib = GLContext.getFunctionAddress("glEnableVertexArrayAttrib")) != 0L) ? 1 : 0) & (((this.glVertexArrayElementBuffer = GLContext.getFunctionAddress("glVertexArrayElementBuffer")) != 0L) ? 1 : 0) & (((this.glVertexArrayVertexBuffer = GLContext.getFunctionAddress("glVertexArrayVertexBuffer")) != 0L) ? 1 : 0) & (((this.glVertexArrayVertexBuffers = GLContext.getFunctionAddress("glVertexArrayVertexBuffers")) != 0L) ? 1 : 0) & (((this.glVertexArrayAttribFormat = GLContext.getFunctionAddress("glVertexArrayAttribFormat")) != 0L) ? 1 : 0) & (((this.glVertexArrayAttribIFormat = GLContext.getFunctionAddress("glVertexArrayAttribIFormat")) != 0L) ? 1 : 0) & (((this.glVertexArrayAttribLFormat = GLContext.getFunctionAddress("glVertexArrayAttribLFormat")) != 0L) ? 1 : 0) & (((this.glVertexArrayAttribBinding = GLContext.getFunctionAddress("glVertexArrayAttribBinding")) != 0L) ? 1 : 0) & (((this.glVertexArrayBindingDivisor = GLContext.getFunctionAddress("glVertexArrayBindingDivisor")) != 0L) ? 1 : 0) & (((this.glGetVertexArrayiv = GLContext.getFunctionAddress("glGetVertexArrayiv")) != 0L) ? 1 : 0) & (((this.glGetVertexArrayIndexediv = GLContext.getFunctionAddress("glGetVertexArrayIndexediv")) != 0L) ? 1 : 0) & (((this.glGetVertexArrayIndexed64iv = GLContext.getFunctionAddress("glGetVertexArrayIndexed64iv")) != 0L) ? 1 : 0) & (((this.glCreateSamplers = GLContext.getFunctionAddress("glCreateSamplers")) != 0L) ? 1 : 0) & (((this.glCreateProgramPipelines = GLContext.getFunctionAddress("glCreateProgramPipelines")) != 0L) ? 1 : 0) & (((this.glCreateQueries = GLContext.getFunctionAddress("glCreateQueries")) != 0L) ? 1 : 0);
  }
  
  private boolean ARB_draw_buffers_initNativeFunctionAddresses() {
    return ((this.glDrawBuffersARB = GLContext.getFunctionAddress("glDrawBuffersARB")) != 0L);
  }
  
  private boolean ARB_draw_buffers_blend_initNativeFunctionAddresses() {
    return (((this.glBlendEquationiARB = GLContext.getFunctionAddress("glBlendEquationiARB")) != 0L)) & (((this.glBlendEquationSeparateiARB = GLContext.getFunctionAddress("glBlendEquationSeparateiARB")) != 0L)) & (((this.glBlendFunciARB = GLContext.getFunctionAddress("glBlendFunciARB")) != 0L) ? 1 : 0) & (((this.glBlendFuncSeparateiARB = GLContext.getFunctionAddress("glBlendFuncSeparateiARB")) != 0L) ? 1 : 0);
  }
  
  private boolean ARB_draw_elements_base_vertex_initNativeFunctionAddresses() {
    return (((this.glDrawElementsBaseVertex = GLContext.getFunctionAddress("glDrawElementsBaseVertex")) != 0L)) & (((this.glDrawRangeElementsBaseVertex = GLContext.getFunctionAddress("glDrawRangeElementsBaseVertex")) != 0L)) & (((this.glDrawElementsInstancedBaseVertex = GLContext.getFunctionAddress("glDrawElementsInstancedBaseVertex")) != 0L) ? 1 : 0);
  }
  
  private boolean ARB_draw_indirect_initNativeFunctionAddresses() {
    return (((this.glDrawArraysIndirect = GLContext.getFunctionAddress("glDrawArraysIndirect")) != 0L)) & (((this.glDrawElementsIndirect = GLContext.getFunctionAddress("glDrawElementsIndirect")) != 0L));
  }
  
  private boolean ARB_draw_instanced_initNativeFunctionAddresses() {
    return (((this.glDrawArraysInstancedARB = GLContext.getFunctionAddress("glDrawArraysInstancedARB")) != 0L)) & (((this.glDrawElementsInstancedARB = GLContext.getFunctionAddress("glDrawElementsInstancedARB")) != 0L));
  }
  
  private boolean ARB_framebuffer_no_attachments_initNativeFunctionAddresses(Set<String> supported_extensions) {
    return (((this.glFramebufferParameteri = GLContext.getFunctionAddress("glFramebufferParameteri")) != 0L)) & (((this.glGetFramebufferParameteriv = GLContext.getFunctionAddress("glGetFramebufferParameteriv")) != 0L)) & ((!supported_extensions.contains("GL_EXT_direct_state_access") || (this.glNamedFramebufferParameteriEXT = GLContext.getFunctionAddress("glNamedFramebufferParameteriEXT")) != 0L) ? 1 : 0) & ((!supported_extensions.contains("GL_EXT_direct_state_access") || (this.glGetNamedFramebufferParameterivEXT = GLContext.getFunctionAddress("glGetNamedFramebufferParameterivEXT")) != 0L) ? 1 : 0);
  }
  
  private boolean ARB_framebuffer_object_initNativeFunctionAddresses() {
    return (((this.glIsRenderbuffer = GLContext.getFunctionAddress("glIsRenderbuffer")) != 0L)) & (((this.glBindRenderbuffer = GLContext.getFunctionAddress("glBindRenderbuffer")) != 0L)) & (((this.glDeleteRenderbuffers = GLContext.getFunctionAddress("glDeleteRenderbuffers")) != 0L) ? 1 : 0) & (((this.glGenRenderbuffers = GLContext.getFunctionAddress("glGenRenderbuffers")) != 0L) ? 1 : 0) & (((this.glRenderbufferStorage = GLContext.getFunctionAddress("glRenderbufferStorage")) != 0L) ? 1 : 0) & (((this.glRenderbufferStorageMultisample = GLContext.getFunctionAddress("glRenderbufferStorageMultisample")) != 0L) ? 1 : 0) & (((this.glGetRenderbufferParameteriv = GLContext.getFunctionAddress("glGetRenderbufferParameteriv")) != 0L) ? 1 : 0) & (((this.glIsFramebuffer = GLContext.getFunctionAddress("glIsFramebuffer")) != 0L) ? 1 : 0) & (((this.glBindFramebuffer = GLContext.getFunctionAddress("glBindFramebuffer")) != 0L) ? 1 : 0) & (((this.glDeleteFramebuffers = GLContext.getFunctionAddress("glDeleteFramebuffers")) != 0L) ? 1 : 0) & (((this.glGenFramebuffers = GLContext.getFunctionAddress("glGenFramebuffers")) != 0L) ? 1 : 0) & (((this.glCheckFramebufferStatus = GLContext.getFunctionAddress("glCheckFramebufferStatus")) != 0L) ? 1 : 0) & (((this.glFramebufferTexture1D = GLContext.getFunctionAddress("glFramebufferTexture1D")) != 0L) ? 1 : 0) & (((this.glFramebufferTexture2D = GLContext.getFunctionAddress("glFramebufferTexture2D")) != 0L) ? 1 : 0) & (((this.glFramebufferTexture3D = GLContext.getFunctionAddress("glFramebufferTexture3D")) != 0L) ? 1 : 0) & (((this.glFramebufferTextureLayer = GLContext.getFunctionAddress("glFramebufferTextureLayer")) != 0L) ? 1 : 0) & (((this.glFramebufferRenderbuffer = GLContext.getFunctionAddress("glFramebufferRenderbuffer")) != 0L) ? 1 : 0) & (((this.glGetFramebufferAttachmentParameteriv = GLContext.getFunctionAddress("glGetFramebufferAttachmentParameteriv")) != 0L) ? 1 : 0) & (((this.glBlitFramebuffer = GLContext.getFunctionAddress("glBlitFramebuffer")) != 0L) ? 1 : 0) & (((this.glGenerateMipmap = GLContext.getFunctionAddress("glGenerateMipmap")) != 0L) ? 1 : 0);
  }
  
  private boolean ARB_geometry_shader4_initNativeFunctionAddresses() {
    return (((this.glProgramParameteriARB = GLContext.getFunctionAddress("glProgramParameteriARB")) != 0L)) & (((this.glFramebufferTextureARB = GLContext.getFunctionAddress("glFramebufferTextureARB")) != 0L)) & (((this.glFramebufferTextureLayerARB = GLContext.getFunctionAddress("glFramebufferTextureLayerARB")) != 0L) ? 1 : 0) & (((this.glFramebufferTextureFaceARB = GLContext.getFunctionAddress("glFramebufferTextureFaceARB")) != 0L) ? 1 : 0);
  }
  
  private boolean ARB_get_program_binary_initNativeFunctionAddresses() {
    return (((this.glGetProgramBinary = GLContext.getFunctionAddress("glGetProgramBinary")) != 0L)) & (((this.glProgramBinary = GLContext.getFunctionAddress("glProgramBinary")) != 0L)) & (((this.glProgramParameteri = GLContext.getFunctionAddress("glProgramParameteri")) != 0L) ? 1 : 0);
  }
  
  private boolean ARB_get_texture_sub_image_initNativeFunctionAddresses() {
    return (((this.glGetTextureSubImage = GLContext.getFunctionAddress("glGetTextureSubImage")) != 0L)) & (((this.glGetCompressedTextureSubImage = GLContext.getFunctionAddress("glGetCompressedTextureSubImage")) != 0L));
  }
  
  private boolean ARB_gpu_shader_fp64_initNativeFunctionAddresses(Set<String> supported_extensions) {
    return (((this.glUniform1d = GLContext.getFunctionAddress("glUniform1d")) != 0L)) & (((this.glUniform2d = GLContext.getFunctionAddress("glUniform2d")) != 0L)) & (((this.glUniform3d = GLContext.getFunctionAddress("glUniform3d")) != 0L) ? 1 : 0) & (((this.glUniform4d = GLContext.getFunctionAddress("glUniform4d")) != 0L) ? 1 : 0) & (((this.glUniform1dv = GLContext.getFunctionAddress("glUniform1dv")) != 0L) ? 1 : 0) & (((this.glUniform2dv = GLContext.getFunctionAddress("glUniform2dv")) != 0L) ? 1 : 0) & (((this.glUniform3dv = GLContext.getFunctionAddress("glUniform3dv")) != 0L) ? 1 : 0) & (((this.glUniform4dv = GLContext.getFunctionAddress("glUniform4dv")) != 0L) ? 1 : 0) & (((this.glUniformMatrix2dv = GLContext.getFunctionAddress("glUniformMatrix2dv")) != 0L) ? 1 : 0) & (((this.glUniformMatrix3dv = GLContext.getFunctionAddress("glUniformMatrix3dv")) != 0L) ? 1 : 0) & (((this.glUniformMatrix4dv = GLContext.getFunctionAddress("glUniformMatrix4dv")) != 0L) ? 1 : 0) & (((this.glUniformMatrix2x3dv = GLContext.getFunctionAddress("glUniformMatrix2x3dv")) != 0L) ? 1 : 0) & (((this.glUniformMatrix2x4dv = GLContext.getFunctionAddress("glUniformMatrix2x4dv")) != 0L) ? 1 : 0) & (((this.glUniformMatrix3x2dv = GLContext.getFunctionAddress("glUniformMatrix3x2dv")) != 0L) ? 1 : 0) & (((this.glUniformMatrix3x4dv = GLContext.getFunctionAddress("glUniformMatrix3x4dv")) != 0L) ? 1 : 0) & (((this.glUniformMatrix4x2dv = GLContext.getFunctionAddress("glUniformMatrix4x2dv")) != 0L) ? 1 : 0) & (((this.glUniformMatrix4x3dv = GLContext.getFunctionAddress("glUniformMatrix4x3dv")) != 0L) ? 1 : 0) & (((this.glGetUniformdv = GLContext.getFunctionAddress("glGetUniformdv")) != 0L) ? 1 : 0) & ((!supported_extensions.contains("GL_EXT_direct_state_access") || (this.glProgramUniform1dEXT = GLContext.getFunctionAddress("glProgramUniform1dEXT")) != 0L) ? 1 : 0) & ((!supported_extensions.contains("GL_EXT_direct_state_access") || (this.glProgramUniform2dEXT = GLContext.getFunctionAddress("glProgramUniform2dEXT")) != 0L) ? 1 : 0) & ((!supported_extensions.contains("GL_EXT_direct_state_access") || (this.glProgramUniform3dEXT = GLContext.getFunctionAddress("glProgramUniform3dEXT")) != 0L) ? 1 : 0) & ((!supported_extensions.contains("GL_EXT_direct_state_access") || (this.glProgramUniform4dEXT = GLContext.getFunctionAddress("glProgramUniform4dEXT")) != 0L) ? 1 : 0) & ((!supported_extensions.contains("GL_EXT_direct_state_access") || (this.glProgramUniform1dvEXT = GLContext.getFunctionAddress("glProgramUniform1dvEXT")) != 0L) ? 1 : 0) & ((!supported_extensions.contains("GL_EXT_direct_state_access") || (this.glProgramUniform2dvEXT = GLContext.getFunctionAddress("glProgramUniform2dvEXT")) != 0L) ? 1 : 0) & ((!supported_extensions.contains("GL_EXT_direct_state_access") || (this.glProgramUniform3dvEXT = GLContext.getFunctionAddress("glProgramUniform3dvEXT")) != 0L) ? 1 : 0) & ((!supported_extensions.contains("GL_EXT_direct_state_access") || (this.glProgramUniform4dvEXT = GLContext.getFunctionAddress("glProgramUniform4dvEXT")) != 0L) ? 1 : 0) & ((!supported_extensions.contains("GL_EXT_direct_state_access") || (this.glProgramUniformMatrix2dvEXT = GLContext.getFunctionAddress("glProgramUniformMatrix2dvEXT")) != 0L) ? 1 : 0) & ((!supported_extensions.contains("GL_EXT_direct_state_access") || (this.glProgramUniformMatrix3dvEXT = GLContext.getFunctionAddress("glProgramUniformMatrix3dvEXT")) != 0L) ? 1 : 0) & ((!supported_extensions.contains("GL_EXT_direct_state_access") || (this.glProgramUniformMatrix4dvEXT = GLContext.getFunctionAddress("glProgramUniformMatrix4dvEXT")) != 0L) ? 1 : 0) & ((!supported_extensions.contains("GL_EXT_direct_state_access") || (this.glProgramUniformMatrix2x3dvEXT = GLContext.getFunctionAddress("glProgramUniformMatrix2x3dvEXT")) != 0L) ? 1 : 0) & ((!supported_extensions.contains("GL_EXT_direct_state_access") || (this.glProgramUniformMatrix2x4dvEXT = GLContext.getFunctionAddress("glProgramUniformMatrix2x4dvEXT")) != 0L) ? 1 : 0) & ((!supported_extensions.contains("GL_EXT_direct_state_access") || (this.glProgramUniformMatrix3x2dvEXT = GLContext.getFunctionAddress("glProgramUniformMatrix3x2dvEXT")) != 0L) ? 1 : 0) & ((!supported_extensions.contains("GL_EXT_direct_state_access") || (this.glProgramUniformMatrix3x4dvEXT = GLContext.getFunctionAddress("glProgramUniformMatrix3x4dvEXT")) != 0L) ? 1 : 0) & ((!supported_extensions.contains("GL_EXT_direct_state_access") || (this.glProgramUniformMatrix4x2dvEXT = GLContext.getFunctionAddress("glProgramUniformMatrix4x2dvEXT")) != 0L) ? 1 : 0) & ((!supported_extensions.contains("GL_EXT_direct_state_access") || (this.glProgramUniformMatrix4x3dvEXT = GLContext.getFunctionAddress("glProgramUniformMatrix4x3dvEXT")) != 0L) ? 1 : 0);
  }
  
  private boolean ARB_imaging_initNativeFunctionAddresses(boolean forwardCompatible) {
    return ((forwardCompatible || (this.glColorTable = GLContext.getFunctionAddress("glColorTable")) != 0L)) & ((forwardCompatible || (this.glColorSubTable = GLContext.getFunctionAddress("glColorSubTable")) != 0L)) & ((forwardCompatible || (this.glColorTableParameteriv = GLContext.getFunctionAddress("glColorTableParameteriv")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glColorTableParameterfv = GLContext.getFunctionAddress("glColorTableParameterfv")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glCopyColorSubTable = GLContext.getFunctionAddress("glCopyColorSubTable")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glCopyColorTable = GLContext.getFunctionAddress("glCopyColorTable")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glGetColorTable = GLContext.getFunctionAddress("glGetColorTable")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glGetColorTableParameteriv = GLContext.getFunctionAddress("glGetColorTableParameteriv")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glGetColorTableParameterfv = GLContext.getFunctionAddress("glGetColorTableParameterfv")) != 0L) ? 1 : 0) & (((this.glBlendEquation = GLContext.getFunctionAddress("glBlendEquation")) != 0L) ? 1 : 0) & (((this.glBlendColor = GLContext.getFunctionAddress("glBlendColor")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glHistogram = GLContext.getFunctionAddress("glHistogram")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glResetHistogram = GLContext.getFunctionAddress("glResetHistogram")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glGetHistogram = GLContext.getFunctionAddress("glGetHistogram")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glGetHistogramParameterfv = GLContext.getFunctionAddress("glGetHistogramParameterfv")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glGetHistogramParameteriv = GLContext.getFunctionAddress("glGetHistogramParameteriv")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glMinmax = GLContext.getFunctionAddress("glMinmax")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glResetMinmax = GLContext.getFunctionAddress("glResetMinmax")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glGetMinmax = GLContext.getFunctionAddress("glGetMinmax")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glGetMinmaxParameterfv = GLContext.getFunctionAddress("glGetMinmaxParameterfv")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glGetMinmaxParameteriv = GLContext.getFunctionAddress("glGetMinmaxParameteriv")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glConvolutionFilter1D = GLContext.getFunctionAddress("glConvolutionFilter1D")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glConvolutionFilter2D = GLContext.getFunctionAddress("glConvolutionFilter2D")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glConvolutionParameterf = GLContext.getFunctionAddress("glConvolutionParameterf")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glConvolutionParameterfv = GLContext.getFunctionAddress("glConvolutionParameterfv")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glConvolutionParameteri = GLContext.getFunctionAddress("glConvolutionParameteri")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glConvolutionParameteriv = GLContext.getFunctionAddress("glConvolutionParameteriv")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glCopyConvolutionFilter1D = GLContext.getFunctionAddress("glCopyConvolutionFilter1D")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glCopyConvolutionFilter2D = GLContext.getFunctionAddress("glCopyConvolutionFilter2D")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glGetConvolutionFilter = GLContext.getFunctionAddress("glGetConvolutionFilter")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glGetConvolutionParameterfv = GLContext.getFunctionAddress("glGetConvolutionParameterfv")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glGetConvolutionParameteriv = GLContext.getFunctionAddress("glGetConvolutionParameteriv")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glSeparableFilter2D = GLContext.getFunctionAddress("glSeparableFilter2D")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glGetSeparableFilter = GLContext.getFunctionAddress("glGetSeparableFilter")) != 0L) ? 1 : 0);
  }
  
  private boolean ARB_indirect_parameters_initNativeFunctionAddresses() {
    return (((this.glMultiDrawArraysIndirectCountARB = GLContext.getFunctionAddress("glMultiDrawArraysIndirectCountARB")) != 0L)) & (((this.glMultiDrawElementsIndirectCountARB = GLContext.getFunctionAddress("glMultiDrawElementsIndirectCountARB")) != 0L));
  }
  
  private boolean ARB_instanced_arrays_initNativeFunctionAddresses() {
    return ((this.glVertexAttribDivisorARB = GLContext.getFunctionAddress("glVertexAttribDivisorARB")) != 0L);
  }
  
  private boolean ARB_internalformat_query_initNativeFunctionAddresses() {
    return ((this.glGetInternalformativ = GLContext.getFunctionAddress("glGetInternalformativ")) != 0L);
  }
  
  private boolean ARB_internalformat_query2_initNativeFunctionAddresses() {
    return ((this.glGetInternalformati64v = GLContext.getFunctionAddress("glGetInternalformati64v")) != 0L);
  }
  
  private boolean ARB_invalidate_subdata_initNativeFunctionAddresses() {
    return (((this.glInvalidateTexSubImage = GLContext.getFunctionAddress("glInvalidateTexSubImage")) != 0L)) & (((this.glInvalidateTexImage = GLContext.getFunctionAddress("glInvalidateTexImage")) != 0L)) & (((this.glInvalidateBufferSubData = GLContext.getFunctionAddress("glInvalidateBufferSubData")) != 0L) ? 1 : 0) & (((this.glInvalidateBufferData = GLContext.getFunctionAddress("glInvalidateBufferData")) != 0L) ? 1 : 0) & (((this.glInvalidateFramebuffer = GLContext.getFunctionAddress("glInvalidateFramebuffer")) != 0L) ? 1 : 0) & (((this.glInvalidateSubFramebuffer = GLContext.getFunctionAddress("glInvalidateSubFramebuffer")) != 0L) ? 1 : 0);
  }
  
  private boolean ARB_map_buffer_range_initNativeFunctionAddresses() {
    return (((this.glMapBufferRange = GLContext.getFunctionAddress("glMapBufferRange")) != 0L)) & (((this.glFlushMappedBufferRange = GLContext.getFunctionAddress("glFlushMappedBufferRange")) != 0L));
  }
  
  private boolean ARB_matrix_palette_initNativeFunctionAddresses() {
    return (((this.glCurrentPaletteMatrixARB = GLContext.getFunctionAddress("glCurrentPaletteMatrixARB")) != 0L)) & (((this.glMatrixIndexPointerARB = GLContext.getFunctionAddress("glMatrixIndexPointerARB")) != 0L)) & (((this.glMatrixIndexubvARB = GLContext.getFunctionAddress("glMatrixIndexubvARB")) != 0L) ? 1 : 0) & (((this.glMatrixIndexusvARB = GLContext.getFunctionAddress("glMatrixIndexusvARB")) != 0L) ? 1 : 0) & (((this.glMatrixIndexuivARB = GLContext.getFunctionAddress("glMatrixIndexuivARB")) != 0L) ? 1 : 0);
  }
  
  private boolean ARB_multi_bind_initNativeFunctionAddresses() {
    return (((this.glBindBuffersBase = GLContext.getFunctionAddress("glBindBuffersBase")) != 0L)) & (((this.glBindBuffersRange = GLContext.getFunctionAddress("glBindBuffersRange")) != 0L)) & (((this.glBindTextures = GLContext.getFunctionAddress("glBindTextures")) != 0L) ? 1 : 0) & (((this.glBindSamplers = GLContext.getFunctionAddress("glBindSamplers")) != 0L) ? 1 : 0) & (((this.glBindImageTextures = GLContext.getFunctionAddress("glBindImageTextures")) != 0L) ? 1 : 0) & (((this.glBindVertexBuffers = GLContext.getFunctionAddress("glBindVertexBuffers")) != 0L) ? 1 : 0);
  }
  
  private boolean ARB_multi_draw_indirect_initNativeFunctionAddresses() {
    return (((this.glMultiDrawArraysIndirect = GLContext.getFunctionAddress("glMultiDrawArraysIndirect")) != 0L)) & (((this.glMultiDrawElementsIndirect = GLContext.getFunctionAddress("glMultiDrawElementsIndirect")) != 0L));
  }
  
  private boolean ARB_multisample_initNativeFunctionAddresses() {
    return ((this.glSampleCoverageARB = GLContext.getFunctionAddress("glSampleCoverageARB")) != 0L);
  }
  
  private boolean ARB_multitexture_initNativeFunctionAddresses() {
    return (((this.glClientActiveTextureARB = GLContext.getFunctionAddress("glClientActiveTextureARB")) != 0L)) & (((this.glActiveTextureARB = GLContext.getFunctionAddress("glActiveTextureARB")) != 0L)) & (((this.glMultiTexCoord1fARB = GLContext.getFunctionAddress("glMultiTexCoord1fARB")) != 0L) ? 1 : 0) & (((this.glMultiTexCoord1dARB = GLContext.getFunctionAddress("glMultiTexCoord1dARB")) != 0L) ? 1 : 0) & (((this.glMultiTexCoord1iARB = GLContext.getFunctionAddress("glMultiTexCoord1iARB")) != 0L) ? 1 : 0) & (((this.glMultiTexCoord1sARB = GLContext.getFunctionAddress("glMultiTexCoord1sARB")) != 0L) ? 1 : 0) & (((this.glMultiTexCoord2fARB = GLContext.getFunctionAddress("glMultiTexCoord2fARB")) != 0L) ? 1 : 0) & (((this.glMultiTexCoord2dARB = GLContext.getFunctionAddress("glMultiTexCoord2dARB")) != 0L) ? 1 : 0) & (((this.glMultiTexCoord2iARB = GLContext.getFunctionAddress("glMultiTexCoord2iARB")) != 0L) ? 1 : 0) & (((this.glMultiTexCoord2sARB = GLContext.getFunctionAddress("glMultiTexCoord2sARB")) != 0L) ? 1 : 0) & (((this.glMultiTexCoord3fARB = GLContext.getFunctionAddress("glMultiTexCoord3fARB")) != 0L) ? 1 : 0) & (((this.glMultiTexCoord3dARB = GLContext.getFunctionAddress("glMultiTexCoord3dARB")) != 0L) ? 1 : 0) & (((this.glMultiTexCoord3iARB = GLContext.getFunctionAddress("glMultiTexCoord3iARB")) != 0L) ? 1 : 0) & (((this.glMultiTexCoord3sARB = GLContext.getFunctionAddress("glMultiTexCoord3sARB")) != 0L) ? 1 : 0) & (((this.glMultiTexCoord4fARB = GLContext.getFunctionAddress("glMultiTexCoord4fARB")) != 0L) ? 1 : 0) & (((this.glMultiTexCoord4dARB = GLContext.getFunctionAddress("glMultiTexCoord4dARB")) != 0L) ? 1 : 0) & (((this.glMultiTexCoord4iARB = GLContext.getFunctionAddress("glMultiTexCoord4iARB")) != 0L) ? 1 : 0) & (((this.glMultiTexCoord4sARB = GLContext.getFunctionAddress("glMultiTexCoord4sARB")) != 0L) ? 1 : 0);
  }
  
  private boolean ARB_occlusion_query_initNativeFunctionAddresses() {
    return (((this.glGenQueriesARB = GLContext.getFunctionAddress("glGenQueriesARB")) != 0L)) & (((this.glDeleteQueriesARB = GLContext.getFunctionAddress("glDeleteQueriesARB")) != 0L)) & (((this.glIsQueryARB = GLContext.getFunctionAddress("glIsQueryARB")) != 0L) ? 1 : 0) & (((this.glBeginQueryARB = GLContext.getFunctionAddress("glBeginQueryARB")) != 0L) ? 1 : 0) & (((this.glEndQueryARB = GLContext.getFunctionAddress("glEndQueryARB")) != 0L) ? 1 : 0) & (((this.glGetQueryivARB = GLContext.getFunctionAddress("glGetQueryivARB")) != 0L) ? 1 : 0) & (((this.glGetQueryObjectivARB = GLContext.getFunctionAddress("glGetQueryObjectivARB")) != 0L) ? 1 : 0) & (((this.glGetQueryObjectuivARB = GLContext.getFunctionAddress("glGetQueryObjectuivARB")) != 0L) ? 1 : 0);
  }
  
  private boolean ARB_point_parameters_initNativeFunctionAddresses() {
    return (((this.glPointParameterfARB = GLContext.getFunctionAddress("glPointParameterfARB")) != 0L)) & (((this.glPointParameterfvARB = GLContext.getFunctionAddress("glPointParameterfvARB")) != 0L));
  }
  
  private boolean ARB_program_initNativeFunctionAddresses() {
    return (((this.glProgramStringARB = GLContext.getFunctionAddress("glProgramStringARB")) != 0L)) & (((this.glBindProgramARB = GLContext.getFunctionAddress("glBindProgramARB")) != 0L)) & (((this.glDeleteProgramsARB = GLContext.getFunctionAddress("glDeleteProgramsARB")) != 0L) ? 1 : 0) & (((this.glGenProgramsARB = GLContext.getFunctionAddress("glGenProgramsARB")) != 0L) ? 1 : 0) & (((this.glProgramEnvParameter4fARB = GLContext.getFunctionAddress("glProgramEnvParameter4fARB")) != 0L) ? 1 : 0) & (((this.glProgramEnvParameter4dARB = GLContext.getFunctionAddress("glProgramEnvParameter4dARB")) != 0L) ? 1 : 0) & (((this.glProgramEnvParameter4fvARB = GLContext.getFunctionAddress("glProgramEnvParameter4fvARB")) != 0L) ? 1 : 0) & (((this.glProgramEnvParameter4dvARB = GLContext.getFunctionAddress("glProgramEnvParameter4dvARB")) != 0L) ? 1 : 0) & (((this.glProgramLocalParameter4fARB = GLContext.getFunctionAddress("glProgramLocalParameter4fARB")) != 0L) ? 1 : 0) & (((this.glProgramLocalParameter4dARB = GLContext.getFunctionAddress("glProgramLocalParameter4dARB")) != 0L) ? 1 : 0) & (((this.glProgramLocalParameter4fvARB = GLContext.getFunctionAddress("glProgramLocalParameter4fvARB")) != 0L) ? 1 : 0) & (((this.glProgramLocalParameter4dvARB = GLContext.getFunctionAddress("glProgramLocalParameter4dvARB")) != 0L) ? 1 : 0) & (((this.glGetProgramEnvParameterfvARB = GLContext.getFunctionAddress("glGetProgramEnvParameterfvARB")) != 0L) ? 1 : 0) & (((this.glGetProgramEnvParameterdvARB = GLContext.getFunctionAddress("glGetProgramEnvParameterdvARB")) != 0L) ? 1 : 0) & (((this.glGetProgramLocalParameterfvARB = GLContext.getFunctionAddress("glGetProgramLocalParameterfvARB")) != 0L) ? 1 : 0) & (((this.glGetProgramLocalParameterdvARB = GLContext.getFunctionAddress("glGetProgramLocalParameterdvARB")) != 0L) ? 1 : 0) & (((this.glGetProgramivARB = GLContext.getFunctionAddress("glGetProgramivARB")) != 0L) ? 1 : 0) & (((this.glGetProgramStringARB = GLContext.getFunctionAddress("glGetProgramStringARB")) != 0L) ? 1 : 0) & (((this.glIsProgramARB = GLContext.getFunctionAddress("glIsProgramARB")) != 0L) ? 1 : 0);
  }
  
  private boolean ARB_program_interface_query_initNativeFunctionAddresses() {
    return (((this.glGetProgramInterfaceiv = GLContext.getFunctionAddress("glGetProgramInterfaceiv")) != 0L)) & (((this.glGetProgramResourceIndex = GLContext.getFunctionAddress("glGetProgramResourceIndex")) != 0L)) & (((this.glGetProgramResourceName = GLContext.getFunctionAddress("glGetProgramResourceName")) != 0L) ? 1 : 0) & (((this.glGetProgramResourceiv = GLContext.getFunctionAddress("glGetProgramResourceiv")) != 0L) ? 1 : 0) & (((this.glGetProgramResourceLocation = GLContext.getFunctionAddress("glGetProgramResourceLocation")) != 0L) ? 1 : 0) & (((this.glGetProgramResourceLocationIndex = GLContext.getFunctionAddress("glGetProgramResourceLocationIndex")) != 0L) ? 1 : 0);
  }
  
  private boolean ARB_provoking_vertex_initNativeFunctionAddresses() {
    return ((this.glProvokingVertex = GLContext.getFunctionAddress("glProvokingVertex")) != 0L);
  }
  
  private boolean ARB_robustness_initNativeFunctionAddresses(boolean forwardCompatible, Set<String> supported_extensions) {
    return (((this.glGetGraphicsResetStatusARB = GLContext.getFunctionAddress("glGetGraphicsResetStatusARB")) != 0L)) & ((forwardCompatible || (this.glGetnMapdvARB = GLContext.getFunctionAddress("glGetnMapdvARB")) != 0L)) & ((forwardCompatible || (this.glGetnMapfvARB = GLContext.getFunctionAddress("glGetnMapfvARB")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glGetnMapivARB = GLContext.getFunctionAddress("glGetnMapivARB")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glGetnPixelMapfvARB = GLContext.getFunctionAddress("glGetnPixelMapfvARB")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glGetnPixelMapuivARB = GLContext.getFunctionAddress("glGetnPixelMapuivARB")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glGetnPixelMapusvARB = GLContext.getFunctionAddress("glGetnPixelMapusvARB")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glGetnPolygonStippleARB = GLContext.getFunctionAddress("glGetnPolygonStippleARB")) != 0L) ? 1 : 0) & (((this.glGetnTexImageARB = GLContext.getFunctionAddress("glGetnTexImageARB")) != 0L) ? 1 : 0) & (((this.glReadnPixelsARB = GLContext.getFunctionAddress("glReadnPixelsARB")) != 0L) ? 1 : 0) & ((!supported_extensions.contains("GL_ARB_imaging") || (this.glGetnColorTableARB = GLContext.getFunctionAddress("glGetnColorTableARB")) != 0L) ? 1 : 0) & ((!supported_extensions.contains("GL_ARB_imaging") || (this.glGetnConvolutionFilterARB = GLContext.getFunctionAddress("glGetnConvolutionFilterARB")) != 0L) ? 1 : 0) & ((!supported_extensions.contains("GL_ARB_imaging") || (this.glGetnSeparableFilterARB = GLContext.getFunctionAddress("glGetnSeparableFilterARB")) != 0L) ? 1 : 0) & ((!supported_extensions.contains("GL_ARB_imaging") || (this.glGetnHistogramARB = GLContext.getFunctionAddress("glGetnHistogramARB")) != 0L) ? 1 : 0) & ((!supported_extensions.contains("GL_ARB_imaging") || (this.glGetnMinmaxARB = GLContext.getFunctionAddress("glGetnMinmaxARB")) != 0L) ? 1 : 0) & ((!supported_extensions.contains("OpenGL13") || (this.glGetnCompressedTexImageARB = GLContext.getFunctionAddress("glGetnCompressedTexImageARB")) != 0L) ? 1 : 0) & ((!supported_extensions.contains("OpenGL20") || (this.glGetnUniformfvARB = GLContext.getFunctionAddress("glGetnUniformfvARB")) != 0L) ? 1 : 0) & ((!supported_extensions.contains("OpenGL20") || (this.glGetnUniformivARB = GLContext.getFunctionAddress("glGetnUniformivARB")) != 0L) ? 1 : 0) & ((!supported_extensions.contains("OpenGL20") || (this.glGetnUniformuivARB = GLContext.getFunctionAddress("glGetnUniformuivARB")) != 0L) ? 1 : 0) & ((!supported_extensions.contains("OpenGL20") || (this.glGetnUniformdvARB = GLContext.getFunctionAddress("glGetnUniformdvARB")) != 0L) ? 1 : 0);
  }
  
  private boolean ARB_sample_shading_initNativeFunctionAddresses() {
    return ((this.glMinSampleShadingARB = GLContext.getFunctionAddress("glMinSampleShadingARB")) != 0L);
  }
  
  private boolean ARB_sampler_objects_initNativeFunctionAddresses() {
    return (((this.glGenSamplers = GLContext.getFunctionAddress("glGenSamplers")) != 0L)) & (((this.glDeleteSamplers = GLContext.getFunctionAddress("glDeleteSamplers")) != 0L)) & (((this.glIsSampler = GLContext.getFunctionAddress("glIsSampler")) != 0L) ? 1 : 0) & (((this.glBindSampler = GLContext.getFunctionAddress("glBindSampler")) != 0L) ? 1 : 0) & (((this.glSamplerParameteri = GLContext.getFunctionAddress("glSamplerParameteri")) != 0L) ? 1 : 0) & (((this.glSamplerParameterf = GLContext.getFunctionAddress("glSamplerParameterf")) != 0L) ? 1 : 0) & (((this.glSamplerParameteriv = GLContext.getFunctionAddress("glSamplerParameteriv")) != 0L) ? 1 : 0) & (((this.glSamplerParameterfv = GLContext.getFunctionAddress("glSamplerParameterfv")) != 0L) ? 1 : 0) & (((this.glSamplerParameterIiv = GLContext.getFunctionAddress("glSamplerParameterIiv")) != 0L) ? 1 : 0) & (((this.glSamplerParameterIuiv = GLContext.getFunctionAddress("glSamplerParameterIuiv")) != 0L) ? 1 : 0) & (((this.glGetSamplerParameteriv = GLContext.getFunctionAddress("glGetSamplerParameteriv")) != 0L) ? 1 : 0) & (((this.glGetSamplerParameterfv = GLContext.getFunctionAddress("glGetSamplerParameterfv")) != 0L) ? 1 : 0) & (((this.glGetSamplerParameterIiv = GLContext.getFunctionAddress("glGetSamplerParameterIiv")) != 0L) ? 1 : 0) & (((this.glGetSamplerParameterIuiv = GLContext.getFunctionAddress("glGetSamplerParameterIuiv")) != 0L) ? 1 : 0);
  }
  
  private boolean ARB_separate_shader_objects_initNativeFunctionAddresses() {
    return (((this.glUseProgramStages = GLContext.getFunctionAddress("glUseProgramStages")) != 0L)) & (((this.glActiveShaderProgram = GLContext.getFunctionAddress("glActiveShaderProgram")) != 0L)) & (((this.glCreateShaderProgramv = GLContext.getFunctionAddress("glCreateShaderProgramv")) != 0L) ? 1 : 0) & (((this.glBindProgramPipeline = GLContext.getFunctionAddress("glBindProgramPipeline")) != 0L) ? 1 : 0) & (((this.glDeleteProgramPipelines = GLContext.getFunctionAddress("glDeleteProgramPipelines")) != 0L) ? 1 : 0) & (((this.glGenProgramPipelines = GLContext.getFunctionAddress("glGenProgramPipelines")) != 0L) ? 1 : 0) & (((this.glIsProgramPipeline = GLContext.getFunctionAddress("glIsProgramPipeline")) != 0L) ? 1 : 0) & (((this.glProgramParameteri = GLContext.getFunctionAddress("glProgramParameteri")) != 0L) ? 1 : 0) & (((this.glGetProgramPipelineiv = GLContext.getFunctionAddress("glGetProgramPipelineiv")) != 0L) ? 1 : 0) & (((this.glProgramUniform1i = GLContext.getFunctionAddress("glProgramUniform1i")) != 0L) ? 1 : 0) & (((this.glProgramUniform2i = GLContext.getFunctionAddress("glProgramUniform2i")) != 0L) ? 1 : 0) & (((this.glProgramUniform3i = GLContext.getFunctionAddress("glProgramUniform3i")) != 0L) ? 1 : 0) & (((this.glProgramUniform4i = GLContext.getFunctionAddress("glProgramUniform4i")) != 0L) ? 1 : 0) & (((this.glProgramUniform1f = GLContext.getFunctionAddress("glProgramUniform1f")) != 0L) ? 1 : 0) & (((this.glProgramUniform2f = GLContext.getFunctionAddress("glProgramUniform2f")) != 0L) ? 1 : 0) & (((this.glProgramUniform3f = GLContext.getFunctionAddress("glProgramUniform3f")) != 0L) ? 1 : 0) & (((this.glProgramUniform4f = GLContext.getFunctionAddress("glProgramUniform4f")) != 0L) ? 1 : 0) & (((this.glProgramUniform1d = GLContext.getFunctionAddress("glProgramUniform1d")) != 0L) ? 1 : 0) & (((this.glProgramUniform2d = GLContext.getFunctionAddress("glProgramUniform2d")) != 0L) ? 1 : 0) & (((this.glProgramUniform3d = GLContext.getFunctionAddress("glProgramUniform3d")) != 0L) ? 1 : 0) & (((this.glProgramUniform4d = GLContext.getFunctionAddress("glProgramUniform4d")) != 0L) ? 1 : 0) & (((this.glProgramUniform1iv = GLContext.getFunctionAddress("glProgramUniform1iv")) != 0L) ? 1 : 0) & (((this.glProgramUniform2iv = GLContext.getFunctionAddress("glProgramUniform2iv")) != 0L) ? 1 : 0) & (((this.glProgramUniform3iv = GLContext.getFunctionAddress("glProgramUniform3iv")) != 0L) ? 1 : 0) & (((this.glProgramUniform4iv = GLContext.getFunctionAddress("glProgramUniform4iv")) != 0L) ? 1 : 0) & (((this.glProgramUniform1fv = GLContext.getFunctionAddress("glProgramUniform1fv")) != 0L) ? 1 : 0) & (((this.glProgramUniform2fv = GLContext.getFunctionAddress("glProgramUniform2fv")) != 0L) ? 1 : 0) & (((this.glProgramUniform3fv = GLContext.getFunctionAddress("glProgramUniform3fv")) != 0L) ? 1 : 0) & (((this.glProgramUniform4fv = GLContext.getFunctionAddress("glProgramUniform4fv")) != 0L) ? 1 : 0) & (((this.glProgramUniform1dv = GLContext.getFunctionAddress("glProgramUniform1dv")) != 0L) ? 1 : 0) & (((this.glProgramUniform2dv = GLContext.getFunctionAddress("glProgramUniform2dv")) != 0L) ? 1 : 0) & (((this.glProgramUniform3dv = GLContext.getFunctionAddress("glProgramUniform3dv")) != 0L) ? 1 : 0) & (((this.glProgramUniform4dv = GLContext.getFunctionAddress("glProgramUniform4dv")) != 0L) ? 1 : 0) & (((this.glProgramUniform1ui = GLContext.getFunctionAddress("glProgramUniform1ui")) != 0L) ? 1 : 0) & (((this.glProgramUniform2ui = GLContext.getFunctionAddress("glProgramUniform2ui")) != 0L) ? 1 : 0) & (((this.glProgramUniform3ui = GLContext.getFunctionAddress("glProgramUniform3ui")) != 0L) ? 1 : 0) & (((this.glProgramUniform4ui = GLContext.getFunctionAddress("glProgramUniform4ui")) != 0L) ? 1 : 0) & (((this.glProgramUniform1uiv = GLContext.getFunctionAddress("glProgramUniform1uiv")) != 0L) ? 1 : 0) & (((this.glProgramUniform2uiv = GLContext.getFunctionAddress("glProgramUniform2uiv")) != 0L) ? 1 : 0) & (((this.glProgramUniform3uiv = GLContext.getFunctionAddress("glProgramUniform3uiv")) != 0L) ? 1 : 0) & (((this.glProgramUniform4uiv = GLContext.getFunctionAddress("glProgramUniform4uiv")) != 0L) ? 1 : 0) & (((this.glProgramUniformMatrix2fv = GLContext.getFunctionAddress("glProgramUniformMatrix2fv")) != 0L) ? 1 : 0) & (((this.glProgramUniformMatrix3fv = GLContext.getFunctionAddress("glProgramUniformMatrix3fv")) != 0L) ? 1 : 0) & (((this.glProgramUniformMatrix4fv = GLContext.getFunctionAddress("glProgramUniformMatrix4fv")) != 0L) ? 1 : 0) & (((this.glProgramUniformMatrix2dv = GLContext.getFunctionAddress("glProgramUniformMatrix2dv")) != 0L) ? 1 : 0) & (((this.glProgramUniformMatrix3dv = GLContext.getFunctionAddress("glProgramUniformMatrix3dv")) != 0L) ? 1 : 0) & (((this.glProgramUniformMatrix4dv = GLContext.getFunctionAddress("glProgramUniformMatrix4dv")) != 0L) ? 1 : 0) & (((this.glProgramUniformMatrix2x3fv = GLContext.getFunctionAddress("glProgramUniformMatrix2x3fv")) != 0L) ? 1 : 0) & (((this.glProgramUniformMatrix3x2fv = GLContext.getFunctionAddress("glProgramUniformMatrix3x2fv")) != 0L) ? 1 : 0) & (((this.glProgramUniformMatrix2x4fv = GLContext.getFunctionAddress("glProgramUniformMatrix2x4fv")) != 0L) ? 1 : 0) & (((this.glProgramUniformMatrix4x2fv = GLContext.getFunctionAddress("glProgramUniformMatrix4x2fv")) != 0L) ? 1 : 0) & (((this.glProgramUniformMatrix3x4fv = GLContext.getFunctionAddress("glProgramUniformMatrix3x4fv")) != 0L) ? 1 : 0) & (((this.glProgramUniformMatrix4x3fv = GLContext.getFunctionAddress("glProgramUniformMatrix4x3fv")) != 0L) ? 1 : 0) & (((this.glProgramUniformMatrix2x3dv = GLContext.getFunctionAddress("glProgramUniformMatrix2x3dv")) != 0L) ? 1 : 0) & (((this.glProgramUniformMatrix3x2dv = GLContext.getFunctionAddress("glProgramUniformMatrix3x2dv")) != 0L) ? 1 : 0) & (((this.glProgramUniformMatrix2x4dv = GLContext.getFunctionAddress("glProgramUniformMatrix2x4dv")) != 0L) ? 1 : 0) & (((this.glProgramUniformMatrix4x2dv = GLContext.getFunctionAddress("glProgramUniformMatrix4x2dv")) != 0L) ? 1 : 0) & (((this.glProgramUniformMatrix3x4dv = GLContext.getFunctionAddress("glProgramUniformMatrix3x4dv")) != 0L) ? 1 : 0) & (((this.glProgramUniformMatrix4x3dv = GLContext.getFunctionAddress("glProgramUniformMatrix4x3dv")) != 0L) ? 1 : 0) & (((this.glValidateProgramPipeline = GLContext.getFunctionAddress("glValidateProgramPipeline")) != 0L) ? 1 : 0) & (((this.glGetProgramPipelineInfoLog = GLContext.getFunctionAddress("glGetProgramPipelineInfoLog")) != 0L) ? 1 : 0);
  }
  
  private boolean ARB_shader_atomic_counters_initNativeFunctionAddresses() {
    return ((this.glGetActiveAtomicCounterBufferiv = GLContext.getFunctionAddress("glGetActiveAtomicCounterBufferiv")) != 0L);
  }
  
  private boolean ARB_shader_image_load_store_initNativeFunctionAddresses() {
    return (((this.glBindImageTexture = GLContext.getFunctionAddress("glBindImageTexture")) != 0L)) & (((this.glMemoryBarrier = GLContext.getFunctionAddress("glMemoryBarrier")) != 0L));
  }
  
  private boolean ARB_shader_objects_initNativeFunctionAddresses() {
    return (((this.glDeleteObjectARB = GLContext.getFunctionAddress("glDeleteObjectARB")) != 0L)) & (((this.glGetHandleARB = GLContext.getFunctionAddress("glGetHandleARB")) != 0L)) & (((this.glDetachObjectARB = GLContext.getFunctionAddress("glDetachObjectARB")) != 0L) ? 1 : 0) & (((this.glCreateShaderObjectARB = GLContext.getFunctionAddress("glCreateShaderObjectARB")) != 0L) ? 1 : 0) & (((this.glShaderSourceARB = GLContext.getFunctionAddress("glShaderSourceARB")) != 0L) ? 1 : 0) & (((this.glCompileShaderARB = GLContext.getFunctionAddress("glCompileShaderARB")) != 0L) ? 1 : 0) & (((this.glCreateProgramObjectARB = GLContext.getFunctionAddress("glCreateProgramObjectARB")) != 0L) ? 1 : 0) & (((this.glAttachObjectARB = GLContext.getFunctionAddress("glAttachObjectARB")) != 0L) ? 1 : 0) & (((this.glLinkProgramARB = GLContext.getFunctionAddress("glLinkProgramARB")) != 0L) ? 1 : 0) & (((this.glUseProgramObjectARB = GLContext.getFunctionAddress("glUseProgramObjectARB")) != 0L) ? 1 : 0) & (((this.glValidateProgramARB = GLContext.getFunctionAddress("glValidateProgramARB")) != 0L) ? 1 : 0) & (((this.glUniform1fARB = GLContext.getFunctionAddress("glUniform1fARB")) != 0L) ? 1 : 0) & (((this.glUniform2fARB = GLContext.getFunctionAddress("glUniform2fARB")) != 0L) ? 1 : 0) & (((this.glUniform3fARB = GLContext.getFunctionAddress("glUniform3fARB")) != 0L) ? 1 : 0) & (((this.glUniform4fARB = GLContext.getFunctionAddress("glUniform4fARB")) != 0L) ? 1 : 0) & (((this.glUniform1iARB = GLContext.getFunctionAddress("glUniform1iARB")) != 0L) ? 1 : 0) & (((this.glUniform2iARB = GLContext.getFunctionAddress("glUniform2iARB")) != 0L) ? 1 : 0) & (((this.glUniform3iARB = GLContext.getFunctionAddress("glUniform3iARB")) != 0L) ? 1 : 0) & (((this.glUniform4iARB = GLContext.getFunctionAddress("glUniform4iARB")) != 0L) ? 1 : 0) & (((this.glUniform1fvARB = GLContext.getFunctionAddress("glUniform1fvARB")) != 0L) ? 1 : 0) & (((this.glUniform2fvARB = GLContext.getFunctionAddress("glUniform2fvARB")) != 0L) ? 1 : 0) & (((this.glUniform3fvARB = GLContext.getFunctionAddress("glUniform3fvARB")) != 0L) ? 1 : 0) & (((this.glUniform4fvARB = GLContext.getFunctionAddress("glUniform4fvARB")) != 0L) ? 1 : 0) & (((this.glUniform1ivARB = GLContext.getFunctionAddress("glUniform1ivARB")) != 0L) ? 1 : 0) & (((this.glUniform2ivARB = GLContext.getFunctionAddress("glUniform2ivARB")) != 0L) ? 1 : 0) & (((this.glUniform3ivARB = GLContext.getFunctionAddress("glUniform3ivARB")) != 0L) ? 1 : 0) & (((this.glUniform4ivARB = GLContext.getFunctionAddress("glUniform4ivARB")) != 0L) ? 1 : 0) & (((this.glUniformMatrix2fvARB = GLContext.getFunctionAddress("glUniformMatrix2fvARB")) != 0L) ? 1 : 0) & (((this.glUniformMatrix3fvARB = GLContext.getFunctionAddress("glUniformMatrix3fvARB")) != 0L) ? 1 : 0) & (((this.glUniformMatrix4fvARB = GLContext.getFunctionAddress("glUniformMatrix4fvARB")) != 0L) ? 1 : 0) & (((this.glGetObjectParameterfvARB = GLContext.getFunctionAddress("glGetObjectParameterfvARB")) != 0L) ? 1 : 0) & (((this.glGetObjectParameterivARB = GLContext.getFunctionAddress("glGetObjectParameterivARB")) != 0L) ? 1 : 0) & (((this.glGetInfoLogARB = GLContext.getFunctionAddress("glGetInfoLogARB")) != 0L) ? 1 : 0) & (((this.glGetAttachedObjectsARB = GLContext.getFunctionAddress("glGetAttachedObjectsARB")) != 0L) ? 1 : 0) & (((this.glGetUniformLocationARB = GLContext.getFunctionAddress("glGetUniformLocationARB")) != 0L) ? 1 : 0) & (((this.glGetActiveUniformARB = GLContext.getFunctionAddress("glGetActiveUniformARB")) != 0L) ? 1 : 0) & (((this.glGetUniformfvARB = GLContext.getFunctionAddress("glGetUniformfvARB")) != 0L) ? 1 : 0) & (((this.glGetUniformivARB = GLContext.getFunctionAddress("glGetUniformivARB")) != 0L) ? 1 : 0) & (((this.glGetShaderSourceARB = GLContext.getFunctionAddress("glGetShaderSourceARB")) != 0L) ? 1 : 0);
  }
  
  private boolean ARB_shader_storage_buffer_object_initNativeFunctionAddresses() {
    return ((this.glShaderStorageBlockBinding = GLContext.getFunctionAddress("glShaderStorageBlockBinding")) != 0L);
  }
  
  private boolean ARB_shader_subroutine_initNativeFunctionAddresses() {
    return (((this.glGetSubroutineUniformLocation = GLContext.getFunctionAddress("glGetSubroutineUniformLocation")) != 0L)) & (((this.glGetSubroutineIndex = GLContext.getFunctionAddress("glGetSubroutineIndex")) != 0L)) & (((this.glGetActiveSubroutineUniformiv = GLContext.getFunctionAddress("glGetActiveSubroutineUniformiv")) != 0L) ? 1 : 0) & (((this.glGetActiveSubroutineUniformName = GLContext.getFunctionAddress("glGetActiveSubroutineUniformName")) != 0L) ? 1 : 0) & (((this.glGetActiveSubroutineName = GLContext.getFunctionAddress("glGetActiveSubroutineName")) != 0L) ? 1 : 0) & (((this.glUniformSubroutinesuiv = GLContext.getFunctionAddress("glUniformSubroutinesuiv")) != 0L) ? 1 : 0) & (((this.glGetUniformSubroutineuiv = GLContext.getFunctionAddress("glGetUniformSubroutineuiv")) != 0L) ? 1 : 0) & (((this.glGetProgramStageiv = GLContext.getFunctionAddress("glGetProgramStageiv")) != 0L) ? 1 : 0);
  }
  
  private boolean ARB_shading_language_include_initNativeFunctionAddresses() {
    return (((this.glNamedStringARB = GLContext.getFunctionAddress("glNamedStringARB")) != 0L)) & (((this.glDeleteNamedStringARB = GLContext.getFunctionAddress("glDeleteNamedStringARB")) != 0L)) & (((this.glCompileShaderIncludeARB = GLContext.getFunctionAddress("glCompileShaderIncludeARB")) != 0L) ? 1 : 0) & (((this.glIsNamedStringARB = GLContext.getFunctionAddress("glIsNamedStringARB")) != 0L) ? 1 : 0) & (((this.glGetNamedStringARB = GLContext.getFunctionAddress("glGetNamedStringARB")) != 0L) ? 1 : 0) & (((this.glGetNamedStringivARB = GLContext.getFunctionAddress("glGetNamedStringivARB")) != 0L) ? 1 : 0);
  }
  
  private boolean ARB_sparse_buffer_initNativeFunctionAddresses() {
    return ((this.glBufferPageCommitmentARB = GLContext.getFunctionAddress("glBufferPageCommitmentARB")) != 0L);
  }
  
  private boolean ARB_sparse_texture_initNativeFunctionAddresses(Set<String> supported_extensions) {
    return (((this.glTexPageCommitmentARB = GLContext.getFunctionAddress("glTexPageCommitmentARB")) != 0L)) & ((!supported_extensions.contains("GL_EXT_direct_state_access") || (this.glTexturePageCommitmentEXT = GLContext.getFunctionAddress("glTexturePageCommitmentEXT")) != 0L));
  }
  
  private boolean ARB_sync_initNativeFunctionAddresses() {
    return (((this.glFenceSync = GLContext.getFunctionAddress("glFenceSync")) != 0L)) & (((this.glIsSync = GLContext.getFunctionAddress("glIsSync")) != 0L)) & (((this.glDeleteSync = GLContext.getFunctionAddress("glDeleteSync")) != 0L) ? 1 : 0) & (((this.glClientWaitSync = GLContext.getFunctionAddress("glClientWaitSync")) != 0L) ? 1 : 0) & (((this.glWaitSync = GLContext.getFunctionAddress("glWaitSync")) != 0L) ? 1 : 0) & (((this.glGetInteger64v = GLContext.getFunctionAddress("glGetInteger64v")) != 0L) ? 1 : 0) & (((this.glGetSynciv = GLContext.getFunctionAddress("glGetSynciv")) != 0L) ? 1 : 0);
  }
  
  private boolean ARB_tessellation_shader_initNativeFunctionAddresses() {
    return (((this.glPatchParameteri = GLContext.getFunctionAddress("glPatchParameteri")) != 0L)) & (((this.glPatchParameterfv = GLContext.getFunctionAddress("glPatchParameterfv")) != 0L));
  }
  
  private boolean ARB_texture_barrier_initNativeFunctionAddresses() {
    return ((this.glTextureBarrier = GLContext.getFunctionAddress("glTextureBarrier")) != 0L);
  }
  
  private boolean ARB_texture_buffer_object_initNativeFunctionAddresses() {
    return ((this.glTexBufferARB = GLContext.getFunctionAddress("glTexBufferARB")) != 0L);
  }
  
  private boolean ARB_texture_buffer_range_initNativeFunctionAddresses(Set<String> supported_extensions) {
    return (((this.glTexBufferRange = GLContext.getFunctionAddress("glTexBufferRange")) != 0L)) & ((!supported_extensions.contains("GL_EXT_direct_state_access") || (this.glTextureBufferRangeEXT = GLContext.getFunctionAddress("glTextureBufferRangeEXT")) != 0L));
  }
  
  private boolean ARB_texture_compression_initNativeFunctionAddresses() {
    return (((this.glCompressedTexImage1DARB = GLContext.getFunctionAddress("glCompressedTexImage1DARB")) != 0L)) & (((this.glCompressedTexImage2DARB = GLContext.getFunctionAddress("glCompressedTexImage2DARB")) != 0L)) & (((this.glCompressedTexImage3DARB = GLContext.getFunctionAddress("glCompressedTexImage3DARB")) != 0L) ? 1 : 0) & (((this.glCompressedTexSubImage1DARB = GLContext.getFunctionAddress("glCompressedTexSubImage1DARB")) != 0L) ? 1 : 0) & (((this.glCompressedTexSubImage2DARB = GLContext.getFunctionAddress("glCompressedTexSubImage2DARB")) != 0L) ? 1 : 0) & (((this.glCompressedTexSubImage3DARB = GLContext.getFunctionAddress("glCompressedTexSubImage3DARB")) != 0L) ? 1 : 0) & (((this.glGetCompressedTexImageARB = GLContext.getFunctionAddress("glGetCompressedTexImageARB")) != 0L) ? 1 : 0);
  }
  
  private boolean ARB_texture_multisample_initNativeFunctionAddresses() {
    return (((this.glTexImage2DMultisample = GLContext.getFunctionAddress("glTexImage2DMultisample")) != 0L)) & (((this.glTexImage3DMultisample = GLContext.getFunctionAddress("glTexImage3DMultisample")) != 0L)) & (((this.glGetMultisamplefv = GLContext.getFunctionAddress("glGetMultisamplefv")) != 0L) ? 1 : 0) & (((this.glSampleMaski = GLContext.getFunctionAddress("glSampleMaski")) != 0L) ? 1 : 0);
  }
  
  private boolean ARB_texture_storage_initNativeFunctionAddresses(Set<String> supported_extensions) {
    return (((this.glTexStorage1D = GLContext.getFunctionAddress(new String[] { "glTexStorage1D", "glTexStorage1DEXT" })) != 0L)) & (((this.glTexStorage2D = GLContext.getFunctionAddress(new String[] { "glTexStorage2D", "glTexStorage2DEXT" })) != 0L)) & (((this.glTexStorage3D = GLContext.getFunctionAddress(new String[] { "glTexStorage3D", "glTexStorage3DEXT" })) != 0L) ? 1 : 0) & ((!supported_extensions.contains("GL_EXT_direct_state_access") || (this.glTextureStorage1DEXT = GLContext.getFunctionAddress(new String[] { "glTextureStorage1DEXT", "glTextureStorage1DEXTEXT" })) != 0L) ? 1 : 0) & ((!supported_extensions.contains("GL_EXT_direct_state_access") || (this.glTextureStorage2DEXT = GLContext.getFunctionAddress(new String[] { "glTextureStorage2DEXT", "glTextureStorage2DEXTEXT" })) != 0L) ? 1 : 0) & ((!supported_extensions.contains("GL_EXT_direct_state_access") || (this.glTextureStorage3DEXT = GLContext.getFunctionAddress(new String[] { "glTextureStorage3DEXT", "glTextureStorage3DEXTEXT" })) != 0L) ? 1 : 0);
  }
  
  private boolean ARB_texture_storage_multisample_initNativeFunctionAddresses(Set<String> supported_extensions) {
    return (((this.glTexStorage2DMultisample = GLContext.getFunctionAddress("glTexStorage2DMultisample")) != 0L)) & (((this.glTexStorage3DMultisample = GLContext.getFunctionAddress("glTexStorage3DMultisample")) != 0L)) & ((!supported_extensions.contains("GL_EXT_direct_state_access") || (this.glTextureStorage2DMultisampleEXT = GLContext.getFunctionAddress("glTextureStorage2DMultisampleEXT")) != 0L) ? 1 : 0) & ((!supported_extensions.contains("GL_EXT_direct_state_access") || (this.glTextureStorage3DMultisampleEXT = GLContext.getFunctionAddress("glTextureStorage3DMultisampleEXT")) != 0L) ? 1 : 0);
  }
  
  private boolean ARB_texture_view_initNativeFunctionAddresses() {
    return ((this.glTextureView = GLContext.getFunctionAddress("glTextureView")) != 0L);
  }
  
  private boolean ARB_timer_query_initNativeFunctionAddresses() {
    return (((this.glQueryCounter = GLContext.getFunctionAddress("glQueryCounter")) != 0L)) & (((this.glGetQueryObjecti64v = GLContext.getFunctionAddress("glGetQueryObjecti64v")) != 0L)) & (((this.glGetQueryObjectui64v = GLContext.getFunctionAddress("glGetQueryObjectui64v")) != 0L) ? 1 : 0);
  }
  
  private boolean ARB_transform_feedback2_initNativeFunctionAddresses() {
    return (((this.glBindTransformFeedback = GLContext.getFunctionAddress("glBindTransformFeedback")) != 0L)) & (((this.glDeleteTransformFeedbacks = GLContext.getFunctionAddress("glDeleteTransformFeedbacks")) != 0L)) & (((this.glGenTransformFeedbacks = GLContext.getFunctionAddress("glGenTransformFeedbacks")) != 0L) ? 1 : 0) & (((this.glIsTransformFeedback = GLContext.getFunctionAddress("glIsTransformFeedback")) != 0L) ? 1 : 0) & (((this.glPauseTransformFeedback = GLContext.getFunctionAddress("glPauseTransformFeedback")) != 0L) ? 1 : 0) & (((this.glResumeTransformFeedback = GLContext.getFunctionAddress("glResumeTransformFeedback")) != 0L) ? 1 : 0) & (((this.glDrawTransformFeedback = GLContext.getFunctionAddress("glDrawTransformFeedback")) != 0L) ? 1 : 0);
  }
  
  private boolean ARB_transform_feedback3_initNativeFunctionAddresses() {
    return (((this.glDrawTransformFeedbackStream = GLContext.getFunctionAddress("glDrawTransformFeedbackStream")) != 0L)) & (((this.glBeginQueryIndexed = GLContext.getFunctionAddress("glBeginQueryIndexed")) != 0L)) & (((this.glEndQueryIndexed = GLContext.getFunctionAddress("glEndQueryIndexed")) != 0L) ? 1 : 0) & (((this.glGetQueryIndexediv = GLContext.getFunctionAddress("glGetQueryIndexediv")) != 0L) ? 1 : 0);
  }
  
  private boolean ARB_transform_feedback_instanced_initNativeFunctionAddresses() {
    return (((this.glDrawTransformFeedbackInstanced = GLContext.getFunctionAddress("glDrawTransformFeedbackInstanced")) != 0L)) & (((this.glDrawTransformFeedbackStreamInstanced = GLContext.getFunctionAddress("glDrawTransformFeedbackStreamInstanced")) != 0L));
  }
  
  private boolean ARB_transpose_matrix_initNativeFunctionAddresses() {
    return (((this.glLoadTransposeMatrixfARB = GLContext.getFunctionAddress("glLoadTransposeMatrixfARB")) != 0L)) & (((this.glMultTransposeMatrixfARB = GLContext.getFunctionAddress("glMultTransposeMatrixfARB")) != 0L));
  }
  
  private boolean ARB_uniform_buffer_object_initNativeFunctionAddresses() {
    return (((this.glGetUniformIndices = GLContext.getFunctionAddress("glGetUniformIndices")) != 0L)) & (((this.glGetActiveUniformsiv = GLContext.getFunctionAddress("glGetActiveUniformsiv")) != 0L)) & (((this.glGetActiveUniformName = GLContext.getFunctionAddress("glGetActiveUniformName")) != 0L) ? 1 : 0) & (((this.glGetUniformBlockIndex = GLContext.getFunctionAddress("glGetUniformBlockIndex")) != 0L) ? 1 : 0) & (((this.glGetActiveUniformBlockiv = GLContext.getFunctionAddress("glGetActiveUniformBlockiv")) != 0L) ? 1 : 0) & (((this.glGetActiveUniformBlockName = GLContext.getFunctionAddress("glGetActiveUniformBlockName")) != 0L) ? 1 : 0) & (((this.glBindBufferRange = GLContext.getFunctionAddress("glBindBufferRange")) != 0L) ? 1 : 0) & (((this.glBindBufferBase = GLContext.getFunctionAddress("glBindBufferBase")) != 0L) ? 1 : 0) & (((this.glGetIntegeri_v = GLContext.getFunctionAddress("glGetIntegeri_v")) != 0L) ? 1 : 0) & (((this.glUniformBlockBinding = GLContext.getFunctionAddress("glUniformBlockBinding")) != 0L) ? 1 : 0);
  }
  
  private boolean ARB_vertex_array_object_initNativeFunctionAddresses() {
    return (((this.glBindVertexArray = GLContext.getFunctionAddress("glBindVertexArray")) != 0L)) & (((this.glDeleteVertexArrays = GLContext.getFunctionAddress("glDeleteVertexArrays")) != 0L)) & (((this.glGenVertexArrays = GLContext.getFunctionAddress("glGenVertexArrays")) != 0L) ? 1 : 0) & (((this.glIsVertexArray = GLContext.getFunctionAddress("glIsVertexArray")) != 0L) ? 1 : 0);
  }
  
  private boolean ARB_vertex_attrib_64bit_initNativeFunctionAddresses(Set<String> supported_extensions) {
    return (((this.glVertexAttribL1d = GLContext.getFunctionAddress("glVertexAttribL1d")) != 0L)) & (((this.glVertexAttribL2d = GLContext.getFunctionAddress("glVertexAttribL2d")) != 0L)) & (((this.glVertexAttribL3d = GLContext.getFunctionAddress("glVertexAttribL3d")) != 0L) ? 1 : 0) & (((this.glVertexAttribL4d = GLContext.getFunctionAddress("glVertexAttribL4d")) != 0L) ? 1 : 0) & (((this.glVertexAttribL1dv = GLContext.getFunctionAddress("glVertexAttribL1dv")) != 0L) ? 1 : 0) & (((this.glVertexAttribL2dv = GLContext.getFunctionAddress("glVertexAttribL2dv")) != 0L) ? 1 : 0) & (((this.glVertexAttribL3dv = GLContext.getFunctionAddress("glVertexAttribL3dv")) != 0L) ? 1 : 0) & (((this.glVertexAttribL4dv = GLContext.getFunctionAddress("glVertexAttribL4dv")) != 0L) ? 1 : 0) & (((this.glVertexAttribLPointer = GLContext.getFunctionAddress("glVertexAttribLPointer")) != 0L) ? 1 : 0) & (((this.glGetVertexAttribLdv = GLContext.getFunctionAddress("glGetVertexAttribLdv")) != 0L) ? 1 : 0) & ((!supported_extensions.contains("GL_EXT_direct_state_access") || (this.glVertexArrayVertexAttribLOffsetEXT = GLContext.getFunctionAddress("glVertexArrayVertexAttribLOffsetEXT")) != 0L) ? 1 : 0);
  }
  
  private boolean ARB_vertex_attrib_binding_initNativeFunctionAddresses() {
    return (((this.glBindVertexBuffer = GLContext.getFunctionAddress("glBindVertexBuffer")) != 0L)) & (((this.glVertexAttribFormat = GLContext.getFunctionAddress("glVertexAttribFormat")) != 0L)) & (((this.glVertexAttribIFormat = GLContext.getFunctionAddress("glVertexAttribIFormat")) != 0L) ? 1 : 0) & (((this.glVertexAttribLFormat = GLContext.getFunctionAddress("glVertexAttribLFormat")) != 0L) ? 1 : 0) & (((this.glVertexAttribBinding = GLContext.getFunctionAddress("glVertexAttribBinding")) != 0L) ? 1 : 0) & (((this.glVertexBindingDivisor = GLContext.getFunctionAddress("glVertexBindingDivisor")) != 0L) ? 1 : 0);
  }
  
  private boolean ARB_vertex_blend_initNativeFunctionAddresses() {
    return (((this.glWeightbvARB = GLContext.getFunctionAddress("glWeightbvARB")) != 0L)) & (((this.glWeightsvARB = GLContext.getFunctionAddress("glWeightsvARB")) != 0L)) & (((this.glWeightivARB = GLContext.getFunctionAddress("glWeightivARB")) != 0L) ? 1 : 0) & (((this.glWeightfvARB = GLContext.getFunctionAddress("glWeightfvARB")) != 0L) ? 1 : 0) & (((this.glWeightdvARB = GLContext.getFunctionAddress("glWeightdvARB")) != 0L) ? 1 : 0) & (((this.glWeightubvARB = GLContext.getFunctionAddress("glWeightubvARB")) != 0L) ? 1 : 0) & (((this.glWeightusvARB = GLContext.getFunctionAddress("glWeightusvARB")) != 0L) ? 1 : 0) & (((this.glWeightuivARB = GLContext.getFunctionAddress("glWeightuivARB")) != 0L) ? 1 : 0) & (((this.glWeightPointerARB = GLContext.getFunctionAddress("glWeightPointerARB")) != 0L) ? 1 : 0) & (((this.glVertexBlendARB = GLContext.getFunctionAddress("glVertexBlendARB")) != 0L) ? 1 : 0);
  }
  
  private boolean ARB_vertex_program_initNativeFunctionAddresses() {
    return (((this.glVertexAttrib1sARB = GLContext.getFunctionAddress("glVertexAttrib1sARB")) != 0L)) & (((this.glVertexAttrib1fARB = GLContext.getFunctionAddress("glVertexAttrib1fARB")) != 0L)) & (((this.glVertexAttrib1dARB = GLContext.getFunctionAddress("glVertexAttrib1dARB")) != 0L) ? 1 : 0) & (((this.glVertexAttrib2sARB = GLContext.getFunctionAddress("glVertexAttrib2sARB")) != 0L) ? 1 : 0) & (((this.glVertexAttrib2fARB = GLContext.getFunctionAddress("glVertexAttrib2fARB")) != 0L) ? 1 : 0) & (((this.glVertexAttrib2dARB = GLContext.getFunctionAddress("glVertexAttrib2dARB")) != 0L) ? 1 : 0) & (((this.glVertexAttrib3sARB = GLContext.getFunctionAddress("glVertexAttrib3sARB")) != 0L) ? 1 : 0) & (((this.glVertexAttrib3fARB = GLContext.getFunctionAddress("glVertexAttrib3fARB")) != 0L) ? 1 : 0) & (((this.glVertexAttrib3dARB = GLContext.getFunctionAddress("glVertexAttrib3dARB")) != 0L) ? 1 : 0) & (((this.glVertexAttrib4sARB = GLContext.getFunctionAddress("glVertexAttrib4sARB")) != 0L) ? 1 : 0) & (((this.glVertexAttrib4fARB = GLContext.getFunctionAddress("glVertexAttrib4fARB")) != 0L) ? 1 : 0) & (((this.glVertexAttrib4dARB = GLContext.getFunctionAddress("glVertexAttrib4dARB")) != 0L) ? 1 : 0) & (((this.glVertexAttrib4NubARB = GLContext.getFunctionAddress("glVertexAttrib4NubARB")) != 0L) ? 1 : 0) & (((this.glVertexAttribPointerARB = GLContext.getFunctionAddress("glVertexAttribPointerARB")) != 0L) ? 1 : 0) & (((this.glEnableVertexAttribArrayARB = GLContext.getFunctionAddress("glEnableVertexAttribArrayARB")) != 0L) ? 1 : 0) & (((this.glDisableVertexAttribArrayARB = GLContext.getFunctionAddress("glDisableVertexAttribArrayARB")) != 0L) ? 1 : 0) & (((this.glGetVertexAttribfvARB = GLContext.getFunctionAddress("glGetVertexAttribfvARB")) != 0L) ? 1 : 0) & (((this.glGetVertexAttribdvARB = GLContext.getFunctionAddress("glGetVertexAttribdvARB")) != 0L) ? 1 : 0) & (((this.glGetVertexAttribivARB = GLContext.getFunctionAddress("glGetVertexAttribivARB")) != 0L) ? 1 : 0) & (((this.glGetVertexAttribPointervARB = GLContext.getFunctionAddress("glGetVertexAttribPointervARB")) != 0L) ? 1 : 0);
  }
  
  private boolean ARB_vertex_shader_initNativeFunctionAddresses() {
    return (((this.glVertexAttrib1sARB = GLContext.getFunctionAddress("glVertexAttrib1sARB")) != 0L)) & (((this.glVertexAttrib1fARB = GLContext.getFunctionAddress("glVertexAttrib1fARB")) != 0L)) & (((this.glVertexAttrib1dARB = GLContext.getFunctionAddress("glVertexAttrib1dARB")) != 0L) ? 1 : 0) & (((this.glVertexAttrib2sARB = GLContext.getFunctionAddress("glVertexAttrib2sARB")) != 0L) ? 1 : 0) & (((this.glVertexAttrib2fARB = GLContext.getFunctionAddress("glVertexAttrib2fARB")) != 0L) ? 1 : 0) & (((this.glVertexAttrib2dARB = GLContext.getFunctionAddress("glVertexAttrib2dARB")) != 0L) ? 1 : 0) & (((this.glVertexAttrib3sARB = GLContext.getFunctionAddress("glVertexAttrib3sARB")) != 0L) ? 1 : 0) & (((this.glVertexAttrib3fARB = GLContext.getFunctionAddress("glVertexAttrib3fARB")) != 0L) ? 1 : 0) & (((this.glVertexAttrib3dARB = GLContext.getFunctionAddress("glVertexAttrib3dARB")) != 0L) ? 1 : 0) & (((this.glVertexAttrib4sARB = GLContext.getFunctionAddress("glVertexAttrib4sARB")) != 0L) ? 1 : 0) & (((this.glVertexAttrib4fARB = GLContext.getFunctionAddress("glVertexAttrib4fARB")) != 0L) ? 1 : 0) & (((this.glVertexAttrib4dARB = GLContext.getFunctionAddress("glVertexAttrib4dARB")) != 0L) ? 1 : 0) & (((this.glVertexAttrib4NubARB = GLContext.getFunctionAddress("glVertexAttrib4NubARB")) != 0L) ? 1 : 0) & (((this.glVertexAttribPointerARB = GLContext.getFunctionAddress("glVertexAttribPointerARB")) != 0L) ? 1 : 0) & (((this.glEnableVertexAttribArrayARB = GLContext.getFunctionAddress("glEnableVertexAttribArrayARB")) != 0L) ? 1 : 0) & (((this.glDisableVertexAttribArrayARB = GLContext.getFunctionAddress("glDisableVertexAttribArrayARB")) != 0L) ? 1 : 0) & (((this.glBindAttribLocationARB = GLContext.getFunctionAddress("glBindAttribLocationARB")) != 0L) ? 1 : 0) & (((this.glGetActiveAttribARB = GLContext.getFunctionAddress("glGetActiveAttribARB")) != 0L) ? 1 : 0) & (((this.glGetAttribLocationARB = GLContext.getFunctionAddress("glGetAttribLocationARB")) != 0L) ? 1 : 0) & (((this.glGetVertexAttribfvARB = GLContext.getFunctionAddress("glGetVertexAttribfvARB")) != 0L) ? 1 : 0) & (((this.glGetVertexAttribdvARB = GLContext.getFunctionAddress("glGetVertexAttribdvARB")) != 0L) ? 1 : 0) & (((this.glGetVertexAttribivARB = GLContext.getFunctionAddress("glGetVertexAttribivARB")) != 0L) ? 1 : 0) & (((this.glGetVertexAttribPointervARB = GLContext.getFunctionAddress("glGetVertexAttribPointervARB")) != 0L) ? 1 : 0);
  }
  
  private boolean ARB_vertex_type_2_10_10_10_rev_initNativeFunctionAddresses() {
    return (((this.glVertexP2ui = GLContext.getFunctionAddress("glVertexP2ui")) != 0L)) & (((this.glVertexP3ui = GLContext.getFunctionAddress("glVertexP3ui")) != 0L)) & (((this.glVertexP4ui = GLContext.getFunctionAddress("glVertexP4ui")) != 0L) ? 1 : 0) & (((this.glVertexP2uiv = GLContext.getFunctionAddress("glVertexP2uiv")) != 0L) ? 1 : 0) & (((this.glVertexP3uiv = GLContext.getFunctionAddress("glVertexP3uiv")) != 0L) ? 1 : 0) & (((this.glVertexP4uiv = GLContext.getFunctionAddress("glVertexP4uiv")) != 0L) ? 1 : 0) & (((this.glTexCoordP1ui = GLContext.getFunctionAddress("glTexCoordP1ui")) != 0L) ? 1 : 0) & (((this.glTexCoordP2ui = GLContext.getFunctionAddress("glTexCoordP2ui")) != 0L) ? 1 : 0) & (((this.glTexCoordP3ui = GLContext.getFunctionAddress("glTexCoordP3ui")) != 0L) ? 1 : 0) & (((this.glTexCoordP4ui = GLContext.getFunctionAddress("glTexCoordP4ui")) != 0L) ? 1 : 0) & (((this.glTexCoordP1uiv = GLContext.getFunctionAddress("glTexCoordP1uiv")) != 0L) ? 1 : 0) & (((this.glTexCoordP2uiv = GLContext.getFunctionAddress("glTexCoordP2uiv")) != 0L) ? 1 : 0) & (((this.glTexCoordP3uiv = GLContext.getFunctionAddress("glTexCoordP3uiv")) != 0L) ? 1 : 0) & (((this.glTexCoordP4uiv = GLContext.getFunctionAddress("glTexCoordP4uiv")) != 0L) ? 1 : 0) & (((this.glMultiTexCoordP1ui = GLContext.getFunctionAddress("glMultiTexCoordP1ui")) != 0L) ? 1 : 0) & (((this.glMultiTexCoordP2ui = GLContext.getFunctionAddress("glMultiTexCoordP2ui")) != 0L) ? 1 : 0) & (((this.glMultiTexCoordP3ui = GLContext.getFunctionAddress("glMultiTexCoordP3ui")) != 0L) ? 1 : 0) & (((this.glMultiTexCoordP4ui = GLContext.getFunctionAddress("glMultiTexCoordP4ui")) != 0L) ? 1 : 0) & (((this.glMultiTexCoordP1uiv = GLContext.getFunctionAddress("glMultiTexCoordP1uiv")) != 0L) ? 1 : 0) & (((this.glMultiTexCoordP2uiv = GLContext.getFunctionAddress("glMultiTexCoordP2uiv")) != 0L) ? 1 : 0) & (((this.glMultiTexCoordP3uiv = GLContext.getFunctionAddress("glMultiTexCoordP3uiv")) != 0L) ? 1 : 0) & (((this.glMultiTexCoordP4uiv = GLContext.getFunctionAddress("glMultiTexCoordP4uiv")) != 0L) ? 1 : 0) & (((this.glNormalP3ui = GLContext.getFunctionAddress("glNormalP3ui")) != 0L) ? 1 : 0) & (((this.glNormalP3uiv = GLContext.getFunctionAddress("glNormalP3uiv")) != 0L) ? 1 : 0) & (((this.glColorP3ui = GLContext.getFunctionAddress("glColorP3ui")) != 0L) ? 1 : 0) & (((this.glColorP4ui = GLContext.getFunctionAddress("glColorP4ui")) != 0L) ? 1 : 0) & (((this.glColorP3uiv = GLContext.getFunctionAddress("glColorP3uiv")) != 0L) ? 1 : 0) & (((this.glColorP4uiv = GLContext.getFunctionAddress("glColorP4uiv")) != 0L) ? 1 : 0) & (((this.glSecondaryColorP3ui = GLContext.getFunctionAddress("glSecondaryColorP3ui")) != 0L) ? 1 : 0) & (((this.glSecondaryColorP3uiv = GLContext.getFunctionAddress("glSecondaryColorP3uiv")) != 0L) ? 1 : 0) & (((this.glVertexAttribP1ui = GLContext.getFunctionAddress("glVertexAttribP1ui")) != 0L) ? 1 : 0) & (((this.glVertexAttribP2ui = GLContext.getFunctionAddress("glVertexAttribP2ui")) != 0L) ? 1 : 0) & (((this.glVertexAttribP3ui = GLContext.getFunctionAddress("glVertexAttribP3ui")) != 0L) ? 1 : 0) & (((this.glVertexAttribP4ui = GLContext.getFunctionAddress("glVertexAttribP4ui")) != 0L) ? 1 : 0) & (((this.glVertexAttribP1uiv = GLContext.getFunctionAddress("glVertexAttribP1uiv")) != 0L) ? 1 : 0) & (((this.glVertexAttribP2uiv = GLContext.getFunctionAddress("glVertexAttribP2uiv")) != 0L) ? 1 : 0) & (((this.glVertexAttribP3uiv = GLContext.getFunctionAddress("glVertexAttribP3uiv")) != 0L) ? 1 : 0) & (((this.glVertexAttribP4uiv = GLContext.getFunctionAddress("glVertexAttribP4uiv")) != 0L) ? 1 : 0);
  }
  
  private boolean ARB_viewport_array_initNativeFunctionAddresses() {
    return (((this.glViewportArrayv = GLContext.getFunctionAddress("glViewportArrayv")) != 0L)) & (((this.glViewportIndexedf = GLContext.getFunctionAddress("glViewportIndexedf")) != 0L)) & (((this.glViewportIndexedfv = GLContext.getFunctionAddress("glViewportIndexedfv")) != 0L) ? 1 : 0) & (((this.glScissorArrayv = GLContext.getFunctionAddress("glScissorArrayv")) != 0L) ? 1 : 0) & (((this.glScissorIndexed = GLContext.getFunctionAddress("glScissorIndexed")) != 0L) ? 1 : 0) & (((this.glScissorIndexedv = GLContext.getFunctionAddress("glScissorIndexedv")) != 0L) ? 1 : 0) & (((this.glDepthRangeArrayv = GLContext.getFunctionAddress("glDepthRangeArrayv")) != 0L) ? 1 : 0) & (((this.glDepthRangeIndexed = GLContext.getFunctionAddress("glDepthRangeIndexed")) != 0L) ? 1 : 0) & (((this.glGetFloati_v = GLContext.getFunctionAddress("glGetFloati_v")) != 0L) ? 1 : 0) & (((this.glGetDoublei_v = GLContext.getFunctionAddress("glGetDoublei_v")) != 0L) ? 1 : 0) & (((this.glGetIntegerIndexedvEXT = GLContext.getFunctionAddress("glGetIntegerIndexedvEXT")) != 0L) ? 1 : 0) & (((this.glEnableIndexedEXT = GLContext.getFunctionAddress("glEnableIndexedEXT")) != 0L) ? 1 : 0) & (((this.glDisableIndexedEXT = GLContext.getFunctionAddress("glDisableIndexedEXT")) != 0L) ? 1 : 0) & (((this.glIsEnabledIndexedEXT = GLContext.getFunctionAddress("glIsEnabledIndexedEXT")) != 0L) ? 1 : 0);
  }
  
  private boolean ARB_window_pos_initNativeFunctionAddresses(boolean forwardCompatible) {
    return ((forwardCompatible || (this.glWindowPos2fARB = GLContext.getFunctionAddress("glWindowPos2fARB")) != 0L)) & ((forwardCompatible || (this.glWindowPos2dARB = GLContext.getFunctionAddress("glWindowPos2dARB")) != 0L)) & ((forwardCompatible || (this.glWindowPos2iARB = GLContext.getFunctionAddress("glWindowPos2iARB")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glWindowPos2sARB = GLContext.getFunctionAddress("glWindowPos2sARB")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glWindowPos3fARB = GLContext.getFunctionAddress("glWindowPos3fARB")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glWindowPos3dARB = GLContext.getFunctionAddress("glWindowPos3dARB")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glWindowPos3iARB = GLContext.getFunctionAddress("glWindowPos3iARB")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glWindowPos3sARB = GLContext.getFunctionAddress("glWindowPos3sARB")) != 0L) ? 1 : 0);
  }
  
  private boolean ATI_draw_buffers_initNativeFunctionAddresses() {
    return ((this.glDrawBuffersATI = GLContext.getFunctionAddress("glDrawBuffersATI")) != 0L);
  }
  
  private boolean ATI_element_array_initNativeFunctionAddresses() {
    return (((this.glElementPointerATI = GLContext.getFunctionAddress("glElementPointerATI")) != 0L)) & (((this.glDrawElementArrayATI = GLContext.getFunctionAddress("glDrawElementArrayATI")) != 0L)) & (((this.glDrawRangeElementArrayATI = GLContext.getFunctionAddress("glDrawRangeElementArrayATI")) != 0L) ? 1 : 0);
  }
  
  private boolean ATI_envmap_bumpmap_initNativeFunctionAddresses() {
    return (((this.glTexBumpParameterfvATI = GLContext.getFunctionAddress("glTexBumpParameterfvATI")) != 0L)) & (((this.glTexBumpParameterivATI = GLContext.getFunctionAddress("glTexBumpParameterivATI")) != 0L)) & (((this.glGetTexBumpParameterfvATI = GLContext.getFunctionAddress("glGetTexBumpParameterfvATI")) != 0L) ? 1 : 0) & (((this.glGetTexBumpParameterivATI = GLContext.getFunctionAddress("glGetTexBumpParameterivATI")) != 0L) ? 1 : 0);
  }
  
  private boolean ATI_fragment_shader_initNativeFunctionAddresses() {
    return (((this.glGenFragmentShadersATI = GLContext.getFunctionAddress("glGenFragmentShadersATI")) != 0L)) & (((this.glBindFragmentShaderATI = GLContext.getFunctionAddress("glBindFragmentShaderATI")) != 0L)) & (((this.glDeleteFragmentShaderATI = GLContext.getFunctionAddress("glDeleteFragmentShaderATI")) != 0L) ? 1 : 0) & (((this.glBeginFragmentShaderATI = GLContext.getFunctionAddress("glBeginFragmentShaderATI")) != 0L) ? 1 : 0) & (((this.glEndFragmentShaderATI = GLContext.getFunctionAddress("glEndFragmentShaderATI")) != 0L) ? 1 : 0) & (((this.glPassTexCoordATI = GLContext.getFunctionAddress("glPassTexCoordATI")) != 0L) ? 1 : 0) & (((this.glSampleMapATI = GLContext.getFunctionAddress("glSampleMapATI")) != 0L) ? 1 : 0) & (((this.glColorFragmentOp1ATI = GLContext.getFunctionAddress("glColorFragmentOp1ATI")) != 0L) ? 1 : 0) & (((this.glColorFragmentOp2ATI = GLContext.getFunctionAddress("glColorFragmentOp2ATI")) != 0L) ? 1 : 0) & (((this.glColorFragmentOp3ATI = GLContext.getFunctionAddress("glColorFragmentOp3ATI")) != 0L) ? 1 : 0) & (((this.glAlphaFragmentOp1ATI = GLContext.getFunctionAddress("glAlphaFragmentOp1ATI")) != 0L) ? 1 : 0) & (((this.glAlphaFragmentOp2ATI = GLContext.getFunctionAddress("glAlphaFragmentOp2ATI")) != 0L) ? 1 : 0) & (((this.glAlphaFragmentOp3ATI = GLContext.getFunctionAddress("glAlphaFragmentOp3ATI")) != 0L) ? 1 : 0) & (((this.glSetFragmentShaderConstantATI = GLContext.getFunctionAddress("glSetFragmentShaderConstantATI")) != 0L) ? 1 : 0);
  }
  
  private boolean ATI_map_object_buffer_initNativeFunctionAddresses() {
    return (((this.glMapObjectBufferATI = GLContext.getFunctionAddress("glMapObjectBufferATI")) != 0L)) & (((this.glUnmapObjectBufferATI = GLContext.getFunctionAddress("glUnmapObjectBufferATI")) != 0L));
  }
  
  private boolean ATI_pn_triangles_initNativeFunctionAddresses() {
    return (((this.glPNTrianglesfATI = GLContext.getFunctionAddress("glPNTrianglesfATI")) != 0L)) & (((this.glPNTrianglesiATI = GLContext.getFunctionAddress("glPNTrianglesiATI")) != 0L));
  }
  
  private boolean ATI_separate_stencil_initNativeFunctionAddresses() {
    return (((this.glStencilOpSeparateATI = GLContext.getFunctionAddress("glStencilOpSeparateATI")) != 0L)) & (((this.glStencilFuncSeparateATI = GLContext.getFunctionAddress("glStencilFuncSeparateATI")) != 0L));
  }
  
  private boolean ATI_vertex_array_object_initNativeFunctionAddresses() {
    return (((this.glNewObjectBufferATI = GLContext.getFunctionAddress("glNewObjectBufferATI")) != 0L)) & (((this.glIsObjectBufferATI = GLContext.getFunctionAddress("glIsObjectBufferATI")) != 0L)) & (((this.glUpdateObjectBufferATI = GLContext.getFunctionAddress("glUpdateObjectBufferATI")) != 0L) ? 1 : 0) & (((this.glGetObjectBufferfvATI = GLContext.getFunctionAddress("glGetObjectBufferfvATI")) != 0L) ? 1 : 0) & (((this.glGetObjectBufferivATI = GLContext.getFunctionAddress("glGetObjectBufferivATI")) != 0L) ? 1 : 0) & (((this.glFreeObjectBufferATI = GLContext.getFunctionAddress("glFreeObjectBufferATI")) != 0L) ? 1 : 0) & (((this.glArrayObjectATI = GLContext.getFunctionAddress("glArrayObjectATI")) != 0L) ? 1 : 0) & (((this.glGetArrayObjectfvATI = GLContext.getFunctionAddress("glGetArrayObjectfvATI")) != 0L) ? 1 : 0) & (((this.glGetArrayObjectivATI = GLContext.getFunctionAddress("glGetArrayObjectivATI")) != 0L) ? 1 : 0) & (((this.glVariantArrayObjectATI = GLContext.getFunctionAddress("glVariantArrayObjectATI")) != 0L) ? 1 : 0) & (((this.glGetVariantArrayObjectfvATI = GLContext.getFunctionAddress("glGetVariantArrayObjectfvATI")) != 0L) ? 1 : 0) & (((this.glGetVariantArrayObjectivATI = GLContext.getFunctionAddress("glGetVariantArrayObjectivATI")) != 0L) ? 1 : 0);
  }
  
  private boolean ATI_vertex_attrib_array_object_initNativeFunctionAddresses() {
    return (((this.glVertexAttribArrayObjectATI = GLContext.getFunctionAddress("glVertexAttribArrayObjectATI")) != 0L)) & (((this.glGetVertexAttribArrayObjectfvATI = GLContext.getFunctionAddress("glGetVertexAttribArrayObjectfvATI")) != 0L)) & (((this.glGetVertexAttribArrayObjectivATI = GLContext.getFunctionAddress("glGetVertexAttribArrayObjectivATI")) != 0L) ? 1 : 0);
  }
  
  private boolean ATI_vertex_streams_initNativeFunctionAddresses() {
    return (((this.glVertexStream2fATI = GLContext.getFunctionAddress("glVertexStream2fATI")) != 0L)) & (((this.glVertexStream2dATI = GLContext.getFunctionAddress("glVertexStream2dATI")) != 0L)) & (((this.glVertexStream2iATI = GLContext.getFunctionAddress("glVertexStream2iATI")) != 0L) ? 1 : 0) & (((this.glVertexStream2sATI = GLContext.getFunctionAddress("glVertexStream2sATI")) != 0L) ? 1 : 0) & (((this.glVertexStream3fATI = GLContext.getFunctionAddress("glVertexStream3fATI")) != 0L) ? 1 : 0) & (((this.glVertexStream3dATI = GLContext.getFunctionAddress("glVertexStream3dATI")) != 0L) ? 1 : 0) & (((this.glVertexStream3iATI = GLContext.getFunctionAddress("glVertexStream3iATI")) != 0L) ? 1 : 0) & (((this.glVertexStream3sATI = GLContext.getFunctionAddress("glVertexStream3sATI")) != 0L) ? 1 : 0) & (((this.glVertexStream4fATI = GLContext.getFunctionAddress("glVertexStream4fATI")) != 0L) ? 1 : 0) & (((this.glVertexStream4dATI = GLContext.getFunctionAddress("glVertexStream4dATI")) != 0L) ? 1 : 0) & (((this.glVertexStream4iATI = GLContext.getFunctionAddress("glVertexStream4iATI")) != 0L) ? 1 : 0) & (((this.glVertexStream4sATI = GLContext.getFunctionAddress("glVertexStream4sATI")) != 0L) ? 1 : 0) & (((this.glNormalStream3bATI = GLContext.getFunctionAddress("glNormalStream3bATI")) != 0L) ? 1 : 0) & (((this.glNormalStream3fATI = GLContext.getFunctionAddress("glNormalStream3fATI")) != 0L) ? 1 : 0) & (((this.glNormalStream3dATI = GLContext.getFunctionAddress("glNormalStream3dATI")) != 0L) ? 1 : 0) & (((this.glNormalStream3iATI = GLContext.getFunctionAddress("glNormalStream3iATI")) != 0L) ? 1 : 0) & (((this.glNormalStream3sATI = GLContext.getFunctionAddress("glNormalStream3sATI")) != 0L) ? 1 : 0) & (((this.glClientActiveVertexStreamATI = GLContext.getFunctionAddress("glClientActiveVertexStreamATI")) != 0L) ? 1 : 0) & (((this.glVertexBlendEnvfATI = GLContext.getFunctionAddress("glVertexBlendEnvfATI")) != 0L) ? 1 : 0) & (((this.glVertexBlendEnviATI = GLContext.getFunctionAddress("glVertexBlendEnviATI")) != 0L) ? 1 : 0);
  }
  
  private boolean EXT_bindable_uniform_initNativeFunctionAddresses() {
    return (((this.glUniformBufferEXT = GLContext.getFunctionAddress("glUniformBufferEXT")) != 0L)) & (((this.glGetUniformBufferSizeEXT = GLContext.getFunctionAddress("glGetUniformBufferSizeEXT")) != 0L)) & (((this.glGetUniformOffsetEXT = GLContext.getFunctionAddress("glGetUniformOffsetEXT")) != 0L) ? 1 : 0);
  }
  
  private boolean EXT_blend_color_initNativeFunctionAddresses() {
    return ((this.glBlendColorEXT = GLContext.getFunctionAddress("glBlendColorEXT")) != 0L);
  }
  
  private boolean EXT_blend_equation_separate_initNativeFunctionAddresses() {
    return ((this.glBlendEquationSeparateEXT = GLContext.getFunctionAddress("glBlendEquationSeparateEXT")) != 0L);
  }
  
  private boolean EXT_blend_func_separate_initNativeFunctionAddresses() {
    return ((this.glBlendFuncSeparateEXT = GLContext.getFunctionAddress("glBlendFuncSeparateEXT")) != 0L);
  }
  
  private boolean EXT_blend_minmax_initNativeFunctionAddresses() {
    return ((this.glBlendEquationEXT = GLContext.getFunctionAddress("glBlendEquationEXT")) != 0L);
  }
  
  private boolean EXT_compiled_vertex_array_initNativeFunctionAddresses() {
    return (((this.glLockArraysEXT = GLContext.getFunctionAddress("glLockArraysEXT")) != 0L)) & (((this.glUnlockArraysEXT = GLContext.getFunctionAddress("glUnlockArraysEXT")) != 0L));
  }
  
  private boolean EXT_depth_bounds_test_initNativeFunctionAddresses() {
    return ((this.glDepthBoundsEXT = GLContext.getFunctionAddress("glDepthBoundsEXT")) != 0L);
  }
  
  private boolean EXT_direct_state_access_initNativeFunctionAddresses(boolean forwardCompatible, Set<String> supported_extensions) {
    // Byte code:
    //   0: iload_1
    //   1: ifne -> 20
    //   4: aload_0
    //   5: ldc_w 'glClientAttribDefaultEXT'
    //   8: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   11: dup2_x1
    //   12: putfield glClientAttribDefaultEXT : J
    //   15: lconst_0
    //   16: lcmp
    //   17: ifeq -> 24
    //   20: iconst_1
    //   21: goto -> 25
    //   24: iconst_0
    //   25: iload_1
    //   26: ifne -> 45
    //   29: aload_0
    //   30: ldc_w 'glPushClientAttribDefaultEXT'
    //   33: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   36: dup2_x1
    //   37: putfield glPushClientAttribDefaultEXT : J
    //   40: lconst_0
    //   41: lcmp
    //   42: ifeq -> 49
    //   45: iconst_1
    //   46: goto -> 50
    //   49: iconst_0
    //   50: iand
    //   51: iload_1
    //   52: ifne -> 71
    //   55: aload_0
    //   56: ldc_w 'glMatrixLoadfEXT'
    //   59: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   62: dup2_x1
    //   63: putfield glMatrixLoadfEXT : J
    //   66: lconst_0
    //   67: lcmp
    //   68: ifeq -> 75
    //   71: iconst_1
    //   72: goto -> 76
    //   75: iconst_0
    //   76: iand
    //   77: iload_1
    //   78: ifne -> 97
    //   81: aload_0
    //   82: ldc_w 'glMatrixLoaddEXT'
    //   85: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   88: dup2_x1
    //   89: putfield glMatrixLoaddEXT : J
    //   92: lconst_0
    //   93: lcmp
    //   94: ifeq -> 101
    //   97: iconst_1
    //   98: goto -> 102
    //   101: iconst_0
    //   102: iand
    //   103: iload_1
    //   104: ifne -> 123
    //   107: aload_0
    //   108: ldc_w 'glMatrixMultfEXT'
    //   111: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   114: dup2_x1
    //   115: putfield glMatrixMultfEXT : J
    //   118: lconst_0
    //   119: lcmp
    //   120: ifeq -> 127
    //   123: iconst_1
    //   124: goto -> 128
    //   127: iconst_0
    //   128: iand
    //   129: iload_1
    //   130: ifne -> 149
    //   133: aload_0
    //   134: ldc_w 'glMatrixMultdEXT'
    //   137: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   140: dup2_x1
    //   141: putfield glMatrixMultdEXT : J
    //   144: lconst_0
    //   145: lcmp
    //   146: ifeq -> 153
    //   149: iconst_1
    //   150: goto -> 154
    //   153: iconst_0
    //   154: iand
    //   155: iload_1
    //   156: ifne -> 175
    //   159: aload_0
    //   160: ldc_w 'glMatrixLoadIdentityEXT'
    //   163: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   166: dup2_x1
    //   167: putfield glMatrixLoadIdentityEXT : J
    //   170: lconst_0
    //   171: lcmp
    //   172: ifeq -> 179
    //   175: iconst_1
    //   176: goto -> 180
    //   179: iconst_0
    //   180: iand
    //   181: iload_1
    //   182: ifne -> 201
    //   185: aload_0
    //   186: ldc_w 'glMatrixRotatefEXT'
    //   189: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   192: dup2_x1
    //   193: putfield glMatrixRotatefEXT : J
    //   196: lconst_0
    //   197: lcmp
    //   198: ifeq -> 205
    //   201: iconst_1
    //   202: goto -> 206
    //   205: iconst_0
    //   206: iand
    //   207: iload_1
    //   208: ifne -> 227
    //   211: aload_0
    //   212: ldc_w 'glMatrixRotatedEXT'
    //   215: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   218: dup2_x1
    //   219: putfield glMatrixRotatedEXT : J
    //   222: lconst_0
    //   223: lcmp
    //   224: ifeq -> 231
    //   227: iconst_1
    //   228: goto -> 232
    //   231: iconst_0
    //   232: iand
    //   233: iload_1
    //   234: ifne -> 253
    //   237: aload_0
    //   238: ldc_w 'glMatrixScalefEXT'
    //   241: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   244: dup2_x1
    //   245: putfield glMatrixScalefEXT : J
    //   248: lconst_0
    //   249: lcmp
    //   250: ifeq -> 257
    //   253: iconst_1
    //   254: goto -> 258
    //   257: iconst_0
    //   258: iand
    //   259: iload_1
    //   260: ifne -> 279
    //   263: aload_0
    //   264: ldc_w 'glMatrixScaledEXT'
    //   267: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   270: dup2_x1
    //   271: putfield glMatrixScaledEXT : J
    //   274: lconst_0
    //   275: lcmp
    //   276: ifeq -> 283
    //   279: iconst_1
    //   280: goto -> 284
    //   283: iconst_0
    //   284: iand
    //   285: iload_1
    //   286: ifne -> 305
    //   289: aload_0
    //   290: ldc_w 'glMatrixTranslatefEXT'
    //   293: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   296: dup2_x1
    //   297: putfield glMatrixTranslatefEXT : J
    //   300: lconst_0
    //   301: lcmp
    //   302: ifeq -> 309
    //   305: iconst_1
    //   306: goto -> 310
    //   309: iconst_0
    //   310: iand
    //   311: iload_1
    //   312: ifne -> 331
    //   315: aload_0
    //   316: ldc_w 'glMatrixTranslatedEXT'
    //   319: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   322: dup2_x1
    //   323: putfield glMatrixTranslatedEXT : J
    //   326: lconst_0
    //   327: lcmp
    //   328: ifeq -> 335
    //   331: iconst_1
    //   332: goto -> 336
    //   335: iconst_0
    //   336: iand
    //   337: iload_1
    //   338: ifne -> 357
    //   341: aload_0
    //   342: ldc_w 'glMatrixOrthoEXT'
    //   345: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   348: dup2_x1
    //   349: putfield glMatrixOrthoEXT : J
    //   352: lconst_0
    //   353: lcmp
    //   354: ifeq -> 361
    //   357: iconst_1
    //   358: goto -> 362
    //   361: iconst_0
    //   362: iand
    //   363: iload_1
    //   364: ifne -> 383
    //   367: aload_0
    //   368: ldc_w 'glMatrixFrustumEXT'
    //   371: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   374: dup2_x1
    //   375: putfield glMatrixFrustumEXT : J
    //   378: lconst_0
    //   379: lcmp
    //   380: ifeq -> 387
    //   383: iconst_1
    //   384: goto -> 388
    //   387: iconst_0
    //   388: iand
    //   389: iload_1
    //   390: ifne -> 409
    //   393: aload_0
    //   394: ldc_w 'glMatrixPushEXT'
    //   397: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   400: dup2_x1
    //   401: putfield glMatrixPushEXT : J
    //   404: lconst_0
    //   405: lcmp
    //   406: ifeq -> 413
    //   409: iconst_1
    //   410: goto -> 414
    //   413: iconst_0
    //   414: iand
    //   415: iload_1
    //   416: ifne -> 435
    //   419: aload_0
    //   420: ldc_w 'glMatrixPopEXT'
    //   423: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   426: dup2_x1
    //   427: putfield glMatrixPopEXT : J
    //   430: lconst_0
    //   431: lcmp
    //   432: ifeq -> 439
    //   435: iconst_1
    //   436: goto -> 440
    //   439: iconst_0
    //   440: iand
    //   441: aload_0
    //   442: ldc_w 'glTextureParameteriEXT'
    //   445: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   448: dup2_x1
    //   449: putfield glTextureParameteriEXT : J
    //   452: lconst_0
    //   453: lcmp
    //   454: ifeq -> 461
    //   457: iconst_1
    //   458: goto -> 462
    //   461: iconst_0
    //   462: iand
    //   463: aload_0
    //   464: ldc_w 'glTextureParameterivEXT'
    //   467: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   470: dup2_x1
    //   471: putfield glTextureParameterivEXT : J
    //   474: lconst_0
    //   475: lcmp
    //   476: ifeq -> 483
    //   479: iconst_1
    //   480: goto -> 484
    //   483: iconst_0
    //   484: iand
    //   485: aload_0
    //   486: ldc_w 'glTextureParameterfEXT'
    //   489: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   492: dup2_x1
    //   493: putfield glTextureParameterfEXT : J
    //   496: lconst_0
    //   497: lcmp
    //   498: ifeq -> 505
    //   501: iconst_1
    //   502: goto -> 506
    //   505: iconst_0
    //   506: iand
    //   507: aload_0
    //   508: ldc_w 'glTextureParameterfvEXT'
    //   511: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   514: dup2_x1
    //   515: putfield glTextureParameterfvEXT : J
    //   518: lconst_0
    //   519: lcmp
    //   520: ifeq -> 527
    //   523: iconst_1
    //   524: goto -> 528
    //   527: iconst_0
    //   528: iand
    //   529: aload_0
    //   530: ldc_w 'glTextureImage1DEXT'
    //   533: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   536: dup2_x1
    //   537: putfield glTextureImage1DEXT : J
    //   540: lconst_0
    //   541: lcmp
    //   542: ifeq -> 549
    //   545: iconst_1
    //   546: goto -> 550
    //   549: iconst_0
    //   550: iand
    //   551: aload_0
    //   552: ldc_w 'glTextureImage2DEXT'
    //   555: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   558: dup2_x1
    //   559: putfield glTextureImage2DEXT : J
    //   562: lconst_0
    //   563: lcmp
    //   564: ifeq -> 571
    //   567: iconst_1
    //   568: goto -> 572
    //   571: iconst_0
    //   572: iand
    //   573: aload_0
    //   574: ldc_w 'glTextureSubImage1DEXT'
    //   577: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   580: dup2_x1
    //   581: putfield glTextureSubImage1DEXT : J
    //   584: lconst_0
    //   585: lcmp
    //   586: ifeq -> 593
    //   589: iconst_1
    //   590: goto -> 594
    //   593: iconst_0
    //   594: iand
    //   595: aload_0
    //   596: ldc_w 'glTextureSubImage2DEXT'
    //   599: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   602: dup2_x1
    //   603: putfield glTextureSubImage2DEXT : J
    //   606: lconst_0
    //   607: lcmp
    //   608: ifeq -> 615
    //   611: iconst_1
    //   612: goto -> 616
    //   615: iconst_0
    //   616: iand
    //   617: aload_0
    //   618: ldc_w 'glCopyTextureImage1DEXT'
    //   621: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   624: dup2_x1
    //   625: putfield glCopyTextureImage1DEXT : J
    //   628: lconst_0
    //   629: lcmp
    //   630: ifeq -> 637
    //   633: iconst_1
    //   634: goto -> 638
    //   637: iconst_0
    //   638: iand
    //   639: aload_0
    //   640: ldc_w 'glCopyTextureImage2DEXT'
    //   643: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   646: dup2_x1
    //   647: putfield glCopyTextureImage2DEXT : J
    //   650: lconst_0
    //   651: lcmp
    //   652: ifeq -> 659
    //   655: iconst_1
    //   656: goto -> 660
    //   659: iconst_0
    //   660: iand
    //   661: aload_0
    //   662: ldc_w 'glCopyTextureSubImage1DEXT'
    //   665: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   668: dup2_x1
    //   669: putfield glCopyTextureSubImage1DEXT : J
    //   672: lconst_0
    //   673: lcmp
    //   674: ifeq -> 681
    //   677: iconst_1
    //   678: goto -> 682
    //   681: iconst_0
    //   682: iand
    //   683: aload_0
    //   684: ldc_w 'glCopyTextureSubImage2DEXT'
    //   687: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   690: dup2_x1
    //   691: putfield glCopyTextureSubImage2DEXT : J
    //   694: lconst_0
    //   695: lcmp
    //   696: ifeq -> 703
    //   699: iconst_1
    //   700: goto -> 704
    //   703: iconst_0
    //   704: iand
    //   705: aload_0
    //   706: ldc_w 'glGetTextureImageEXT'
    //   709: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   712: dup2_x1
    //   713: putfield glGetTextureImageEXT : J
    //   716: lconst_0
    //   717: lcmp
    //   718: ifeq -> 725
    //   721: iconst_1
    //   722: goto -> 726
    //   725: iconst_0
    //   726: iand
    //   727: aload_0
    //   728: ldc_w 'glGetTextureParameterfvEXT'
    //   731: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   734: dup2_x1
    //   735: putfield glGetTextureParameterfvEXT : J
    //   738: lconst_0
    //   739: lcmp
    //   740: ifeq -> 747
    //   743: iconst_1
    //   744: goto -> 748
    //   747: iconst_0
    //   748: iand
    //   749: aload_0
    //   750: ldc_w 'glGetTextureParameterivEXT'
    //   753: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   756: dup2_x1
    //   757: putfield glGetTextureParameterivEXT : J
    //   760: lconst_0
    //   761: lcmp
    //   762: ifeq -> 769
    //   765: iconst_1
    //   766: goto -> 770
    //   769: iconst_0
    //   770: iand
    //   771: aload_0
    //   772: ldc_w 'glGetTextureLevelParameterfvEXT'
    //   775: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   778: dup2_x1
    //   779: putfield glGetTextureLevelParameterfvEXT : J
    //   782: lconst_0
    //   783: lcmp
    //   784: ifeq -> 791
    //   787: iconst_1
    //   788: goto -> 792
    //   791: iconst_0
    //   792: iand
    //   793: aload_0
    //   794: ldc_w 'glGetTextureLevelParameterivEXT'
    //   797: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   800: dup2_x1
    //   801: putfield glGetTextureLevelParameterivEXT : J
    //   804: lconst_0
    //   805: lcmp
    //   806: ifeq -> 813
    //   809: iconst_1
    //   810: goto -> 814
    //   813: iconst_0
    //   814: iand
    //   815: aload_2
    //   816: ldc_w 'OpenGL12'
    //   819: invokeinterface contains : (Ljava/lang/Object;)Z
    //   824: ifeq -> 843
    //   827: aload_0
    //   828: ldc_w 'glTextureImage3DEXT'
    //   831: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   834: dup2_x1
    //   835: putfield glTextureImage3DEXT : J
    //   838: lconst_0
    //   839: lcmp
    //   840: ifeq -> 847
    //   843: iconst_1
    //   844: goto -> 848
    //   847: iconst_0
    //   848: iand
    //   849: aload_2
    //   850: ldc_w 'OpenGL12'
    //   853: invokeinterface contains : (Ljava/lang/Object;)Z
    //   858: ifeq -> 877
    //   861: aload_0
    //   862: ldc_w 'glTextureSubImage3DEXT'
    //   865: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   868: dup2_x1
    //   869: putfield glTextureSubImage3DEXT : J
    //   872: lconst_0
    //   873: lcmp
    //   874: ifeq -> 881
    //   877: iconst_1
    //   878: goto -> 882
    //   881: iconst_0
    //   882: iand
    //   883: aload_2
    //   884: ldc_w 'OpenGL12'
    //   887: invokeinterface contains : (Ljava/lang/Object;)Z
    //   892: ifeq -> 911
    //   895: aload_0
    //   896: ldc_w 'glCopyTextureSubImage3DEXT'
    //   899: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   902: dup2_x1
    //   903: putfield glCopyTextureSubImage3DEXT : J
    //   906: lconst_0
    //   907: lcmp
    //   908: ifeq -> 915
    //   911: iconst_1
    //   912: goto -> 916
    //   915: iconst_0
    //   916: iand
    //   917: aload_2
    //   918: ldc_w 'OpenGL13'
    //   921: invokeinterface contains : (Ljava/lang/Object;)Z
    //   926: ifeq -> 945
    //   929: aload_0
    //   930: ldc_w 'glBindMultiTextureEXT'
    //   933: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   936: dup2_x1
    //   937: putfield glBindMultiTextureEXT : J
    //   940: lconst_0
    //   941: lcmp
    //   942: ifeq -> 949
    //   945: iconst_1
    //   946: goto -> 950
    //   949: iconst_0
    //   950: iand
    //   951: iload_1
    //   952: ifne -> 983
    //   955: aload_2
    //   956: ldc_w 'OpenGL13'
    //   959: invokeinterface contains : (Ljava/lang/Object;)Z
    //   964: ifeq -> 983
    //   967: aload_0
    //   968: ldc_w 'glMultiTexCoordPointerEXT'
    //   971: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   974: dup2_x1
    //   975: putfield glMultiTexCoordPointerEXT : J
    //   978: lconst_0
    //   979: lcmp
    //   980: ifeq -> 987
    //   983: iconst_1
    //   984: goto -> 988
    //   987: iconst_0
    //   988: iand
    //   989: iload_1
    //   990: ifne -> 1021
    //   993: aload_2
    //   994: ldc_w 'OpenGL13'
    //   997: invokeinterface contains : (Ljava/lang/Object;)Z
    //   1002: ifeq -> 1021
    //   1005: aload_0
    //   1006: ldc_w 'glMultiTexEnvfEXT'
    //   1009: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   1012: dup2_x1
    //   1013: putfield glMultiTexEnvfEXT : J
    //   1016: lconst_0
    //   1017: lcmp
    //   1018: ifeq -> 1025
    //   1021: iconst_1
    //   1022: goto -> 1026
    //   1025: iconst_0
    //   1026: iand
    //   1027: iload_1
    //   1028: ifne -> 1059
    //   1031: aload_2
    //   1032: ldc_w 'OpenGL13'
    //   1035: invokeinterface contains : (Ljava/lang/Object;)Z
    //   1040: ifeq -> 1059
    //   1043: aload_0
    //   1044: ldc_w 'glMultiTexEnvfvEXT'
    //   1047: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   1050: dup2_x1
    //   1051: putfield glMultiTexEnvfvEXT : J
    //   1054: lconst_0
    //   1055: lcmp
    //   1056: ifeq -> 1063
    //   1059: iconst_1
    //   1060: goto -> 1064
    //   1063: iconst_0
    //   1064: iand
    //   1065: iload_1
    //   1066: ifne -> 1097
    //   1069: aload_2
    //   1070: ldc_w 'OpenGL13'
    //   1073: invokeinterface contains : (Ljava/lang/Object;)Z
    //   1078: ifeq -> 1097
    //   1081: aload_0
    //   1082: ldc_w 'glMultiTexEnviEXT'
    //   1085: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   1088: dup2_x1
    //   1089: putfield glMultiTexEnviEXT : J
    //   1092: lconst_0
    //   1093: lcmp
    //   1094: ifeq -> 1101
    //   1097: iconst_1
    //   1098: goto -> 1102
    //   1101: iconst_0
    //   1102: iand
    //   1103: iload_1
    //   1104: ifne -> 1135
    //   1107: aload_2
    //   1108: ldc_w 'OpenGL13'
    //   1111: invokeinterface contains : (Ljava/lang/Object;)Z
    //   1116: ifeq -> 1135
    //   1119: aload_0
    //   1120: ldc_w 'glMultiTexEnvivEXT'
    //   1123: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   1126: dup2_x1
    //   1127: putfield glMultiTexEnvivEXT : J
    //   1130: lconst_0
    //   1131: lcmp
    //   1132: ifeq -> 1139
    //   1135: iconst_1
    //   1136: goto -> 1140
    //   1139: iconst_0
    //   1140: iand
    //   1141: iload_1
    //   1142: ifne -> 1173
    //   1145: aload_2
    //   1146: ldc_w 'OpenGL13'
    //   1149: invokeinterface contains : (Ljava/lang/Object;)Z
    //   1154: ifeq -> 1173
    //   1157: aload_0
    //   1158: ldc_w 'glMultiTexGendEXT'
    //   1161: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   1164: dup2_x1
    //   1165: putfield glMultiTexGendEXT : J
    //   1168: lconst_0
    //   1169: lcmp
    //   1170: ifeq -> 1177
    //   1173: iconst_1
    //   1174: goto -> 1178
    //   1177: iconst_0
    //   1178: iand
    //   1179: iload_1
    //   1180: ifne -> 1211
    //   1183: aload_2
    //   1184: ldc_w 'OpenGL13'
    //   1187: invokeinterface contains : (Ljava/lang/Object;)Z
    //   1192: ifeq -> 1211
    //   1195: aload_0
    //   1196: ldc_w 'glMultiTexGendvEXT'
    //   1199: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   1202: dup2_x1
    //   1203: putfield glMultiTexGendvEXT : J
    //   1206: lconst_0
    //   1207: lcmp
    //   1208: ifeq -> 1215
    //   1211: iconst_1
    //   1212: goto -> 1216
    //   1215: iconst_0
    //   1216: iand
    //   1217: iload_1
    //   1218: ifne -> 1249
    //   1221: aload_2
    //   1222: ldc_w 'OpenGL13'
    //   1225: invokeinterface contains : (Ljava/lang/Object;)Z
    //   1230: ifeq -> 1249
    //   1233: aload_0
    //   1234: ldc_w 'glMultiTexGenfEXT'
    //   1237: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   1240: dup2_x1
    //   1241: putfield glMultiTexGenfEXT : J
    //   1244: lconst_0
    //   1245: lcmp
    //   1246: ifeq -> 1253
    //   1249: iconst_1
    //   1250: goto -> 1254
    //   1253: iconst_0
    //   1254: iand
    //   1255: iload_1
    //   1256: ifne -> 1287
    //   1259: aload_2
    //   1260: ldc_w 'OpenGL13'
    //   1263: invokeinterface contains : (Ljava/lang/Object;)Z
    //   1268: ifeq -> 1287
    //   1271: aload_0
    //   1272: ldc_w 'glMultiTexGenfvEXT'
    //   1275: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   1278: dup2_x1
    //   1279: putfield glMultiTexGenfvEXT : J
    //   1282: lconst_0
    //   1283: lcmp
    //   1284: ifeq -> 1291
    //   1287: iconst_1
    //   1288: goto -> 1292
    //   1291: iconst_0
    //   1292: iand
    //   1293: iload_1
    //   1294: ifne -> 1325
    //   1297: aload_2
    //   1298: ldc_w 'OpenGL13'
    //   1301: invokeinterface contains : (Ljava/lang/Object;)Z
    //   1306: ifeq -> 1325
    //   1309: aload_0
    //   1310: ldc_w 'glMultiTexGeniEXT'
    //   1313: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   1316: dup2_x1
    //   1317: putfield glMultiTexGeniEXT : J
    //   1320: lconst_0
    //   1321: lcmp
    //   1322: ifeq -> 1329
    //   1325: iconst_1
    //   1326: goto -> 1330
    //   1329: iconst_0
    //   1330: iand
    //   1331: iload_1
    //   1332: ifne -> 1363
    //   1335: aload_2
    //   1336: ldc_w 'OpenGL13'
    //   1339: invokeinterface contains : (Ljava/lang/Object;)Z
    //   1344: ifeq -> 1363
    //   1347: aload_0
    //   1348: ldc_w 'glMultiTexGenivEXT'
    //   1351: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   1354: dup2_x1
    //   1355: putfield glMultiTexGenivEXT : J
    //   1358: lconst_0
    //   1359: lcmp
    //   1360: ifeq -> 1367
    //   1363: iconst_1
    //   1364: goto -> 1368
    //   1367: iconst_0
    //   1368: iand
    //   1369: iload_1
    //   1370: ifne -> 1401
    //   1373: aload_2
    //   1374: ldc_w 'OpenGL13'
    //   1377: invokeinterface contains : (Ljava/lang/Object;)Z
    //   1382: ifeq -> 1401
    //   1385: aload_0
    //   1386: ldc_w 'glGetMultiTexEnvfvEXT'
    //   1389: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   1392: dup2_x1
    //   1393: putfield glGetMultiTexEnvfvEXT : J
    //   1396: lconst_0
    //   1397: lcmp
    //   1398: ifeq -> 1405
    //   1401: iconst_1
    //   1402: goto -> 1406
    //   1405: iconst_0
    //   1406: iand
    //   1407: iload_1
    //   1408: ifne -> 1439
    //   1411: aload_2
    //   1412: ldc_w 'OpenGL13'
    //   1415: invokeinterface contains : (Ljava/lang/Object;)Z
    //   1420: ifeq -> 1439
    //   1423: aload_0
    //   1424: ldc_w 'glGetMultiTexEnvivEXT'
    //   1427: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   1430: dup2_x1
    //   1431: putfield glGetMultiTexEnvivEXT : J
    //   1434: lconst_0
    //   1435: lcmp
    //   1436: ifeq -> 1443
    //   1439: iconst_1
    //   1440: goto -> 1444
    //   1443: iconst_0
    //   1444: iand
    //   1445: iload_1
    //   1446: ifne -> 1477
    //   1449: aload_2
    //   1450: ldc_w 'OpenGL13'
    //   1453: invokeinterface contains : (Ljava/lang/Object;)Z
    //   1458: ifeq -> 1477
    //   1461: aload_0
    //   1462: ldc_w 'glGetMultiTexGendvEXT'
    //   1465: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   1468: dup2_x1
    //   1469: putfield glGetMultiTexGendvEXT : J
    //   1472: lconst_0
    //   1473: lcmp
    //   1474: ifeq -> 1481
    //   1477: iconst_1
    //   1478: goto -> 1482
    //   1481: iconst_0
    //   1482: iand
    //   1483: iload_1
    //   1484: ifne -> 1515
    //   1487: aload_2
    //   1488: ldc_w 'OpenGL13'
    //   1491: invokeinterface contains : (Ljava/lang/Object;)Z
    //   1496: ifeq -> 1515
    //   1499: aload_0
    //   1500: ldc_w 'glGetMultiTexGenfvEXT'
    //   1503: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   1506: dup2_x1
    //   1507: putfield glGetMultiTexGenfvEXT : J
    //   1510: lconst_0
    //   1511: lcmp
    //   1512: ifeq -> 1519
    //   1515: iconst_1
    //   1516: goto -> 1520
    //   1519: iconst_0
    //   1520: iand
    //   1521: iload_1
    //   1522: ifne -> 1553
    //   1525: aload_2
    //   1526: ldc_w 'OpenGL13'
    //   1529: invokeinterface contains : (Ljava/lang/Object;)Z
    //   1534: ifeq -> 1553
    //   1537: aload_0
    //   1538: ldc_w 'glGetMultiTexGenivEXT'
    //   1541: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   1544: dup2_x1
    //   1545: putfield glGetMultiTexGenivEXT : J
    //   1548: lconst_0
    //   1549: lcmp
    //   1550: ifeq -> 1557
    //   1553: iconst_1
    //   1554: goto -> 1558
    //   1557: iconst_0
    //   1558: iand
    //   1559: aload_2
    //   1560: ldc_w 'OpenGL13'
    //   1563: invokeinterface contains : (Ljava/lang/Object;)Z
    //   1568: ifeq -> 1587
    //   1571: aload_0
    //   1572: ldc_w 'glMultiTexParameteriEXT'
    //   1575: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   1578: dup2_x1
    //   1579: putfield glMultiTexParameteriEXT : J
    //   1582: lconst_0
    //   1583: lcmp
    //   1584: ifeq -> 1591
    //   1587: iconst_1
    //   1588: goto -> 1592
    //   1591: iconst_0
    //   1592: iand
    //   1593: aload_2
    //   1594: ldc_w 'OpenGL13'
    //   1597: invokeinterface contains : (Ljava/lang/Object;)Z
    //   1602: ifeq -> 1621
    //   1605: aload_0
    //   1606: ldc_w 'glMultiTexParameterivEXT'
    //   1609: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   1612: dup2_x1
    //   1613: putfield glMultiTexParameterivEXT : J
    //   1616: lconst_0
    //   1617: lcmp
    //   1618: ifeq -> 1625
    //   1621: iconst_1
    //   1622: goto -> 1626
    //   1625: iconst_0
    //   1626: iand
    //   1627: aload_2
    //   1628: ldc_w 'OpenGL13'
    //   1631: invokeinterface contains : (Ljava/lang/Object;)Z
    //   1636: ifeq -> 1655
    //   1639: aload_0
    //   1640: ldc_w 'glMultiTexParameterfEXT'
    //   1643: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   1646: dup2_x1
    //   1647: putfield glMultiTexParameterfEXT : J
    //   1650: lconst_0
    //   1651: lcmp
    //   1652: ifeq -> 1659
    //   1655: iconst_1
    //   1656: goto -> 1660
    //   1659: iconst_0
    //   1660: iand
    //   1661: aload_2
    //   1662: ldc_w 'OpenGL13'
    //   1665: invokeinterface contains : (Ljava/lang/Object;)Z
    //   1670: ifeq -> 1689
    //   1673: aload_0
    //   1674: ldc_w 'glMultiTexParameterfvEXT'
    //   1677: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   1680: dup2_x1
    //   1681: putfield glMultiTexParameterfvEXT : J
    //   1684: lconst_0
    //   1685: lcmp
    //   1686: ifeq -> 1693
    //   1689: iconst_1
    //   1690: goto -> 1694
    //   1693: iconst_0
    //   1694: iand
    //   1695: aload_2
    //   1696: ldc_w 'OpenGL13'
    //   1699: invokeinterface contains : (Ljava/lang/Object;)Z
    //   1704: ifeq -> 1723
    //   1707: aload_0
    //   1708: ldc_w 'glMultiTexImage1DEXT'
    //   1711: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   1714: dup2_x1
    //   1715: putfield glMultiTexImage1DEXT : J
    //   1718: lconst_0
    //   1719: lcmp
    //   1720: ifeq -> 1727
    //   1723: iconst_1
    //   1724: goto -> 1728
    //   1727: iconst_0
    //   1728: iand
    //   1729: aload_2
    //   1730: ldc_w 'OpenGL13'
    //   1733: invokeinterface contains : (Ljava/lang/Object;)Z
    //   1738: ifeq -> 1757
    //   1741: aload_0
    //   1742: ldc_w 'glMultiTexImage2DEXT'
    //   1745: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   1748: dup2_x1
    //   1749: putfield glMultiTexImage2DEXT : J
    //   1752: lconst_0
    //   1753: lcmp
    //   1754: ifeq -> 1761
    //   1757: iconst_1
    //   1758: goto -> 1762
    //   1761: iconst_0
    //   1762: iand
    //   1763: aload_2
    //   1764: ldc_w 'OpenGL13'
    //   1767: invokeinterface contains : (Ljava/lang/Object;)Z
    //   1772: ifeq -> 1791
    //   1775: aload_0
    //   1776: ldc_w 'glMultiTexSubImage1DEXT'
    //   1779: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   1782: dup2_x1
    //   1783: putfield glMultiTexSubImage1DEXT : J
    //   1786: lconst_0
    //   1787: lcmp
    //   1788: ifeq -> 1795
    //   1791: iconst_1
    //   1792: goto -> 1796
    //   1795: iconst_0
    //   1796: iand
    //   1797: aload_2
    //   1798: ldc_w 'OpenGL13'
    //   1801: invokeinterface contains : (Ljava/lang/Object;)Z
    //   1806: ifeq -> 1825
    //   1809: aload_0
    //   1810: ldc_w 'glMultiTexSubImage2DEXT'
    //   1813: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   1816: dup2_x1
    //   1817: putfield glMultiTexSubImage2DEXT : J
    //   1820: lconst_0
    //   1821: lcmp
    //   1822: ifeq -> 1829
    //   1825: iconst_1
    //   1826: goto -> 1830
    //   1829: iconst_0
    //   1830: iand
    //   1831: aload_2
    //   1832: ldc_w 'OpenGL13'
    //   1835: invokeinterface contains : (Ljava/lang/Object;)Z
    //   1840: ifeq -> 1859
    //   1843: aload_0
    //   1844: ldc_w 'glCopyMultiTexImage1DEXT'
    //   1847: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   1850: dup2_x1
    //   1851: putfield glCopyMultiTexImage1DEXT : J
    //   1854: lconst_0
    //   1855: lcmp
    //   1856: ifeq -> 1863
    //   1859: iconst_1
    //   1860: goto -> 1864
    //   1863: iconst_0
    //   1864: iand
    //   1865: aload_2
    //   1866: ldc_w 'OpenGL13'
    //   1869: invokeinterface contains : (Ljava/lang/Object;)Z
    //   1874: ifeq -> 1893
    //   1877: aload_0
    //   1878: ldc_w 'glCopyMultiTexImage2DEXT'
    //   1881: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   1884: dup2_x1
    //   1885: putfield glCopyMultiTexImage2DEXT : J
    //   1888: lconst_0
    //   1889: lcmp
    //   1890: ifeq -> 1897
    //   1893: iconst_1
    //   1894: goto -> 1898
    //   1897: iconst_0
    //   1898: iand
    //   1899: aload_2
    //   1900: ldc_w 'OpenGL13'
    //   1903: invokeinterface contains : (Ljava/lang/Object;)Z
    //   1908: ifeq -> 1927
    //   1911: aload_0
    //   1912: ldc_w 'glCopyMultiTexSubImage1DEXT'
    //   1915: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   1918: dup2_x1
    //   1919: putfield glCopyMultiTexSubImage1DEXT : J
    //   1922: lconst_0
    //   1923: lcmp
    //   1924: ifeq -> 1931
    //   1927: iconst_1
    //   1928: goto -> 1932
    //   1931: iconst_0
    //   1932: iand
    //   1933: aload_2
    //   1934: ldc_w 'OpenGL13'
    //   1937: invokeinterface contains : (Ljava/lang/Object;)Z
    //   1942: ifeq -> 1961
    //   1945: aload_0
    //   1946: ldc_w 'glCopyMultiTexSubImage2DEXT'
    //   1949: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   1952: dup2_x1
    //   1953: putfield glCopyMultiTexSubImage2DEXT : J
    //   1956: lconst_0
    //   1957: lcmp
    //   1958: ifeq -> 1965
    //   1961: iconst_1
    //   1962: goto -> 1966
    //   1965: iconst_0
    //   1966: iand
    //   1967: aload_2
    //   1968: ldc_w 'OpenGL13'
    //   1971: invokeinterface contains : (Ljava/lang/Object;)Z
    //   1976: ifeq -> 1995
    //   1979: aload_0
    //   1980: ldc_w 'glGetMultiTexImageEXT'
    //   1983: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   1986: dup2_x1
    //   1987: putfield glGetMultiTexImageEXT : J
    //   1990: lconst_0
    //   1991: lcmp
    //   1992: ifeq -> 1999
    //   1995: iconst_1
    //   1996: goto -> 2000
    //   1999: iconst_0
    //   2000: iand
    //   2001: aload_2
    //   2002: ldc_w 'OpenGL13'
    //   2005: invokeinterface contains : (Ljava/lang/Object;)Z
    //   2010: ifeq -> 2029
    //   2013: aload_0
    //   2014: ldc_w 'glGetMultiTexParameterfvEXT'
    //   2017: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   2020: dup2_x1
    //   2021: putfield glGetMultiTexParameterfvEXT : J
    //   2024: lconst_0
    //   2025: lcmp
    //   2026: ifeq -> 2033
    //   2029: iconst_1
    //   2030: goto -> 2034
    //   2033: iconst_0
    //   2034: iand
    //   2035: aload_2
    //   2036: ldc_w 'OpenGL13'
    //   2039: invokeinterface contains : (Ljava/lang/Object;)Z
    //   2044: ifeq -> 2063
    //   2047: aload_0
    //   2048: ldc_w 'glGetMultiTexParameterivEXT'
    //   2051: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   2054: dup2_x1
    //   2055: putfield glGetMultiTexParameterivEXT : J
    //   2058: lconst_0
    //   2059: lcmp
    //   2060: ifeq -> 2067
    //   2063: iconst_1
    //   2064: goto -> 2068
    //   2067: iconst_0
    //   2068: iand
    //   2069: aload_2
    //   2070: ldc_w 'OpenGL13'
    //   2073: invokeinterface contains : (Ljava/lang/Object;)Z
    //   2078: ifeq -> 2097
    //   2081: aload_0
    //   2082: ldc_w 'glGetMultiTexLevelParameterfvEXT'
    //   2085: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   2088: dup2_x1
    //   2089: putfield glGetMultiTexLevelParameterfvEXT : J
    //   2092: lconst_0
    //   2093: lcmp
    //   2094: ifeq -> 2101
    //   2097: iconst_1
    //   2098: goto -> 2102
    //   2101: iconst_0
    //   2102: iand
    //   2103: aload_2
    //   2104: ldc_w 'OpenGL13'
    //   2107: invokeinterface contains : (Ljava/lang/Object;)Z
    //   2112: ifeq -> 2131
    //   2115: aload_0
    //   2116: ldc_w 'glGetMultiTexLevelParameterivEXT'
    //   2119: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   2122: dup2_x1
    //   2123: putfield glGetMultiTexLevelParameterivEXT : J
    //   2126: lconst_0
    //   2127: lcmp
    //   2128: ifeq -> 2135
    //   2131: iconst_1
    //   2132: goto -> 2136
    //   2135: iconst_0
    //   2136: iand
    //   2137: aload_2
    //   2138: ldc_w 'OpenGL13'
    //   2141: invokeinterface contains : (Ljava/lang/Object;)Z
    //   2146: ifeq -> 2165
    //   2149: aload_0
    //   2150: ldc_w 'glMultiTexImage3DEXT'
    //   2153: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   2156: dup2_x1
    //   2157: putfield glMultiTexImage3DEXT : J
    //   2160: lconst_0
    //   2161: lcmp
    //   2162: ifeq -> 2169
    //   2165: iconst_1
    //   2166: goto -> 2170
    //   2169: iconst_0
    //   2170: iand
    //   2171: aload_2
    //   2172: ldc_w 'OpenGL13'
    //   2175: invokeinterface contains : (Ljava/lang/Object;)Z
    //   2180: ifeq -> 2199
    //   2183: aload_0
    //   2184: ldc_w 'glMultiTexSubImage3DEXT'
    //   2187: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   2190: dup2_x1
    //   2191: putfield glMultiTexSubImage3DEXT : J
    //   2194: lconst_0
    //   2195: lcmp
    //   2196: ifeq -> 2203
    //   2199: iconst_1
    //   2200: goto -> 2204
    //   2203: iconst_0
    //   2204: iand
    //   2205: aload_2
    //   2206: ldc_w 'OpenGL13'
    //   2209: invokeinterface contains : (Ljava/lang/Object;)Z
    //   2214: ifeq -> 2233
    //   2217: aload_0
    //   2218: ldc_w 'glCopyMultiTexSubImage3DEXT'
    //   2221: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   2224: dup2_x1
    //   2225: putfield glCopyMultiTexSubImage3DEXT : J
    //   2228: lconst_0
    //   2229: lcmp
    //   2230: ifeq -> 2237
    //   2233: iconst_1
    //   2234: goto -> 2238
    //   2237: iconst_0
    //   2238: iand
    //   2239: iload_1
    //   2240: ifne -> 2271
    //   2243: aload_2
    //   2244: ldc_w 'OpenGL13'
    //   2247: invokeinterface contains : (Ljava/lang/Object;)Z
    //   2252: ifeq -> 2271
    //   2255: aload_0
    //   2256: ldc_w 'glEnableClientStateIndexedEXT'
    //   2259: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   2262: dup2_x1
    //   2263: putfield glEnableClientStateIndexedEXT : J
    //   2266: lconst_0
    //   2267: lcmp
    //   2268: ifeq -> 2275
    //   2271: iconst_1
    //   2272: goto -> 2276
    //   2275: iconst_0
    //   2276: iand
    //   2277: iload_1
    //   2278: ifne -> 2309
    //   2281: aload_2
    //   2282: ldc_w 'OpenGL13'
    //   2285: invokeinterface contains : (Ljava/lang/Object;)Z
    //   2290: ifeq -> 2309
    //   2293: aload_0
    //   2294: ldc_w 'glDisableClientStateIndexedEXT'
    //   2297: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   2300: dup2_x1
    //   2301: putfield glDisableClientStateIndexedEXT : J
    //   2304: lconst_0
    //   2305: lcmp
    //   2306: ifeq -> 2313
    //   2309: iconst_1
    //   2310: goto -> 2314
    //   2313: iconst_0
    //   2314: iand
    //   2315: aload_2
    //   2316: ldc_w 'OpenGL30'
    //   2319: invokeinterface contains : (Ljava/lang/Object;)Z
    //   2324: ifeq -> 2343
    //   2327: aload_0
    //   2328: ldc_w 'glEnableClientStateiEXT'
    //   2331: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   2334: dup2_x1
    //   2335: putfield glEnableClientStateiEXT : J
    //   2338: lconst_0
    //   2339: lcmp
    //   2340: ifne -> 2343
    //   2343: iconst_1
    //   2344: iand
    //   2345: aload_2
    //   2346: ldc_w 'OpenGL30'
    //   2349: invokeinterface contains : (Ljava/lang/Object;)Z
    //   2354: ifeq -> 2373
    //   2357: aload_0
    //   2358: ldc_w 'glDisableClientStateiEXT'
    //   2361: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   2364: dup2_x1
    //   2365: putfield glDisableClientStateiEXT : J
    //   2368: lconst_0
    //   2369: lcmp
    //   2370: ifne -> 2373
    //   2373: iconst_1
    //   2374: iand
    //   2375: aload_2
    //   2376: ldc_w 'OpenGL13'
    //   2379: invokeinterface contains : (Ljava/lang/Object;)Z
    //   2384: ifeq -> 2403
    //   2387: aload_0
    //   2388: ldc_w 'glGetFloatIndexedvEXT'
    //   2391: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   2394: dup2_x1
    //   2395: putfield glGetFloatIndexedvEXT : J
    //   2398: lconst_0
    //   2399: lcmp
    //   2400: ifeq -> 2407
    //   2403: iconst_1
    //   2404: goto -> 2408
    //   2407: iconst_0
    //   2408: iand
    //   2409: aload_2
    //   2410: ldc_w 'OpenGL13'
    //   2413: invokeinterface contains : (Ljava/lang/Object;)Z
    //   2418: ifeq -> 2437
    //   2421: aload_0
    //   2422: ldc_w 'glGetDoubleIndexedvEXT'
    //   2425: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   2428: dup2_x1
    //   2429: putfield glGetDoubleIndexedvEXT : J
    //   2432: lconst_0
    //   2433: lcmp
    //   2434: ifeq -> 2441
    //   2437: iconst_1
    //   2438: goto -> 2442
    //   2441: iconst_0
    //   2442: iand
    //   2443: aload_2
    //   2444: ldc_w 'OpenGL13'
    //   2447: invokeinterface contains : (Ljava/lang/Object;)Z
    //   2452: ifeq -> 2471
    //   2455: aload_0
    //   2456: ldc_w 'glGetPointerIndexedvEXT'
    //   2459: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   2462: dup2_x1
    //   2463: putfield glGetPointerIndexedvEXT : J
    //   2466: lconst_0
    //   2467: lcmp
    //   2468: ifeq -> 2475
    //   2471: iconst_1
    //   2472: goto -> 2476
    //   2475: iconst_0
    //   2476: iand
    //   2477: aload_2
    //   2478: ldc_w 'OpenGL30'
    //   2481: invokeinterface contains : (Ljava/lang/Object;)Z
    //   2486: ifeq -> 2505
    //   2489: aload_0
    //   2490: ldc_w 'glGetFloati_vEXT'
    //   2493: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   2496: dup2_x1
    //   2497: putfield glGetFloati_vEXT : J
    //   2500: lconst_0
    //   2501: lcmp
    //   2502: ifne -> 2505
    //   2505: iconst_1
    //   2506: iand
    //   2507: aload_2
    //   2508: ldc_w 'OpenGL30'
    //   2511: invokeinterface contains : (Ljava/lang/Object;)Z
    //   2516: ifeq -> 2535
    //   2519: aload_0
    //   2520: ldc_w 'glGetDoublei_vEXT'
    //   2523: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   2526: dup2_x1
    //   2527: putfield glGetDoublei_vEXT : J
    //   2530: lconst_0
    //   2531: lcmp
    //   2532: ifne -> 2535
    //   2535: iconst_1
    //   2536: iand
    //   2537: aload_2
    //   2538: ldc_w 'OpenGL30'
    //   2541: invokeinterface contains : (Ljava/lang/Object;)Z
    //   2546: ifeq -> 2565
    //   2549: aload_0
    //   2550: ldc_w 'glGetPointeri_vEXT'
    //   2553: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   2556: dup2_x1
    //   2557: putfield glGetPointeri_vEXT : J
    //   2560: lconst_0
    //   2561: lcmp
    //   2562: ifne -> 2565
    //   2565: iconst_1
    //   2566: iand
    //   2567: aload_2
    //   2568: ldc_w 'OpenGL13'
    //   2571: invokeinterface contains : (Ljava/lang/Object;)Z
    //   2576: ifeq -> 2595
    //   2579: aload_0
    //   2580: ldc_w 'glEnableIndexedEXT'
    //   2583: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   2586: dup2_x1
    //   2587: putfield glEnableIndexedEXT : J
    //   2590: lconst_0
    //   2591: lcmp
    //   2592: ifeq -> 2599
    //   2595: iconst_1
    //   2596: goto -> 2600
    //   2599: iconst_0
    //   2600: iand
    //   2601: aload_2
    //   2602: ldc_w 'OpenGL13'
    //   2605: invokeinterface contains : (Ljava/lang/Object;)Z
    //   2610: ifeq -> 2629
    //   2613: aload_0
    //   2614: ldc_w 'glDisableIndexedEXT'
    //   2617: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   2620: dup2_x1
    //   2621: putfield glDisableIndexedEXT : J
    //   2624: lconst_0
    //   2625: lcmp
    //   2626: ifeq -> 2633
    //   2629: iconst_1
    //   2630: goto -> 2634
    //   2633: iconst_0
    //   2634: iand
    //   2635: aload_2
    //   2636: ldc_w 'OpenGL13'
    //   2639: invokeinterface contains : (Ljava/lang/Object;)Z
    //   2644: ifeq -> 2663
    //   2647: aload_0
    //   2648: ldc_w 'glIsEnabledIndexedEXT'
    //   2651: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   2654: dup2_x1
    //   2655: putfield glIsEnabledIndexedEXT : J
    //   2658: lconst_0
    //   2659: lcmp
    //   2660: ifeq -> 2667
    //   2663: iconst_1
    //   2664: goto -> 2668
    //   2667: iconst_0
    //   2668: iand
    //   2669: aload_2
    //   2670: ldc_w 'OpenGL13'
    //   2673: invokeinterface contains : (Ljava/lang/Object;)Z
    //   2678: ifeq -> 2697
    //   2681: aload_0
    //   2682: ldc_w 'glGetIntegerIndexedvEXT'
    //   2685: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   2688: dup2_x1
    //   2689: putfield glGetIntegerIndexedvEXT : J
    //   2692: lconst_0
    //   2693: lcmp
    //   2694: ifeq -> 2701
    //   2697: iconst_1
    //   2698: goto -> 2702
    //   2701: iconst_0
    //   2702: iand
    //   2703: aload_2
    //   2704: ldc_w 'OpenGL13'
    //   2707: invokeinterface contains : (Ljava/lang/Object;)Z
    //   2712: ifeq -> 2731
    //   2715: aload_0
    //   2716: ldc_w 'glGetBooleanIndexedvEXT'
    //   2719: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   2722: dup2_x1
    //   2723: putfield glGetBooleanIndexedvEXT : J
    //   2726: lconst_0
    //   2727: lcmp
    //   2728: ifeq -> 2735
    //   2731: iconst_1
    //   2732: goto -> 2736
    //   2735: iconst_0
    //   2736: iand
    //   2737: aload_2
    //   2738: ldc_w 'GL_ARB_vertex_program'
    //   2741: invokeinterface contains : (Ljava/lang/Object;)Z
    //   2746: ifeq -> 2765
    //   2749: aload_0
    //   2750: ldc_w 'glNamedProgramStringEXT'
    //   2753: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   2756: dup2_x1
    //   2757: putfield glNamedProgramStringEXT : J
    //   2760: lconst_0
    //   2761: lcmp
    //   2762: ifeq -> 2769
    //   2765: iconst_1
    //   2766: goto -> 2770
    //   2769: iconst_0
    //   2770: iand
    //   2771: aload_2
    //   2772: ldc_w 'GL_ARB_vertex_program'
    //   2775: invokeinterface contains : (Ljava/lang/Object;)Z
    //   2780: ifeq -> 2799
    //   2783: aload_0
    //   2784: ldc_w 'glNamedProgramLocalParameter4dEXT'
    //   2787: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   2790: dup2_x1
    //   2791: putfield glNamedProgramLocalParameter4dEXT : J
    //   2794: lconst_0
    //   2795: lcmp
    //   2796: ifeq -> 2803
    //   2799: iconst_1
    //   2800: goto -> 2804
    //   2803: iconst_0
    //   2804: iand
    //   2805: aload_2
    //   2806: ldc_w 'GL_ARB_vertex_program'
    //   2809: invokeinterface contains : (Ljava/lang/Object;)Z
    //   2814: ifeq -> 2833
    //   2817: aload_0
    //   2818: ldc_w 'glNamedProgramLocalParameter4dvEXT'
    //   2821: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   2824: dup2_x1
    //   2825: putfield glNamedProgramLocalParameter4dvEXT : J
    //   2828: lconst_0
    //   2829: lcmp
    //   2830: ifeq -> 2837
    //   2833: iconst_1
    //   2834: goto -> 2838
    //   2837: iconst_0
    //   2838: iand
    //   2839: aload_2
    //   2840: ldc_w 'GL_ARB_vertex_program'
    //   2843: invokeinterface contains : (Ljava/lang/Object;)Z
    //   2848: ifeq -> 2867
    //   2851: aload_0
    //   2852: ldc_w 'glNamedProgramLocalParameter4fEXT'
    //   2855: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   2858: dup2_x1
    //   2859: putfield glNamedProgramLocalParameter4fEXT : J
    //   2862: lconst_0
    //   2863: lcmp
    //   2864: ifeq -> 2871
    //   2867: iconst_1
    //   2868: goto -> 2872
    //   2871: iconst_0
    //   2872: iand
    //   2873: aload_2
    //   2874: ldc_w 'GL_ARB_vertex_program'
    //   2877: invokeinterface contains : (Ljava/lang/Object;)Z
    //   2882: ifeq -> 2901
    //   2885: aload_0
    //   2886: ldc_w 'glNamedProgramLocalParameter4fvEXT'
    //   2889: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   2892: dup2_x1
    //   2893: putfield glNamedProgramLocalParameter4fvEXT : J
    //   2896: lconst_0
    //   2897: lcmp
    //   2898: ifeq -> 2905
    //   2901: iconst_1
    //   2902: goto -> 2906
    //   2905: iconst_0
    //   2906: iand
    //   2907: aload_2
    //   2908: ldc_w 'GL_ARB_vertex_program'
    //   2911: invokeinterface contains : (Ljava/lang/Object;)Z
    //   2916: ifeq -> 2935
    //   2919: aload_0
    //   2920: ldc_w 'glGetNamedProgramLocalParameterdvEXT'
    //   2923: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   2926: dup2_x1
    //   2927: putfield glGetNamedProgramLocalParameterdvEXT : J
    //   2930: lconst_0
    //   2931: lcmp
    //   2932: ifeq -> 2939
    //   2935: iconst_1
    //   2936: goto -> 2940
    //   2939: iconst_0
    //   2940: iand
    //   2941: aload_2
    //   2942: ldc_w 'GL_ARB_vertex_program'
    //   2945: invokeinterface contains : (Ljava/lang/Object;)Z
    //   2950: ifeq -> 2969
    //   2953: aload_0
    //   2954: ldc_w 'glGetNamedProgramLocalParameterfvEXT'
    //   2957: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   2960: dup2_x1
    //   2961: putfield glGetNamedProgramLocalParameterfvEXT : J
    //   2964: lconst_0
    //   2965: lcmp
    //   2966: ifeq -> 2973
    //   2969: iconst_1
    //   2970: goto -> 2974
    //   2973: iconst_0
    //   2974: iand
    //   2975: aload_2
    //   2976: ldc_w 'GL_ARB_vertex_program'
    //   2979: invokeinterface contains : (Ljava/lang/Object;)Z
    //   2984: ifeq -> 3003
    //   2987: aload_0
    //   2988: ldc_w 'glGetNamedProgramivEXT'
    //   2991: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   2994: dup2_x1
    //   2995: putfield glGetNamedProgramivEXT : J
    //   2998: lconst_0
    //   2999: lcmp
    //   3000: ifeq -> 3007
    //   3003: iconst_1
    //   3004: goto -> 3008
    //   3007: iconst_0
    //   3008: iand
    //   3009: aload_2
    //   3010: ldc_w 'GL_ARB_vertex_program'
    //   3013: invokeinterface contains : (Ljava/lang/Object;)Z
    //   3018: ifeq -> 3037
    //   3021: aload_0
    //   3022: ldc_w 'glGetNamedProgramStringEXT'
    //   3025: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   3028: dup2_x1
    //   3029: putfield glGetNamedProgramStringEXT : J
    //   3032: lconst_0
    //   3033: lcmp
    //   3034: ifeq -> 3041
    //   3037: iconst_1
    //   3038: goto -> 3042
    //   3041: iconst_0
    //   3042: iand
    //   3043: aload_2
    //   3044: ldc_w 'OpenGL13'
    //   3047: invokeinterface contains : (Ljava/lang/Object;)Z
    //   3052: ifeq -> 3071
    //   3055: aload_0
    //   3056: ldc_w 'glCompressedTextureImage3DEXT'
    //   3059: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   3062: dup2_x1
    //   3063: putfield glCompressedTextureImage3DEXT : J
    //   3066: lconst_0
    //   3067: lcmp
    //   3068: ifeq -> 3075
    //   3071: iconst_1
    //   3072: goto -> 3076
    //   3075: iconst_0
    //   3076: iand
    //   3077: aload_2
    //   3078: ldc_w 'OpenGL13'
    //   3081: invokeinterface contains : (Ljava/lang/Object;)Z
    //   3086: ifeq -> 3105
    //   3089: aload_0
    //   3090: ldc_w 'glCompressedTextureImage2DEXT'
    //   3093: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   3096: dup2_x1
    //   3097: putfield glCompressedTextureImage2DEXT : J
    //   3100: lconst_0
    //   3101: lcmp
    //   3102: ifeq -> 3109
    //   3105: iconst_1
    //   3106: goto -> 3110
    //   3109: iconst_0
    //   3110: iand
    //   3111: aload_2
    //   3112: ldc_w 'OpenGL13'
    //   3115: invokeinterface contains : (Ljava/lang/Object;)Z
    //   3120: ifeq -> 3139
    //   3123: aload_0
    //   3124: ldc_w 'glCompressedTextureImage1DEXT'
    //   3127: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   3130: dup2_x1
    //   3131: putfield glCompressedTextureImage1DEXT : J
    //   3134: lconst_0
    //   3135: lcmp
    //   3136: ifeq -> 3143
    //   3139: iconst_1
    //   3140: goto -> 3144
    //   3143: iconst_0
    //   3144: iand
    //   3145: aload_2
    //   3146: ldc_w 'OpenGL13'
    //   3149: invokeinterface contains : (Ljava/lang/Object;)Z
    //   3154: ifeq -> 3173
    //   3157: aload_0
    //   3158: ldc_w 'glCompressedTextureSubImage3DEXT'
    //   3161: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   3164: dup2_x1
    //   3165: putfield glCompressedTextureSubImage3DEXT : J
    //   3168: lconst_0
    //   3169: lcmp
    //   3170: ifeq -> 3177
    //   3173: iconst_1
    //   3174: goto -> 3178
    //   3177: iconst_0
    //   3178: iand
    //   3179: aload_2
    //   3180: ldc_w 'OpenGL13'
    //   3183: invokeinterface contains : (Ljava/lang/Object;)Z
    //   3188: ifeq -> 3207
    //   3191: aload_0
    //   3192: ldc_w 'glCompressedTextureSubImage2DEXT'
    //   3195: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   3198: dup2_x1
    //   3199: putfield glCompressedTextureSubImage2DEXT : J
    //   3202: lconst_0
    //   3203: lcmp
    //   3204: ifeq -> 3211
    //   3207: iconst_1
    //   3208: goto -> 3212
    //   3211: iconst_0
    //   3212: iand
    //   3213: aload_2
    //   3214: ldc_w 'OpenGL13'
    //   3217: invokeinterface contains : (Ljava/lang/Object;)Z
    //   3222: ifeq -> 3241
    //   3225: aload_0
    //   3226: ldc_w 'glCompressedTextureSubImage1DEXT'
    //   3229: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   3232: dup2_x1
    //   3233: putfield glCompressedTextureSubImage1DEXT : J
    //   3236: lconst_0
    //   3237: lcmp
    //   3238: ifeq -> 3245
    //   3241: iconst_1
    //   3242: goto -> 3246
    //   3245: iconst_0
    //   3246: iand
    //   3247: aload_2
    //   3248: ldc_w 'OpenGL13'
    //   3251: invokeinterface contains : (Ljava/lang/Object;)Z
    //   3256: ifeq -> 3275
    //   3259: aload_0
    //   3260: ldc_w 'glGetCompressedTextureImageEXT'
    //   3263: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   3266: dup2_x1
    //   3267: putfield glGetCompressedTextureImageEXT : J
    //   3270: lconst_0
    //   3271: lcmp
    //   3272: ifeq -> 3279
    //   3275: iconst_1
    //   3276: goto -> 3280
    //   3279: iconst_0
    //   3280: iand
    //   3281: aload_2
    //   3282: ldc_w 'OpenGL13'
    //   3285: invokeinterface contains : (Ljava/lang/Object;)Z
    //   3290: ifeq -> 3309
    //   3293: aload_0
    //   3294: ldc_w 'glCompressedMultiTexImage3DEXT'
    //   3297: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   3300: dup2_x1
    //   3301: putfield glCompressedMultiTexImage3DEXT : J
    //   3304: lconst_0
    //   3305: lcmp
    //   3306: ifeq -> 3313
    //   3309: iconst_1
    //   3310: goto -> 3314
    //   3313: iconst_0
    //   3314: iand
    //   3315: aload_2
    //   3316: ldc_w 'OpenGL13'
    //   3319: invokeinterface contains : (Ljava/lang/Object;)Z
    //   3324: ifeq -> 3343
    //   3327: aload_0
    //   3328: ldc_w 'glCompressedMultiTexImage2DEXT'
    //   3331: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   3334: dup2_x1
    //   3335: putfield glCompressedMultiTexImage2DEXT : J
    //   3338: lconst_0
    //   3339: lcmp
    //   3340: ifeq -> 3347
    //   3343: iconst_1
    //   3344: goto -> 3348
    //   3347: iconst_0
    //   3348: iand
    //   3349: aload_2
    //   3350: ldc_w 'OpenGL13'
    //   3353: invokeinterface contains : (Ljava/lang/Object;)Z
    //   3358: ifeq -> 3377
    //   3361: aload_0
    //   3362: ldc_w 'glCompressedMultiTexImage1DEXT'
    //   3365: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   3368: dup2_x1
    //   3369: putfield glCompressedMultiTexImage1DEXT : J
    //   3372: lconst_0
    //   3373: lcmp
    //   3374: ifeq -> 3381
    //   3377: iconst_1
    //   3378: goto -> 3382
    //   3381: iconst_0
    //   3382: iand
    //   3383: aload_2
    //   3384: ldc_w 'OpenGL13'
    //   3387: invokeinterface contains : (Ljava/lang/Object;)Z
    //   3392: ifeq -> 3411
    //   3395: aload_0
    //   3396: ldc_w 'glCompressedMultiTexSubImage3DEXT'
    //   3399: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   3402: dup2_x1
    //   3403: putfield glCompressedMultiTexSubImage3DEXT : J
    //   3406: lconst_0
    //   3407: lcmp
    //   3408: ifeq -> 3415
    //   3411: iconst_1
    //   3412: goto -> 3416
    //   3415: iconst_0
    //   3416: iand
    //   3417: aload_2
    //   3418: ldc_w 'OpenGL13'
    //   3421: invokeinterface contains : (Ljava/lang/Object;)Z
    //   3426: ifeq -> 3445
    //   3429: aload_0
    //   3430: ldc_w 'glCompressedMultiTexSubImage2DEXT'
    //   3433: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   3436: dup2_x1
    //   3437: putfield glCompressedMultiTexSubImage2DEXT : J
    //   3440: lconst_0
    //   3441: lcmp
    //   3442: ifeq -> 3449
    //   3445: iconst_1
    //   3446: goto -> 3450
    //   3449: iconst_0
    //   3450: iand
    //   3451: aload_2
    //   3452: ldc_w 'OpenGL13'
    //   3455: invokeinterface contains : (Ljava/lang/Object;)Z
    //   3460: ifeq -> 3479
    //   3463: aload_0
    //   3464: ldc_w 'glCompressedMultiTexSubImage1DEXT'
    //   3467: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   3470: dup2_x1
    //   3471: putfield glCompressedMultiTexSubImage1DEXT : J
    //   3474: lconst_0
    //   3475: lcmp
    //   3476: ifeq -> 3483
    //   3479: iconst_1
    //   3480: goto -> 3484
    //   3483: iconst_0
    //   3484: iand
    //   3485: aload_2
    //   3486: ldc_w 'OpenGL13'
    //   3489: invokeinterface contains : (Ljava/lang/Object;)Z
    //   3494: ifeq -> 3513
    //   3497: aload_0
    //   3498: ldc_w 'glGetCompressedMultiTexImageEXT'
    //   3501: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   3504: dup2_x1
    //   3505: putfield glGetCompressedMultiTexImageEXT : J
    //   3508: lconst_0
    //   3509: lcmp
    //   3510: ifeq -> 3517
    //   3513: iconst_1
    //   3514: goto -> 3518
    //   3517: iconst_0
    //   3518: iand
    //   3519: iload_1
    //   3520: ifne -> 3551
    //   3523: aload_2
    //   3524: ldc_w 'OpenGL13'
    //   3527: invokeinterface contains : (Ljava/lang/Object;)Z
    //   3532: ifeq -> 3551
    //   3535: aload_0
    //   3536: ldc_w 'glMatrixLoadTransposefEXT'
    //   3539: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   3542: dup2_x1
    //   3543: putfield glMatrixLoadTransposefEXT : J
    //   3546: lconst_0
    //   3547: lcmp
    //   3548: ifeq -> 3555
    //   3551: iconst_1
    //   3552: goto -> 3556
    //   3555: iconst_0
    //   3556: iand
    //   3557: iload_1
    //   3558: ifne -> 3589
    //   3561: aload_2
    //   3562: ldc_w 'OpenGL13'
    //   3565: invokeinterface contains : (Ljava/lang/Object;)Z
    //   3570: ifeq -> 3589
    //   3573: aload_0
    //   3574: ldc_w 'glMatrixLoadTransposedEXT'
    //   3577: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   3580: dup2_x1
    //   3581: putfield glMatrixLoadTransposedEXT : J
    //   3584: lconst_0
    //   3585: lcmp
    //   3586: ifeq -> 3593
    //   3589: iconst_1
    //   3590: goto -> 3594
    //   3593: iconst_0
    //   3594: iand
    //   3595: iload_1
    //   3596: ifne -> 3627
    //   3599: aload_2
    //   3600: ldc_w 'OpenGL13'
    //   3603: invokeinterface contains : (Ljava/lang/Object;)Z
    //   3608: ifeq -> 3627
    //   3611: aload_0
    //   3612: ldc_w 'glMatrixMultTransposefEXT'
    //   3615: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   3618: dup2_x1
    //   3619: putfield glMatrixMultTransposefEXT : J
    //   3622: lconst_0
    //   3623: lcmp
    //   3624: ifeq -> 3631
    //   3627: iconst_1
    //   3628: goto -> 3632
    //   3631: iconst_0
    //   3632: iand
    //   3633: iload_1
    //   3634: ifne -> 3665
    //   3637: aload_2
    //   3638: ldc_w 'OpenGL13'
    //   3641: invokeinterface contains : (Ljava/lang/Object;)Z
    //   3646: ifeq -> 3665
    //   3649: aload_0
    //   3650: ldc_w 'glMatrixMultTransposedEXT'
    //   3653: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   3656: dup2_x1
    //   3657: putfield glMatrixMultTransposedEXT : J
    //   3660: lconst_0
    //   3661: lcmp
    //   3662: ifeq -> 3669
    //   3665: iconst_1
    //   3666: goto -> 3670
    //   3669: iconst_0
    //   3670: iand
    //   3671: aload_2
    //   3672: ldc_w 'OpenGL15'
    //   3675: invokeinterface contains : (Ljava/lang/Object;)Z
    //   3680: ifeq -> 3699
    //   3683: aload_0
    //   3684: ldc_w 'glNamedBufferDataEXT'
    //   3687: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   3690: dup2_x1
    //   3691: putfield glNamedBufferDataEXT : J
    //   3694: lconst_0
    //   3695: lcmp
    //   3696: ifeq -> 3703
    //   3699: iconst_1
    //   3700: goto -> 3704
    //   3703: iconst_0
    //   3704: iand
    //   3705: aload_2
    //   3706: ldc_w 'OpenGL15'
    //   3709: invokeinterface contains : (Ljava/lang/Object;)Z
    //   3714: ifeq -> 3733
    //   3717: aload_0
    //   3718: ldc_w 'glNamedBufferSubDataEXT'
    //   3721: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   3724: dup2_x1
    //   3725: putfield glNamedBufferSubDataEXT : J
    //   3728: lconst_0
    //   3729: lcmp
    //   3730: ifeq -> 3737
    //   3733: iconst_1
    //   3734: goto -> 3738
    //   3737: iconst_0
    //   3738: iand
    //   3739: aload_2
    //   3740: ldc_w 'OpenGL15'
    //   3743: invokeinterface contains : (Ljava/lang/Object;)Z
    //   3748: ifeq -> 3767
    //   3751: aload_0
    //   3752: ldc_w 'glMapNamedBufferEXT'
    //   3755: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   3758: dup2_x1
    //   3759: putfield glMapNamedBufferEXT : J
    //   3762: lconst_0
    //   3763: lcmp
    //   3764: ifeq -> 3771
    //   3767: iconst_1
    //   3768: goto -> 3772
    //   3771: iconst_0
    //   3772: iand
    //   3773: aload_2
    //   3774: ldc_w 'OpenGL15'
    //   3777: invokeinterface contains : (Ljava/lang/Object;)Z
    //   3782: ifeq -> 3801
    //   3785: aload_0
    //   3786: ldc_w 'glUnmapNamedBufferEXT'
    //   3789: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   3792: dup2_x1
    //   3793: putfield glUnmapNamedBufferEXT : J
    //   3796: lconst_0
    //   3797: lcmp
    //   3798: ifeq -> 3805
    //   3801: iconst_1
    //   3802: goto -> 3806
    //   3805: iconst_0
    //   3806: iand
    //   3807: aload_2
    //   3808: ldc_w 'OpenGL15'
    //   3811: invokeinterface contains : (Ljava/lang/Object;)Z
    //   3816: ifeq -> 3835
    //   3819: aload_0
    //   3820: ldc_w 'glGetNamedBufferParameterivEXT'
    //   3823: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   3826: dup2_x1
    //   3827: putfield glGetNamedBufferParameterivEXT : J
    //   3830: lconst_0
    //   3831: lcmp
    //   3832: ifeq -> 3839
    //   3835: iconst_1
    //   3836: goto -> 3840
    //   3839: iconst_0
    //   3840: iand
    //   3841: aload_2
    //   3842: ldc_w 'OpenGL15'
    //   3845: invokeinterface contains : (Ljava/lang/Object;)Z
    //   3850: ifeq -> 3869
    //   3853: aload_0
    //   3854: ldc_w 'glGetNamedBufferPointervEXT'
    //   3857: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   3860: dup2_x1
    //   3861: putfield glGetNamedBufferPointervEXT : J
    //   3864: lconst_0
    //   3865: lcmp
    //   3866: ifeq -> 3873
    //   3869: iconst_1
    //   3870: goto -> 3874
    //   3873: iconst_0
    //   3874: iand
    //   3875: aload_2
    //   3876: ldc_w 'OpenGL15'
    //   3879: invokeinterface contains : (Ljava/lang/Object;)Z
    //   3884: ifeq -> 3903
    //   3887: aload_0
    //   3888: ldc_w 'glGetNamedBufferSubDataEXT'
    //   3891: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   3894: dup2_x1
    //   3895: putfield glGetNamedBufferSubDataEXT : J
    //   3898: lconst_0
    //   3899: lcmp
    //   3900: ifeq -> 3907
    //   3903: iconst_1
    //   3904: goto -> 3908
    //   3907: iconst_0
    //   3908: iand
    //   3909: aload_2
    //   3910: ldc_w 'OpenGL20'
    //   3913: invokeinterface contains : (Ljava/lang/Object;)Z
    //   3918: ifeq -> 3937
    //   3921: aload_0
    //   3922: ldc_w 'glProgramUniform1fEXT'
    //   3925: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   3928: dup2_x1
    //   3929: putfield glProgramUniform1fEXT : J
    //   3932: lconst_0
    //   3933: lcmp
    //   3934: ifeq -> 3941
    //   3937: iconst_1
    //   3938: goto -> 3942
    //   3941: iconst_0
    //   3942: iand
    //   3943: aload_2
    //   3944: ldc_w 'OpenGL20'
    //   3947: invokeinterface contains : (Ljava/lang/Object;)Z
    //   3952: ifeq -> 3971
    //   3955: aload_0
    //   3956: ldc_w 'glProgramUniform2fEXT'
    //   3959: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   3962: dup2_x1
    //   3963: putfield glProgramUniform2fEXT : J
    //   3966: lconst_0
    //   3967: lcmp
    //   3968: ifeq -> 3975
    //   3971: iconst_1
    //   3972: goto -> 3976
    //   3975: iconst_0
    //   3976: iand
    //   3977: aload_2
    //   3978: ldc_w 'OpenGL20'
    //   3981: invokeinterface contains : (Ljava/lang/Object;)Z
    //   3986: ifeq -> 4005
    //   3989: aload_0
    //   3990: ldc_w 'glProgramUniform3fEXT'
    //   3993: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   3996: dup2_x1
    //   3997: putfield glProgramUniform3fEXT : J
    //   4000: lconst_0
    //   4001: lcmp
    //   4002: ifeq -> 4009
    //   4005: iconst_1
    //   4006: goto -> 4010
    //   4009: iconst_0
    //   4010: iand
    //   4011: aload_2
    //   4012: ldc_w 'OpenGL20'
    //   4015: invokeinterface contains : (Ljava/lang/Object;)Z
    //   4020: ifeq -> 4039
    //   4023: aload_0
    //   4024: ldc_w 'glProgramUniform4fEXT'
    //   4027: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   4030: dup2_x1
    //   4031: putfield glProgramUniform4fEXT : J
    //   4034: lconst_0
    //   4035: lcmp
    //   4036: ifeq -> 4043
    //   4039: iconst_1
    //   4040: goto -> 4044
    //   4043: iconst_0
    //   4044: iand
    //   4045: aload_2
    //   4046: ldc_w 'OpenGL20'
    //   4049: invokeinterface contains : (Ljava/lang/Object;)Z
    //   4054: ifeq -> 4073
    //   4057: aload_0
    //   4058: ldc_w 'glProgramUniform1iEXT'
    //   4061: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   4064: dup2_x1
    //   4065: putfield glProgramUniform1iEXT : J
    //   4068: lconst_0
    //   4069: lcmp
    //   4070: ifeq -> 4077
    //   4073: iconst_1
    //   4074: goto -> 4078
    //   4077: iconst_0
    //   4078: iand
    //   4079: aload_2
    //   4080: ldc_w 'OpenGL20'
    //   4083: invokeinterface contains : (Ljava/lang/Object;)Z
    //   4088: ifeq -> 4107
    //   4091: aload_0
    //   4092: ldc_w 'glProgramUniform2iEXT'
    //   4095: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   4098: dup2_x1
    //   4099: putfield glProgramUniform2iEXT : J
    //   4102: lconst_0
    //   4103: lcmp
    //   4104: ifeq -> 4111
    //   4107: iconst_1
    //   4108: goto -> 4112
    //   4111: iconst_0
    //   4112: iand
    //   4113: aload_2
    //   4114: ldc_w 'OpenGL20'
    //   4117: invokeinterface contains : (Ljava/lang/Object;)Z
    //   4122: ifeq -> 4141
    //   4125: aload_0
    //   4126: ldc_w 'glProgramUniform3iEXT'
    //   4129: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   4132: dup2_x1
    //   4133: putfield glProgramUniform3iEXT : J
    //   4136: lconst_0
    //   4137: lcmp
    //   4138: ifeq -> 4145
    //   4141: iconst_1
    //   4142: goto -> 4146
    //   4145: iconst_0
    //   4146: iand
    //   4147: aload_2
    //   4148: ldc_w 'OpenGL20'
    //   4151: invokeinterface contains : (Ljava/lang/Object;)Z
    //   4156: ifeq -> 4175
    //   4159: aload_0
    //   4160: ldc_w 'glProgramUniform4iEXT'
    //   4163: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   4166: dup2_x1
    //   4167: putfield glProgramUniform4iEXT : J
    //   4170: lconst_0
    //   4171: lcmp
    //   4172: ifeq -> 4179
    //   4175: iconst_1
    //   4176: goto -> 4180
    //   4179: iconst_0
    //   4180: iand
    //   4181: aload_2
    //   4182: ldc_w 'OpenGL20'
    //   4185: invokeinterface contains : (Ljava/lang/Object;)Z
    //   4190: ifeq -> 4209
    //   4193: aload_0
    //   4194: ldc_w 'glProgramUniform1fvEXT'
    //   4197: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   4200: dup2_x1
    //   4201: putfield glProgramUniform1fvEXT : J
    //   4204: lconst_0
    //   4205: lcmp
    //   4206: ifeq -> 4213
    //   4209: iconst_1
    //   4210: goto -> 4214
    //   4213: iconst_0
    //   4214: iand
    //   4215: aload_2
    //   4216: ldc_w 'OpenGL20'
    //   4219: invokeinterface contains : (Ljava/lang/Object;)Z
    //   4224: ifeq -> 4243
    //   4227: aload_0
    //   4228: ldc_w 'glProgramUniform2fvEXT'
    //   4231: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   4234: dup2_x1
    //   4235: putfield glProgramUniform2fvEXT : J
    //   4238: lconst_0
    //   4239: lcmp
    //   4240: ifeq -> 4247
    //   4243: iconst_1
    //   4244: goto -> 4248
    //   4247: iconst_0
    //   4248: iand
    //   4249: aload_2
    //   4250: ldc_w 'OpenGL20'
    //   4253: invokeinterface contains : (Ljava/lang/Object;)Z
    //   4258: ifeq -> 4277
    //   4261: aload_0
    //   4262: ldc_w 'glProgramUniform3fvEXT'
    //   4265: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   4268: dup2_x1
    //   4269: putfield glProgramUniform3fvEXT : J
    //   4272: lconst_0
    //   4273: lcmp
    //   4274: ifeq -> 4281
    //   4277: iconst_1
    //   4278: goto -> 4282
    //   4281: iconst_0
    //   4282: iand
    //   4283: aload_2
    //   4284: ldc_w 'OpenGL20'
    //   4287: invokeinterface contains : (Ljava/lang/Object;)Z
    //   4292: ifeq -> 4311
    //   4295: aload_0
    //   4296: ldc_w 'glProgramUniform4fvEXT'
    //   4299: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   4302: dup2_x1
    //   4303: putfield glProgramUniform4fvEXT : J
    //   4306: lconst_0
    //   4307: lcmp
    //   4308: ifeq -> 4315
    //   4311: iconst_1
    //   4312: goto -> 4316
    //   4315: iconst_0
    //   4316: iand
    //   4317: aload_2
    //   4318: ldc_w 'OpenGL20'
    //   4321: invokeinterface contains : (Ljava/lang/Object;)Z
    //   4326: ifeq -> 4345
    //   4329: aload_0
    //   4330: ldc_w 'glProgramUniform1ivEXT'
    //   4333: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   4336: dup2_x1
    //   4337: putfield glProgramUniform1ivEXT : J
    //   4340: lconst_0
    //   4341: lcmp
    //   4342: ifeq -> 4349
    //   4345: iconst_1
    //   4346: goto -> 4350
    //   4349: iconst_0
    //   4350: iand
    //   4351: aload_2
    //   4352: ldc_w 'OpenGL20'
    //   4355: invokeinterface contains : (Ljava/lang/Object;)Z
    //   4360: ifeq -> 4379
    //   4363: aload_0
    //   4364: ldc_w 'glProgramUniform2ivEXT'
    //   4367: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   4370: dup2_x1
    //   4371: putfield glProgramUniform2ivEXT : J
    //   4374: lconst_0
    //   4375: lcmp
    //   4376: ifeq -> 4383
    //   4379: iconst_1
    //   4380: goto -> 4384
    //   4383: iconst_0
    //   4384: iand
    //   4385: aload_2
    //   4386: ldc_w 'OpenGL20'
    //   4389: invokeinterface contains : (Ljava/lang/Object;)Z
    //   4394: ifeq -> 4413
    //   4397: aload_0
    //   4398: ldc_w 'glProgramUniform3ivEXT'
    //   4401: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   4404: dup2_x1
    //   4405: putfield glProgramUniform3ivEXT : J
    //   4408: lconst_0
    //   4409: lcmp
    //   4410: ifeq -> 4417
    //   4413: iconst_1
    //   4414: goto -> 4418
    //   4417: iconst_0
    //   4418: iand
    //   4419: aload_2
    //   4420: ldc_w 'OpenGL20'
    //   4423: invokeinterface contains : (Ljava/lang/Object;)Z
    //   4428: ifeq -> 4447
    //   4431: aload_0
    //   4432: ldc_w 'glProgramUniform4ivEXT'
    //   4435: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   4438: dup2_x1
    //   4439: putfield glProgramUniform4ivEXT : J
    //   4442: lconst_0
    //   4443: lcmp
    //   4444: ifeq -> 4451
    //   4447: iconst_1
    //   4448: goto -> 4452
    //   4451: iconst_0
    //   4452: iand
    //   4453: aload_2
    //   4454: ldc_w 'OpenGL20'
    //   4457: invokeinterface contains : (Ljava/lang/Object;)Z
    //   4462: ifeq -> 4481
    //   4465: aload_0
    //   4466: ldc_w 'glProgramUniformMatrix2fvEXT'
    //   4469: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   4472: dup2_x1
    //   4473: putfield glProgramUniformMatrix2fvEXT : J
    //   4476: lconst_0
    //   4477: lcmp
    //   4478: ifeq -> 4485
    //   4481: iconst_1
    //   4482: goto -> 4486
    //   4485: iconst_0
    //   4486: iand
    //   4487: aload_2
    //   4488: ldc_w 'OpenGL20'
    //   4491: invokeinterface contains : (Ljava/lang/Object;)Z
    //   4496: ifeq -> 4515
    //   4499: aload_0
    //   4500: ldc_w 'glProgramUniformMatrix3fvEXT'
    //   4503: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   4506: dup2_x1
    //   4507: putfield glProgramUniformMatrix3fvEXT : J
    //   4510: lconst_0
    //   4511: lcmp
    //   4512: ifeq -> 4519
    //   4515: iconst_1
    //   4516: goto -> 4520
    //   4519: iconst_0
    //   4520: iand
    //   4521: aload_2
    //   4522: ldc_w 'OpenGL20'
    //   4525: invokeinterface contains : (Ljava/lang/Object;)Z
    //   4530: ifeq -> 4549
    //   4533: aload_0
    //   4534: ldc_w 'glProgramUniformMatrix4fvEXT'
    //   4537: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   4540: dup2_x1
    //   4541: putfield glProgramUniformMatrix4fvEXT : J
    //   4544: lconst_0
    //   4545: lcmp
    //   4546: ifeq -> 4553
    //   4549: iconst_1
    //   4550: goto -> 4554
    //   4553: iconst_0
    //   4554: iand
    //   4555: aload_2
    //   4556: ldc_w 'OpenGL21'
    //   4559: invokeinterface contains : (Ljava/lang/Object;)Z
    //   4564: ifeq -> 4583
    //   4567: aload_0
    //   4568: ldc_w 'glProgramUniformMatrix2x3fvEXT'
    //   4571: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   4574: dup2_x1
    //   4575: putfield glProgramUniformMatrix2x3fvEXT : J
    //   4578: lconst_0
    //   4579: lcmp
    //   4580: ifeq -> 4587
    //   4583: iconst_1
    //   4584: goto -> 4588
    //   4587: iconst_0
    //   4588: iand
    //   4589: aload_2
    //   4590: ldc_w 'OpenGL21'
    //   4593: invokeinterface contains : (Ljava/lang/Object;)Z
    //   4598: ifeq -> 4617
    //   4601: aload_0
    //   4602: ldc_w 'glProgramUniformMatrix3x2fvEXT'
    //   4605: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   4608: dup2_x1
    //   4609: putfield glProgramUniformMatrix3x2fvEXT : J
    //   4612: lconst_0
    //   4613: lcmp
    //   4614: ifeq -> 4621
    //   4617: iconst_1
    //   4618: goto -> 4622
    //   4621: iconst_0
    //   4622: iand
    //   4623: aload_2
    //   4624: ldc_w 'OpenGL21'
    //   4627: invokeinterface contains : (Ljava/lang/Object;)Z
    //   4632: ifeq -> 4651
    //   4635: aload_0
    //   4636: ldc_w 'glProgramUniformMatrix2x4fvEXT'
    //   4639: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   4642: dup2_x1
    //   4643: putfield glProgramUniformMatrix2x4fvEXT : J
    //   4646: lconst_0
    //   4647: lcmp
    //   4648: ifeq -> 4655
    //   4651: iconst_1
    //   4652: goto -> 4656
    //   4655: iconst_0
    //   4656: iand
    //   4657: aload_2
    //   4658: ldc_w 'OpenGL21'
    //   4661: invokeinterface contains : (Ljava/lang/Object;)Z
    //   4666: ifeq -> 4685
    //   4669: aload_0
    //   4670: ldc_w 'glProgramUniformMatrix4x2fvEXT'
    //   4673: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   4676: dup2_x1
    //   4677: putfield glProgramUniformMatrix4x2fvEXT : J
    //   4680: lconst_0
    //   4681: lcmp
    //   4682: ifeq -> 4689
    //   4685: iconst_1
    //   4686: goto -> 4690
    //   4689: iconst_0
    //   4690: iand
    //   4691: aload_2
    //   4692: ldc_w 'OpenGL21'
    //   4695: invokeinterface contains : (Ljava/lang/Object;)Z
    //   4700: ifeq -> 4719
    //   4703: aload_0
    //   4704: ldc_w 'glProgramUniformMatrix3x4fvEXT'
    //   4707: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   4710: dup2_x1
    //   4711: putfield glProgramUniformMatrix3x4fvEXT : J
    //   4714: lconst_0
    //   4715: lcmp
    //   4716: ifeq -> 4723
    //   4719: iconst_1
    //   4720: goto -> 4724
    //   4723: iconst_0
    //   4724: iand
    //   4725: aload_2
    //   4726: ldc_w 'OpenGL21'
    //   4729: invokeinterface contains : (Ljava/lang/Object;)Z
    //   4734: ifeq -> 4753
    //   4737: aload_0
    //   4738: ldc_w 'glProgramUniformMatrix4x3fvEXT'
    //   4741: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   4744: dup2_x1
    //   4745: putfield glProgramUniformMatrix4x3fvEXT : J
    //   4748: lconst_0
    //   4749: lcmp
    //   4750: ifeq -> 4757
    //   4753: iconst_1
    //   4754: goto -> 4758
    //   4757: iconst_0
    //   4758: iand
    //   4759: aload_2
    //   4760: ldc_w 'GL_EXT_texture_buffer_object'
    //   4763: invokeinterface contains : (Ljava/lang/Object;)Z
    //   4768: ifeq -> 4787
    //   4771: aload_0
    //   4772: ldc_w 'glTextureBufferEXT'
    //   4775: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   4778: dup2_x1
    //   4779: putfield glTextureBufferEXT : J
    //   4782: lconst_0
    //   4783: lcmp
    //   4784: ifeq -> 4791
    //   4787: iconst_1
    //   4788: goto -> 4792
    //   4791: iconst_0
    //   4792: iand
    //   4793: aload_2
    //   4794: ldc_w 'GL_EXT_texture_buffer_object'
    //   4797: invokeinterface contains : (Ljava/lang/Object;)Z
    //   4802: ifeq -> 4821
    //   4805: aload_0
    //   4806: ldc_w 'glMultiTexBufferEXT'
    //   4809: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   4812: dup2_x1
    //   4813: putfield glMultiTexBufferEXT : J
    //   4816: lconst_0
    //   4817: lcmp
    //   4818: ifeq -> 4825
    //   4821: iconst_1
    //   4822: goto -> 4826
    //   4825: iconst_0
    //   4826: iand
    //   4827: aload_2
    //   4828: ldc_w 'GL_EXT_texture_integer'
    //   4831: invokeinterface contains : (Ljava/lang/Object;)Z
    //   4836: ifeq -> 4855
    //   4839: aload_0
    //   4840: ldc_w 'glTextureParameterIivEXT'
    //   4843: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   4846: dup2_x1
    //   4847: putfield glTextureParameterIivEXT : J
    //   4850: lconst_0
    //   4851: lcmp
    //   4852: ifeq -> 4859
    //   4855: iconst_1
    //   4856: goto -> 4860
    //   4859: iconst_0
    //   4860: iand
    //   4861: aload_2
    //   4862: ldc_w 'GL_EXT_texture_integer'
    //   4865: invokeinterface contains : (Ljava/lang/Object;)Z
    //   4870: ifeq -> 4889
    //   4873: aload_0
    //   4874: ldc_w 'glTextureParameterIuivEXT'
    //   4877: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   4880: dup2_x1
    //   4881: putfield glTextureParameterIuivEXT : J
    //   4884: lconst_0
    //   4885: lcmp
    //   4886: ifeq -> 4893
    //   4889: iconst_1
    //   4890: goto -> 4894
    //   4893: iconst_0
    //   4894: iand
    //   4895: aload_2
    //   4896: ldc_w 'GL_EXT_texture_integer'
    //   4899: invokeinterface contains : (Ljava/lang/Object;)Z
    //   4904: ifeq -> 4923
    //   4907: aload_0
    //   4908: ldc_w 'glGetTextureParameterIivEXT'
    //   4911: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   4914: dup2_x1
    //   4915: putfield glGetTextureParameterIivEXT : J
    //   4918: lconst_0
    //   4919: lcmp
    //   4920: ifeq -> 4927
    //   4923: iconst_1
    //   4924: goto -> 4928
    //   4927: iconst_0
    //   4928: iand
    //   4929: aload_2
    //   4930: ldc_w 'GL_EXT_texture_integer'
    //   4933: invokeinterface contains : (Ljava/lang/Object;)Z
    //   4938: ifeq -> 4957
    //   4941: aload_0
    //   4942: ldc_w 'glGetTextureParameterIuivEXT'
    //   4945: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   4948: dup2_x1
    //   4949: putfield glGetTextureParameterIuivEXT : J
    //   4952: lconst_0
    //   4953: lcmp
    //   4954: ifeq -> 4961
    //   4957: iconst_1
    //   4958: goto -> 4962
    //   4961: iconst_0
    //   4962: iand
    //   4963: aload_2
    //   4964: ldc_w 'GL_EXT_texture_integer'
    //   4967: invokeinterface contains : (Ljava/lang/Object;)Z
    //   4972: ifeq -> 4991
    //   4975: aload_0
    //   4976: ldc_w 'glMultiTexParameterIivEXT'
    //   4979: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   4982: dup2_x1
    //   4983: putfield glMultiTexParameterIivEXT : J
    //   4986: lconst_0
    //   4987: lcmp
    //   4988: ifeq -> 4995
    //   4991: iconst_1
    //   4992: goto -> 4996
    //   4995: iconst_0
    //   4996: iand
    //   4997: aload_2
    //   4998: ldc_w 'GL_EXT_texture_integer'
    //   5001: invokeinterface contains : (Ljava/lang/Object;)Z
    //   5006: ifeq -> 5025
    //   5009: aload_0
    //   5010: ldc_w 'glMultiTexParameterIuivEXT'
    //   5013: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   5016: dup2_x1
    //   5017: putfield glMultiTexParameterIuivEXT : J
    //   5020: lconst_0
    //   5021: lcmp
    //   5022: ifeq -> 5029
    //   5025: iconst_1
    //   5026: goto -> 5030
    //   5029: iconst_0
    //   5030: iand
    //   5031: aload_2
    //   5032: ldc_w 'GL_EXT_texture_integer'
    //   5035: invokeinterface contains : (Ljava/lang/Object;)Z
    //   5040: ifeq -> 5059
    //   5043: aload_0
    //   5044: ldc_w 'glGetMultiTexParameterIivEXT'
    //   5047: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   5050: dup2_x1
    //   5051: putfield glGetMultiTexParameterIivEXT : J
    //   5054: lconst_0
    //   5055: lcmp
    //   5056: ifeq -> 5063
    //   5059: iconst_1
    //   5060: goto -> 5064
    //   5063: iconst_0
    //   5064: iand
    //   5065: aload_2
    //   5066: ldc_w 'GL_EXT_texture_integer'
    //   5069: invokeinterface contains : (Ljava/lang/Object;)Z
    //   5074: ifeq -> 5093
    //   5077: aload_0
    //   5078: ldc_w 'glGetMultiTexParameterIuivEXT'
    //   5081: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   5084: dup2_x1
    //   5085: putfield glGetMultiTexParameterIuivEXT : J
    //   5088: lconst_0
    //   5089: lcmp
    //   5090: ifeq -> 5097
    //   5093: iconst_1
    //   5094: goto -> 5098
    //   5097: iconst_0
    //   5098: iand
    //   5099: aload_2
    //   5100: ldc_w 'GL_EXT_gpu_shader4'
    //   5103: invokeinterface contains : (Ljava/lang/Object;)Z
    //   5108: ifeq -> 5127
    //   5111: aload_0
    //   5112: ldc_w 'glProgramUniform1uiEXT'
    //   5115: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   5118: dup2_x1
    //   5119: putfield glProgramUniform1uiEXT : J
    //   5122: lconst_0
    //   5123: lcmp
    //   5124: ifeq -> 5131
    //   5127: iconst_1
    //   5128: goto -> 5132
    //   5131: iconst_0
    //   5132: iand
    //   5133: aload_2
    //   5134: ldc_w 'GL_EXT_gpu_shader4'
    //   5137: invokeinterface contains : (Ljava/lang/Object;)Z
    //   5142: ifeq -> 5161
    //   5145: aload_0
    //   5146: ldc_w 'glProgramUniform2uiEXT'
    //   5149: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   5152: dup2_x1
    //   5153: putfield glProgramUniform2uiEXT : J
    //   5156: lconst_0
    //   5157: lcmp
    //   5158: ifeq -> 5165
    //   5161: iconst_1
    //   5162: goto -> 5166
    //   5165: iconst_0
    //   5166: iand
    //   5167: aload_2
    //   5168: ldc_w 'GL_EXT_gpu_shader4'
    //   5171: invokeinterface contains : (Ljava/lang/Object;)Z
    //   5176: ifeq -> 5195
    //   5179: aload_0
    //   5180: ldc_w 'glProgramUniform3uiEXT'
    //   5183: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   5186: dup2_x1
    //   5187: putfield glProgramUniform3uiEXT : J
    //   5190: lconst_0
    //   5191: lcmp
    //   5192: ifeq -> 5199
    //   5195: iconst_1
    //   5196: goto -> 5200
    //   5199: iconst_0
    //   5200: iand
    //   5201: aload_2
    //   5202: ldc_w 'GL_EXT_gpu_shader4'
    //   5205: invokeinterface contains : (Ljava/lang/Object;)Z
    //   5210: ifeq -> 5229
    //   5213: aload_0
    //   5214: ldc_w 'glProgramUniform4uiEXT'
    //   5217: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   5220: dup2_x1
    //   5221: putfield glProgramUniform4uiEXT : J
    //   5224: lconst_0
    //   5225: lcmp
    //   5226: ifeq -> 5233
    //   5229: iconst_1
    //   5230: goto -> 5234
    //   5233: iconst_0
    //   5234: iand
    //   5235: aload_2
    //   5236: ldc_w 'GL_EXT_gpu_shader4'
    //   5239: invokeinterface contains : (Ljava/lang/Object;)Z
    //   5244: ifeq -> 5263
    //   5247: aload_0
    //   5248: ldc_w 'glProgramUniform1uivEXT'
    //   5251: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   5254: dup2_x1
    //   5255: putfield glProgramUniform1uivEXT : J
    //   5258: lconst_0
    //   5259: lcmp
    //   5260: ifeq -> 5267
    //   5263: iconst_1
    //   5264: goto -> 5268
    //   5267: iconst_0
    //   5268: iand
    //   5269: aload_2
    //   5270: ldc_w 'GL_EXT_gpu_shader4'
    //   5273: invokeinterface contains : (Ljava/lang/Object;)Z
    //   5278: ifeq -> 5297
    //   5281: aload_0
    //   5282: ldc_w 'glProgramUniform2uivEXT'
    //   5285: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   5288: dup2_x1
    //   5289: putfield glProgramUniform2uivEXT : J
    //   5292: lconst_0
    //   5293: lcmp
    //   5294: ifeq -> 5301
    //   5297: iconst_1
    //   5298: goto -> 5302
    //   5301: iconst_0
    //   5302: iand
    //   5303: aload_2
    //   5304: ldc_w 'GL_EXT_gpu_shader4'
    //   5307: invokeinterface contains : (Ljava/lang/Object;)Z
    //   5312: ifeq -> 5331
    //   5315: aload_0
    //   5316: ldc_w 'glProgramUniform3uivEXT'
    //   5319: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   5322: dup2_x1
    //   5323: putfield glProgramUniform3uivEXT : J
    //   5326: lconst_0
    //   5327: lcmp
    //   5328: ifeq -> 5335
    //   5331: iconst_1
    //   5332: goto -> 5336
    //   5335: iconst_0
    //   5336: iand
    //   5337: aload_2
    //   5338: ldc_w 'GL_EXT_gpu_shader4'
    //   5341: invokeinterface contains : (Ljava/lang/Object;)Z
    //   5346: ifeq -> 5365
    //   5349: aload_0
    //   5350: ldc_w 'glProgramUniform4uivEXT'
    //   5353: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   5356: dup2_x1
    //   5357: putfield glProgramUniform4uivEXT : J
    //   5360: lconst_0
    //   5361: lcmp
    //   5362: ifeq -> 5369
    //   5365: iconst_1
    //   5366: goto -> 5370
    //   5369: iconst_0
    //   5370: iand
    //   5371: aload_2
    //   5372: ldc_w 'GL_EXT_gpu_program_parameters'
    //   5375: invokeinterface contains : (Ljava/lang/Object;)Z
    //   5380: ifeq -> 5399
    //   5383: aload_0
    //   5384: ldc_w 'glNamedProgramLocalParameters4fvEXT'
    //   5387: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   5390: dup2_x1
    //   5391: putfield glNamedProgramLocalParameters4fvEXT : J
    //   5394: lconst_0
    //   5395: lcmp
    //   5396: ifeq -> 5403
    //   5399: iconst_1
    //   5400: goto -> 5404
    //   5403: iconst_0
    //   5404: iand
    //   5405: aload_2
    //   5406: ldc_w 'GL_NV_gpu_program4'
    //   5409: invokeinterface contains : (Ljava/lang/Object;)Z
    //   5414: ifeq -> 5433
    //   5417: aload_0
    //   5418: ldc_w 'glNamedProgramLocalParameterI4iEXT'
    //   5421: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   5424: dup2_x1
    //   5425: putfield glNamedProgramLocalParameterI4iEXT : J
    //   5428: lconst_0
    //   5429: lcmp
    //   5430: ifeq -> 5437
    //   5433: iconst_1
    //   5434: goto -> 5438
    //   5437: iconst_0
    //   5438: iand
    //   5439: aload_2
    //   5440: ldc_w 'GL_NV_gpu_program4'
    //   5443: invokeinterface contains : (Ljava/lang/Object;)Z
    //   5448: ifeq -> 5467
    //   5451: aload_0
    //   5452: ldc_w 'glNamedProgramLocalParameterI4ivEXT'
    //   5455: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   5458: dup2_x1
    //   5459: putfield glNamedProgramLocalParameterI4ivEXT : J
    //   5462: lconst_0
    //   5463: lcmp
    //   5464: ifeq -> 5471
    //   5467: iconst_1
    //   5468: goto -> 5472
    //   5471: iconst_0
    //   5472: iand
    //   5473: aload_2
    //   5474: ldc_w 'GL_NV_gpu_program4'
    //   5477: invokeinterface contains : (Ljava/lang/Object;)Z
    //   5482: ifeq -> 5501
    //   5485: aload_0
    //   5486: ldc_w 'glNamedProgramLocalParametersI4ivEXT'
    //   5489: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   5492: dup2_x1
    //   5493: putfield glNamedProgramLocalParametersI4ivEXT : J
    //   5496: lconst_0
    //   5497: lcmp
    //   5498: ifeq -> 5505
    //   5501: iconst_1
    //   5502: goto -> 5506
    //   5505: iconst_0
    //   5506: iand
    //   5507: aload_2
    //   5508: ldc_w 'GL_NV_gpu_program4'
    //   5511: invokeinterface contains : (Ljava/lang/Object;)Z
    //   5516: ifeq -> 5535
    //   5519: aload_0
    //   5520: ldc_w 'glNamedProgramLocalParameterI4uiEXT'
    //   5523: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   5526: dup2_x1
    //   5527: putfield glNamedProgramLocalParameterI4uiEXT : J
    //   5530: lconst_0
    //   5531: lcmp
    //   5532: ifeq -> 5539
    //   5535: iconst_1
    //   5536: goto -> 5540
    //   5539: iconst_0
    //   5540: iand
    //   5541: aload_2
    //   5542: ldc_w 'GL_NV_gpu_program4'
    //   5545: invokeinterface contains : (Ljava/lang/Object;)Z
    //   5550: ifeq -> 5569
    //   5553: aload_0
    //   5554: ldc_w 'glNamedProgramLocalParameterI4uivEXT'
    //   5557: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   5560: dup2_x1
    //   5561: putfield glNamedProgramLocalParameterI4uivEXT : J
    //   5564: lconst_0
    //   5565: lcmp
    //   5566: ifeq -> 5573
    //   5569: iconst_1
    //   5570: goto -> 5574
    //   5573: iconst_0
    //   5574: iand
    //   5575: aload_2
    //   5576: ldc_w 'GL_NV_gpu_program4'
    //   5579: invokeinterface contains : (Ljava/lang/Object;)Z
    //   5584: ifeq -> 5603
    //   5587: aload_0
    //   5588: ldc_w 'glNamedProgramLocalParametersI4uivEXT'
    //   5591: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   5594: dup2_x1
    //   5595: putfield glNamedProgramLocalParametersI4uivEXT : J
    //   5598: lconst_0
    //   5599: lcmp
    //   5600: ifeq -> 5607
    //   5603: iconst_1
    //   5604: goto -> 5608
    //   5607: iconst_0
    //   5608: iand
    //   5609: aload_2
    //   5610: ldc_w 'GL_NV_gpu_program4'
    //   5613: invokeinterface contains : (Ljava/lang/Object;)Z
    //   5618: ifeq -> 5637
    //   5621: aload_0
    //   5622: ldc_w 'glGetNamedProgramLocalParameterIivEXT'
    //   5625: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   5628: dup2_x1
    //   5629: putfield glGetNamedProgramLocalParameterIivEXT : J
    //   5632: lconst_0
    //   5633: lcmp
    //   5634: ifeq -> 5641
    //   5637: iconst_1
    //   5638: goto -> 5642
    //   5641: iconst_0
    //   5642: iand
    //   5643: aload_2
    //   5644: ldc_w 'GL_NV_gpu_program4'
    //   5647: invokeinterface contains : (Ljava/lang/Object;)Z
    //   5652: ifeq -> 5671
    //   5655: aload_0
    //   5656: ldc_w 'glGetNamedProgramLocalParameterIuivEXT'
    //   5659: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   5662: dup2_x1
    //   5663: putfield glGetNamedProgramLocalParameterIuivEXT : J
    //   5666: lconst_0
    //   5667: lcmp
    //   5668: ifeq -> 5675
    //   5671: iconst_1
    //   5672: goto -> 5676
    //   5675: iconst_0
    //   5676: iand
    //   5677: aload_2
    //   5678: ldc_w 'OpenGL30'
    //   5681: invokeinterface contains : (Ljava/lang/Object;)Z
    //   5686: ifne -> 5701
    //   5689: aload_2
    //   5690: ldc_w 'GL_EXT_framebuffer_object'
    //   5693: invokeinterface contains : (Ljava/lang/Object;)Z
    //   5698: ifeq -> 5717
    //   5701: aload_0
    //   5702: ldc_w 'glNamedRenderbufferStorageEXT'
    //   5705: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   5708: dup2_x1
    //   5709: putfield glNamedRenderbufferStorageEXT : J
    //   5712: lconst_0
    //   5713: lcmp
    //   5714: ifeq -> 5721
    //   5717: iconst_1
    //   5718: goto -> 5722
    //   5721: iconst_0
    //   5722: iand
    //   5723: aload_2
    //   5724: ldc_w 'OpenGL30'
    //   5727: invokeinterface contains : (Ljava/lang/Object;)Z
    //   5732: ifne -> 5747
    //   5735: aload_2
    //   5736: ldc_w 'GL_EXT_framebuffer_object'
    //   5739: invokeinterface contains : (Ljava/lang/Object;)Z
    //   5744: ifeq -> 5763
    //   5747: aload_0
    //   5748: ldc_w 'glGetNamedRenderbufferParameterivEXT'
    //   5751: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   5754: dup2_x1
    //   5755: putfield glGetNamedRenderbufferParameterivEXT : J
    //   5758: lconst_0
    //   5759: lcmp
    //   5760: ifeq -> 5767
    //   5763: iconst_1
    //   5764: goto -> 5768
    //   5767: iconst_0
    //   5768: iand
    //   5769: aload_2
    //   5770: ldc_w 'OpenGL30'
    //   5773: invokeinterface contains : (Ljava/lang/Object;)Z
    //   5778: ifne -> 5793
    //   5781: aload_2
    //   5782: ldc_w 'GL_EXT_framebuffer_multisample'
    //   5785: invokeinterface contains : (Ljava/lang/Object;)Z
    //   5790: ifeq -> 5809
    //   5793: aload_0
    //   5794: ldc_w 'glNamedRenderbufferStorageMultisampleEXT'
    //   5797: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   5800: dup2_x1
    //   5801: putfield glNamedRenderbufferStorageMultisampleEXT : J
    //   5804: lconst_0
    //   5805: lcmp
    //   5806: ifeq -> 5813
    //   5809: iconst_1
    //   5810: goto -> 5814
    //   5813: iconst_0
    //   5814: iand
    //   5815: aload_2
    //   5816: ldc_w 'GL_NV_framebuffer_multisample_coverage'
    //   5819: invokeinterface contains : (Ljava/lang/Object;)Z
    //   5824: ifeq -> 5843
    //   5827: aload_0
    //   5828: ldc_w 'glNamedRenderbufferStorageMultisampleCoverageEXT'
    //   5831: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   5834: dup2_x1
    //   5835: putfield glNamedRenderbufferStorageMultisampleCoverageEXT : J
    //   5838: lconst_0
    //   5839: lcmp
    //   5840: ifeq -> 5847
    //   5843: iconst_1
    //   5844: goto -> 5848
    //   5847: iconst_0
    //   5848: iand
    //   5849: aload_2
    //   5850: ldc_w 'OpenGL30'
    //   5853: invokeinterface contains : (Ljava/lang/Object;)Z
    //   5858: ifne -> 5873
    //   5861: aload_2
    //   5862: ldc_w 'GL_EXT_framebuffer_object'
    //   5865: invokeinterface contains : (Ljava/lang/Object;)Z
    //   5870: ifeq -> 5889
    //   5873: aload_0
    //   5874: ldc_w 'glCheckNamedFramebufferStatusEXT'
    //   5877: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   5880: dup2_x1
    //   5881: putfield glCheckNamedFramebufferStatusEXT : J
    //   5884: lconst_0
    //   5885: lcmp
    //   5886: ifeq -> 5893
    //   5889: iconst_1
    //   5890: goto -> 5894
    //   5893: iconst_0
    //   5894: iand
    //   5895: aload_2
    //   5896: ldc_w 'OpenGL30'
    //   5899: invokeinterface contains : (Ljava/lang/Object;)Z
    //   5904: ifne -> 5919
    //   5907: aload_2
    //   5908: ldc_w 'GL_EXT_framebuffer_object'
    //   5911: invokeinterface contains : (Ljava/lang/Object;)Z
    //   5916: ifeq -> 5935
    //   5919: aload_0
    //   5920: ldc_w 'glNamedFramebufferTexture1DEXT'
    //   5923: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   5926: dup2_x1
    //   5927: putfield glNamedFramebufferTexture1DEXT : J
    //   5930: lconst_0
    //   5931: lcmp
    //   5932: ifeq -> 5939
    //   5935: iconst_1
    //   5936: goto -> 5940
    //   5939: iconst_0
    //   5940: iand
    //   5941: aload_2
    //   5942: ldc_w 'OpenGL30'
    //   5945: invokeinterface contains : (Ljava/lang/Object;)Z
    //   5950: ifne -> 5965
    //   5953: aload_2
    //   5954: ldc_w 'GL_EXT_framebuffer_object'
    //   5957: invokeinterface contains : (Ljava/lang/Object;)Z
    //   5962: ifeq -> 5981
    //   5965: aload_0
    //   5966: ldc_w 'glNamedFramebufferTexture2DEXT'
    //   5969: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   5972: dup2_x1
    //   5973: putfield glNamedFramebufferTexture2DEXT : J
    //   5976: lconst_0
    //   5977: lcmp
    //   5978: ifeq -> 5985
    //   5981: iconst_1
    //   5982: goto -> 5986
    //   5985: iconst_0
    //   5986: iand
    //   5987: aload_2
    //   5988: ldc_w 'OpenGL30'
    //   5991: invokeinterface contains : (Ljava/lang/Object;)Z
    //   5996: ifne -> 6011
    //   5999: aload_2
    //   6000: ldc_w 'GL_EXT_framebuffer_object'
    //   6003: invokeinterface contains : (Ljava/lang/Object;)Z
    //   6008: ifeq -> 6027
    //   6011: aload_0
    //   6012: ldc_w 'glNamedFramebufferTexture3DEXT'
    //   6015: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   6018: dup2_x1
    //   6019: putfield glNamedFramebufferTexture3DEXT : J
    //   6022: lconst_0
    //   6023: lcmp
    //   6024: ifeq -> 6031
    //   6027: iconst_1
    //   6028: goto -> 6032
    //   6031: iconst_0
    //   6032: iand
    //   6033: aload_2
    //   6034: ldc_w 'OpenGL30'
    //   6037: invokeinterface contains : (Ljava/lang/Object;)Z
    //   6042: ifne -> 6057
    //   6045: aload_2
    //   6046: ldc_w 'GL_EXT_framebuffer_object'
    //   6049: invokeinterface contains : (Ljava/lang/Object;)Z
    //   6054: ifeq -> 6073
    //   6057: aload_0
    //   6058: ldc_w 'glNamedFramebufferRenderbufferEXT'
    //   6061: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   6064: dup2_x1
    //   6065: putfield glNamedFramebufferRenderbufferEXT : J
    //   6068: lconst_0
    //   6069: lcmp
    //   6070: ifeq -> 6077
    //   6073: iconst_1
    //   6074: goto -> 6078
    //   6077: iconst_0
    //   6078: iand
    //   6079: aload_2
    //   6080: ldc_w 'OpenGL30'
    //   6083: invokeinterface contains : (Ljava/lang/Object;)Z
    //   6088: ifne -> 6103
    //   6091: aload_2
    //   6092: ldc_w 'GL_EXT_framebuffer_object'
    //   6095: invokeinterface contains : (Ljava/lang/Object;)Z
    //   6100: ifeq -> 6119
    //   6103: aload_0
    //   6104: ldc_w 'glGetNamedFramebufferAttachmentParameterivEXT'
    //   6107: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   6110: dup2_x1
    //   6111: putfield glGetNamedFramebufferAttachmentParameterivEXT : J
    //   6114: lconst_0
    //   6115: lcmp
    //   6116: ifeq -> 6123
    //   6119: iconst_1
    //   6120: goto -> 6124
    //   6123: iconst_0
    //   6124: iand
    //   6125: aload_2
    //   6126: ldc_w 'OpenGL30'
    //   6129: invokeinterface contains : (Ljava/lang/Object;)Z
    //   6134: ifne -> 6149
    //   6137: aload_2
    //   6138: ldc_w 'GL_EXT_framebuffer_object'
    //   6141: invokeinterface contains : (Ljava/lang/Object;)Z
    //   6146: ifeq -> 6165
    //   6149: aload_0
    //   6150: ldc_w 'glGenerateTextureMipmapEXT'
    //   6153: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   6156: dup2_x1
    //   6157: putfield glGenerateTextureMipmapEXT : J
    //   6160: lconst_0
    //   6161: lcmp
    //   6162: ifeq -> 6169
    //   6165: iconst_1
    //   6166: goto -> 6170
    //   6169: iconst_0
    //   6170: iand
    //   6171: aload_2
    //   6172: ldc_w 'OpenGL30'
    //   6175: invokeinterface contains : (Ljava/lang/Object;)Z
    //   6180: ifne -> 6195
    //   6183: aload_2
    //   6184: ldc_w 'GL_EXT_framebuffer_object'
    //   6187: invokeinterface contains : (Ljava/lang/Object;)Z
    //   6192: ifeq -> 6211
    //   6195: aload_0
    //   6196: ldc_w 'glGenerateMultiTexMipmapEXT'
    //   6199: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   6202: dup2_x1
    //   6203: putfield glGenerateMultiTexMipmapEXT : J
    //   6206: lconst_0
    //   6207: lcmp
    //   6208: ifeq -> 6215
    //   6211: iconst_1
    //   6212: goto -> 6216
    //   6215: iconst_0
    //   6216: iand
    //   6217: aload_2
    //   6218: ldc_w 'OpenGL30'
    //   6221: invokeinterface contains : (Ljava/lang/Object;)Z
    //   6226: ifne -> 6241
    //   6229: aload_2
    //   6230: ldc_w 'GL_EXT_framebuffer_object'
    //   6233: invokeinterface contains : (Ljava/lang/Object;)Z
    //   6238: ifeq -> 6257
    //   6241: aload_0
    //   6242: ldc_w 'glFramebufferDrawBufferEXT'
    //   6245: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   6248: dup2_x1
    //   6249: putfield glFramebufferDrawBufferEXT : J
    //   6252: lconst_0
    //   6253: lcmp
    //   6254: ifeq -> 6261
    //   6257: iconst_1
    //   6258: goto -> 6262
    //   6261: iconst_0
    //   6262: iand
    //   6263: aload_2
    //   6264: ldc_w 'OpenGL30'
    //   6267: invokeinterface contains : (Ljava/lang/Object;)Z
    //   6272: ifne -> 6287
    //   6275: aload_2
    //   6276: ldc_w 'GL_EXT_framebuffer_object'
    //   6279: invokeinterface contains : (Ljava/lang/Object;)Z
    //   6284: ifeq -> 6303
    //   6287: aload_0
    //   6288: ldc_w 'glFramebufferDrawBuffersEXT'
    //   6291: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   6294: dup2_x1
    //   6295: putfield glFramebufferDrawBuffersEXT : J
    //   6298: lconst_0
    //   6299: lcmp
    //   6300: ifeq -> 6307
    //   6303: iconst_1
    //   6304: goto -> 6308
    //   6307: iconst_0
    //   6308: iand
    //   6309: aload_2
    //   6310: ldc_w 'OpenGL30'
    //   6313: invokeinterface contains : (Ljava/lang/Object;)Z
    //   6318: ifne -> 6333
    //   6321: aload_2
    //   6322: ldc_w 'GL_EXT_framebuffer_object'
    //   6325: invokeinterface contains : (Ljava/lang/Object;)Z
    //   6330: ifeq -> 6349
    //   6333: aload_0
    //   6334: ldc_w 'glFramebufferReadBufferEXT'
    //   6337: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   6340: dup2_x1
    //   6341: putfield glFramebufferReadBufferEXT : J
    //   6344: lconst_0
    //   6345: lcmp
    //   6346: ifeq -> 6353
    //   6349: iconst_1
    //   6350: goto -> 6354
    //   6353: iconst_0
    //   6354: iand
    //   6355: aload_2
    //   6356: ldc_w 'OpenGL30'
    //   6359: invokeinterface contains : (Ljava/lang/Object;)Z
    //   6364: ifne -> 6379
    //   6367: aload_2
    //   6368: ldc_w 'GL_EXT_framebuffer_object'
    //   6371: invokeinterface contains : (Ljava/lang/Object;)Z
    //   6376: ifeq -> 6395
    //   6379: aload_0
    //   6380: ldc_w 'glGetFramebufferParameterivEXT'
    //   6383: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   6386: dup2_x1
    //   6387: putfield glGetFramebufferParameterivEXT : J
    //   6390: lconst_0
    //   6391: lcmp
    //   6392: ifeq -> 6399
    //   6395: iconst_1
    //   6396: goto -> 6400
    //   6399: iconst_0
    //   6400: iand
    //   6401: aload_2
    //   6402: ldc_w 'OpenGL31'
    //   6405: invokeinterface contains : (Ljava/lang/Object;)Z
    //   6410: ifne -> 6425
    //   6413: aload_2
    //   6414: ldc_w 'GL_ARB_copy_buffer'
    //   6417: invokeinterface contains : (Ljava/lang/Object;)Z
    //   6422: ifeq -> 6441
    //   6425: aload_0
    //   6426: ldc_w 'glNamedCopyBufferSubDataEXT'
    //   6429: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   6432: dup2_x1
    //   6433: putfield glNamedCopyBufferSubDataEXT : J
    //   6436: lconst_0
    //   6437: lcmp
    //   6438: ifeq -> 6445
    //   6441: iconst_1
    //   6442: goto -> 6446
    //   6445: iconst_0
    //   6446: iand
    //   6447: aload_2
    //   6448: ldc_w 'GL_EXT_geometry_shader4'
    //   6451: invokeinterface contains : (Ljava/lang/Object;)Z
    //   6456: ifne -> 6471
    //   6459: aload_2
    //   6460: ldc_w 'GL_NV_geometry_program4'
    //   6463: invokeinterface contains : (Ljava/lang/Object;)Z
    //   6468: ifeq -> 6487
    //   6471: aload_0
    //   6472: ldc_w 'glNamedFramebufferTextureEXT'
    //   6475: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   6478: dup2_x1
    //   6479: putfield glNamedFramebufferTextureEXT : J
    //   6482: lconst_0
    //   6483: lcmp
    //   6484: ifeq -> 6491
    //   6487: iconst_1
    //   6488: goto -> 6492
    //   6491: iconst_0
    //   6492: iand
    //   6493: aload_2
    //   6494: ldc_w 'GL_EXT_geometry_shader4'
    //   6497: invokeinterface contains : (Ljava/lang/Object;)Z
    //   6502: ifne -> 6517
    //   6505: aload_2
    //   6506: ldc_w 'GL_NV_geometry_program4'
    //   6509: invokeinterface contains : (Ljava/lang/Object;)Z
    //   6514: ifeq -> 6533
    //   6517: aload_0
    //   6518: ldc_w 'glNamedFramebufferTextureLayerEXT'
    //   6521: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   6524: dup2_x1
    //   6525: putfield glNamedFramebufferTextureLayerEXT : J
    //   6528: lconst_0
    //   6529: lcmp
    //   6530: ifeq -> 6537
    //   6533: iconst_1
    //   6534: goto -> 6538
    //   6537: iconst_0
    //   6538: iand
    //   6539: aload_2
    //   6540: ldc_w 'GL_EXT_geometry_shader4'
    //   6543: invokeinterface contains : (Ljava/lang/Object;)Z
    //   6548: ifne -> 6563
    //   6551: aload_2
    //   6552: ldc_w 'GL_NV_geometry_program4'
    //   6555: invokeinterface contains : (Ljava/lang/Object;)Z
    //   6560: ifeq -> 6579
    //   6563: aload_0
    //   6564: ldc_w 'glNamedFramebufferTextureFaceEXT'
    //   6567: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   6570: dup2_x1
    //   6571: putfield glNamedFramebufferTextureFaceEXT : J
    //   6574: lconst_0
    //   6575: lcmp
    //   6576: ifeq -> 6583
    //   6579: iconst_1
    //   6580: goto -> 6584
    //   6583: iconst_0
    //   6584: iand
    //   6585: aload_2
    //   6586: ldc_w 'GL_NV_explicit_multisample'
    //   6589: invokeinterface contains : (Ljava/lang/Object;)Z
    //   6594: ifeq -> 6613
    //   6597: aload_0
    //   6598: ldc_w 'glTextureRenderbufferEXT'
    //   6601: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   6604: dup2_x1
    //   6605: putfield glTextureRenderbufferEXT : J
    //   6608: lconst_0
    //   6609: lcmp
    //   6610: ifeq -> 6617
    //   6613: iconst_1
    //   6614: goto -> 6618
    //   6617: iconst_0
    //   6618: iand
    //   6619: aload_2
    //   6620: ldc_w 'GL_NV_explicit_multisample'
    //   6623: invokeinterface contains : (Ljava/lang/Object;)Z
    //   6628: ifeq -> 6647
    //   6631: aload_0
    //   6632: ldc_w 'glMultiTexRenderbufferEXT'
    //   6635: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   6638: dup2_x1
    //   6639: putfield glMultiTexRenderbufferEXT : J
    //   6642: lconst_0
    //   6643: lcmp
    //   6644: ifeq -> 6651
    //   6647: iconst_1
    //   6648: goto -> 6652
    //   6651: iconst_0
    //   6652: iand
    //   6653: iload_1
    //   6654: ifne -> 6685
    //   6657: aload_2
    //   6658: ldc_w 'OpenGL30'
    //   6661: invokeinterface contains : (Ljava/lang/Object;)Z
    //   6666: ifeq -> 6685
    //   6669: aload_0
    //   6670: ldc_w 'glVertexArrayVertexOffsetEXT'
    //   6673: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   6676: dup2_x1
    //   6677: putfield glVertexArrayVertexOffsetEXT : J
    //   6680: lconst_0
    //   6681: lcmp
    //   6682: ifeq -> 6689
    //   6685: iconst_1
    //   6686: goto -> 6690
    //   6689: iconst_0
    //   6690: iand
    //   6691: iload_1
    //   6692: ifne -> 6723
    //   6695: aload_2
    //   6696: ldc_w 'OpenGL30'
    //   6699: invokeinterface contains : (Ljava/lang/Object;)Z
    //   6704: ifeq -> 6723
    //   6707: aload_0
    //   6708: ldc_w 'glVertexArrayColorOffsetEXT'
    //   6711: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   6714: dup2_x1
    //   6715: putfield glVertexArrayColorOffsetEXT : J
    //   6718: lconst_0
    //   6719: lcmp
    //   6720: ifeq -> 6727
    //   6723: iconst_1
    //   6724: goto -> 6728
    //   6727: iconst_0
    //   6728: iand
    //   6729: iload_1
    //   6730: ifne -> 6761
    //   6733: aload_2
    //   6734: ldc_w 'OpenGL30'
    //   6737: invokeinterface contains : (Ljava/lang/Object;)Z
    //   6742: ifeq -> 6761
    //   6745: aload_0
    //   6746: ldc_w 'glVertexArrayEdgeFlagOffsetEXT'
    //   6749: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   6752: dup2_x1
    //   6753: putfield glVertexArrayEdgeFlagOffsetEXT : J
    //   6756: lconst_0
    //   6757: lcmp
    //   6758: ifeq -> 6765
    //   6761: iconst_1
    //   6762: goto -> 6766
    //   6765: iconst_0
    //   6766: iand
    //   6767: aload_2
    //   6768: ldc_w 'OpenGL30'
    //   6771: invokeinterface contains : (Ljava/lang/Object;)Z
    //   6776: ifeq -> 6795
    //   6779: aload_0
    //   6780: ldc_w 'glVertexArrayIndexOffsetEXT'
    //   6783: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   6786: dup2_x1
    //   6787: putfield glVertexArrayIndexOffsetEXT : J
    //   6790: lconst_0
    //   6791: lcmp
    //   6792: ifeq -> 6799
    //   6795: iconst_1
    //   6796: goto -> 6800
    //   6799: iconst_0
    //   6800: iand
    //   6801: iload_1
    //   6802: ifne -> 6833
    //   6805: aload_2
    //   6806: ldc_w 'OpenGL30'
    //   6809: invokeinterface contains : (Ljava/lang/Object;)Z
    //   6814: ifeq -> 6833
    //   6817: aload_0
    //   6818: ldc_w 'glVertexArrayNormalOffsetEXT'
    //   6821: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   6824: dup2_x1
    //   6825: putfield glVertexArrayNormalOffsetEXT : J
    //   6828: lconst_0
    //   6829: lcmp
    //   6830: ifeq -> 6837
    //   6833: iconst_1
    //   6834: goto -> 6838
    //   6837: iconst_0
    //   6838: iand
    //   6839: iload_1
    //   6840: ifne -> 6871
    //   6843: aload_2
    //   6844: ldc_w 'OpenGL30'
    //   6847: invokeinterface contains : (Ljava/lang/Object;)Z
    //   6852: ifeq -> 6871
    //   6855: aload_0
    //   6856: ldc_w 'glVertexArrayTexCoordOffsetEXT'
    //   6859: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   6862: dup2_x1
    //   6863: putfield glVertexArrayTexCoordOffsetEXT : J
    //   6866: lconst_0
    //   6867: lcmp
    //   6868: ifeq -> 6875
    //   6871: iconst_1
    //   6872: goto -> 6876
    //   6875: iconst_0
    //   6876: iand
    //   6877: iload_1
    //   6878: ifne -> 6909
    //   6881: aload_2
    //   6882: ldc_w 'OpenGL30'
    //   6885: invokeinterface contains : (Ljava/lang/Object;)Z
    //   6890: ifeq -> 6909
    //   6893: aload_0
    //   6894: ldc_w 'glVertexArrayMultiTexCoordOffsetEXT'
    //   6897: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   6900: dup2_x1
    //   6901: putfield glVertexArrayMultiTexCoordOffsetEXT : J
    //   6904: lconst_0
    //   6905: lcmp
    //   6906: ifeq -> 6913
    //   6909: iconst_1
    //   6910: goto -> 6914
    //   6913: iconst_0
    //   6914: iand
    //   6915: iload_1
    //   6916: ifne -> 6947
    //   6919: aload_2
    //   6920: ldc_w 'OpenGL30'
    //   6923: invokeinterface contains : (Ljava/lang/Object;)Z
    //   6928: ifeq -> 6947
    //   6931: aload_0
    //   6932: ldc_w 'glVertexArrayFogCoordOffsetEXT'
    //   6935: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   6938: dup2_x1
    //   6939: putfield glVertexArrayFogCoordOffsetEXT : J
    //   6942: lconst_0
    //   6943: lcmp
    //   6944: ifeq -> 6951
    //   6947: iconst_1
    //   6948: goto -> 6952
    //   6951: iconst_0
    //   6952: iand
    //   6953: iload_1
    //   6954: ifne -> 6985
    //   6957: aload_2
    //   6958: ldc_w 'OpenGL30'
    //   6961: invokeinterface contains : (Ljava/lang/Object;)Z
    //   6966: ifeq -> 6985
    //   6969: aload_0
    //   6970: ldc_w 'glVertexArraySecondaryColorOffsetEXT'
    //   6973: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   6976: dup2_x1
    //   6977: putfield glVertexArraySecondaryColorOffsetEXT : J
    //   6980: lconst_0
    //   6981: lcmp
    //   6982: ifeq -> 6989
    //   6985: iconst_1
    //   6986: goto -> 6990
    //   6989: iconst_0
    //   6990: iand
    //   6991: aload_2
    //   6992: ldc_w 'OpenGL30'
    //   6995: invokeinterface contains : (Ljava/lang/Object;)Z
    //   7000: ifeq -> 7019
    //   7003: aload_0
    //   7004: ldc_w 'glVertexArrayVertexAttribOffsetEXT'
    //   7007: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   7010: dup2_x1
    //   7011: putfield glVertexArrayVertexAttribOffsetEXT : J
    //   7014: lconst_0
    //   7015: lcmp
    //   7016: ifeq -> 7023
    //   7019: iconst_1
    //   7020: goto -> 7024
    //   7023: iconst_0
    //   7024: iand
    //   7025: aload_2
    //   7026: ldc_w 'OpenGL30'
    //   7029: invokeinterface contains : (Ljava/lang/Object;)Z
    //   7034: ifeq -> 7053
    //   7037: aload_0
    //   7038: ldc_w 'glVertexArrayVertexAttribIOffsetEXT'
    //   7041: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   7044: dup2_x1
    //   7045: putfield glVertexArrayVertexAttribIOffsetEXT : J
    //   7048: lconst_0
    //   7049: lcmp
    //   7050: ifeq -> 7057
    //   7053: iconst_1
    //   7054: goto -> 7058
    //   7057: iconst_0
    //   7058: iand
    //   7059: aload_2
    //   7060: ldc_w 'OpenGL30'
    //   7063: invokeinterface contains : (Ljava/lang/Object;)Z
    //   7068: ifeq -> 7087
    //   7071: aload_0
    //   7072: ldc_w 'glEnableVertexArrayEXT'
    //   7075: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   7078: dup2_x1
    //   7079: putfield glEnableVertexArrayEXT : J
    //   7082: lconst_0
    //   7083: lcmp
    //   7084: ifeq -> 7091
    //   7087: iconst_1
    //   7088: goto -> 7092
    //   7091: iconst_0
    //   7092: iand
    //   7093: aload_2
    //   7094: ldc_w 'OpenGL30'
    //   7097: invokeinterface contains : (Ljava/lang/Object;)Z
    //   7102: ifeq -> 7121
    //   7105: aload_0
    //   7106: ldc_w 'glDisableVertexArrayEXT'
    //   7109: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   7112: dup2_x1
    //   7113: putfield glDisableVertexArrayEXT : J
    //   7116: lconst_0
    //   7117: lcmp
    //   7118: ifeq -> 7125
    //   7121: iconst_1
    //   7122: goto -> 7126
    //   7125: iconst_0
    //   7126: iand
    //   7127: aload_2
    //   7128: ldc_w 'OpenGL30'
    //   7131: invokeinterface contains : (Ljava/lang/Object;)Z
    //   7136: ifeq -> 7155
    //   7139: aload_0
    //   7140: ldc_w 'glEnableVertexArrayAttribEXT'
    //   7143: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   7146: dup2_x1
    //   7147: putfield glEnableVertexArrayAttribEXT : J
    //   7150: lconst_0
    //   7151: lcmp
    //   7152: ifeq -> 7159
    //   7155: iconst_1
    //   7156: goto -> 7160
    //   7159: iconst_0
    //   7160: iand
    //   7161: aload_2
    //   7162: ldc_w 'OpenGL30'
    //   7165: invokeinterface contains : (Ljava/lang/Object;)Z
    //   7170: ifeq -> 7189
    //   7173: aload_0
    //   7174: ldc_w 'glDisableVertexArrayAttribEXT'
    //   7177: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   7180: dup2_x1
    //   7181: putfield glDisableVertexArrayAttribEXT : J
    //   7184: lconst_0
    //   7185: lcmp
    //   7186: ifeq -> 7193
    //   7189: iconst_1
    //   7190: goto -> 7194
    //   7193: iconst_0
    //   7194: iand
    //   7195: aload_2
    //   7196: ldc_w 'OpenGL30'
    //   7199: invokeinterface contains : (Ljava/lang/Object;)Z
    //   7204: ifeq -> 7223
    //   7207: aload_0
    //   7208: ldc_w 'glGetVertexArrayIntegervEXT'
    //   7211: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   7214: dup2_x1
    //   7215: putfield glGetVertexArrayIntegervEXT : J
    //   7218: lconst_0
    //   7219: lcmp
    //   7220: ifeq -> 7227
    //   7223: iconst_1
    //   7224: goto -> 7228
    //   7227: iconst_0
    //   7228: iand
    //   7229: aload_2
    //   7230: ldc_w 'OpenGL30'
    //   7233: invokeinterface contains : (Ljava/lang/Object;)Z
    //   7238: ifeq -> 7257
    //   7241: aload_0
    //   7242: ldc_w 'glGetVertexArrayPointervEXT'
    //   7245: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   7248: dup2_x1
    //   7249: putfield glGetVertexArrayPointervEXT : J
    //   7252: lconst_0
    //   7253: lcmp
    //   7254: ifeq -> 7261
    //   7257: iconst_1
    //   7258: goto -> 7262
    //   7261: iconst_0
    //   7262: iand
    //   7263: aload_2
    //   7264: ldc_w 'OpenGL30'
    //   7267: invokeinterface contains : (Ljava/lang/Object;)Z
    //   7272: ifeq -> 7291
    //   7275: aload_0
    //   7276: ldc_w 'glGetVertexArrayIntegeri_vEXT'
    //   7279: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   7282: dup2_x1
    //   7283: putfield glGetVertexArrayIntegeri_vEXT : J
    //   7286: lconst_0
    //   7287: lcmp
    //   7288: ifeq -> 7295
    //   7291: iconst_1
    //   7292: goto -> 7296
    //   7295: iconst_0
    //   7296: iand
    //   7297: aload_2
    //   7298: ldc_w 'OpenGL30'
    //   7301: invokeinterface contains : (Ljava/lang/Object;)Z
    //   7306: ifeq -> 7325
    //   7309: aload_0
    //   7310: ldc_w 'glGetVertexArrayPointeri_vEXT'
    //   7313: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   7316: dup2_x1
    //   7317: putfield glGetVertexArrayPointeri_vEXT : J
    //   7320: lconst_0
    //   7321: lcmp
    //   7322: ifeq -> 7329
    //   7325: iconst_1
    //   7326: goto -> 7330
    //   7329: iconst_0
    //   7330: iand
    //   7331: aload_2
    //   7332: ldc_w 'OpenGL30'
    //   7335: invokeinterface contains : (Ljava/lang/Object;)Z
    //   7340: ifeq -> 7359
    //   7343: aload_0
    //   7344: ldc_w 'glMapNamedBufferRangeEXT'
    //   7347: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   7350: dup2_x1
    //   7351: putfield glMapNamedBufferRangeEXT : J
    //   7354: lconst_0
    //   7355: lcmp
    //   7356: ifeq -> 7363
    //   7359: iconst_1
    //   7360: goto -> 7364
    //   7363: iconst_0
    //   7364: iand
    //   7365: aload_2
    //   7366: ldc_w 'OpenGL30'
    //   7369: invokeinterface contains : (Ljava/lang/Object;)Z
    //   7374: ifeq -> 7393
    //   7377: aload_0
    //   7378: ldc_w 'glFlushMappedNamedBufferRangeEXT'
    //   7381: invokestatic getFunctionAddress : (Ljava/lang/String;)J
    //   7384: dup2_x1
    //   7385: putfield glFlushMappedNamedBufferRangeEXT : J
    //   7388: lconst_0
    //   7389: lcmp
    //   7390: ifeq -> 7397
    //   7393: iconst_1
    //   7394: goto -> 7398
    //   7397: iconst_0
    //   7398: iand
    //   7399: ireturn
    // Line number table:
    //   Java source line number -> byte code offset
    //   #3850	-> 0
    // Local variable table:
    //   start	length	slot	name	descriptor
    //   0	7400	0	this	Lorg/lwjgl/opengl/ContextCapabilities;
    //   0	7400	1	forwardCompatible	Z
    //   0	7400	2	supported_extensions	Ljava/util/Set;
    // Local variable type table:
    //   start	length	slot	name	signature
    //   0	7400	2	supported_extensions	Ljava/util/Set<Ljava/lang/String;>;
  }
  
  private boolean EXT_draw_buffers2_initNativeFunctionAddresses() {
    return (((this.glColorMaskIndexedEXT = GLContext.getFunctionAddress("glColorMaskIndexedEXT")) != 0L)) & (((this.glGetBooleanIndexedvEXT = GLContext.getFunctionAddress("glGetBooleanIndexedvEXT")) != 0L)) & (((this.glGetIntegerIndexedvEXT = GLContext.getFunctionAddress("glGetIntegerIndexedvEXT")) != 0L) ? 1 : 0) & (((this.glEnableIndexedEXT = GLContext.getFunctionAddress("glEnableIndexedEXT")) != 0L) ? 1 : 0) & (((this.glDisableIndexedEXT = GLContext.getFunctionAddress("glDisableIndexedEXT")) != 0L) ? 1 : 0) & (((this.glIsEnabledIndexedEXT = GLContext.getFunctionAddress("glIsEnabledIndexedEXT")) != 0L) ? 1 : 0);
  }
  
  private boolean EXT_draw_instanced_initNativeFunctionAddresses() {
    return (((this.glDrawArraysInstancedEXT = GLContext.getFunctionAddress("glDrawArraysInstancedEXT")) != 0L)) & (((this.glDrawElementsInstancedEXT = GLContext.getFunctionAddress("glDrawElementsInstancedEXT")) != 0L));
  }
  
  private boolean EXT_draw_range_elements_initNativeFunctionAddresses() {
    return ((this.glDrawRangeElementsEXT = GLContext.getFunctionAddress("glDrawRangeElementsEXT")) != 0L);
  }
  
  private boolean EXT_fog_coord_initNativeFunctionAddresses() {
    return (((this.glFogCoordfEXT = GLContext.getFunctionAddress("glFogCoordfEXT")) != 0L)) & (((this.glFogCoorddEXT = GLContext.getFunctionAddress("glFogCoorddEXT")) != 0L)) & (((this.glFogCoordPointerEXT = GLContext.getFunctionAddress("glFogCoordPointerEXT")) != 0L) ? 1 : 0);
  }
  
  private boolean EXT_framebuffer_blit_initNativeFunctionAddresses() {
    return ((this.glBlitFramebufferEXT = GLContext.getFunctionAddress("glBlitFramebufferEXT")) != 0L);
  }
  
  private boolean EXT_framebuffer_multisample_initNativeFunctionAddresses() {
    return ((this.glRenderbufferStorageMultisampleEXT = GLContext.getFunctionAddress("glRenderbufferStorageMultisampleEXT")) != 0L);
  }
  
  private boolean EXT_framebuffer_object_initNativeFunctionAddresses() {
    return (((this.glIsRenderbufferEXT = GLContext.getFunctionAddress("glIsRenderbufferEXT")) != 0L)) & (((this.glBindRenderbufferEXT = GLContext.getFunctionAddress("glBindRenderbufferEXT")) != 0L)) & (((this.glDeleteRenderbuffersEXT = GLContext.getFunctionAddress("glDeleteRenderbuffersEXT")) != 0L) ? 1 : 0) & (((this.glGenRenderbuffersEXT = GLContext.getFunctionAddress("glGenRenderbuffersEXT")) != 0L) ? 1 : 0) & (((this.glRenderbufferStorageEXT = GLContext.getFunctionAddress("glRenderbufferStorageEXT")) != 0L) ? 1 : 0) & (((this.glGetRenderbufferParameterivEXT = GLContext.getFunctionAddress("glGetRenderbufferParameterivEXT")) != 0L) ? 1 : 0) & (((this.glIsFramebufferEXT = GLContext.getFunctionAddress("glIsFramebufferEXT")) != 0L) ? 1 : 0) & (((this.glBindFramebufferEXT = GLContext.getFunctionAddress("glBindFramebufferEXT")) != 0L) ? 1 : 0) & (((this.glDeleteFramebuffersEXT = GLContext.getFunctionAddress("glDeleteFramebuffersEXT")) != 0L) ? 1 : 0) & (((this.glGenFramebuffersEXT = GLContext.getFunctionAddress("glGenFramebuffersEXT")) != 0L) ? 1 : 0) & (((this.glCheckFramebufferStatusEXT = GLContext.getFunctionAddress("glCheckFramebufferStatusEXT")) != 0L) ? 1 : 0) & (((this.glFramebufferTexture1DEXT = GLContext.getFunctionAddress("glFramebufferTexture1DEXT")) != 0L) ? 1 : 0) & (((this.glFramebufferTexture2DEXT = GLContext.getFunctionAddress("glFramebufferTexture2DEXT")) != 0L) ? 1 : 0) & (((this.glFramebufferTexture3DEXT = GLContext.getFunctionAddress("glFramebufferTexture3DEXT")) != 0L) ? 1 : 0) & (((this.glFramebufferRenderbufferEXT = GLContext.getFunctionAddress("glFramebufferRenderbufferEXT")) != 0L) ? 1 : 0) & (((this.glGetFramebufferAttachmentParameterivEXT = GLContext.getFunctionAddress("glGetFramebufferAttachmentParameterivEXT")) != 0L) ? 1 : 0) & (((this.glGenerateMipmapEXT = GLContext.getFunctionAddress("glGenerateMipmapEXT")) != 0L) ? 1 : 0);
  }
  
  private boolean EXT_geometry_shader4_initNativeFunctionAddresses() {
    return (((this.glProgramParameteriEXT = GLContext.getFunctionAddress("glProgramParameteriEXT")) != 0L)) & (((this.glFramebufferTextureEXT = GLContext.getFunctionAddress("glFramebufferTextureEXT")) != 0L)) & (((this.glFramebufferTextureLayerEXT = GLContext.getFunctionAddress("glFramebufferTextureLayerEXT")) != 0L) ? 1 : 0) & (((this.glFramebufferTextureFaceEXT = GLContext.getFunctionAddress("glFramebufferTextureFaceEXT")) != 0L) ? 1 : 0);
  }
  
  private boolean EXT_gpu_program_parameters_initNativeFunctionAddresses() {
    return (((this.glProgramEnvParameters4fvEXT = GLContext.getFunctionAddress("glProgramEnvParameters4fvEXT")) != 0L)) & (((this.glProgramLocalParameters4fvEXT = GLContext.getFunctionAddress("glProgramLocalParameters4fvEXT")) != 0L));
  }
  
  private boolean EXT_gpu_shader4_initNativeFunctionAddresses() {
    return (((this.glVertexAttribI1iEXT = GLContext.getFunctionAddress("glVertexAttribI1iEXT")) != 0L)) & (((this.glVertexAttribI2iEXT = GLContext.getFunctionAddress("glVertexAttribI2iEXT")) != 0L)) & (((this.glVertexAttribI3iEXT = GLContext.getFunctionAddress("glVertexAttribI3iEXT")) != 0L) ? 1 : 0) & (((this.glVertexAttribI4iEXT = GLContext.getFunctionAddress("glVertexAttribI4iEXT")) != 0L) ? 1 : 0) & (((this.glVertexAttribI1uiEXT = GLContext.getFunctionAddress("glVertexAttribI1uiEXT")) != 0L) ? 1 : 0) & (((this.glVertexAttribI2uiEXT = GLContext.getFunctionAddress("glVertexAttribI2uiEXT")) != 0L) ? 1 : 0) & (((this.glVertexAttribI3uiEXT = GLContext.getFunctionAddress("glVertexAttribI3uiEXT")) != 0L) ? 1 : 0) & (((this.glVertexAttribI4uiEXT = GLContext.getFunctionAddress("glVertexAttribI4uiEXT")) != 0L) ? 1 : 0) & (((this.glVertexAttribI1ivEXT = GLContext.getFunctionAddress("glVertexAttribI1ivEXT")) != 0L) ? 1 : 0) & (((this.glVertexAttribI2ivEXT = GLContext.getFunctionAddress("glVertexAttribI2ivEXT")) != 0L) ? 1 : 0) & (((this.glVertexAttribI3ivEXT = GLContext.getFunctionAddress("glVertexAttribI3ivEXT")) != 0L) ? 1 : 0) & (((this.glVertexAttribI4ivEXT = GLContext.getFunctionAddress("glVertexAttribI4ivEXT")) != 0L) ? 1 : 0) & (((this.glVertexAttribI1uivEXT = GLContext.getFunctionAddress("glVertexAttribI1uivEXT")) != 0L) ? 1 : 0) & (((this.glVertexAttribI2uivEXT = GLContext.getFunctionAddress("glVertexAttribI2uivEXT")) != 0L) ? 1 : 0) & (((this.glVertexAttribI3uivEXT = GLContext.getFunctionAddress("glVertexAttribI3uivEXT")) != 0L) ? 1 : 0) & (((this.glVertexAttribI4uivEXT = GLContext.getFunctionAddress("glVertexAttribI4uivEXT")) != 0L) ? 1 : 0) & (((this.glVertexAttribI4bvEXT = GLContext.getFunctionAddress("glVertexAttribI4bvEXT")) != 0L) ? 1 : 0) & (((this.glVertexAttribI4svEXT = GLContext.getFunctionAddress("glVertexAttribI4svEXT")) != 0L) ? 1 : 0) & (((this.glVertexAttribI4ubvEXT = GLContext.getFunctionAddress("glVertexAttribI4ubvEXT")) != 0L) ? 1 : 0) & (((this.glVertexAttribI4usvEXT = GLContext.getFunctionAddress("glVertexAttribI4usvEXT")) != 0L) ? 1 : 0) & (((this.glVertexAttribIPointerEXT = GLContext.getFunctionAddress("glVertexAttribIPointerEXT")) != 0L) ? 1 : 0) & (((this.glGetVertexAttribIivEXT = GLContext.getFunctionAddress("glGetVertexAttribIivEXT")) != 0L) ? 1 : 0) & (((this.glGetVertexAttribIuivEXT = GLContext.getFunctionAddress("glGetVertexAttribIuivEXT")) != 0L) ? 1 : 0) & (((this.glUniform1uiEXT = GLContext.getFunctionAddress("glUniform1uiEXT")) != 0L) ? 1 : 0) & (((this.glUniform2uiEXT = GLContext.getFunctionAddress("glUniform2uiEXT")) != 0L) ? 1 : 0) & (((this.glUniform3uiEXT = GLContext.getFunctionAddress("glUniform3uiEXT")) != 0L) ? 1 : 0) & (((this.glUniform4uiEXT = GLContext.getFunctionAddress("glUniform4uiEXT")) != 0L) ? 1 : 0) & (((this.glUniform1uivEXT = GLContext.getFunctionAddress("glUniform1uivEXT")) != 0L) ? 1 : 0) & (((this.glUniform2uivEXT = GLContext.getFunctionAddress("glUniform2uivEXT")) != 0L) ? 1 : 0) & (((this.glUniform3uivEXT = GLContext.getFunctionAddress("glUniform3uivEXT")) != 0L) ? 1 : 0) & (((this.glUniform4uivEXT = GLContext.getFunctionAddress("glUniform4uivEXT")) != 0L) ? 1 : 0) & (((this.glGetUniformuivEXT = GLContext.getFunctionAddress("glGetUniformuivEXT")) != 0L) ? 1 : 0) & (((this.glBindFragDataLocationEXT = GLContext.getFunctionAddress("glBindFragDataLocationEXT")) != 0L) ? 1 : 0) & (((this.glGetFragDataLocationEXT = GLContext.getFunctionAddress("glGetFragDataLocationEXT")) != 0L) ? 1 : 0);
  }
  
  private boolean EXT_multi_draw_arrays_initNativeFunctionAddresses() {
    return ((this.glMultiDrawArraysEXT = GLContext.getFunctionAddress("glMultiDrawArraysEXT")) != 0L);
  }
  
  private boolean EXT_paletted_texture_initNativeFunctionAddresses() {
    return (((this.glColorTableEXT = GLContext.getFunctionAddress("glColorTableEXT")) != 0L)) & (((this.glColorSubTableEXT = GLContext.getFunctionAddress("glColorSubTableEXT")) != 0L)) & (((this.glGetColorTableEXT = GLContext.getFunctionAddress("glGetColorTableEXT")) != 0L) ? 1 : 0) & (((this.glGetColorTableParameterivEXT = GLContext.getFunctionAddress("glGetColorTableParameterivEXT")) != 0L) ? 1 : 0) & (((this.glGetColorTableParameterfvEXT = GLContext.getFunctionAddress("glGetColorTableParameterfvEXT")) != 0L) ? 1 : 0);
  }
  
  private boolean EXT_point_parameters_initNativeFunctionAddresses() {
    return (((this.glPointParameterfEXT = GLContext.getFunctionAddress("glPointParameterfEXT")) != 0L)) & (((this.glPointParameterfvEXT = GLContext.getFunctionAddress("glPointParameterfvEXT")) != 0L));
  }
  
  private boolean EXT_provoking_vertex_initNativeFunctionAddresses() {
    return ((this.glProvokingVertexEXT = GLContext.getFunctionAddress("glProvokingVertexEXT")) != 0L);
  }
  
  private boolean EXT_secondary_color_initNativeFunctionAddresses() {
    return (((this.glSecondaryColor3bEXT = GLContext.getFunctionAddress("glSecondaryColor3bEXT")) != 0L)) & (((this.glSecondaryColor3fEXT = GLContext.getFunctionAddress("glSecondaryColor3fEXT")) != 0L)) & (((this.glSecondaryColor3dEXT = GLContext.getFunctionAddress("glSecondaryColor3dEXT")) != 0L) ? 1 : 0) & (((this.glSecondaryColor3ubEXT = GLContext.getFunctionAddress("glSecondaryColor3ubEXT")) != 0L) ? 1 : 0) & (((this.glSecondaryColorPointerEXT = GLContext.getFunctionAddress("glSecondaryColorPointerEXT")) != 0L) ? 1 : 0);
  }
  
  private boolean EXT_separate_shader_objects_initNativeFunctionAddresses() {
    return (((this.glUseShaderProgramEXT = GLContext.getFunctionAddress("glUseShaderProgramEXT")) != 0L)) & (((this.glActiveProgramEXT = GLContext.getFunctionAddress("glActiveProgramEXT")) != 0L)) & (((this.glCreateShaderProgramEXT = GLContext.getFunctionAddress("glCreateShaderProgramEXT")) != 0L) ? 1 : 0);
  }
  
  private boolean EXT_shader_image_load_store_initNativeFunctionAddresses() {
    return (((this.glBindImageTextureEXT = GLContext.getFunctionAddress("glBindImageTextureEXT")) != 0L)) & (((this.glMemoryBarrierEXT = GLContext.getFunctionAddress("glMemoryBarrierEXT")) != 0L));
  }
  
  private boolean EXT_stencil_clear_tag_initNativeFunctionAddresses() {
    return ((this.glStencilClearTagEXT = GLContext.getFunctionAddress("glStencilClearTagEXT")) != 0L);
  }
  
  private boolean EXT_stencil_two_side_initNativeFunctionAddresses() {
    return ((this.glActiveStencilFaceEXT = GLContext.getFunctionAddress("glActiveStencilFaceEXT")) != 0L);
  }
  
  private boolean EXT_texture_array_initNativeFunctionAddresses() {
    return ((this.glFramebufferTextureLayerEXT = GLContext.getFunctionAddress("glFramebufferTextureLayerEXT")) != 0L);
  }
  
  private boolean EXT_texture_buffer_object_initNativeFunctionAddresses() {
    return ((this.glTexBufferEXT = GLContext.getFunctionAddress("glTexBufferEXT")) != 0L);
  }
  
  private boolean EXT_texture_integer_initNativeFunctionAddresses() {
    return (((this.glClearColorIiEXT = GLContext.getFunctionAddress("glClearColorIiEXT")) != 0L)) & (((this.glClearColorIuiEXT = GLContext.getFunctionAddress("glClearColorIuiEXT")) != 0L)) & (((this.glTexParameterIivEXT = GLContext.getFunctionAddress("glTexParameterIivEXT")) != 0L) ? 1 : 0) & (((this.glTexParameterIuivEXT = GLContext.getFunctionAddress("glTexParameterIuivEXT")) != 0L) ? 1 : 0) & (((this.glGetTexParameterIivEXT = GLContext.getFunctionAddress("glGetTexParameterIivEXT")) != 0L) ? 1 : 0) & (((this.glGetTexParameterIuivEXT = GLContext.getFunctionAddress("glGetTexParameterIuivEXT")) != 0L) ? 1 : 0);
  }
  
  private boolean EXT_timer_query_initNativeFunctionAddresses() {
    return (((this.glGetQueryObjecti64vEXT = GLContext.getFunctionAddress("glGetQueryObjecti64vEXT")) != 0L)) & (((this.glGetQueryObjectui64vEXT = GLContext.getFunctionAddress("glGetQueryObjectui64vEXT")) != 0L));
  }
  
  private boolean EXT_transform_feedback_initNativeFunctionAddresses() {
    return (((this.glBindBufferRangeEXT = GLContext.getFunctionAddress("glBindBufferRangeEXT")) != 0L)) & (((this.glBindBufferOffsetEXT = GLContext.getFunctionAddress("glBindBufferOffsetEXT")) != 0L)) & (((this.glBindBufferBaseEXT = GLContext.getFunctionAddress("glBindBufferBaseEXT")) != 0L) ? 1 : 0) & (((this.glBeginTransformFeedbackEXT = GLContext.getFunctionAddress("glBeginTransformFeedbackEXT")) != 0L) ? 1 : 0) & (((this.glEndTransformFeedbackEXT = GLContext.getFunctionAddress("glEndTransformFeedbackEXT")) != 0L) ? 1 : 0) & (((this.glTransformFeedbackVaryingsEXT = GLContext.getFunctionAddress("glTransformFeedbackVaryingsEXT")) != 0L) ? 1 : 0) & (((this.glGetTransformFeedbackVaryingEXT = GLContext.getFunctionAddress("glGetTransformFeedbackVaryingEXT")) != 0L) ? 1 : 0);
  }
  
  private boolean EXT_vertex_attrib_64bit_initNativeFunctionAddresses(Set<String> supported_extensions) {
    return (((this.glVertexAttribL1dEXT = GLContext.getFunctionAddress("glVertexAttribL1dEXT")) != 0L)) & (((this.glVertexAttribL2dEXT = GLContext.getFunctionAddress("glVertexAttribL2dEXT")) != 0L)) & (((this.glVertexAttribL3dEXT = GLContext.getFunctionAddress("glVertexAttribL3dEXT")) != 0L) ? 1 : 0) & (((this.glVertexAttribL4dEXT = GLContext.getFunctionAddress("glVertexAttribL4dEXT")) != 0L) ? 1 : 0) & (((this.glVertexAttribL1dvEXT = GLContext.getFunctionAddress("glVertexAttribL1dvEXT")) != 0L) ? 1 : 0) & (((this.glVertexAttribL2dvEXT = GLContext.getFunctionAddress("glVertexAttribL2dvEXT")) != 0L) ? 1 : 0) & (((this.glVertexAttribL3dvEXT = GLContext.getFunctionAddress("glVertexAttribL3dvEXT")) != 0L) ? 1 : 0) & (((this.glVertexAttribL4dvEXT = GLContext.getFunctionAddress("glVertexAttribL4dvEXT")) != 0L) ? 1 : 0) & (((this.glVertexAttribLPointerEXT = GLContext.getFunctionAddress("glVertexAttribLPointerEXT")) != 0L) ? 1 : 0) & (((this.glGetVertexAttribLdvEXT = GLContext.getFunctionAddress("glGetVertexAttribLdvEXT")) != 0L) ? 1 : 0) & ((!supported_extensions.contains("GL_EXT_direct_state_access") || (this.glVertexArrayVertexAttribLOffsetEXT = GLContext.getFunctionAddress("glVertexArrayVertexAttribLOffsetEXT")) != 0L) ? 1 : 0);
  }
  
  private boolean EXT_vertex_shader_initNativeFunctionAddresses() {
    return (((this.glBeginVertexShaderEXT = GLContext.getFunctionAddress("glBeginVertexShaderEXT")) != 0L)) & (((this.glEndVertexShaderEXT = GLContext.getFunctionAddress("glEndVertexShaderEXT")) != 0L)) & (((this.glBindVertexShaderEXT = GLContext.getFunctionAddress("glBindVertexShaderEXT")) != 0L) ? 1 : 0) & (((this.glGenVertexShadersEXT = GLContext.getFunctionAddress("glGenVertexShadersEXT")) != 0L) ? 1 : 0) & (((this.glDeleteVertexShaderEXT = GLContext.getFunctionAddress("glDeleteVertexShaderEXT")) != 0L) ? 1 : 0) & (((this.glShaderOp1EXT = GLContext.getFunctionAddress("glShaderOp1EXT")) != 0L) ? 1 : 0) & (((this.glShaderOp2EXT = GLContext.getFunctionAddress("glShaderOp2EXT")) != 0L) ? 1 : 0) & (((this.glShaderOp3EXT = GLContext.getFunctionAddress("glShaderOp3EXT")) != 0L) ? 1 : 0) & (((this.glSwizzleEXT = GLContext.getFunctionAddress("glSwizzleEXT")) != 0L) ? 1 : 0) & (((this.glWriteMaskEXT = GLContext.getFunctionAddress("glWriteMaskEXT")) != 0L) ? 1 : 0) & (((this.glInsertComponentEXT = GLContext.getFunctionAddress("glInsertComponentEXT")) != 0L) ? 1 : 0) & (((this.glExtractComponentEXT = GLContext.getFunctionAddress("glExtractComponentEXT")) != 0L) ? 1 : 0) & (((this.glGenSymbolsEXT = GLContext.getFunctionAddress("glGenSymbolsEXT")) != 0L) ? 1 : 0) & (((this.glSetInvariantEXT = GLContext.getFunctionAddress("glSetInvariantEXT")) != 0L) ? 1 : 0) & (((this.glSetLocalConstantEXT = GLContext.getFunctionAddress("glSetLocalConstantEXT")) != 0L) ? 1 : 0) & (((this.glVariantbvEXT = GLContext.getFunctionAddress("glVariantbvEXT")) != 0L) ? 1 : 0) & (((this.glVariantsvEXT = GLContext.getFunctionAddress("glVariantsvEXT")) != 0L) ? 1 : 0) & (((this.glVariantivEXT = GLContext.getFunctionAddress("glVariantivEXT")) != 0L) ? 1 : 0) & (((this.glVariantfvEXT = GLContext.getFunctionAddress("glVariantfvEXT")) != 0L) ? 1 : 0) & (((this.glVariantdvEXT = GLContext.getFunctionAddress("glVariantdvEXT")) != 0L) ? 1 : 0) & (((this.glVariantubvEXT = GLContext.getFunctionAddress("glVariantubvEXT")) != 0L) ? 1 : 0) & (((this.glVariantusvEXT = GLContext.getFunctionAddress("glVariantusvEXT")) != 0L) ? 1 : 0) & (((this.glVariantuivEXT = GLContext.getFunctionAddress("glVariantuivEXT")) != 0L) ? 1 : 0) & (((this.glVariantPointerEXT = GLContext.getFunctionAddress("glVariantPointerEXT")) != 0L) ? 1 : 0) & (((this.glEnableVariantClientStateEXT = GLContext.getFunctionAddress("glEnableVariantClientStateEXT")) != 0L) ? 1 : 0) & (((this.glDisableVariantClientStateEXT = GLContext.getFunctionAddress("glDisableVariantClientStateEXT")) != 0L) ? 1 : 0) & (((this.glBindLightParameterEXT = GLContext.getFunctionAddress("glBindLightParameterEXT")) != 0L) ? 1 : 0) & (((this.glBindMaterialParameterEXT = GLContext.getFunctionAddress("glBindMaterialParameterEXT")) != 0L) ? 1 : 0) & (((this.glBindTexGenParameterEXT = GLContext.getFunctionAddress("glBindTexGenParameterEXT")) != 0L) ? 1 : 0) & (((this.glBindTextureUnitParameterEXT = GLContext.getFunctionAddress("glBindTextureUnitParameterEXT")) != 0L) ? 1 : 0) & (((this.glBindParameterEXT = GLContext.getFunctionAddress("glBindParameterEXT")) != 0L) ? 1 : 0) & (((this.glIsVariantEnabledEXT = GLContext.getFunctionAddress("glIsVariantEnabledEXT")) != 0L) ? 1 : 0) & (((this.glGetVariantBooleanvEXT = GLContext.getFunctionAddress("glGetVariantBooleanvEXT")) != 0L) ? 1 : 0) & (((this.glGetVariantIntegervEXT = GLContext.getFunctionAddress("glGetVariantIntegervEXT")) != 0L) ? 1 : 0) & (((this.glGetVariantFloatvEXT = GLContext.getFunctionAddress("glGetVariantFloatvEXT")) != 0L) ? 1 : 0) & (((this.glGetVariantPointervEXT = GLContext.getFunctionAddress("glGetVariantPointervEXT")) != 0L) ? 1 : 0) & (((this.glGetInvariantBooleanvEXT = GLContext.getFunctionAddress("glGetInvariantBooleanvEXT")) != 0L) ? 1 : 0) & (((this.glGetInvariantIntegervEXT = GLContext.getFunctionAddress("glGetInvariantIntegervEXT")) != 0L) ? 1 : 0) & (((this.glGetInvariantFloatvEXT = GLContext.getFunctionAddress("glGetInvariantFloatvEXT")) != 0L) ? 1 : 0) & (((this.glGetLocalConstantBooleanvEXT = GLContext.getFunctionAddress("glGetLocalConstantBooleanvEXT")) != 0L) ? 1 : 0) & (((this.glGetLocalConstantIntegervEXT = GLContext.getFunctionAddress("glGetLocalConstantIntegervEXT")) != 0L) ? 1 : 0) & (((this.glGetLocalConstantFloatvEXT = GLContext.getFunctionAddress("glGetLocalConstantFloatvEXT")) != 0L) ? 1 : 0);
  }
  
  private boolean EXT_vertex_weighting_initNativeFunctionAddresses() {
    return (((this.glVertexWeightfEXT = GLContext.getFunctionAddress("glVertexWeightfEXT")) != 0L)) & (((this.glVertexWeightPointerEXT = GLContext.getFunctionAddress("glVertexWeightPointerEXT")) != 0L));
  }
  
  private boolean GL11_initNativeFunctionAddresses(boolean forwardCompatible) {
    return ((forwardCompatible || (this.glAccum = GLContext.getFunctionAddress("glAccum")) != 0L)) & ((forwardCompatible || (this.glAlphaFunc = GLContext.getFunctionAddress("glAlphaFunc")) != 0L)) & (((this.glClearColor = GLContext.getFunctionAddress("glClearColor")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glClearAccum = GLContext.getFunctionAddress("glClearAccum")) != 0L) ? 1 : 0) & (((this.glClear = GLContext.getFunctionAddress("glClear")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glCallLists = GLContext.getFunctionAddress("glCallLists")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glCallList = GLContext.getFunctionAddress("glCallList")) != 0L) ? 1 : 0) & (((this.glBlendFunc = GLContext.getFunctionAddress("glBlendFunc")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glBitmap = GLContext.getFunctionAddress("glBitmap")) != 0L) ? 1 : 0) & (((this.glBindTexture = GLContext.getFunctionAddress("glBindTexture")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glPrioritizeTextures = GLContext.getFunctionAddress("glPrioritizeTextures")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glAreTexturesResident = GLContext.getFunctionAddress("glAreTexturesResident")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glBegin = GLContext.getFunctionAddress("glBegin")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glEnd = GLContext.getFunctionAddress("glEnd")) != 0L) ? 1 : 0) & (((this.glArrayElement = GLContext.getFunctionAddress("glArrayElement")) != 0L) ? 1 : 0) & (((this.glClearDepth = GLContext.getFunctionAddress("glClearDepth")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glDeleteLists = GLContext.getFunctionAddress("glDeleteLists")) != 0L) ? 1 : 0) & (((this.glDeleteTextures = GLContext.getFunctionAddress("glDeleteTextures")) != 0L) ? 1 : 0) & (((this.glCullFace = GLContext.getFunctionAddress("glCullFace")) != 0L) ? 1 : 0) & (((this.glCopyTexSubImage2D = GLContext.getFunctionAddress("glCopyTexSubImage2D")) != 0L) ? 1 : 0) & (((this.glCopyTexSubImage1D = GLContext.getFunctionAddress("glCopyTexSubImage1D")) != 0L) ? 1 : 0) & (((this.glCopyTexImage2D = GLContext.getFunctionAddress("glCopyTexImage2D")) != 0L) ? 1 : 0) & (((this.glCopyTexImage1D = GLContext.getFunctionAddress("glCopyTexImage1D")) != 0L) ? 1 : 0) & (((this.glCopyPixels = GLContext.getFunctionAddress("glCopyPixels")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glColorPointer = GLContext.getFunctionAddress("glColorPointer")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glColorMaterial = GLContext.getFunctionAddress("glColorMaterial")) != 0L) ? 1 : 0) & (((this.glColorMask = GLContext.getFunctionAddress("glColorMask")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glColor3b = GLContext.getFunctionAddress("glColor3b")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glColor3f = GLContext.getFunctionAddress("glColor3f")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glColor3d = GLContext.getFunctionAddress("glColor3d")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glColor3ub = GLContext.getFunctionAddress("glColor3ub")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glColor4b = GLContext.getFunctionAddress("glColor4b")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glColor4f = GLContext.getFunctionAddress("glColor4f")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glColor4d = GLContext.getFunctionAddress("glColor4d")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glColor4ub = GLContext.getFunctionAddress("glColor4ub")) != 0L) ? 1 : 0) & (((this.glClipPlane = GLContext.getFunctionAddress("glClipPlane")) != 0L) ? 1 : 0) & (((this.glClearStencil = GLContext.getFunctionAddress("glClearStencil")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glEvalPoint1 = GLContext.getFunctionAddress("glEvalPoint1")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glEvalPoint2 = GLContext.getFunctionAddress("glEvalPoint2")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glEvalMesh1 = GLContext.getFunctionAddress("glEvalMesh1")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glEvalMesh2 = GLContext.getFunctionAddress("glEvalMesh2")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glEvalCoord1f = GLContext.getFunctionAddress("glEvalCoord1f")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glEvalCoord1d = GLContext.getFunctionAddress("glEvalCoord1d")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glEvalCoord2f = GLContext.getFunctionAddress("glEvalCoord2f")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glEvalCoord2d = GLContext.getFunctionAddress("glEvalCoord2d")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glEnableClientState = GLContext.getFunctionAddress("glEnableClientState")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glDisableClientState = GLContext.getFunctionAddress("glDisableClientState")) != 0L) ? 1 : 0) & (((this.glEnable = GLContext.getFunctionAddress("glEnable")) != 0L) ? 1 : 0) & (((this.glDisable = GLContext.getFunctionAddress("glDisable")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glEdgeFlagPointer = GLContext.getFunctionAddress("glEdgeFlagPointer")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glEdgeFlag = GLContext.getFunctionAddress("glEdgeFlag")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glDrawPixels = GLContext.getFunctionAddress("glDrawPixels")) != 0L) ? 1 : 0) & (((this.glDrawElements = GLContext.getFunctionAddress("glDrawElements")) != 0L) ? 1 : 0) & (((this.glDrawBuffer = GLContext.getFunctionAddress("glDrawBuffer")) != 0L) ? 1 : 0) & (((this.glDrawArrays = GLContext.getFunctionAddress("glDrawArrays")) != 0L) ? 1 : 0) & (((this.glDepthRange = GLContext.getFunctionAddress("glDepthRange")) != 0L) ? 1 : 0) & (((this.glDepthMask = GLContext.getFunctionAddress("glDepthMask")) != 0L) ? 1 : 0) & (((this.glDepthFunc = GLContext.getFunctionAddress("glDepthFunc")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glFeedbackBuffer = GLContext.getFunctionAddress("glFeedbackBuffer")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glGetPixelMapfv = GLContext.getFunctionAddress("glGetPixelMapfv")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glGetPixelMapuiv = GLContext.getFunctionAddress("glGetPixelMapuiv")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glGetPixelMapusv = GLContext.getFunctionAddress("glGetPixelMapusv")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glGetMaterialfv = GLContext.getFunctionAddress("glGetMaterialfv")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glGetMaterialiv = GLContext.getFunctionAddress("glGetMaterialiv")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glGetMapfv = GLContext.getFunctionAddress("glGetMapfv")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glGetMapdv = GLContext.getFunctionAddress("glGetMapdv")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glGetMapiv = GLContext.getFunctionAddress("glGetMapiv")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glGetLightfv = GLContext.getFunctionAddress("glGetLightfv")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glGetLightiv = GLContext.getFunctionAddress("glGetLightiv")) != 0L) ? 1 : 0) & (((this.glGetError = GLContext.getFunctionAddress("glGetError")) != 0L) ? 1 : 0) & (((this.glGetClipPlane = GLContext.getFunctionAddress("glGetClipPlane")) != 0L) ? 1 : 0) & (((this.glGetBooleanv = GLContext.getFunctionAddress("glGetBooleanv")) != 0L) ? 1 : 0) & (((this.glGetDoublev = GLContext.getFunctionAddress("glGetDoublev")) != 0L) ? 1 : 0) & (((this.glGetFloatv = GLContext.getFunctionAddress("glGetFloatv")) != 0L) ? 1 : 0) & (((this.glGetIntegerv = GLContext.getFunctionAddress("glGetIntegerv")) != 0L) ? 1 : 0) & (((this.glGenTextures = GLContext.getFunctionAddress("glGenTextures")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glGenLists = GLContext.getFunctionAddress("glGenLists")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glFrustum = GLContext.getFunctionAddress("glFrustum")) != 0L) ? 1 : 0) & (((this.glFrontFace = GLContext.getFunctionAddress("glFrontFace")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glFogf = GLContext.getFunctionAddress("glFogf")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glFogi = GLContext.getFunctionAddress("glFogi")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glFogfv = GLContext.getFunctionAddress("glFogfv")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glFogiv = GLContext.getFunctionAddress("glFogiv")) != 0L) ? 1 : 0) & (((this.glFlush = GLContext.getFunctionAddress("glFlush")) != 0L) ? 1 : 0) & (((this.glFinish = GLContext.getFunctionAddress("glFinish")) != 0L) ? 1 : 0) & (((this.glGetPointerv = GLContext.getFunctionAddress("glGetPointerv")) != 0L) ? 1 : 0) & (((this.glIsEnabled = GLContext.getFunctionAddress("glIsEnabled")) != 0L) ? 1 : 0) & (((this.glInterleavedArrays = GLContext.getFunctionAddress("glInterleavedArrays")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glInitNames = GLContext.getFunctionAddress("glInitNames")) != 0L) ? 1 : 0) & (((this.glHint = GLContext.getFunctionAddress("glHint")) != 0L) ? 1 : 0) & (((this.glGetTexParameterfv = GLContext.getFunctionAddress("glGetTexParameterfv")) != 0L) ? 1 : 0) & (((this.glGetTexParameteriv = GLContext.getFunctionAddress("glGetTexParameteriv")) != 0L) ? 1 : 0) & (((this.glGetTexLevelParameterfv = GLContext.getFunctionAddress("glGetTexLevelParameterfv")) != 0L) ? 1 : 0) & (((this.glGetTexLevelParameteriv = GLContext.getFunctionAddress("glGetTexLevelParameteriv")) != 0L) ? 1 : 0) & (((this.glGetTexImage = GLContext.getFunctionAddress("glGetTexImage")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glGetTexGeniv = GLContext.getFunctionAddress("glGetTexGeniv")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glGetTexGenfv = GLContext.getFunctionAddress("glGetTexGenfv")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glGetTexGendv = GLContext.getFunctionAddress("glGetTexGendv")) != 0L) ? 1 : 0) & (((this.glGetTexEnviv = GLContext.getFunctionAddress("glGetTexEnviv")) != 0L) ? 1 : 0) & (((this.glGetTexEnvfv = GLContext.getFunctionAddress("glGetTexEnvfv")) != 0L) ? 1 : 0) & (((this.glGetString = GLContext.getFunctionAddress("glGetString")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glGetPolygonStipple = GLContext.getFunctionAddress("glGetPolygonStipple")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glIsList = GLContext.getFunctionAddress("glIsList")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glMaterialf = GLContext.getFunctionAddress("glMaterialf")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glMateriali = GLContext.getFunctionAddress("glMateriali")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glMaterialfv = GLContext.getFunctionAddress("glMaterialfv")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glMaterialiv = GLContext.getFunctionAddress("glMaterialiv")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glMapGrid1f = GLContext.getFunctionAddress("glMapGrid1f")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glMapGrid1d = GLContext.getFunctionAddress("glMapGrid1d")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glMapGrid2f = GLContext.getFunctionAddress("glMapGrid2f")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glMapGrid2d = GLContext.getFunctionAddress("glMapGrid2d")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glMap2f = GLContext.getFunctionAddress("glMap2f")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glMap2d = GLContext.getFunctionAddress("glMap2d")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glMap1f = GLContext.getFunctionAddress("glMap1f")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glMap1d = GLContext.getFunctionAddress("glMap1d")) != 0L) ? 1 : 0) & (((this.glLogicOp = GLContext.getFunctionAddress("glLogicOp")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glLoadName = GLContext.getFunctionAddress("glLoadName")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glLoadMatrixf = GLContext.getFunctionAddress("glLoadMatrixf")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glLoadMatrixd = GLContext.getFunctionAddress("glLoadMatrixd")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glLoadIdentity = GLContext.getFunctionAddress("glLoadIdentity")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glListBase = GLContext.getFunctionAddress("glListBase")) != 0L) ? 1 : 0) & (((this.glLineWidth = GLContext.getFunctionAddress("glLineWidth")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glLineStipple = GLContext.getFunctionAddress("glLineStipple")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glLightModelf = GLContext.getFunctionAddress("glLightModelf")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glLightModeli = GLContext.getFunctionAddress("glLightModeli")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glLightModelfv = GLContext.getFunctionAddress("glLightModelfv")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glLightModeliv = GLContext.getFunctionAddress("glLightModeliv")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glLightf = GLContext.getFunctionAddress("glLightf")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glLighti = GLContext.getFunctionAddress("glLighti")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glLightfv = GLContext.getFunctionAddress("glLightfv")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glLightiv = GLContext.getFunctionAddress("glLightiv")) != 0L) ? 1 : 0) & (((this.glIsTexture = GLContext.getFunctionAddress("glIsTexture")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glMatrixMode = GLContext.getFunctionAddress("glMatrixMode")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glPolygonStipple = GLContext.getFunctionAddress("glPolygonStipple")) != 0L) ? 1 : 0) & (((this.glPolygonOffset = GLContext.getFunctionAddress("glPolygonOffset")) != 0L) ? 1 : 0) & (((this.glPolygonMode = GLContext.getFunctionAddress("glPolygonMode")) != 0L) ? 1 : 0) & (((this.glPointSize = GLContext.getFunctionAddress("glPointSize")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glPixelZoom = GLContext.getFunctionAddress("glPixelZoom")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glPixelTransferf = GLContext.getFunctionAddress("glPixelTransferf")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glPixelTransferi = GLContext.getFunctionAddress("glPixelTransferi")) != 0L) ? 1 : 0) & (((this.glPixelStoref = GLContext.getFunctionAddress("glPixelStoref")) != 0L) ? 1 : 0) & (((this.glPixelStorei = GLContext.getFunctionAddress("glPixelStorei")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glPixelMapfv = GLContext.getFunctionAddress("glPixelMapfv")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glPixelMapuiv = GLContext.getFunctionAddress("glPixelMapuiv")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glPixelMapusv = GLContext.getFunctionAddress("glPixelMapusv")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glPassThrough = GLContext.getFunctionAddress("glPassThrough")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glOrtho = GLContext.getFunctionAddress("glOrtho")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glNormalPointer = GLContext.getFunctionAddress("glNormalPointer")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glNormal3b = GLContext.getFunctionAddress("glNormal3b")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glNormal3f = GLContext.getFunctionAddress("glNormal3f")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glNormal3d = GLContext.getFunctionAddress("glNormal3d")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glNormal3i = GLContext.getFunctionAddress("glNormal3i")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glNewList = GLContext.getFunctionAddress("glNewList")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glEndList = GLContext.getFunctionAddress("glEndList")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glMultMatrixf = GLContext.getFunctionAddress("glMultMatrixf")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glMultMatrixd = GLContext.getFunctionAddress("glMultMatrixd")) != 0L) ? 1 : 0) & (((this.glShadeModel = GLContext.getFunctionAddress("glShadeModel")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glSelectBuffer = GLContext.getFunctionAddress("glSelectBuffer")) != 0L) ? 1 : 0) & (((this.glScissor = GLContext.getFunctionAddress("glScissor")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glScalef = GLContext.getFunctionAddress("glScalef")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glScaled = GLContext.getFunctionAddress("glScaled")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glRotatef = GLContext.getFunctionAddress("glRotatef")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glRotated = GLContext.getFunctionAddress("glRotated")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glRenderMode = GLContext.getFunctionAddress("glRenderMode")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glRectf = GLContext.getFunctionAddress("glRectf")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glRectd = GLContext.getFunctionAddress("glRectd")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glRecti = GLContext.getFunctionAddress("glRecti")) != 0L) ? 1 : 0) & (((this.glReadPixels = GLContext.getFunctionAddress("glReadPixels")) != 0L) ? 1 : 0) & (((this.glReadBuffer = GLContext.getFunctionAddress("glReadBuffer")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glRasterPos2f = GLContext.getFunctionAddress("glRasterPos2f")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glRasterPos2d = GLContext.getFunctionAddress("glRasterPos2d")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glRasterPos2i = GLContext.getFunctionAddress("glRasterPos2i")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glRasterPos3f = GLContext.getFunctionAddress("glRasterPos3f")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glRasterPos3d = GLContext.getFunctionAddress("glRasterPos3d")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glRasterPos3i = GLContext.getFunctionAddress("glRasterPos3i")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glRasterPos4f = GLContext.getFunctionAddress("glRasterPos4f")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glRasterPos4d = GLContext.getFunctionAddress("glRasterPos4d")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glRasterPos4i = GLContext.getFunctionAddress("glRasterPos4i")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glPushName = GLContext.getFunctionAddress("glPushName")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glPopName = GLContext.getFunctionAddress("glPopName")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glPushMatrix = GLContext.getFunctionAddress("glPushMatrix")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glPopMatrix = GLContext.getFunctionAddress("glPopMatrix")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glPushClientAttrib = GLContext.getFunctionAddress("glPushClientAttrib")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glPopClientAttrib = GLContext.getFunctionAddress("glPopClientAttrib")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glPushAttrib = GLContext.getFunctionAddress("glPushAttrib")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glPopAttrib = GLContext.getFunctionAddress("glPopAttrib")) != 0L) ? 1 : 0) & (((this.glStencilFunc = GLContext.getFunctionAddress("glStencilFunc")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glVertexPointer = GLContext.getFunctionAddress("glVertexPointer")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glVertex2f = GLContext.getFunctionAddress("glVertex2f")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glVertex2d = GLContext.getFunctionAddress("glVertex2d")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glVertex2i = GLContext.getFunctionAddress("glVertex2i")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glVertex3f = GLContext.getFunctionAddress("glVertex3f")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glVertex3d = GLContext.getFunctionAddress("glVertex3d")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glVertex3i = GLContext.getFunctionAddress("glVertex3i")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glVertex4f = GLContext.getFunctionAddress("glVertex4f")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glVertex4d = GLContext.getFunctionAddress("glVertex4d")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glVertex4i = GLContext.getFunctionAddress("glVertex4i")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glTranslatef = GLContext.getFunctionAddress("glTranslatef")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glTranslated = GLContext.getFunctionAddress("glTranslated")) != 0L) ? 1 : 0) & (((this.glTexImage1D = GLContext.getFunctionAddress("glTexImage1D")) != 0L) ? 1 : 0) & (((this.glTexImage2D = GLContext.getFunctionAddress("glTexImage2D")) != 0L) ? 1 : 0) & (((this.glTexSubImage1D = GLContext.getFunctionAddress("glTexSubImage1D")) != 0L) ? 1 : 0) & (((this.glTexSubImage2D = GLContext.getFunctionAddress("glTexSubImage2D")) != 0L) ? 1 : 0) & (((this.glTexParameterf = GLContext.getFunctionAddress("glTexParameterf")) != 0L) ? 1 : 0) & (((this.glTexParameteri = GLContext.getFunctionAddress("glTexParameteri")) != 0L) ? 1 : 0) & (((this.glTexParameterfv = GLContext.getFunctionAddress("glTexParameterfv")) != 0L) ? 1 : 0) & (((this.glTexParameteriv = GLContext.getFunctionAddress("glTexParameteriv")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glTexGenf = GLContext.getFunctionAddress("glTexGenf")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glTexGend = GLContext.getFunctionAddress("glTexGend")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glTexGenfv = GLContext.getFunctionAddress("glTexGenfv")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glTexGendv = GLContext.getFunctionAddress("glTexGendv")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glTexGeni = GLContext.getFunctionAddress("glTexGeni")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glTexGeniv = GLContext.getFunctionAddress("glTexGeniv")) != 0L) ? 1 : 0) & (((this.glTexEnvf = GLContext.getFunctionAddress("glTexEnvf")) != 0L) ? 1 : 0) & (((this.glTexEnvi = GLContext.getFunctionAddress("glTexEnvi")) != 0L) ? 1 : 0) & (((this.glTexEnvfv = GLContext.getFunctionAddress("glTexEnvfv")) != 0L) ? 1 : 0) & (((this.glTexEnviv = GLContext.getFunctionAddress("glTexEnviv")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glTexCoordPointer = GLContext.getFunctionAddress("glTexCoordPointer")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glTexCoord1f = GLContext.getFunctionAddress("glTexCoord1f")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glTexCoord1d = GLContext.getFunctionAddress("glTexCoord1d")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glTexCoord2f = GLContext.getFunctionAddress("glTexCoord2f")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glTexCoord2d = GLContext.getFunctionAddress("glTexCoord2d")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glTexCoord3f = GLContext.getFunctionAddress("glTexCoord3f")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glTexCoord3d = GLContext.getFunctionAddress("glTexCoord3d")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glTexCoord4f = GLContext.getFunctionAddress("glTexCoord4f")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glTexCoord4d = GLContext.getFunctionAddress("glTexCoord4d")) != 0L) ? 1 : 0) & (((this.glStencilOp = GLContext.getFunctionAddress("glStencilOp")) != 0L) ? 1 : 0) & (((this.glStencilMask = GLContext.getFunctionAddress("glStencilMask")) != 0L) ? 1 : 0) & (((this.glViewport = GLContext.getFunctionAddress("glViewport")) != 0L) ? 1 : 0);
  }
  
  private boolean GL12_initNativeFunctionAddresses() {
    return (((this.glDrawRangeElements = GLContext.getFunctionAddress("glDrawRangeElements")) != 0L)) & (((this.glTexImage3D = GLContext.getFunctionAddress("glTexImage3D")) != 0L)) & (((this.glTexSubImage3D = GLContext.getFunctionAddress("glTexSubImage3D")) != 0L) ? 1 : 0) & (((this.glCopyTexSubImage3D = GLContext.getFunctionAddress("glCopyTexSubImage3D")) != 0L) ? 1 : 0);
  }
  
  private boolean GL13_initNativeFunctionAddresses(boolean forwardCompatible) {
    return (((this.glActiveTexture = GLContext.getFunctionAddress("glActiveTexture")) != 0L)) & ((forwardCompatible || (this.glClientActiveTexture = GLContext.getFunctionAddress("glClientActiveTexture")) != 0L)) & (((this.glCompressedTexImage1D = GLContext.getFunctionAddress("glCompressedTexImage1D")) != 0L) ? 1 : 0) & (((this.glCompressedTexImage2D = GLContext.getFunctionAddress("glCompressedTexImage2D")) != 0L) ? 1 : 0) & (((this.glCompressedTexImage3D = GLContext.getFunctionAddress("glCompressedTexImage3D")) != 0L) ? 1 : 0) & (((this.glCompressedTexSubImage1D = GLContext.getFunctionAddress("glCompressedTexSubImage1D")) != 0L) ? 1 : 0) & (((this.glCompressedTexSubImage2D = GLContext.getFunctionAddress("glCompressedTexSubImage2D")) != 0L) ? 1 : 0) & (((this.glCompressedTexSubImage3D = GLContext.getFunctionAddress("glCompressedTexSubImage3D")) != 0L) ? 1 : 0) & (((this.glGetCompressedTexImage = GLContext.getFunctionAddress("glGetCompressedTexImage")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glMultiTexCoord1f = GLContext.getFunctionAddress("glMultiTexCoord1f")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glMultiTexCoord1d = GLContext.getFunctionAddress("glMultiTexCoord1d")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glMultiTexCoord2f = GLContext.getFunctionAddress("glMultiTexCoord2f")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glMultiTexCoord2d = GLContext.getFunctionAddress("glMultiTexCoord2d")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glMultiTexCoord3f = GLContext.getFunctionAddress("glMultiTexCoord3f")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glMultiTexCoord3d = GLContext.getFunctionAddress("glMultiTexCoord3d")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glMultiTexCoord4f = GLContext.getFunctionAddress("glMultiTexCoord4f")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glMultiTexCoord4d = GLContext.getFunctionAddress("glMultiTexCoord4d")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glLoadTransposeMatrixf = GLContext.getFunctionAddress("glLoadTransposeMatrixf")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glLoadTransposeMatrixd = GLContext.getFunctionAddress("glLoadTransposeMatrixd")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glMultTransposeMatrixf = GLContext.getFunctionAddress("glMultTransposeMatrixf")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glMultTransposeMatrixd = GLContext.getFunctionAddress("glMultTransposeMatrixd")) != 0L) ? 1 : 0) & (((this.glSampleCoverage = GLContext.getFunctionAddress("glSampleCoverage")) != 0L) ? 1 : 0);
  }
  
  private boolean GL14_initNativeFunctionAddresses(boolean forwardCompatible) {
    return (((this.glBlendEquation = GLContext.getFunctionAddress("glBlendEquation")) != 0L)) & (((this.glBlendColor = GLContext.getFunctionAddress("glBlendColor")) != 0L)) & ((forwardCompatible || (this.glFogCoordf = GLContext.getFunctionAddress("glFogCoordf")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glFogCoordd = GLContext.getFunctionAddress("glFogCoordd")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glFogCoordPointer = GLContext.getFunctionAddress("glFogCoordPointer")) != 0L) ? 1 : 0) & (((this.glMultiDrawArrays = GLContext.getFunctionAddress("glMultiDrawArrays")) != 0L) ? 1 : 0) & (((this.glPointParameteri = GLContext.getFunctionAddress("glPointParameteri")) != 0L) ? 1 : 0) & (((this.glPointParameterf = GLContext.getFunctionAddress("glPointParameterf")) != 0L) ? 1 : 0) & (((this.glPointParameteriv = GLContext.getFunctionAddress("glPointParameteriv")) != 0L) ? 1 : 0) & (((this.glPointParameterfv = GLContext.getFunctionAddress("glPointParameterfv")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glSecondaryColor3b = GLContext.getFunctionAddress("glSecondaryColor3b")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glSecondaryColor3f = GLContext.getFunctionAddress("glSecondaryColor3f")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glSecondaryColor3d = GLContext.getFunctionAddress("glSecondaryColor3d")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glSecondaryColor3ub = GLContext.getFunctionAddress("glSecondaryColor3ub")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glSecondaryColorPointer = GLContext.getFunctionAddress("glSecondaryColorPointer")) != 0L) ? 1 : 0) & (((this.glBlendFuncSeparate = GLContext.getFunctionAddress("glBlendFuncSeparate")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glWindowPos2f = GLContext.getFunctionAddress("glWindowPos2f")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glWindowPos2d = GLContext.getFunctionAddress("glWindowPos2d")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glWindowPos2i = GLContext.getFunctionAddress("glWindowPos2i")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glWindowPos3f = GLContext.getFunctionAddress("glWindowPos3f")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glWindowPos3d = GLContext.getFunctionAddress("glWindowPos3d")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glWindowPos3i = GLContext.getFunctionAddress("glWindowPos3i")) != 0L) ? 1 : 0);
  }
  
  private boolean GL15_initNativeFunctionAddresses() {
    return (((this.glBindBuffer = GLContext.getFunctionAddress("glBindBuffer")) != 0L)) & (((this.glDeleteBuffers = GLContext.getFunctionAddress("glDeleteBuffers")) != 0L)) & (((this.glGenBuffers = GLContext.getFunctionAddress("glGenBuffers")) != 0L) ? 1 : 0) & (((this.glIsBuffer = GLContext.getFunctionAddress("glIsBuffer")) != 0L) ? 1 : 0) & (((this.glBufferData = GLContext.getFunctionAddress("glBufferData")) != 0L) ? 1 : 0) & (((this.glBufferSubData = GLContext.getFunctionAddress("glBufferSubData")) != 0L) ? 1 : 0) & (((this.glGetBufferSubData = GLContext.getFunctionAddress("glGetBufferSubData")) != 0L) ? 1 : 0) & (((this.glMapBuffer = GLContext.getFunctionAddress("glMapBuffer")) != 0L) ? 1 : 0) & (((this.glUnmapBuffer = GLContext.getFunctionAddress("glUnmapBuffer")) != 0L) ? 1 : 0) & (((this.glGetBufferParameteriv = GLContext.getFunctionAddress("glGetBufferParameteriv")) != 0L) ? 1 : 0) & (((this.glGetBufferPointerv = GLContext.getFunctionAddress("glGetBufferPointerv")) != 0L) ? 1 : 0) & (((this.glGenQueries = GLContext.getFunctionAddress("glGenQueries")) != 0L) ? 1 : 0) & (((this.glDeleteQueries = GLContext.getFunctionAddress("glDeleteQueries")) != 0L) ? 1 : 0) & (((this.glIsQuery = GLContext.getFunctionAddress("glIsQuery")) != 0L) ? 1 : 0) & (((this.glBeginQuery = GLContext.getFunctionAddress("glBeginQuery")) != 0L) ? 1 : 0) & (((this.glEndQuery = GLContext.getFunctionAddress("glEndQuery")) != 0L) ? 1 : 0) & (((this.glGetQueryiv = GLContext.getFunctionAddress("glGetQueryiv")) != 0L) ? 1 : 0) & (((this.glGetQueryObjectiv = GLContext.getFunctionAddress("glGetQueryObjectiv")) != 0L) ? 1 : 0) & (((this.glGetQueryObjectuiv = GLContext.getFunctionAddress("glGetQueryObjectuiv")) != 0L) ? 1 : 0);
  }
  
  private boolean GL20_initNativeFunctionAddresses() {
    return (((this.glShaderSource = GLContext.getFunctionAddress("glShaderSource")) != 0L)) & (((this.glCreateShader = GLContext.getFunctionAddress("glCreateShader")) != 0L)) & (((this.glIsShader = GLContext.getFunctionAddress("glIsShader")) != 0L) ? 1 : 0) & (((this.glCompileShader = GLContext.getFunctionAddress("glCompileShader")) != 0L) ? 1 : 0) & (((this.glDeleteShader = GLContext.getFunctionAddress("glDeleteShader")) != 0L) ? 1 : 0) & (((this.glCreateProgram = GLContext.getFunctionAddress("glCreateProgram")) != 0L) ? 1 : 0) & (((this.glIsProgram = GLContext.getFunctionAddress("glIsProgram")) != 0L) ? 1 : 0) & (((this.glAttachShader = GLContext.getFunctionAddress("glAttachShader")) != 0L) ? 1 : 0) & (((this.glDetachShader = GLContext.getFunctionAddress("glDetachShader")) != 0L) ? 1 : 0) & (((this.glLinkProgram = GLContext.getFunctionAddress("glLinkProgram")) != 0L) ? 1 : 0) & (((this.glUseProgram = GLContext.getFunctionAddress("glUseProgram")) != 0L) ? 1 : 0) & (((this.glValidateProgram = GLContext.getFunctionAddress("glValidateProgram")) != 0L) ? 1 : 0) & (((this.glDeleteProgram = GLContext.getFunctionAddress("glDeleteProgram")) != 0L) ? 1 : 0) & (((this.glUniform1f = GLContext.getFunctionAddress("glUniform1f")) != 0L) ? 1 : 0) & (((this.glUniform2f = GLContext.getFunctionAddress("glUniform2f")) != 0L) ? 1 : 0) & (((this.glUniform3f = GLContext.getFunctionAddress("glUniform3f")) != 0L) ? 1 : 0) & (((this.glUniform4f = GLContext.getFunctionAddress("glUniform4f")) != 0L) ? 1 : 0) & (((this.glUniform1i = GLContext.getFunctionAddress("glUniform1i")) != 0L) ? 1 : 0) & (((this.glUniform2i = GLContext.getFunctionAddress("glUniform2i")) != 0L) ? 1 : 0) & (((this.glUniform3i = GLContext.getFunctionAddress("glUniform3i")) != 0L) ? 1 : 0) & (((this.glUniform4i = GLContext.getFunctionAddress("glUniform4i")) != 0L) ? 1 : 0) & (((this.glUniform1fv = GLContext.getFunctionAddress("glUniform1fv")) != 0L) ? 1 : 0) & (((this.glUniform2fv = GLContext.getFunctionAddress("glUniform2fv")) != 0L) ? 1 : 0) & (((this.glUniform3fv = GLContext.getFunctionAddress("glUniform3fv")) != 0L) ? 1 : 0) & (((this.glUniform4fv = GLContext.getFunctionAddress("glUniform4fv")) != 0L) ? 1 : 0) & (((this.glUniform1iv = GLContext.getFunctionAddress("glUniform1iv")) != 0L) ? 1 : 0) & (((this.glUniform2iv = GLContext.getFunctionAddress("glUniform2iv")) != 0L) ? 1 : 0) & (((this.glUniform3iv = GLContext.getFunctionAddress("glUniform3iv")) != 0L) ? 1 : 0) & (((this.glUniform4iv = GLContext.getFunctionAddress("glUniform4iv")) != 0L) ? 1 : 0) & (((this.glUniformMatrix2fv = GLContext.getFunctionAddress("glUniformMatrix2fv")) != 0L) ? 1 : 0) & (((this.glUniformMatrix3fv = GLContext.getFunctionAddress("glUniformMatrix3fv")) != 0L) ? 1 : 0) & (((this.glUniformMatrix4fv = GLContext.getFunctionAddress("glUniformMatrix4fv")) != 0L) ? 1 : 0) & (((this.glGetShaderiv = GLContext.getFunctionAddress("glGetShaderiv")) != 0L) ? 1 : 0) & (((this.glGetProgramiv = GLContext.getFunctionAddress("glGetProgramiv")) != 0L) ? 1 : 0) & (((this.glGetShaderInfoLog = GLContext.getFunctionAddress("glGetShaderInfoLog")) != 0L) ? 1 : 0) & (((this.glGetProgramInfoLog = GLContext.getFunctionAddress("glGetProgramInfoLog")) != 0L) ? 1 : 0) & (((this.glGetAttachedShaders = GLContext.getFunctionAddress("glGetAttachedShaders")) != 0L) ? 1 : 0) & (((this.glGetUniformLocation = GLContext.getFunctionAddress("glGetUniformLocation")) != 0L) ? 1 : 0) & (((this.glGetActiveUniform = GLContext.getFunctionAddress("glGetActiveUniform")) != 0L) ? 1 : 0) & (((this.glGetUniformfv = GLContext.getFunctionAddress("glGetUniformfv")) != 0L) ? 1 : 0) & (((this.glGetUniformiv = GLContext.getFunctionAddress("glGetUniformiv")) != 0L) ? 1 : 0) & (((this.glGetShaderSource = GLContext.getFunctionAddress("glGetShaderSource")) != 0L) ? 1 : 0) & (((this.glVertexAttrib1s = GLContext.getFunctionAddress("glVertexAttrib1s")) != 0L) ? 1 : 0) & (((this.glVertexAttrib1f = GLContext.getFunctionAddress("glVertexAttrib1f")) != 0L) ? 1 : 0) & (((this.glVertexAttrib1d = GLContext.getFunctionAddress("glVertexAttrib1d")) != 0L) ? 1 : 0) & (((this.glVertexAttrib2s = GLContext.getFunctionAddress("glVertexAttrib2s")) != 0L) ? 1 : 0) & (((this.glVertexAttrib2f = GLContext.getFunctionAddress("glVertexAttrib2f")) != 0L) ? 1 : 0) & (((this.glVertexAttrib2d = GLContext.getFunctionAddress("glVertexAttrib2d")) != 0L) ? 1 : 0) & (((this.glVertexAttrib3s = GLContext.getFunctionAddress("glVertexAttrib3s")) != 0L) ? 1 : 0) & (((this.glVertexAttrib3f = GLContext.getFunctionAddress("glVertexAttrib3f")) != 0L) ? 1 : 0) & (((this.glVertexAttrib3d = GLContext.getFunctionAddress("glVertexAttrib3d")) != 0L) ? 1 : 0) & (((this.glVertexAttrib4s = GLContext.getFunctionAddress("glVertexAttrib4s")) != 0L) ? 1 : 0) & (((this.glVertexAttrib4f = GLContext.getFunctionAddress("glVertexAttrib4f")) != 0L) ? 1 : 0) & (((this.glVertexAttrib4d = GLContext.getFunctionAddress("glVertexAttrib4d")) != 0L) ? 1 : 0) & (((this.glVertexAttrib4Nub = GLContext.getFunctionAddress("glVertexAttrib4Nub")) != 0L) ? 1 : 0) & (((this.glVertexAttribPointer = GLContext.getFunctionAddress("glVertexAttribPointer")) != 0L) ? 1 : 0) & (((this.glEnableVertexAttribArray = GLContext.getFunctionAddress("glEnableVertexAttribArray")) != 0L) ? 1 : 0) & (((this.glDisableVertexAttribArray = GLContext.getFunctionAddress("glDisableVertexAttribArray")) != 0L) ? 1 : 0) & (((this.glGetVertexAttribfv = GLContext.getFunctionAddress("glGetVertexAttribfv")) != 0L) ? 1 : 0) & (((this.glGetVertexAttribdv = GLContext.getFunctionAddress("glGetVertexAttribdv")) != 0L) ? 1 : 0) & (((this.glGetVertexAttribiv = GLContext.getFunctionAddress("glGetVertexAttribiv")) != 0L) ? 1 : 0) & (((this.glGetVertexAttribPointerv = GLContext.getFunctionAddress("glGetVertexAttribPointerv")) != 0L) ? 1 : 0) & (((this.glBindAttribLocation = GLContext.getFunctionAddress("glBindAttribLocation")) != 0L) ? 1 : 0) & (((this.glGetActiveAttrib = GLContext.getFunctionAddress("glGetActiveAttrib")) != 0L) ? 1 : 0) & (((this.glGetAttribLocation = GLContext.getFunctionAddress("glGetAttribLocation")) != 0L) ? 1 : 0) & (((this.glDrawBuffers = GLContext.getFunctionAddress("glDrawBuffers")) != 0L) ? 1 : 0) & (((this.glStencilOpSeparate = GLContext.getFunctionAddress("glStencilOpSeparate")) != 0L) ? 1 : 0) & (((this.glStencilFuncSeparate = GLContext.getFunctionAddress("glStencilFuncSeparate")) != 0L) ? 1 : 0) & (((this.glStencilMaskSeparate = GLContext.getFunctionAddress("glStencilMaskSeparate")) != 0L) ? 1 : 0) & (((this.glBlendEquationSeparate = GLContext.getFunctionAddress("glBlendEquationSeparate")) != 0L) ? 1 : 0);
  }
  
  private boolean GL21_initNativeFunctionAddresses() {
    return (((this.glUniformMatrix2x3fv = GLContext.getFunctionAddress("glUniformMatrix2x3fv")) != 0L)) & (((this.glUniformMatrix3x2fv = GLContext.getFunctionAddress("glUniformMatrix3x2fv")) != 0L)) & (((this.glUniformMatrix2x4fv = GLContext.getFunctionAddress("glUniformMatrix2x4fv")) != 0L) ? 1 : 0) & (((this.glUniformMatrix4x2fv = GLContext.getFunctionAddress("glUniformMatrix4x2fv")) != 0L) ? 1 : 0) & (((this.glUniformMatrix3x4fv = GLContext.getFunctionAddress("glUniformMatrix3x4fv")) != 0L) ? 1 : 0) & (((this.glUniformMatrix4x3fv = GLContext.getFunctionAddress("glUniformMatrix4x3fv")) != 0L) ? 1 : 0);
  }
  
  private boolean GL30_initNativeFunctionAddresses() {
    return (((this.glGetStringi = GLContext.getFunctionAddress("glGetStringi")) != 0L)) & (((this.glClearBufferfv = GLContext.getFunctionAddress("glClearBufferfv")) != 0L)) & (((this.glClearBufferiv = GLContext.getFunctionAddress("glClearBufferiv")) != 0L) ? 1 : 0) & (((this.glClearBufferuiv = GLContext.getFunctionAddress("glClearBufferuiv")) != 0L) ? 1 : 0) & (((this.glClearBufferfi = GLContext.getFunctionAddress("glClearBufferfi")) != 0L) ? 1 : 0) & (((this.glVertexAttribI1i = GLContext.getFunctionAddress("glVertexAttribI1i")) != 0L) ? 1 : 0) & (((this.glVertexAttribI2i = GLContext.getFunctionAddress("glVertexAttribI2i")) != 0L) ? 1 : 0) & (((this.glVertexAttribI3i = GLContext.getFunctionAddress("glVertexAttribI3i")) != 0L) ? 1 : 0) & (((this.glVertexAttribI4i = GLContext.getFunctionAddress("glVertexAttribI4i")) != 0L) ? 1 : 0) & (((this.glVertexAttribI1ui = GLContext.getFunctionAddress("glVertexAttribI1ui")) != 0L) ? 1 : 0) & (((this.glVertexAttribI2ui = GLContext.getFunctionAddress("glVertexAttribI2ui")) != 0L) ? 1 : 0) & (((this.glVertexAttribI3ui = GLContext.getFunctionAddress("glVertexAttribI3ui")) != 0L) ? 1 : 0) & (((this.glVertexAttribI4ui = GLContext.getFunctionAddress("glVertexAttribI4ui")) != 0L) ? 1 : 0) & (((this.glVertexAttribI1iv = GLContext.getFunctionAddress("glVertexAttribI1iv")) != 0L) ? 1 : 0) & (((this.glVertexAttribI2iv = GLContext.getFunctionAddress("glVertexAttribI2iv")) != 0L) ? 1 : 0) & (((this.glVertexAttribI3iv = GLContext.getFunctionAddress("glVertexAttribI3iv")) != 0L) ? 1 : 0) & (((this.glVertexAttribI4iv = GLContext.getFunctionAddress("glVertexAttribI4iv")) != 0L) ? 1 : 0) & (((this.glVertexAttribI1uiv = GLContext.getFunctionAddress("glVertexAttribI1uiv")) != 0L) ? 1 : 0) & (((this.glVertexAttribI2uiv = GLContext.getFunctionAddress("glVertexAttribI2uiv")) != 0L) ? 1 : 0) & (((this.glVertexAttribI3uiv = GLContext.getFunctionAddress("glVertexAttribI3uiv")) != 0L) ? 1 : 0) & (((this.glVertexAttribI4uiv = GLContext.getFunctionAddress("glVertexAttribI4uiv")) != 0L) ? 1 : 0) & (((this.glVertexAttribI4bv = GLContext.getFunctionAddress("glVertexAttribI4bv")) != 0L) ? 1 : 0) & (((this.glVertexAttribI4sv = GLContext.getFunctionAddress("glVertexAttribI4sv")) != 0L) ? 1 : 0) & (((this.glVertexAttribI4ubv = GLContext.getFunctionAddress("glVertexAttribI4ubv")) != 0L) ? 1 : 0) & (((this.glVertexAttribI4usv = GLContext.getFunctionAddress("glVertexAttribI4usv")) != 0L) ? 1 : 0) & (((this.glVertexAttribIPointer = GLContext.getFunctionAddress("glVertexAttribIPointer")) != 0L) ? 1 : 0) & (((this.glGetVertexAttribIiv = GLContext.getFunctionAddress("glGetVertexAttribIiv")) != 0L) ? 1 : 0) & (((this.glGetVertexAttribIuiv = GLContext.getFunctionAddress("glGetVertexAttribIuiv")) != 0L) ? 1 : 0) & (((this.glUniform1ui = GLContext.getFunctionAddress("glUniform1ui")) != 0L) ? 1 : 0) & (((this.glUniform2ui = GLContext.getFunctionAddress("glUniform2ui")) != 0L) ? 1 : 0) & (((this.glUniform3ui = GLContext.getFunctionAddress("glUniform3ui")) != 0L) ? 1 : 0) & (((this.glUniform4ui = GLContext.getFunctionAddress("glUniform4ui")) != 0L) ? 1 : 0) & (((this.glUniform1uiv = GLContext.getFunctionAddress("glUniform1uiv")) != 0L) ? 1 : 0) & (((this.glUniform2uiv = GLContext.getFunctionAddress("glUniform2uiv")) != 0L) ? 1 : 0) & (((this.glUniform3uiv = GLContext.getFunctionAddress("glUniform3uiv")) != 0L) ? 1 : 0) & (((this.glUniform4uiv = GLContext.getFunctionAddress("glUniform4uiv")) != 0L) ? 1 : 0) & (((this.glGetUniformuiv = GLContext.getFunctionAddress("glGetUniformuiv")) != 0L) ? 1 : 0) & (((this.glBindFragDataLocation = GLContext.getFunctionAddress("glBindFragDataLocation")) != 0L) ? 1 : 0) & (((this.glGetFragDataLocation = GLContext.getFunctionAddress("glGetFragDataLocation")) != 0L) ? 1 : 0) & (((this.glBeginConditionalRender = GLContext.getFunctionAddress("glBeginConditionalRender")) != 0L) ? 1 : 0) & (((this.glEndConditionalRender = GLContext.getFunctionAddress("glEndConditionalRender")) != 0L) ? 1 : 0) & (((this.glMapBufferRange = GLContext.getFunctionAddress("glMapBufferRange")) != 0L) ? 1 : 0) & (((this.glFlushMappedBufferRange = GLContext.getFunctionAddress("glFlushMappedBufferRange")) != 0L) ? 1 : 0) & (((this.glClampColor = GLContext.getFunctionAddress("glClampColor")) != 0L) ? 1 : 0) & (((this.glIsRenderbuffer = GLContext.getFunctionAddress("glIsRenderbuffer")) != 0L) ? 1 : 0) & (((this.glBindRenderbuffer = GLContext.getFunctionAddress("glBindRenderbuffer")) != 0L) ? 1 : 0) & (((this.glDeleteRenderbuffers = GLContext.getFunctionAddress("glDeleteRenderbuffers")) != 0L) ? 1 : 0) & (((this.glGenRenderbuffers = GLContext.getFunctionAddress("glGenRenderbuffers")) != 0L) ? 1 : 0) & (((this.glRenderbufferStorage = GLContext.getFunctionAddress("glRenderbufferStorage")) != 0L) ? 1 : 0) & (((this.glGetRenderbufferParameteriv = GLContext.getFunctionAddress("glGetRenderbufferParameteriv")) != 0L) ? 1 : 0) & (((this.glIsFramebuffer = GLContext.getFunctionAddress("glIsFramebuffer")) != 0L) ? 1 : 0) & (((this.glBindFramebuffer = GLContext.getFunctionAddress("glBindFramebuffer")) != 0L) ? 1 : 0) & (((this.glDeleteFramebuffers = GLContext.getFunctionAddress("glDeleteFramebuffers")) != 0L) ? 1 : 0) & (((this.glGenFramebuffers = GLContext.getFunctionAddress("glGenFramebuffers")) != 0L) ? 1 : 0) & (((this.glCheckFramebufferStatus = GLContext.getFunctionAddress("glCheckFramebufferStatus")) != 0L) ? 1 : 0) & (((this.glFramebufferTexture1D = GLContext.getFunctionAddress("glFramebufferTexture1D")) != 0L) ? 1 : 0) & (((this.glFramebufferTexture2D = GLContext.getFunctionAddress("glFramebufferTexture2D")) != 0L) ? 1 : 0) & (((this.glFramebufferTexture3D = GLContext.getFunctionAddress("glFramebufferTexture3D")) != 0L) ? 1 : 0) & (((this.glFramebufferRenderbuffer = GLContext.getFunctionAddress("glFramebufferRenderbuffer")) != 0L) ? 1 : 0) & (((this.glGetFramebufferAttachmentParameteriv = GLContext.getFunctionAddress("glGetFramebufferAttachmentParameteriv")) != 0L) ? 1 : 0) & (((this.glGenerateMipmap = GLContext.getFunctionAddress("glGenerateMipmap")) != 0L) ? 1 : 0) & (((this.glRenderbufferStorageMultisample = GLContext.getFunctionAddress("glRenderbufferStorageMultisample")) != 0L) ? 1 : 0) & (((this.glBlitFramebuffer = GLContext.getFunctionAddress("glBlitFramebuffer")) != 0L) ? 1 : 0) & (((this.glTexParameterIiv = GLContext.getFunctionAddress("glTexParameterIiv")) != 0L) ? 1 : 0) & (((this.glTexParameterIuiv = GLContext.getFunctionAddress("glTexParameterIuiv")) != 0L) ? 1 : 0) & (((this.glGetTexParameterIiv = GLContext.getFunctionAddress("glGetTexParameterIiv")) != 0L) ? 1 : 0) & (((this.glGetTexParameterIuiv = GLContext.getFunctionAddress("glGetTexParameterIuiv")) != 0L) ? 1 : 0) & (((this.glFramebufferTextureLayer = GLContext.getFunctionAddress("glFramebufferTextureLayer")) != 0L) ? 1 : 0) & (((this.glColorMaski = GLContext.getFunctionAddress("glColorMaski")) != 0L) ? 1 : 0) & (((this.glGetBooleani_v = GLContext.getFunctionAddress("glGetBooleani_v")) != 0L) ? 1 : 0) & (((this.glGetIntegeri_v = GLContext.getFunctionAddress("glGetIntegeri_v")) != 0L) ? 1 : 0) & (((this.glEnablei = GLContext.getFunctionAddress("glEnablei")) != 0L) ? 1 : 0) & (((this.glDisablei = GLContext.getFunctionAddress("glDisablei")) != 0L) ? 1 : 0) & (((this.glIsEnabledi = GLContext.getFunctionAddress("glIsEnabledi")) != 0L) ? 1 : 0) & (((this.glBindBufferRange = GLContext.getFunctionAddress("glBindBufferRange")) != 0L) ? 1 : 0) & (((this.glBindBufferBase = GLContext.getFunctionAddress("glBindBufferBase")) != 0L) ? 1 : 0) & (((this.glBeginTransformFeedback = GLContext.getFunctionAddress("glBeginTransformFeedback")) != 0L) ? 1 : 0) & (((this.glEndTransformFeedback = GLContext.getFunctionAddress("glEndTransformFeedback")) != 0L) ? 1 : 0) & (((this.glTransformFeedbackVaryings = GLContext.getFunctionAddress("glTransformFeedbackVaryings")) != 0L) ? 1 : 0) & (((this.glGetTransformFeedbackVarying = GLContext.getFunctionAddress("glGetTransformFeedbackVarying")) != 0L) ? 1 : 0) & (((this.glBindVertexArray = GLContext.getFunctionAddress("glBindVertexArray")) != 0L) ? 1 : 0) & (((this.glDeleteVertexArrays = GLContext.getFunctionAddress("glDeleteVertexArrays")) != 0L) ? 1 : 0) & (((this.glGenVertexArrays = GLContext.getFunctionAddress("glGenVertexArrays")) != 0L) ? 1 : 0) & (((this.glIsVertexArray = GLContext.getFunctionAddress("glIsVertexArray")) != 0L) ? 1 : 0);
  }
  
  private boolean GL31_initNativeFunctionAddresses() {
    return (((this.glDrawArraysInstanced = GLContext.getFunctionAddress("glDrawArraysInstanced")) != 0L)) & (((this.glDrawElementsInstanced = GLContext.getFunctionAddress("glDrawElementsInstanced")) != 0L)) & (((this.glCopyBufferSubData = GLContext.getFunctionAddress("glCopyBufferSubData")) != 0L) ? 1 : 0) & (((this.glPrimitiveRestartIndex = GLContext.getFunctionAddress("glPrimitiveRestartIndex")) != 0L) ? 1 : 0) & (((this.glTexBuffer = GLContext.getFunctionAddress("glTexBuffer")) != 0L) ? 1 : 0) & (((this.glGetUniformIndices = GLContext.getFunctionAddress("glGetUniformIndices")) != 0L) ? 1 : 0) & (((this.glGetActiveUniformsiv = GLContext.getFunctionAddress("glGetActiveUniformsiv")) != 0L) ? 1 : 0) & (((this.glGetActiveUniformName = GLContext.getFunctionAddress("glGetActiveUniformName")) != 0L) ? 1 : 0) & (((this.glGetUniformBlockIndex = GLContext.getFunctionAddress("glGetUniformBlockIndex")) != 0L) ? 1 : 0) & (((this.glGetActiveUniformBlockiv = GLContext.getFunctionAddress("glGetActiveUniformBlockiv")) != 0L) ? 1 : 0) & (((this.glGetActiveUniformBlockName = GLContext.getFunctionAddress("glGetActiveUniformBlockName")) != 0L) ? 1 : 0) & (((this.glUniformBlockBinding = GLContext.getFunctionAddress("glUniformBlockBinding")) != 0L) ? 1 : 0);
  }
  
  private boolean GL32_initNativeFunctionAddresses() {
    return (((this.glGetBufferParameteri64v = GLContext.getFunctionAddress("glGetBufferParameteri64v")) != 0L)) & (((this.glDrawElementsBaseVertex = GLContext.getFunctionAddress("glDrawElementsBaseVertex")) != 0L)) & (((this.glDrawRangeElementsBaseVertex = GLContext.getFunctionAddress("glDrawRangeElementsBaseVertex")) != 0L) ? 1 : 0) & (((this.glDrawElementsInstancedBaseVertex = GLContext.getFunctionAddress("glDrawElementsInstancedBaseVertex")) != 0L) ? 1 : 0) & (((this.glProvokingVertex = GLContext.getFunctionAddress("glProvokingVertex")) != 0L) ? 1 : 0) & (((this.glTexImage2DMultisample = GLContext.getFunctionAddress("glTexImage2DMultisample")) != 0L) ? 1 : 0) & (((this.glTexImage3DMultisample = GLContext.getFunctionAddress("glTexImage3DMultisample")) != 0L) ? 1 : 0) & (((this.glGetMultisamplefv = GLContext.getFunctionAddress("glGetMultisamplefv")) != 0L) ? 1 : 0) & (((this.glSampleMaski = GLContext.getFunctionAddress("glSampleMaski")) != 0L) ? 1 : 0) & (((this.glFramebufferTexture = GLContext.getFunctionAddress("glFramebufferTexture")) != 0L) ? 1 : 0) & (((this.glFenceSync = GLContext.getFunctionAddress("glFenceSync")) != 0L) ? 1 : 0) & (((this.glIsSync = GLContext.getFunctionAddress("glIsSync")) != 0L) ? 1 : 0) & (((this.glDeleteSync = GLContext.getFunctionAddress("glDeleteSync")) != 0L) ? 1 : 0) & (((this.glClientWaitSync = GLContext.getFunctionAddress("glClientWaitSync")) != 0L) ? 1 : 0) & (((this.glWaitSync = GLContext.getFunctionAddress("glWaitSync")) != 0L) ? 1 : 0) & (((this.glGetInteger64v = GLContext.getFunctionAddress("glGetInteger64v")) != 0L) ? 1 : 0) & (((this.glGetInteger64i_v = GLContext.getFunctionAddress("glGetInteger64i_v")) != 0L) ? 1 : 0) & (((this.glGetSynciv = GLContext.getFunctionAddress("glGetSynciv")) != 0L) ? 1 : 0);
  }
  
  private boolean GL33_initNativeFunctionAddresses(boolean forwardCompatible) {
    return (((this.glBindFragDataLocationIndexed = GLContext.getFunctionAddress("glBindFragDataLocationIndexed")) != 0L)) & (((this.glGetFragDataIndex = GLContext.getFunctionAddress("glGetFragDataIndex")) != 0L)) & (((this.glGenSamplers = GLContext.getFunctionAddress("glGenSamplers")) != 0L) ? 1 : 0) & (((this.glDeleteSamplers = GLContext.getFunctionAddress("glDeleteSamplers")) != 0L) ? 1 : 0) & (((this.glIsSampler = GLContext.getFunctionAddress("glIsSampler")) != 0L) ? 1 : 0) & (((this.glBindSampler = GLContext.getFunctionAddress("glBindSampler")) != 0L) ? 1 : 0) & (((this.glSamplerParameteri = GLContext.getFunctionAddress("glSamplerParameteri")) != 0L) ? 1 : 0) & (((this.glSamplerParameterf = GLContext.getFunctionAddress("glSamplerParameterf")) != 0L) ? 1 : 0) & (((this.glSamplerParameteriv = GLContext.getFunctionAddress("glSamplerParameteriv")) != 0L) ? 1 : 0) & (((this.glSamplerParameterfv = GLContext.getFunctionAddress("glSamplerParameterfv")) != 0L) ? 1 : 0) & (((this.glSamplerParameterIiv = GLContext.getFunctionAddress("glSamplerParameterIiv")) != 0L) ? 1 : 0) & (((this.glSamplerParameterIuiv = GLContext.getFunctionAddress("glSamplerParameterIuiv")) != 0L) ? 1 : 0) & (((this.glGetSamplerParameteriv = GLContext.getFunctionAddress("glGetSamplerParameteriv")) != 0L) ? 1 : 0) & (((this.glGetSamplerParameterfv = GLContext.getFunctionAddress("glGetSamplerParameterfv")) != 0L) ? 1 : 0) & (((this.glGetSamplerParameterIiv = GLContext.getFunctionAddress("glGetSamplerParameterIiv")) != 0L) ? 1 : 0) & (((this.glGetSamplerParameterIuiv = GLContext.getFunctionAddress("glGetSamplerParameterIuiv")) != 0L) ? 1 : 0) & (((this.glQueryCounter = GLContext.getFunctionAddress("glQueryCounter")) != 0L) ? 1 : 0) & (((this.glGetQueryObjecti64v = GLContext.getFunctionAddress("glGetQueryObjecti64v")) != 0L) ? 1 : 0) & (((this.glGetQueryObjectui64v = GLContext.getFunctionAddress("glGetQueryObjectui64v")) != 0L) ? 1 : 0) & (((this.glVertexAttribDivisor = GLContext.getFunctionAddress("glVertexAttribDivisor")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glVertexP2ui = GLContext.getFunctionAddress("glVertexP2ui")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glVertexP3ui = GLContext.getFunctionAddress("glVertexP3ui")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glVertexP4ui = GLContext.getFunctionAddress("glVertexP4ui")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glVertexP2uiv = GLContext.getFunctionAddress("glVertexP2uiv")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glVertexP3uiv = GLContext.getFunctionAddress("glVertexP3uiv")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glVertexP4uiv = GLContext.getFunctionAddress("glVertexP4uiv")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glTexCoordP1ui = GLContext.getFunctionAddress("glTexCoordP1ui")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glTexCoordP2ui = GLContext.getFunctionAddress("glTexCoordP2ui")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glTexCoordP3ui = GLContext.getFunctionAddress("glTexCoordP3ui")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glTexCoordP4ui = GLContext.getFunctionAddress("glTexCoordP4ui")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glTexCoordP1uiv = GLContext.getFunctionAddress("glTexCoordP1uiv")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glTexCoordP2uiv = GLContext.getFunctionAddress("glTexCoordP2uiv")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glTexCoordP3uiv = GLContext.getFunctionAddress("glTexCoordP3uiv")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glTexCoordP4uiv = GLContext.getFunctionAddress("glTexCoordP4uiv")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glMultiTexCoordP1ui = GLContext.getFunctionAddress("glMultiTexCoordP1ui")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glMultiTexCoordP2ui = GLContext.getFunctionAddress("glMultiTexCoordP2ui")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glMultiTexCoordP3ui = GLContext.getFunctionAddress("glMultiTexCoordP3ui")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glMultiTexCoordP4ui = GLContext.getFunctionAddress("glMultiTexCoordP4ui")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glMultiTexCoordP1uiv = GLContext.getFunctionAddress("glMultiTexCoordP1uiv")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glMultiTexCoordP2uiv = GLContext.getFunctionAddress("glMultiTexCoordP2uiv")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glMultiTexCoordP3uiv = GLContext.getFunctionAddress("glMultiTexCoordP3uiv")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glMultiTexCoordP4uiv = GLContext.getFunctionAddress("glMultiTexCoordP4uiv")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glNormalP3ui = GLContext.getFunctionAddress("glNormalP3ui")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glNormalP3uiv = GLContext.getFunctionAddress("glNormalP3uiv")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glColorP3ui = GLContext.getFunctionAddress("glColorP3ui")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glColorP4ui = GLContext.getFunctionAddress("glColorP4ui")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glColorP3uiv = GLContext.getFunctionAddress("glColorP3uiv")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glColorP4uiv = GLContext.getFunctionAddress("glColorP4uiv")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glSecondaryColorP3ui = GLContext.getFunctionAddress("glSecondaryColorP3ui")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glSecondaryColorP3uiv = GLContext.getFunctionAddress("glSecondaryColorP3uiv")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glVertexAttribP1ui = GLContext.getFunctionAddress("glVertexAttribP1ui")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glVertexAttribP2ui = GLContext.getFunctionAddress("glVertexAttribP2ui")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glVertexAttribP3ui = GLContext.getFunctionAddress("glVertexAttribP3ui")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glVertexAttribP4ui = GLContext.getFunctionAddress("glVertexAttribP4ui")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glVertexAttribP1uiv = GLContext.getFunctionAddress("glVertexAttribP1uiv")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glVertexAttribP2uiv = GLContext.getFunctionAddress("glVertexAttribP2uiv")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glVertexAttribP3uiv = GLContext.getFunctionAddress("glVertexAttribP3uiv")) != 0L) ? 1 : 0) & ((forwardCompatible || (this.glVertexAttribP4uiv = GLContext.getFunctionAddress("glVertexAttribP4uiv")) != 0L) ? 1 : 0);
  }
  
  private boolean GL40_initNativeFunctionAddresses() {
    return (((this.glBlendEquationi = GLContext.getFunctionAddress("glBlendEquationi")) != 0L)) & (((this.glBlendEquationSeparatei = GLContext.getFunctionAddress("glBlendEquationSeparatei")) != 0L)) & (((this.glBlendFunci = GLContext.getFunctionAddress("glBlendFunci")) != 0L) ? 1 : 0) & (((this.glBlendFuncSeparatei = GLContext.getFunctionAddress("glBlendFuncSeparatei")) != 0L) ? 1 : 0) & (((this.glDrawArraysIndirect = GLContext.getFunctionAddress("glDrawArraysIndirect")) != 0L) ? 1 : 0) & (((this.glDrawElementsIndirect = GLContext.getFunctionAddress("glDrawElementsIndirect")) != 0L) ? 1 : 0) & (((this.glUniform1d = GLContext.getFunctionAddress("glUniform1d")) != 0L) ? 1 : 0) & (((this.glUniform2d = GLContext.getFunctionAddress("glUniform2d")) != 0L) ? 1 : 0) & (((this.glUniform3d = GLContext.getFunctionAddress("glUniform3d")) != 0L) ? 1 : 0) & (((this.glUniform4d = GLContext.getFunctionAddress("glUniform4d")) != 0L) ? 1 : 0) & (((this.glUniform1dv = GLContext.getFunctionAddress("glUniform1dv")) != 0L) ? 1 : 0) & (((this.glUniform2dv = GLContext.getFunctionAddress("glUniform2dv")) != 0L) ? 1 : 0) & (((this.glUniform3dv = GLContext.getFunctionAddress("glUniform3dv")) != 0L) ? 1 : 0) & (((this.glUniform4dv = GLContext.getFunctionAddress("glUniform4dv")) != 0L) ? 1 : 0) & (((this.glUniformMatrix2dv = GLContext.getFunctionAddress("glUniformMatrix2dv")) != 0L) ? 1 : 0) & (((this.glUniformMatrix3dv = GLContext.getFunctionAddress("glUniformMatrix3dv")) != 0L) ? 1 : 0) & (((this.glUniformMatrix4dv = GLContext.getFunctionAddress("glUniformMatrix4dv")) != 0L) ? 1 : 0) & (((this.glUniformMatrix2x3dv = GLContext.getFunctionAddress("glUniformMatrix2x3dv")) != 0L) ? 1 : 0) & (((this.glUniformMatrix2x4dv = GLContext.getFunctionAddress("glUniformMatrix2x4dv")) != 0L) ? 1 : 0) & (((this.glUniformMatrix3x2dv = GLContext.getFunctionAddress("glUniformMatrix3x2dv")) != 0L) ? 1 : 0) & (((this.glUniformMatrix3x4dv = GLContext.getFunctionAddress("glUniformMatrix3x4dv")) != 0L) ? 1 : 0) & (((this.glUniformMatrix4x2dv = GLContext.getFunctionAddress("glUniformMatrix4x2dv")) != 0L) ? 1 : 0) & (((this.glUniformMatrix4x3dv = GLContext.getFunctionAddress("glUniformMatrix4x3dv")) != 0L) ? 1 : 0) & (((this.glGetUniformdv = GLContext.getFunctionAddress("glGetUniformdv")) != 0L) ? 1 : 0) & (((this.glMinSampleShading = GLContext.getFunctionAddress("glMinSampleShading")) != 0L) ? 1 : 0) & (((this.glGetSubroutineUniformLocation = GLContext.getFunctionAddress("glGetSubroutineUniformLocation")) != 0L) ? 1 : 0) & (((this.glGetSubroutineIndex = GLContext.getFunctionAddress("glGetSubroutineIndex")) != 0L) ? 1 : 0) & (((this.glGetActiveSubroutineUniformiv = GLContext.getFunctionAddress("glGetActiveSubroutineUniformiv")) != 0L) ? 1 : 0) & (((this.glGetActiveSubroutineUniformName = GLContext.getFunctionAddress("glGetActiveSubroutineUniformName")) != 0L) ? 1 : 0) & (((this.glGetActiveSubroutineName = GLContext.getFunctionAddress("glGetActiveSubroutineName")) != 0L) ? 1 : 0) & (((this.glUniformSubroutinesuiv = GLContext.getFunctionAddress("glUniformSubroutinesuiv")) != 0L) ? 1 : 0) & (((this.glGetUniformSubroutineuiv = GLContext.getFunctionAddress("glGetUniformSubroutineuiv")) != 0L) ? 1 : 0) & (((this.glGetProgramStageiv = GLContext.getFunctionAddress("glGetProgramStageiv")) != 0L) ? 1 : 0) & (((this.glPatchParameteri = GLContext.getFunctionAddress("glPatchParameteri")) != 0L) ? 1 : 0) & (((this.glPatchParameterfv = GLContext.getFunctionAddress("glPatchParameterfv")) != 0L) ? 1 : 0) & (((this.glBindTransformFeedback = GLContext.getFunctionAddress("glBindTransformFeedback")) != 0L) ? 1 : 0) & (((this.glDeleteTransformFeedbacks = GLContext.getFunctionAddress("glDeleteTransformFeedbacks")) != 0L) ? 1 : 0) & (((this.glGenTransformFeedbacks = GLContext.getFunctionAddress("glGenTransformFeedbacks")) != 0L) ? 1 : 0) & (((this.glIsTransformFeedback = GLContext.getFunctionAddress("glIsTransformFeedback")) != 0L) ? 1 : 0) & (((this.glPauseTransformFeedback = GLContext.getFunctionAddress("glPauseTransformFeedback")) != 0L) ? 1 : 0) & (((this.glResumeTransformFeedback = GLContext.getFunctionAddress("glResumeTransformFeedback")) != 0L) ? 1 : 0) & (((this.glDrawTransformFeedback = GLContext.getFunctionAddress("glDrawTransformFeedback")) != 0L) ? 1 : 0) & (((this.glDrawTransformFeedbackStream = GLContext.getFunctionAddress("glDrawTransformFeedbackStream")) != 0L) ? 1 : 0) & (((this.glBeginQueryIndexed = GLContext.getFunctionAddress("glBeginQueryIndexed")) != 0L) ? 1 : 0) & (((this.glEndQueryIndexed = GLContext.getFunctionAddress("glEndQueryIndexed")) != 0L) ? 1 : 0) & (((this.glGetQueryIndexediv = GLContext.getFunctionAddress("glGetQueryIndexediv")) != 0L) ? 1 : 0);
  }
  
  private boolean GL41_initNativeFunctionAddresses() {
    return (((this.glReleaseShaderCompiler = GLContext.getFunctionAddress("glReleaseShaderCompiler")) != 0L)) & (((this.glShaderBinary = GLContext.getFunctionAddress("glShaderBinary")) != 0L)) & (((this.glGetShaderPrecisionFormat = GLContext.getFunctionAddress("glGetShaderPrecisionFormat")) != 0L) ? 1 : 0) & (((this.glDepthRangef = GLContext.getFunctionAddress("glDepthRangef")) != 0L) ? 1 : 0) & (((this.glClearDepthf = GLContext.getFunctionAddress("glClearDepthf")) != 0L) ? 1 : 0) & (((this.glGetProgramBinary = GLContext.getFunctionAddress("glGetProgramBinary")) != 0L) ? 1 : 0) & (((this.glProgramBinary = GLContext.getFunctionAddress("glProgramBinary")) != 0L) ? 1 : 0) & (((this.glProgramParameteri = GLContext.getFunctionAddress("glProgramParameteri")) != 0L) ? 1 : 0) & (((this.glUseProgramStages = GLContext.getFunctionAddress("glUseProgramStages")) != 0L) ? 1 : 0) & (((this.glActiveShaderProgram = GLContext.getFunctionAddress("glActiveShaderProgram")) != 0L) ? 1 : 0) & (((this.glCreateShaderProgramv = GLContext.getFunctionAddress("glCreateShaderProgramv")) != 0L) ? 1 : 0) & (((this.glBindProgramPipeline = GLContext.getFunctionAddress("glBindProgramPipeline")) != 0L) ? 1 : 0) & (((this.glDeleteProgramPipelines = GLContext.getFunctionAddress("glDeleteProgramPipelines")) != 0L) ? 1 : 0) & (((this.glGenProgramPipelines = GLContext.getFunctionAddress("glGenProgramPipelines")) != 0L) ? 1 : 0) & (((this.glIsProgramPipeline = GLContext.getFunctionAddress("glIsProgramPipeline")) != 0L) ? 1 : 0) & (((this.glGetProgramPipelineiv = GLContext.getFunctionAddress("glGetProgramPipelineiv")) != 0L) ? 1 : 0) & (((this.glProgramUniform1i = GLContext.getFunctionAddress("glProgramUniform1i")) != 0L) ? 1 : 0) & (((this.glProgramUniform2i = GLContext.getFunctionAddress("glProgramUniform2i")) != 0L) ? 1 : 0) & (((this.glProgramUniform3i = GLContext.getFunctionAddress("glProgramUniform3i")) != 0L) ? 1 : 0) & (((this.glProgramUniform4i = GLContext.getFunctionAddress("glProgramUniform4i")) != 0L) ? 1 : 0) & (((this.glProgramUniform1f = GLContext.getFunctionAddress("glProgramUniform1f")) != 0L) ? 1 : 0) & (((this.glProgramUniform2f = GLContext.getFunctionAddress("glProgramUniform2f")) != 0L) ? 1 : 0) & (((this.glProgramUniform3f = GLContext.getFunctionAddress("glProgramUniform3f")) != 0L) ? 1 : 0) & (((this.glProgramUniform4f = GLContext.getFunctionAddress("glProgramUniform4f")) != 0L) ? 1 : 0) & (((this.glProgramUniform1d = GLContext.getFunctionAddress("glProgramUniform1d")) != 0L) ? 1 : 0) & (((this.glProgramUniform2d = GLContext.getFunctionAddress("glProgramUniform2d")) != 0L) ? 1 : 0) & (((this.glProgramUniform3d = GLContext.getFunctionAddress("glProgramUniform3d")) != 0L) ? 1 : 0) & (((this.glProgramUniform4d = GLContext.getFunctionAddress("glProgramUniform4d")) != 0L) ? 1 : 0) & (((this.glProgramUniform1iv = GLContext.getFunctionAddress("glProgramUniform1iv")) != 0L) ? 1 : 0) & (((this.glProgramUniform2iv = GLContext.getFunctionAddress("glProgramUniform2iv")) != 0L) ? 1 : 0) & (((this.glProgramUniform3iv = GLContext.getFunctionAddress("glProgramUniform3iv")) != 0L) ? 1 : 0) & (((this.glProgramUniform4iv = GLContext.getFunctionAddress("glProgramUniform4iv")) != 0L) ? 1 : 0) & (((this.glProgramUniform1fv = GLContext.getFunctionAddress("glProgramUniform1fv")) != 0L) ? 1 : 0) & (((this.glProgramUniform2fv = GLContext.getFunctionAddress("glProgramUniform2fv")) != 0L) ? 1 : 0) & (((this.glProgramUniform3fv = GLContext.getFunctionAddress("glProgramUniform3fv")) != 0L) ? 1 : 0) & (((this.glProgramUniform4fv = GLContext.getFunctionAddress("glProgramUniform4fv")) != 0L) ? 1 : 0) & (((this.glProgramUniform1dv = GLContext.getFunctionAddress("glProgramUniform1dv")) != 0L) ? 1 : 0) & (((this.glProgramUniform2dv = GLContext.getFunctionAddress("glProgramUniform2dv")) != 0L) ? 1 : 0) & (((this.glProgramUniform3dv = GLContext.getFunctionAddress("glProgramUniform3dv")) != 0L) ? 1 : 0) & (((this.glProgramUniform4dv = GLContext.getFunctionAddress("glProgramUniform4dv")) != 0L) ? 1 : 0) & (((this.glProgramUniform1ui = GLContext.getFunctionAddress("glProgramUniform1ui")) != 0L) ? 1 : 0) & (((this.glProgramUniform2ui = GLContext.getFunctionAddress("glProgramUniform2ui")) != 0L) ? 1 : 0) & (((this.glProgramUniform3ui = GLContext.getFunctionAddress("glProgramUniform3ui")) != 0L) ? 1 : 0) & (((this.glProgramUniform4ui = GLContext.getFunctionAddress("glProgramUniform4ui")) != 0L) ? 1 : 0) & (((this.glProgramUniform1uiv = GLContext.getFunctionAddress("glProgramUniform1uiv")) != 0L) ? 1 : 0) & (((this.glProgramUniform2uiv = GLContext.getFunctionAddress("glProgramUniform2uiv")) != 0L) ? 1 : 0) & (((this.glProgramUniform3uiv = GLContext.getFunctionAddress("glProgramUniform3uiv")) != 0L) ? 1 : 0) & (((this.glProgramUniform4uiv = GLContext.getFunctionAddress("glProgramUniform4uiv")) != 0L) ? 1 : 0) & (((this.glProgramUniformMatrix2fv = GLContext.getFunctionAddress("glProgramUniformMatrix2fv")) != 0L) ? 1 : 0) & (((this.glProgramUniformMatrix3fv = GLContext.getFunctionAddress("glProgramUniformMatrix3fv")) != 0L) ? 1 : 0) & (((this.glProgramUniformMatrix4fv = GLContext.getFunctionAddress("glProgramUniformMatrix4fv")) != 0L) ? 1 : 0) & (((this.glProgramUniformMatrix2dv = GLContext.getFunctionAddress("glProgramUniformMatrix2dv")) != 0L) ? 1 : 0) & (((this.glProgramUniformMatrix3dv = GLContext.getFunctionAddress("glProgramUniformMatrix3dv")) != 0L) ? 1 : 0) & (((this.glProgramUniformMatrix4dv = GLContext.getFunctionAddress("glProgramUniformMatrix4dv")) != 0L) ? 1 : 0) & (((this.glProgramUniformMatrix2x3fv = GLContext.getFunctionAddress("glProgramUniformMatrix2x3fv")) != 0L) ? 1 : 0) & (((this.glProgramUniformMatrix3x2fv = GLContext.getFunctionAddress("glProgramUniformMatrix3x2fv")) != 0L) ? 1 : 0) & (((this.glProgramUniformMatrix2x4fv = GLContext.getFunctionAddress("glProgramUniformMatrix2x4fv")) != 0L) ? 1 : 0) & (((this.glProgramUniformMatrix4x2fv = GLContext.getFunctionAddress("glProgramUniformMatrix4x2fv")) != 0L) ? 1 : 0) & (((this.glProgramUniformMatrix3x4fv = GLContext.getFunctionAddress("glProgramUniformMatrix3x4fv")) != 0L) ? 1 : 0) & (((this.glProgramUniformMatrix4x3fv = GLContext.getFunctionAddress("glProgramUniformMatrix4x3fv")) != 0L) ? 1 : 0) & (((this.glProgramUniformMatrix2x3dv = GLContext.getFunctionAddress("glProgramUniformMatrix2x3dv")) != 0L) ? 1 : 0) & (((this.glProgramUniformMatrix3x2dv = GLContext.getFunctionAddress("glProgramUniformMatrix3x2dv")) != 0L) ? 1 : 0) & (((this.glProgramUniformMatrix2x4dv = GLContext.getFunctionAddress("glProgramUniformMatrix2x4dv")) != 0L) ? 1 : 0) & (((this.glProgramUniformMatrix4x2dv = GLContext.getFunctionAddress("glProgramUniformMatrix4x2dv")) != 0L) ? 1 : 0) & (((this.glProgramUniformMatrix3x4dv = GLContext.getFunctionAddress("glProgramUniformMatrix3x4dv")) != 0L) ? 1 : 0) & (((this.glProgramUniformMatrix4x3dv = GLContext.getFunctionAddress("glProgramUniformMatrix4x3dv")) != 0L) ? 1 : 0) & (((this.glValidateProgramPipeline = GLContext.getFunctionAddress("glValidateProgramPipeline")) != 0L) ? 1 : 0) & (((this.glGetProgramPipelineInfoLog = GLContext.getFunctionAddress("glGetProgramPipelineInfoLog")) != 0L) ? 1 : 0) & (((this.glVertexAttribL1d = GLContext.getFunctionAddress("glVertexAttribL1d")) != 0L) ? 1 : 0) & (((this.glVertexAttribL2d = GLContext.getFunctionAddress("glVertexAttribL2d")) != 0L) ? 1 : 0) & (((this.glVertexAttribL3d = GLContext.getFunctionAddress("glVertexAttribL3d")) != 0L) ? 1 : 0) & (((this.glVertexAttribL4d = GLContext.getFunctionAddress("glVertexAttribL4d")) != 0L) ? 1 : 0) & (((this.glVertexAttribL1dv = GLContext.getFunctionAddress("glVertexAttribL1dv")) != 0L) ? 1 : 0) & (((this.glVertexAttribL2dv = GLContext.getFunctionAddress("glVertexAttribL2dv")) != 0L) ? 1 : 0) & (((this.glVertexAttribL3dv = GLContext.getFunctionAddress("glVertexAttribL3dv")) != 0L) ? 1 : 0) & (((this.glVertexAttribL4dv = GLContext.getFunctionAddress("glVertexAttribL4dv")) != 0L) ? 1 : 0) & (((this.glVertexAttribLPointer = GLContext.getFunctionAddress("glVertexAttribLPointer")) != 0L) ? 1 : 0) & (((this.glGetVertexAttribLdv = GLContext.getFunctionAddress("glGetVertexAttribLdv")) != 0L) ? 1 : 0) & (((this.glViewportArrayv = GLContext.getFunctionAddress("glViewportArrayv")) != 0L) ? 1 : 0) & (((this.glViewportIndexedf = GLContext.getFunctionAddress("glViewportIndexedf")) != 0L) ? 1 : 0) & (((this.glViewportIndexedfv = GLContext.getFunctionAddress("glViewportIndexedfv")) != 0L) ? 1 : 0) & (((this.glScissorArrayv = GLContext.getFunctionAddress("glScissorArrayv")) != 0L) ? 1 : 0) & (((this.glScissorIndexed = GLContext.getFunctionAddress("glScissorIndexed")) != 0L) ? 1 : 0) & (((this.glScissorIndexedv = GLContext.getFunctionAddress("glScissorIndexedv")) != 0L) ? 1 : 0) & (((this.glDepthRangeArrayv = GLContext.getFunctionAddress("glDepthRangeArrayv")) != 0L) ? 1 : 0) & (((this.glDepthRangeIndexed = GLContext.getFunctionAddress("glDepthRangeIndexed")) != 0L) ? 1 : 0) & (((this.glGetFloati_v = GLContext.getFunctionAddress("glGetFloati_v")) != 0L) ? 1 : 0) & (((this.glGetDoublei_v = GLContext.getFunctionAddress("glGetDoublei_v")) != 0L) ? 1 : 0);
  }
  
  private boolean GL42_initNativeFunctionAddresses() {
    return (((this.glGetActiveAtomicCounterBufferiv = GLContext.getFunctionAddress("glGetActiveAtomicCounterBufferiv")) != 0L)) & (((this.glTexStorage1D = GLContext.getFunctionAddress("glTexStorage1D")) != 0L)) & (((this.glTexStorage2D = GLContext.getFunctionAddress("glTexStorage2D")) != 0L) ? 1 : 0) & (((this.glTexStorage3D = GLContext.getFunctionAddress("glTexStorage3D")) != 0L) ? 1 : 0) & (((this.glDrawTransformFeedbackInstanced = GLContext.getFunctionAddress("glDrawTransformFeedbackInstanced")) != 0L) ? 1 : 0) & (((this.glDrawTransformFeedbackStreamInstanced = GLContext.getFunctionAddress("glDrawTransformFeedbackStreamInstanced")) != 0L) ? 1 : 0) & (((this.glDrawArraysInstancedBaseInstance = GLContext.getFunctionAddress("glDrawArraysInstancedBaseInstance")) != 0L) ? 1 : 0) & (((this.glDrawElementsInstancedBaseInstance = GLContext.getFunctionAddress("glDrawElementsInstancedBaseInstance")) != 0L) ? 1 : 0) & (((this.glDrawElementsInstancedBaseVertexBaseInstance = GLContext.getFunctionAddress("glDrawElementsInstancedBaseVertexBaseInstance")) != 0L) ? 1 : 0) & (((this.glBindImageTexture = GLContext.getFunctionAddress("glBindImageTexture")) != 0L) ? 1 : 0) & (((this.glMemoryBarrier = GLContext.getFunctionAddress("glMemoryBarrier")) != 0L) ? 1 : 0) & (((this.glGetInternalformativ = GLContext.getFunctionAddress("glGetInternalformativ")) != 0L) ? 1 : 0);
  }
  
  private boolean GL43_initNativeFunctionAddresses() {
    return (((this.glClearBufferData = GLContext.getFunctionAddress("glClearBufferData")) != 0L)) & (((this.glClearBufferSubData = GLContext.getFunctionAddress("glClearBufferSubData")) != 0L)) & (((this.glDispatchCompute = GLContext.getFunctionAddress("glDispatchCompute")) != 0L) ? 1 : 0) & (((this.glDispatchComputeIndirect = GLContext.getFunctionAddress("glDispatchComputeIndirect")) != 0L) ? 1 : 0) & (((this.glCopyImageSubData = GLContext.getFunctionAddress("glCopyImageSubData")) != 0L) ? 1 : 0) & (((this.glDebugMessageControl = GLContext.getFunctionAddress("glDebugMessageControl")) != 0L) ? 1 : 0) & (((this.glDebugMessageInsert = GLContext.getFunctionAddress("glDebugMessageInsert")) != 0L) ? 1 : 0) & (((this.glDebugMessageCallback = GLContext.getFunctionAddress("glDebugMessageCallback")) != 0L) ? 1 : 0) & (((this.glGetDebugMessageLog = GLContext.getFunctionAddress("glGetDebugMessageLog")) != 0L) ? 1 : 0) & (((this.glPushDebugGroup = GLContext.getFunctionAddress("glPushDebugGroup")) != 0L) ? 1 : 0) & (((this.glPopDebugGroup = GLContext.getFunctionAddress("glPopDebugGroup")) != 0L) ? 1 : 0) & (((this.glObjectLabel = GLContext.getFunctionAddress("glObjectLabel")) != 0L) ? 1 : 0) & (((this.glGetObjectLabel = GLContext.getFunctionAddress("glGetObjectLabel")) != 0L) ? 1 : 0) & (((this.glObjectPtrLabel = GLContext.getFunctionAddress("glObjectPtrLabel")) != 0L) ? 1 : 0) & (((this.glGetObjectPtrLabel = GLContext.getFunctionAddress("glGetObjectPtrLabel")) != 0L) ? 1 : 0) & (((this.glFramebufferParameteri = GLContext.getFunctionAddress("glFramebufferParameteri")) != 0L) ? 1 : 0) & (((this.glGetFramebufferParameteriv = GLContext.getFunctionAddress("glGetFramebufferParameteriv")) != 0L) ? 1 : 0) & (((this.glGetInternalformati64v = GLContext.getFunctionAddress("glGetInternalformati64v")) != 0L) ? 1 : 0) & (((this.glInvalidateTexSubImage = GLContext.getFunctionAddress("glInvalidateTexSubImage")) != 0L) ? 1 : 0) & (((this.glInvalidateTexImage = GLContext.getFunctionAddress("glInvalidateTexImage")) != 0L) ? 1 : 0) & (((this.glInvalidateBufferSubData = GLContext.getFunctionAddress("glInvalidateBufferSubData")) != 0L) ? 1 : 0) & (((this.glInvalidateBufferData = GLContext.getFunctionAddress("glInvalidateBufferData")) != 0L) ? 1 : 0) & (((this.glInvalidateFramebuffer = GLContext.getFunctionAddress("glInvalidateFramebuffer")) != 0L) ? 1 : 0) & (((this.glInvalidateSubFramebuffer = GLContext.getFunctionAddress("glInvalidateSubFramebuffer")) != 0L) ? 1 : 0) & (((this.glMultiDrawArraysIndirect = GLContext.getFunctionAddress("glMultiDrawArraysIndirect")) != 0L) ? 1 : 0) & (((this.glMultiDrawElementsIndirect = GLContext.getFunctionAddress("glMultiDrawElementsIndirect")) != 0L) ? 1 : 0) & (((this.glGetProgramInterfaceiv = GLContext.getFunctionAddress("glGetProgramInterfaceiv")) != 0L) ? 1 : 0) & (((this.glGetProgramResourceIndex = GLContext.getFunctionAddress("glGetProgramResourceIndex")) != 0L) ? 1 : 0) & (((this.glGetProgramResourceName = GLContext.getFunctionAddress("glGetProgramResourceName")) != 0L) ? 1 : 0) & (((this.glGetProgramResourceiv = GLContext.getFunctionAddress("glGetProgramResourceiv")) != 0L) ? 1 : 0) & (((this.glGetProgramResourceLocation = GLContext.getFunctionAddress("glGetProgramResourceLocation")) != 0L) ? 1 : 0) & (((this.glGetProgramResourceLocationIndex = GLContext.getFunctionAddress("glGetProgramResourceLocationIndex")) != 0L) ? 1 : 0) & (((this.glShaderStorageBlockBinding = GLContext.getFunctionAddress("glShaderStorageBlockBinding")) != 0L) ? 1 : 0) & (((this.glTexBufferRange = GLContext.getFunctionAddress("glTexBufferRange")) != 0L) ? 1 : 0) & (((this.glTexStorage2DMultisample = GLContext.getFunctionAddress("glTexStorage2DMultisample")) != 0L) ? 1 : 0) & (((this.glTexStorage3DMultisample = GLContext.getFunctionAddress("glTexStorage3DMultisample")) != 0L) ? 1 : 0) & (((this.glTextureView = GLContext.getFunctionAddress("glTextureView")) != 0L) ? 1 : 0) & (((this.glBindVertexBuffer = GLContext.getFunctionAddress("glBindVertexBuffer")) != 0L) ? 1 : 0) & (((this.glVertexAttribFormat = GLContext.getFunctionAddress("glVertexAttribFormat")) != 0L) ? 1 : 0) & (((this.glVertexAttribIFormat = GLContext.getFunctionAddress("glVertexAttribIFormat")) != 0L) ? 1 : 0) & (((this.glVertexAttribLFormat = GLContext.getFunctionAddress("glVertexAttribLFormat")) != 0L) ? 1 : 0) & (((this.glVertexAttribBinding = GLContext.getFunctionAddress("glVertexAttribBinding")) != 0L) ? 1 : 0) & (((this.glVertexBindingDivisor = GLContext.getFunctionAddress("glVertexBindingDivisor")) != 0L) ? 1 : 0);
  }
  
  private boolean GL44_initNativeFunctionAddresses() {
    return (((this.glBufferStorage = GLContext.getFunctionAddress("glBufferStorage")) != 0L)) & (((this.glClearTexImage = GLContext.getFunctionAddress("glClearTexImage")) != 0L)) & (((this.glClearTexSubImage = GLContext.getFunctionAddress("glClearTexSubImage")) != 0L) ? 1 : 0) & (((this.glBindBuffersBase = GLContext.getFunctionAddress("glBindBuffersBase")) != 0L) ? 1 : 0) & (((this.glBindBuffersRange = GLContext.getFunctionAddress("glBindBuffersRange")) != 0L) ? 1 : 0) & (((this.glBindTextures = GLContext.getFunctionAddress("glBindTextures")) != 0L) ? 1 : 0) & (((this.glBindSamplers = GLContext.getFunctionAddress("glBindSamplers")) != 0L) ? 1 : 0) & (((this.glBindImageTextures = GLContext.getFunctionAddress("glBindImageTextures")) != 0L) ? 1 : 0) & (((this.glBindVertexBuffers = GLContext.getFunctionAddress("glBindVertexBuffers")) != 0L) ? 1 : 0);
  }
  
  private boolean GL45_initNativeFunctionAddresses() {
    return (((this.glClipControl = GLContext.getFunctionAddress("glClipControl")) != 0L)) & (((this.glCreateTransformFeedbacks = GLContext.getFunctionAddress("glCreateTransformFeedbacks")) != 0L)) & (((this.glTransformFeedbackBufferBase = GLContext.getFunctionAddress("glTransformFeedbackBufferBase")) != 0L) ? 1 : 0) & (((this.glTransformFeedbackBufferRange = GLContext.getFunctionAddress("glTransformFeedbackBufferRange")) != 0L) ? 1 : 0) & (((this.glGetTransformFeedbackiv = GLContext.getFunctionAddress("glGetTransformFeedbackiv")) != 0L) ? 1 : 0) & (((this.glGetTransformFeedbacki_v = GLContext.getFunctionAddress("glGetTransformFeedbacki_v")) != 0L) ? 1 : 0) & (((this.glGetTransformFeedbacki64_v = GLContext.getFunctionAddress("glGetTransformFeedbacki64_v")) != 0L) ? 1 : 0) & (((this.glCreateBuffers = GLContext.getFunctionAddress("glCreateBuffers")) != 0L) ? 1 : 0) & (((this.glNamedBufferStorage = GLContext.getFunctionAddress("glNamedBufferStorage")) != 0L) ? 1 : 0) & (((this.glNamedBufferData = GLContext.getFunctionAddress("glNamedBufferData")) != 0L) ? 1 : 0) & (((this.glNamedBufferSubData = GLContext.getFunctionAddress("glNamedBufferSubData")) != 0L) ? 1 : 0) & (((this.glCopyNamedBufferSubData = GLContext.getFunctionAddress("glCopyNamedBufferSubData")) != 0L) ? 1 : 0) & (((this.glClearNamedBufferData = GLContext.getFunctionAddress("glClearNamedBufferData")) != 0L) ? 1 : 0) & (((this.glClearNamedBufferSubData = GLContext.getFunctionAddress("glClearNamedBufferSubData")) != 0L) ? 1 : 0) & (((this.glMapNamedBuffer = GLContext.getFunctionAddress("glMapNamedBuffer")) != 0L) ? 1 : 0) & (((this.glMapNamedBufferRange = GLContext.getFunctionAddress("glMapNamedBufferRange")) != 0L) ? 1 : 0) & (((this.glUnmapNamedBuffer = GLContext.getFunctionAddress("glUnmapNamedBuffer")) != 0L) ? 1 : 0) & (((this.glFlushMappedNamedBufferRange = GLContext.getFunctionAddress("glFlushMappedNamedBufferRange")) != 0L) ? 1 : 0) & (((this.glGetNamedBufferParameteriv = GLContext.getFunctionAddress("glGetNamedBufferParameteriv")) != 0L) ? 1 : 0) & (((this.glGetNamedBufferParameteri64v = GLContext.getFunctionAddress("glGetNamedBufferParameteri64v")) != 0L) ? 1 : 0) & (((this.glGetNamedBufferPointerv = GLContext.getFunctionAddress("glGetNamedBufferPointerv")) != 0L) ? 1 : 0) & (((this.glGetNamedBufferSubData = GLContext.getFunctionAddress("glGetNamedBufferSubData")) != 0L) ? 1 : 0) & (((this.glCreateFramebuffers = GLContext.getFunctionAddress("glCreateFramebuffers")) != 0L) ? 1 : 0) & (((this.glNamedFramebufferRenderbuffer = GLContext.getFunctionAddress("glNamedFramebufferRenderbuffer")) != 0L) ? 1 : 0) & (((this.glNamedFramebufferParameteri = GLContext.getFunctionAddress("glNamedFramebufferParameteri")) != 0L) ? 1 : 0) & (((this.glNamedFramebufferTexture = GLContext.getFunctionAddress("glNamedFramebufferTexture")) != 0L) ? 1 : 0) & (((this.glNamedFramebufferTextureLayer = GLContext.getFunctionAddress("glNamedFramebufferTextureLayer")) != 0L) ? 1 : 0) & (((this.glNamedFramebufferDrawBuffer = GLContext.getFunctionAddress("glNamedFramebufferDrawBuffer")) != 0L) ? 1 : 0) & (((this.glNamedFramebufferDrawBuffers = GLContext.getFunctionAddress("glNamedFramebufferDrawBuffers")) != 0L) ? 1 : 0) & (((this.glNamedFramebufferReadBuffer = GLContext.getFunctionAddress("glNamedFramebufferReadBuffer")) != 0L) ? 1 : 0) & (((this.glInvalidateNamedFramebufferData = GLContext.getFunctionAddress("glInvalidateNamedFramebufferData")) != 0L) ? 1 : 0) & (((this.glInvalidateNamedFramebufferSubData = GLContext.getFunctionAddress("glInvalidateNamedFramebufferSubData")) != 0L) ? 1 : 0) & (((this.glClearNamedFramebufferiv = GLContext.getFunctionAddress("glClearNamedFramebufferiv")) != 0L) ? 1 : 0) & (((this.glClearNamedFramebufferuiv = GLContext.getFunctionAddress("glClearNamedFramebufferuiv")) != 0L) ? 1 : 0) & (((this.glClearNamedFramebufferfv = GLContext.getFunctionAddress("glClearNamedFramebufferfv")) != 0L) ? 1 : 0) & (((this.glClearNamedFramebufferfi = GLContext.getFunctionAddress("glClearNamedFramebufferfi")) != 0L) ? 1 : 0) & (((this.glBlitNamedFramebuffer = GLContext.getFunctionAddress("glBlitNamedFramebuffer")) != 0L) ? 1 : 0) & (((this.glCheckNamedFramebufferStatus = GLContext.getFunctionAddress("glCheckNamedFramebufferStatus")) != 0L) ? 1 : 0) & (((this.glGetNamedFramebufferParameteriv = GLContext.getFunctionAddress("glGetNamedFramebufferParameteriv")) != 0L) ? 1 : 0) & (((this.glGetNamedFramebufferAttachmentParameteriv = GLContext.getFunctionAddress("glGetNamedFramebufferAttachmentParameteriv")) != 0L) ? 1 : 0) & (((this.glCreateRenderbuffers = GLContext.getFunctionAddress("glCreateRenderbuffers")) != 0L) ? 1 : 0) & (((this.glNamedRenderbufferStorage = GLContext.getFunctionAddress("glNamedRenderbufferStorage")) != 0L) ? 1 : 0) & (((this.glNamedRenderbufferStorageMultisample = GLContext.getFunctionAddress("glNamedRenderbufferStorageMultisample")) != 0L) ? 1 : 0) & (((this.glGetNamedRenderbufferParameteriv = GLContext.getFunctionAddress("glGetNamedRenderbufferParameteriv")) != 0L) ? 1 : 0) & (((this.glCreateTextures = GLContext.getFunctionAddress("glCreateTextures")) != 0L) ? 1 : 0) & (((this.glTextureBuffer = GLContext.getFunctionAddress("glTextureBuffer")) != 0L) ? 1 : 0) & (((this.glTextureBufferRange = GLContext.getFunctionAddress("glTextureBufferRange")) != 0L) ? 1 : 0) & (((this.glTextureStorage1D = GLContext.getFunctionAddress("glTextureStorage1D")) != 0L) ? 1 : 0) & (((this.glTextureStorage2D = GLContext.getFunctionAddress("glTextureStorage2D")) != 0L) ? 1 : 0) & (((this.glTextureStorage3D = GLContext.getFunctionAddress("glTextureStorage3D")) != 0L) ? 1 : 0) & (((this.glTextureStorage2DMultisample = GLContext.getFunctionAddress("glTextureStorage2DMultisample")) != 0L) ? 1 : 0) & (((this.glTextureStorage3DMultisample = GLContext.getFunctionAddress("glTextureStorage3DMultisample")) != 0L) ? 1 : 0) & (((this.glTextureSubImage1D = GLContext.getFunctionAddress("glTextureSubImage1D")) != 0L) ? 1 : 0) & (((this.glTextureSubImage2D = GLContext.getFunctionAddress("glTextureSubImage2D")) != 0L) ? 1 : 0) & (((this.glTextureSubImage3D = GLContext.getFunctionAddress("glTextureSubImage3D")) != 0L) ? 1 : 0) & (((this.glCompressedTextureSubImage1D = GLContext.getFunctionAddress("glCompressedTextureSubImage1D")) != 0L) ? 1 : 0) & (((this.glCompressedTextureSubImage2D = GLContext.getFunctionAddress("glCompressedTextureSubImage2D")) != 0L) ? 1 : 0) & (((this.glCompressedTextureSubImage3D = GLContext.getFunctionAddress("glCompressedTextureSubImage3D")) != 0L) ? 1 : 0) & (((this.glCopyTextureSubImage1D = GLContext.getFunctionAddress("glCopyTextureSubImage1D")) != 0L) ? 1 : 0) & (((this.glCopyTextureSubImage2D = GLContext.getFunctionAddress("glCopyTextureSubImage2D")) != 0L) ? 1 : 0) & (((this.glCopyTextureSubImage3D = GLContext.getFunctionAddress("glCopyTextureSubImage3D")) != 0L) ? 1 : 0) & (((this.glTextureParameterf = GLContext.getFunctionAddress("glTextureParameterf")) != 0L) ? 1 : 0) & (((this.glTextureParameterfv = GLContext.getFunctionAddress("glTextureParameterfv")) != 0L) ? 1 : 0) & (((this.glTextureParameteri = GLContext.getFunctionAddress("glTextureParameteri")) != 0L) ? 1 : 0) & (((this.glTextureParameterIiv = GLContext.getFunctionAddress("glTextureParameterIiv")) != 0L) ? 1 : 0) & (((this.glTextureParameterIuiv = GLContext.getFunctionAddress("glTextureParameterIuiv")) != 0L) ? 1 : 0) & (((this.glTextureParameteriv = GLContext.getFunctionAddress("glTextureParameteriv")) != 0L) ? 1 : 0) & (((this.glGenerateTextureMipmap = GLContext.getFunctionAddress("glGenerateTextureMipmap")) != 0L) ? 1 : 0) & (((this.glBindTextureUnit = GLContext.getFunctionAddress("glBindTextureUnit")) != 0L) ? 1 : 0) & (((this.glGetTextureImage = GLContext.getFunctionAddress("glGetTextureImage")) != 0L) ? 1 : 0) & (((this.glGetCompressedTextureImage = GLContext.getFunctionAddress("glGetCompressedTextureImage")) != 0L) ? 1 : 0) & (((this.glGetTextureLevelParameterfv = GLContext.getFunctionAddress("glGetTextureLevelParameterfv")) != 0L) ? 1 : 0) & (((this.glGetTextureLevelParameteriv = GLContext.getFunctionAddress("glGetTextureLevelParameteriv")) != 0L) ? 1 : 0) & (((this.glGetTextureParameterfv = GLContext.getFunctionAddress("glGetTextureParameterfv")) != 0L) ? 1 : 0) & (((this.glGetTextureParameterIiv = GLContext.getFunctionAddress("glGetTextureParameterIiv")) != 0L) ? 1 : 0) & (((this.glGetTextureParameterIuiv = GLContext.getFunctionAddress("glGetTextureParameterIuiv")) != 0L) ? 1 : 0) & (((this.glGetTextureParameteriv = GLContext.getFunctionAddress("glGetTextureParameteriv")) != 0L) ? 1 : 0) & (((this.glCreateVertexArrays = GLContext.getFunctionAddress("glCreateVertexArrays")) != 0L) ? 1 : 0) & (((this.glDisableVertexArrayAttrib = GLContext.getFunctionAddress("glDisableVertexArrayAttrib")) != 0L) ? 1 : 0) & (((this.glEnableVertexArrayAttrib = GLContext.getFunctionAddress("glEnableVertexArrayAttrib")) != 0L) ? 1 : 0) & (((this.glVertexArrayElementBuffer = GLContext.getFunctionAddress("glVertexArrayElementBuffer")) != 0L) ? 1 : 0) & (((this.glVertexArrayVertexBuffer = GLContext.getFunctionAddress("glVertexArrayVertexBuffer")) != 0L) ? 1 : 0) & (((this.glVertexArrayVertexBuffers = GLContext.getFunctionAddress("glVertexArrayVertexBuffers")) != 0L) ? 1 : 0) & (((this.glVertexArrayAttribFormat = GLContext.getFunctionAddress("glVertexArrayAttribFormat")) != 0L) ? 1 : 0) & (((this.glVertexArrayAttribIFormat = GLContext.getFunctionAddress("glVertexArrayAttribIFormat")) != 0L) ? 1 : 0) & (((this.glVertexArrayAttribLFormat = GLContext.getFunctionAddress("glVertexArrayAttribLFormat")) != 0L) ? 1 : 0) & (((this.glVertexArrayAttribBinding = GLContext.getFunctionAddress("glVertexArrayAttribBinding")) != 0L) ? 1 : 0) & (((this.glVertexArrayBindingDivisor = GLContext.getFunctionAddress("glVertexArrayBindingDivisor")) != 0L) ? 1 : 0) & (((this.glGetVertexArrayiv = GLContext.getFunctionAddress("glGetVertexArrayiv")) != 0L) ? 1 : 0) & (((this.glGetVertexArrayIndexediv = GLContext.getFunctionAddress("glGetVertexArrayIndexediv")) != 0L) ? 1 : 0) & (((this.glGetVertexArrayIndexed64iv = GLContext.getFunctionAddress("glGetVertexArrayIndexed64iv")) != 0L) ? 1 : 0) & (((this.glCreateSamplers = GLContext.getFunctionAddress("glCreateSamplers")) != 0L) ? 1 : 0) & (((this.glCreateProgramPipelines = GLContext.getFunctionAddress("glCreateProgramPipelines")) != 0L) ? 1 : 0) & (((this.glCreateQueries = GLContext.getFunctionAddress("glCreateQueries")) != 0L) ? 1 : 0) & (((this.glMemoryBarrierByRegion = GLContext.getFunctionAddress("glMemoryBarrierByRegion")) != 0L) ? 1 : 0) & (((this.glGetTextureSubImage = GLContext.getFunctionAddress("glGetTextureSubImage")) != 0L) ? 1 : 0) & (((this.glGetCompressedTextureSubImage = GLContext.getFunctionAddress("glGetCompressedTextureSubImage")) != 0L) ? 1 : 0) & (((this.glTextureBarrier = GLContext.getFunctionAddress("glTextureBarrier")) != 0L) ? 1 : 0) & (((this.glGetGraphicsResetStatus = GLContext.getFunctionAddress("glGetGraphicsResetStatus")) != 0L) ? 1 : 0) & (((this.glReadnPixels = GLContext.getFunctionAddress("glReadnPixels")) != 0L) ? 1 : 0) & (((this.glGetnUniformfv = GLContext.getFunctionAddress("glGetnUniformfv")) != 0L) ? 1 : 0) & (((this.glGetnUniformiv = GLContext.getFunctionAddress("glGetnUniformiv")) != 0L) ? 1 : 0) & (((this.glGetnUniformuiv = GLContext.getFunctionAddress("glGetnUniformuiv")) != 0L) ? 1 : 0);
  }
  
  private boolean GREMEDY_frame_terminator_initNativeFunctionAddresses() {
    return ((this.glFrameTerminatorGREMEDY = GLContext.getFunctionAddress("glFrameTerminatorGREMEDY")) != 0L);
  }
  
  private boolean GREMEDY_string_marker_initNativeFunctionAddresses() {
    return ((this.glStringMarkerGREMEDY = GLContext.getFunctionAddress("glStringMarkerGREMEDY")) != 0L);
  }
  
  private boolean INTEL_map_texture_initNativeFunctionAddresses() {
    return (((this.glMapTexture2DINTEL = GLContext.getFunctionAddress("glMapTexture2DINTEL")) != 0L)) & (((this.glUnmapTexture2DINTEL = GLContext.getFunctionAddress("glUnmapTexture2DINTEL")) != 0L)) & (((this.glSyncTextureINTEL = GLContext.getFunctionAddress("glSyncTextureINTEL")) != 0L) ? 1 : 0);
  }
  
  private boolean KHR_debug_initNativeFunctionAddresses() {
    return (((this.glDebugMessageControl = GLContext.getFunctionAddress("glDebugMessageControl")) != 0L)) & (((this.glDebugMessageInsert = GLContext.getFunctionAddress("glDebugMessageInsert")) != 0L)) & (((this.glDebugMessageCallback = GLContext.getFunctionAddress("glDebugMessageCallback")) != 0L) ? 1 : 0) & (((this.glGetDebugMessageLog = GLContext.getFunctionAddress("glGetDebugMessageLog")) != 0L) ? 1 : 0) & (((this.glPushDebugGroup = GLContext.getFunctionAddress("glPushDebugGroup")) != 0L) ? 1 : 0) & (((this.glPopDebugGroup = GLContext.getFunctionAddress("glPopDebugGroup")) != 0L) ? 1 : 0) & (((this.glObjectLabel = GLContext.getFunctionAddress("glObjectLabel")) != 0L) ? 1 : 0) & (((this.glGetObjectLabel = GLContext.getFunctionAddress("glGetObjectLabel")) != 0L) ? 1 : 0) & (((this.glObjectPtrLabel = GLContext.getFunctionAddress("glObjectPtrLabel")) != 0L) ? 1 : 0) & (((this.glGetObjectPtrLabel = GLContext.getFunctionAddress("glGetObjectPtrLabel")) != 0L) ? 1 : 0);
  }
  
  private boolean KHR_robustness_initNativeFunctionAddresses() {
    return (((this.glGetGraphicsResetStatus = GLContext.getFunctionAddress("glGetGraphicsResetStatus")) != 0L)) & (((this.glReadnPixels = GLContext.getFunctionAddress("glReadnPixels")) != 0L)) & (((this.glGetnUniformfv = GLContext.getFunctionAddress("glGetnUniformfv")) != 0L) ? 1 : 0) & (((this.glGetnUniformiv = GLContext.getFunctionAddress("glGetnUniformiv")) != 0L) ? 1 : 0) & (((this.glGetnUniformuiv = GLContext.getFunctionAddress("glGetnUniformuiv")) != 0L) ? 1 : 0);
  }
  
  private boolean NV_bindless_multi_draw_indirect_initNativeFunctionAddresses() {
    return (((this.glMultiDrawArraysIndirectBindlessNV = GLContext.getFunctionAddress("glMultiDrawArraysIndirectBindlessNV")) != 0L)) & (((this.glMultiDrawElementsIndirectBindlessNV = GLContext.getFunctionAddress("glMultiDrawElementsIndirectBindlessNV")) != 0L));
  }
  
  private boolean NV_bindless_texture_initNativeFunctionAddresses() {
    return (((this.glGetTextureHandleNV = GLContext.getFunctionAddress("glGetTextureHandleNV")) != 0L)) & (((this.glGetTextureSamplerHandleNV = GLContext.getFunctionAddress("glGetTextureSamplerHandleNV")) != 0L)) & (((this.glMakeTextureHandleResidentNV = GLContext.getFunctionAddress("glMakeTextureHandleResidentNV")) != 0L) ? 1 : 0) & (((this.glMakeTextureHandleNonResidentNV = GLContext.getFunctionAddress("glMakeTextureHandleNonResidentNV")) != 0L) ? 1 : 0) & (((this.glGetImageHandleNV = GLContext.getFunctionAddress("glGetImageHandleNV")) != 0L) ? 1 : 0) & (((this.glMakeImageHandleResidentNV = GLContext.getFunctionAddress("glMakeImageHandleResidentNV")) != 0L) ? 1 : 0) & (((this.glMakeImageHandleNonResidentNV = GLContext.getFunctionAddress("glMakeImageHandleNonResidentNV")) != 0L) ? 1 : 0) & (((this.glUniformHandleui64NV = GLContext.getFunctionAddress("glUniformHandleui64NV")) != 0L) ? 1 : 0) & (((this.glUniformHandleui64vNV = GLContext.getFunctionAddress("glUniformHandleui64vNV")) != 0L) ? 1 : 0) & (((this.glProgramUniformHandleui64NV = GLContext.getFunctionAddress("glProgramUniformHandleui64NV")) != 0L) ? 1 : 0) & (((this.glProgramUniformHandleui64vNV = GLContext.getFunctionAddress("glProgramUniformHandleui64vNV")) != 0L) ? 1 : 0) & (((this.glIsTextureHandleResidentNV = GLContext.getFunctionAddress("glIsTextureHandleResidentNV")) != 0L) ? 1 : 0) & (((this.glIsImageHandleResidentNV = GLContext.getFunctionAddress("glIsImageHandleResidentNV")) != 0L) ? 1 : 0);
  }
  
  private boolean NV_blend_equation_advanced_initNativeFunctionAddresses() {
    return (((this.glBlendParameteriNV = GLContext.getFunctionAddress("glBlendParameteriNV")) != 0L)) & (((this.glBlendBarrierNV = GLContext.getFunctionAddress("glBlendBarrierNV")) != 0L));
  }
  
  private boolean NV_conditional_render_initNativeFunctionAddresses() {
    return (((this.glBeginConditionalRenderNV = GLContext.getFunctionAddress("glBeginConditionalRenderNV")) != 0L)) & (((this.glEndConditionalRenderNV = GLContext.getFunctionAddress("glEndConditionalRenderNV")) != 0L));
  }
  
  private boolean NV_copy_image_initNativeFunctionAddresses() {
    return ((this.glCopyImageSubDataNV = GLContext.getFunctionAddress("glCopyImageSubDataNV")) != 0L);
  }
  
  private boolean NV_depth_buffer_float_initNativeFunctionAddresses() {
    return (((this.glDepthRangedNV = GLContext.getFunctionAddress("glDepthRangedNV")) != 0L)) & (((this.glClearDepthdNV = GLContext.getFunctionAddress("glClearDepthdNV")) != 0L)) & (((this.glDepthBoundsdNV = GLContext.getFunctionAddress("glDepthBoundsdNV")) != 0L) ? 1 : 0);
  }
  
  private boolean NV_draw_texture_initNativeFunctionAddresses() {
    return ((this.glDrawTextureNV = GLContext.getFunctionAddress("glDrawTextureNV")) != 0L);
  }
  
  private boolean NV_evaluators_initNativeFunctionAddresses() {
    return (((this.glGetMapControlPointsNV = GLContext.getFunctionAddress("glGetMapControlPointsNV")) != 0L)) & (((this.glMapControlPointsNV = GLContext.getFunctionAddress("glMapControlPointsNV")) != 0L)) & (((this.glMapParameterfvNV = GLContext.getFunctionAddress("glMapParameterfvNV")) != 0L) ? 1 : 0) & (((this.glMapParameterivNV = GLContext.getFunctionAddress("glMapParameterivNV")) != 0L) ? 1 : 0) & (((this.glGetMapParameterfvNV = GLContext.getFunctionAddress("glGetMapParameterfvNV")) != 0L) ? 1 : 0) & (((this.glGetMapParameterivNV = GLContext.getFunctionAddress("glGetMapParameterivNV")) != 0L) ? 1 : 0) & (((this.glGetMapAttribParameterfvNV = GLContext.getFunctionAddress("glGetMapAttribParameterfvNV")) != 0L) ? 1 : 0) & (((this.glGetMapAttribParameterivNV = GLContext.getFunctionAddress("glGetMapAttribParameterivNV")) != 0L) ? 1 : 0) & (((this.glEvalMapsNV = GLContext.getFunctionAddress("glEvalMapsNV")) != 0L) ? 1 : 0);
  }
  
  private boolean NV_explicit_multisample_initNativeFunctionAddresses() {
    return (((this.glGetBooleanIndexedvEXT = GLContext.getFunctionAddress("glGetBooleanIndexedvEXT")) != 0L)) & (((this.glGetIntegerIndexedvEXT = GLContext.getFunctionAddress("glGetIntegerIndexedvEXT")) != 0L)) & (((this.glGetMultisamplefvNV = GLContext.getFunctionAddress("glGetMultisamplefvNV")) != 0L) ? 1 : 0) & (((this.glSampleMaskIndexedNV = GLContext.getFunctionAddress("glSampleMaskIndexedNV")) != 0L) ? 1 : 0) & (((this.glTexRenderbufferNV = GLContext.getFunctionAddress("glTexRenderbufferNV")) != 0L) ? 1 : 0);
  }
  
  private boolean NV_fence_initNativeFunctionAddresses() {
    return (((this.glGenFencesNV = GLContext.getFunctionAddress("glGenFencesNV")) != 0L)) & (((this.glDeleteFencesNV = GLContext.getFunctionAddress("glDeleteFencesNV")) != 0L)) & (((this.glSetFenceNV = GLContext.getFunctionAddress("glSetFenceNV")) != 0L) ? 1 : 0) & (((this.glTestFenceNV = GLContext.getFunctionAddress("glTestFenceNV")) != 0L) ? 1 : 0) & (((this.glFinishFenceNV = GLContext.getFunctionAddress("glFinishFenceNV")) != 0L) ? 1 : 0) & (((this.glIsFenceNV = GLContext.getFunctionAddress("glIsFenceNV")) != 0L) ? 1 : 0) & (((this.glGetFenceivNV = GLContext.getFunctionAddress("glGetFenceivNV")) != 0L) ? 1 : 0);
  }
  
  private boolean NV_fragment_program_initNativeFunctionAddresses() {
    return (((this.glProgramNamedParameter4fNV = GLContext.getFunctionAddress("glProgramNamedParameter4fNV")) != 0L)) & (((this.glProgramNamedParameter4dNV = GLContext.getFunctionAddress("glProgramNamedParameter4dNV")) != 0L)) & (((this.glGetProgramNamedParameterfvNV = GLContext.getFunctionAddress("glGetProgramNamedParameterfvNV")) != 0L) ? 1 : 0) & (((this.glGetProgramNamedParameterdvNV = GLContext.getFunctionAddress("glGetProgramNamedParameterdvNV")) != 0L) ? 1 : 0);
  }
  
  private boolean NV_framebuffer_multisample_coverage_initNativeFunctionAddresses() {
    return ((this.glRenderbufferStorageMultisampleCoverageNV = GLContext.getFunctionAddress("glRenderbufferStorageMultisampleCoverageNV")) != 0L);
  }
  
  private boolean NV_geometry_program4_initNativeFunctionAddresses() {
    return (((this.glProgramVertexLimitNV = GLContext.getFunctionAddress("glProgramVertexLimitNV")) != 0L)) & (((this.glFramebufferTextureEXT = GLContext.getFunctionAddress("glFramebufferTextureEXT")) != 0L)) & (((this.glFramebufferTextureLayerEXT = GLContext.getFunctionAddress("glFramebufferTextureLayerEXT")) != 0L) ? 1 : 0) & (((this.glFramebufferTextureFaceEXT = GLContext.getFunctionAddress("glFramebufferTextureFaceEXT")) != 0L) ? 1 : 0);
  }
  
  private boolean NV_gpu_program4_initNativeFunctionAddresses() {
    return (((this.glProgramLocalParameterI4iNV = GLContext.getFunctionAddress("glProgramLocalParameterI4iNV")) != 0L)) & (((this.glProgramLocalParameterI4ivNV = GLContext.getFunctionAddress("glProgramLocalParameterI4ivNV")) != 0L)) & (((this.glProgramLocalParametersI4ivNV = GLContext.getFunctionAddress("glProgramLocalParametersI4ivNV")) != 0L) ? 1 : 0) & (((this.glProgramLocalParameterI4uiNV = GLContext.getFunctionAddress("glProgramLocalParameterI4uiNV")) != 0L) ? 1 : 0) & (((this.glProgramLocalParameterI4uivNV = GLContext.getFunctionAddress("glProgramLocalParameterI4uivNV")) != 0L) ? 1 : 0) & (((this.glProgramLocalParametersI4uivNV = GLContext.getFunctionAddress("glProgramLocalParametersI4uivNV")) != 0L) ? 1 : 0) & (((this.glProgramEnvParameterI4iNV = GLContext.getFunctionAddress("glProgramEnvParameterI4iNV")) != 0L) ? 1 : 0) & (((this.glProgramEnvParameterI4ivNV = GLContext.getFunctionAddress("glProgramEnvParameterI4ivNV")) != 0L) ? 1 : 0) & (((this.glProgramEnvParametersI4ivNV = GLContext.getFunctionAddress("glProgramEnvParametersI4ivNV")) != 0L) ? 1 : 0) & (((this.glProgramEnvParameterI4uiNV = GLContext.getFunctionAddress("glProgramEnvParameterI4uiNV")) != 0L) ? 1 : 0) & (((this.glProgramEnvParameterI4uivNV = GLContext.getFunctionAddress("glProgramEnvParameterI4uivNV")) != 0L) ? 1 : 0) & (((this.glProgramEnvParametersI4uivNV = GLContext.getFunctionAddress("glProgramEnvParametersI4uivNV")) != 0L) ? 1 : 0) & (((this.glGetProgramLocalParameterIivNV = GLContext.getFunctionAddress("glGetProgramLocalParameterIivNV")) != 0L) ? 1 : 0) & (((this.glGetProgramLocalParameterIuivNV = GLContext.getFunctionAddress("glGetProgramLocalParameterIuivNV")) != 0L) ? 1 : 0) & (((this.glGetProgramEnvParameterIivNV = GLContext.getFunctionAddress("glGetProgramEnvParameterIivNV")) != 0L) ? 1 : 0) & (((this.glGetProgramEnvParameterIuivNV = GLContext.getFunctionAddress("glGetProgramEnvParameterIuivNV")) != 0L) ? 1 : 0);
  }
  
  private boolean NV_gpu_shader5_initNativeFunctionAddresses(Set<String> supported_extensions) {
    return (((this.glUniform1i64NV = GLContext.getFunctionAddress("glUniform1i64NV")) != 0L)) & (((this.glUniform2i64NV = GLContext.getFunctionAddress("glUniform2i64NV")) != 0L)) & (((this.glUniform3i64NV = GLContext.getFunctionAddress("glUniform3i64NV")) != 0L) ? 1 : 0) & (((this.glUniform4i64NV = GLContext.getFunctionAddress("glUniform4i64NV")) != 0L) ? 1 : 0) & (((this.glUniform1i64vNV = GLContext.getFunctionAddress("glUniform1i64vNV")) != 0L) ? 1 : 0) & (((this.glUniform2i64vNV = GLContext.getFunctionAddress("glUniform2i64vNV")) != 0L) ? 1 : 0) & (((this.glUniform3i64vNV = GLContext.getFunctionAddress("glUniform3i64vNV")) != 0L) ? 1 : 0) & (((this.glUniform4i64vNV = GLContext.getFunctionAddress("glUniform4i64vNV")) != 0L) ? 1 : 0) & (((this.glUniform1ui64NV = GLContext.getFunctionAddress("glUniform1ui64NV")) != 0L) ? 1 : 0) & (((this.glUniform2ui64NV = GLContext.getFunctionAddress("glUniform2ui64NV")) != 0L) ? 1 : 0) & (((this.glUniform3ui64NV = GLContext.getFunctionAddress("glUniform3ui64NV")) != 0L) ? 1 : 0) & (((this.glUniform4ui64NV = GLContext.getFunctionAddress("glUniform4ui64NV")) != 0L) ? 1 : 0) & (((this.glUniform1ui64vNV = GLContext.getFunctionAddress("glUniform1ui64vNV")) != 0L) ? 1 : 0) & (((this.glUniform2ui64vNV = GLContext.getFunctionAddress("glUniform2ui64vNV")) != 0L) ? 1 : 0) & (((this.glUniform3ui64vNV = GLContext.getFunctionAddress("glUniform3ui64vNV")) != 0L) ? 1 : 0) & (((this.glUniform4ui64vNV = GLContext.getFunctionAddress("glUniform4ui64vNV")) != 0L) ? 1 : 0) & (((this.glGetUniformi64vNV = GLContext.getFunctionAddress("glGetUniformi64vNV")) != 0L) ? 1 : 0) & (((this.glGetUniformui64vNV = GLContext.getFunctionAddress("glGetUniformui64vNV")) != 0L) ? 1 : 0) & ((!supported_extensions.contains("GL_EXT_direct_state_access") || (this.glProgramUniform1i64NV = GLContext.getFunctionAddress("glProgramUniform1i64NV")) != 0L) ? 1 : 0) & ((!supported_extensions.contains("GL_EXT_direct_state_access") || (this.glProgramUniform2i64NV = GLContext.getFunctionAddress("glProgramUniform2i64NV")) != 0L) ? 1 : 0) & ((!supported_extensions.contains("GL_EXT_direct_state_access") || (this.glProgramUniform3i64NV = GLContext.getFunctionAddress("glProgramUniform3i64NV")) != 0L) ? 1 : 0) & ((!supported_extensions.contains("GL_EXT_direct_state_access") || (this.glProgramUniform4i64NV = GLContext.getFunctionAddress("glProgramUniform4i64NV")) != 0L) ? 1 : 0) & ((!supported_extensions.contains("GL_EXT_direct_state_access") || (this.glProgramUniform1i64vNV = GLContext.getFunctionAddress("glProgramUniform1i64vNV")) != 0L) ? 1 : 0) & ((!supported_extensions.contains("GL_EXT_direct_state_access") || (this.glProgramUniform2i64vNV = GLContext.getFunctionAddress("glProgramUniform2i64vNV")) != 0L) ? 1 : 0) & ((!supported_extensions.contains("GL_EXT_direct_state_access") || (this.glProgramUniform3i64vNV = GLContext.getFunctionAddress("glProgramUniform3i64vNV")) != 0L) ? 1 : 0) & ((!supported_extensions.contains("GL_EXT_direct_state_access") || (this.glProgramUniform4i64vNV = GLContext.getFunctionAddress("glProgramUniform4i64vNV")) != 0L) ? 1 : 0) & ((!supported_extensions.contains("GL_EXT_direct_state_access") || (this.glProgramUniform1ui64NV = GLContext.getFunctionAddress("glProgramUniform1ui64NV")) != 0L) ? 1 : 0) & ((!supported_extensions.contains("GL_EXT_direct_state_access") || (this.glProgramUniform2ui64NV = GLContext.getFunctionAddress("glProgramUniform2ui64NV")) != 0L) ? 1 : 0) & ((!supported_extensions.contains("GL_EXT_direct_state_access") || (this.glProgramUniform3ui64NV = GLContext.getFunctionAddress("glProgramUniform3ui64NV")) != 0L) ? 1 : 0) & ((!supported_extensions.contains("GL_EXT_direct_state_access") || (this.glProgramUniform4ui64NV = GLContext.getFunctionAddress("glProgramUniform4ui64NV")) != 0L) ? 1 : 0) & ((!supported_extensions.contains("GL_EXT_direct_state_access") || (this.glProgramUniform1ui64vNV = GLContext.getFunctionAddress("glProgramUniform1ui64vNV")) != 0L) ? 1 : 0) & ((!supported_extensions.contains("GL_EXT_direct_state_access") || (this.glProgramUniform2ui64vNV = GLContext.getFunctionAddress("glProgramUniform2ui64vNV")) != 0L) ? 1 : 0) & ((!supported_extensions.contains("GL_EXT_direct_state_access") || (this.glProgramUniform3ui64vNV = GLContext.getFunctionAddress("glProgramUniform3ui64vNV")) != 0L) ? 1 : 0) & ((!supported_extensions.contains("GL_EXT_direct_state_access") || (this.glProgramUniform4ui64vNV = GLContext.getFunctionAddress("glProgramUniform4ui64vNV")) != 0L) ? 1 : 0);
  }
  
  private boolean NV_half_float_initNativeFunctionAddresses(Set<String> supported_extensions) {
    return (((this.glVertex2hNV = GLContext.getFunctionAddress("glVertex2hNV")) != 0L)) & (((this.glVertex3hNV = GLContext.getFunctionAddress("glVertex3hNV")) != 0L)) & (((this.glVertex4hNV = GLContext.getFunctionAddress("glVertex4hNV")) != 0L) ? 1 : 0) & (((this.glNormal3hNV = GLContext.getFunctionAddress("glNormal3hNV")) != 0L) ? 1 : 0) & (((this.glColor3hNV = GLContext.getFunctionAddress("glColor3hNV")) != 0L) ? 1 : 0) & (((this.glColor4hNV = GLContext.getFunctionAddress("glColor4hNV")) != 0L) ? 1 : 0) & (((this.glTexCoord1hNV = GLContext.getFunctionAddress("glTexCoord1hNV")) != 0L) ? 1 : 0) & (((this.glTexCoord2hNV = GLContext.getFunctionAddress("glTexCoord2hNV")) != 0L) ? 1 : 0) & (((this.glTexCoord3hNV = GLContext.getFunctionAddress("glTexCoord3hNV")) != 0L) ? 1 : 0) & (((this.glTexCoord4hNV = GLContext.getFunctionAddress("glTexCoord4hNV")) != 0L) ? 1 : 0) & (((this.glMultiTexCoord1hNV = GLContext.getFunctionAddress("glMultiTexCoord1hNV")) != 0L) ? 1 : 0) & (((this.glMultiTexCoord2hNV = GLContext.getFunctionAddress("glMultiTexCoord2hNV")) != 0L) ? 1 : 0) & (((this.glMultiTexCoord3hNV = GLContext.getFunctionAddress("glMultiTexCoord3hNV")) != 0L) ? 1 : 0) & (((this.glMultiTexCoord4hNV = GLContext.getFunctionAddress("glMultiTexCoord4hNV")) != 0L) ? 1 : 0) & ((!supported_extensions.contains("GL_EXT_fog_coord") || (this.glFogCoordhNV = GLContext.getFunctionAddress("glFogCoordhNV")) != 0L) ? 1 : 0) & ((!supported_extensions.contains("GL_EXT_secondary_color") || (this.glSecondaryColor3hNV = GLContext.getFunctionAddress("glSecondaryColor3hNV")) != 0L) ? 1 : 0) & ((!supported_extensions.contains("GL_EXT_vertex_weighting") || (this.glVertexWeighthNV = GLContext.getFunctionAddress("glVertexWeighthNV")) != 0L) ? 1 : 0) & ((!supported_extensions.contains("GL_NV_vertex_program") || (this.glVertexAttrib1hNV = GLContext.getFunctionAddress("glVertexAttrib1hNV")) != 0L) ? 1 : 0) & ((!supported_extensions.contains("GL_NV_vertex_program") || (this.glVertexAttrib2hNV = GLContext.getFunctionAddress("glVertexAttrib2hNV")) != 0L) ? 1 : 0) & ((!supported_extensions.contains("GL_NV_vertex_program") || (this.glVertexAttrib3hNV = GLContext.getFunctionAddress("glVertexAttrib3hNV")) != 0L) ? 1 : 0) & ((!supported_extensions.contains("GL_NV_vertex_program") || (this.glVertexAttrib4hNV = GLContext.getFunctionAddress("glVertexAttrib4hNV")) != 0L) ? 1 : 0) & ((!supported_extensions.contains("GL_NV_vertex_program") || (this.glVertexAttribs1hvNV = GLContext.getFunctionAddress("glVertexAttribs1hvNV")) != 0L) ? 1 : 0) & ((!supported_extensions.contains("GL_NV_vertex_program") || (this.glVertexAttribs2hvNV = GLContext.getFunctionAddress("glVertexAttribs2hvNV")) != 0L) ? 1 : 0) & ((!supported_extensions.contains("GL_NV_vertex_program") || (this.glVertexAttribs3hvNV = GLContext.getFunctionAddress("glVertexAttribs3hvNV")) != 0L) ? 1 : 0) & ((!supported_extensions.contains("GL_NV_vertex_program") || (this.glVertexAttribs4hvNV = GLContext.getFunctionAddress("glVertexAttribs4hvNV")) != 0L) ? 1 : 0);
  }
  
  private boolean NV_occlusion_query_initNativeFunctionAddresses() {
    return (((this.glGenOcclusionQueriesNV = GLContext.getFunctionAddress("glGenOcclusionQueriesNV")) != 0L)) & (((this.glDeleteOcclusionQueriesNV = GLContext.getFunctionAddress("glDeleteOcclusionQueriesNV")) != 0L)) & (((this.glIsOcclusionQueryNV = GLContext.getFunctionAddress("glIsOcclusionQueryNV")) != 0L) ? 1 : 0) & (((this.glBeginOcclusionQueryNV = GLContext.getFunctionAddress("glBeginOcclusionQueryNV")) != 0L) ? 1 : 0) & (((this.glEndOcclusionQueryNV = GLContext.getFunctionAddress("glEndOcclusionQueryNV")) != 0L) ? 1 : 0) & (((this.glGetOcclusionQueryuivNV = GLContext.getFunctionAddress("glGetOcclusionQueryuivNV")) != 0L) ? 1 : 0) & (((this.glGetOcclusionQueryivNV = GLContext.getFunctionAddress("glGetOcclusionQueryivNV")) != 0L) ? 1 : 0);
  }
  
  private boolean NV_parameter_buffer_object_initNativeFunctionAddresses() {
    return (((this.glProgramBufferParametersfvNV = GLContext.getFunctionAddress("glProgramBufferParametersfvNV")) != 0L)) & (((this.glProgramBufferParametersIivNV = GLContext.getFunctionAddress("glProgramBufferParametersIivNV")) != 0L)) & (((this.glProgramBufferParametersIuivNV = GLContext.getFunctionAddress("glProgramBufferParametersIuivNV")) != 0L) ? 1 : 0);
  }
  
  private boolean NV_path_rendering_initNativeFunctionAddresses() {
    return (((this.glPathCommandsNV = GLContext.getFunctionAddress("glPathCommandsNV")) != 0L)) & (((this.glPathCoordsNV = GLContext.getFunctionAddress("glPathCoordsNV")) != 0L)) & (((this.glPathSubCommandsNV = GLContext.getFunctionAddress("glPathSubCommandsNV")) != 0L) ? 1 : 0) & (((this.glPathSubCoordsNV = GLContext.getFunctionAddress("glPathSubCoordsNV")) != 0L) ? 1 : 0) & (((this.glPathStringNV = GLContext.getFunctionAddress("glPathStringNV")) != 0L) ? 1 : 0) & (((this.glPathGlyphsNV = GLContext.getFunctionAddress("glPathGlyphsNV")) != 0L) ? 1 : 0) & (((this.glPathGlyphRangeNV = GLContext.getFunctionAddress("glPathGlyphRangeNV")) != 0L) ? 1 : 0) & (((this.glWeightPathsNV = GLContext.getFunctionAddress("glWeightPathsNV")) != 0L) ? 1 : 0) & (((this.glCopyPathNV = GLContext.getFunctionAddress("glCopyPathNV")) != 0L) ? 1 : 0) & (((this.glInterpolatePathsNV = GLContext.getFunctionAddress("glInterpolatePathsNV")) != 0L) ? 1 : 0) & (((this.glTransformPathNV = GLContext.getFunctionAddress("glTransformPathNV")) != 0L) ? 1 : 0) & (((this.glPathParameterivNV = GLContext.getFunctionAddress("glPathParameterivNV")) != 0L) ? 1 : 0) & (((this.glPathParameteriNV = GLContext.getFunctionAddress("glPathParameteriNV")) != 0L) ? 1 : 0) & (((this.glPathParameterfvNV = GLContext.getFunctionAddress("glPathParameterfvNV")) != 0L) ? 1 : 0) & (((this.glPathParameterfNV = GLContext.getFunctionAddress("glPathParameterfNV")) != 0L) ? 1 : 0) & (((this.glPathDashArrayNV = GLContext.getFunctionAddress("glPathDashArrayNV")) != 0L) ? 1 : 0) & (((this.glGenPathsNV = GLContext.getFunctionAddress("glGenPathsNV")) != 0L) ? 1 : 0) & (((this.glDeletePathsNV = GLContext.getFunctionAddress("glDeletePathsNV")) != 0L) ? 1 : 0) & (((this.glIsPathNV = GLContext.getFunctionAddress("glIsPathNV")) != 0L) ? 1 : 0) & (((this.glPathStencilFuncNV = GLContext.getFunctionAddress("glPathStencilFuncNV")) != 0L) ? 1 : 0) & (((this.glPathStencilDepthOffsetNV = GLContext.getFunctionAddress("glPathStencilDepthOffsetNV")) != 0L) ? 1 : 0) & (((this.glStencilFillPathNV = GLContext.getFunctionAddress("glStencilFillPathNV")) != 0L) ? 1 : 0) & (((this.glStencilStrokePathNV = GLContext.getFunctionAddress("glStencilStrokePathNV")) != 0L) ? 1 : 0) & (((this.glStencilFillPathInstancedNV = GLContext.getFunctionAddress("glStencilFillPathInstancedNV")) != 0L) ? 1 : 0) & (((this.glStencilStrokePathInstancedNV = GLContext.getFunctionAddress("glStencilStrokePathInstancedNV")) != 0L) ? 1 : 0) & (((this.glPathCoverDepthFuncNV = GLContext.getFunctionAddress("glPathCoverDepthFuncNV")) != 0L) ? 1 : 0) & (((this.glPathColorGenNV = GLContext.getFunctionAddress("glPathColorGenNV")) != 0L) ? 1 : 0) & (((this.glPathTexGenNV = GLContext.getFunctionAddress("glPathTexGenNV")) != 0L) ? 1 : 0) & (((this.glPathFogGenNV = GLContext.getFunctionAddress("glPathFogGenNV")) != 0L) ? 1 : 0) & (((this.glCoverFillPathNV = GLContext.getFunctionAddress("glCoverFillPathNV")) != 0L) ? 1 : 0) & (((this.glCoverStrokePathNV = GLContext.getFunctionAddress("glCoverStrokePathNV")) != 0L) ? 1 : 0) & (((this.glCoverFillPathInstancedNV = GLContext.getFunctionAddress("glCoverFillPathInstancedNV")) != 0L) ? 1 : 0) & (((this.glCoverStrokePathInstancedNV = GLContext.getFunctionAddress("glCoverStrokePathInstancedNV")) != 0L) ? 1 : 0) & (((this.glGetPathParameterivNV = GLContext.getFunctionAddress("glGetPathParameterivNV")) != 0L) ? 1 : 0) & (((this.glGetPathParameterfvNV = GLContext.getFunctionAddress("glGetPathParameterfvNV")) != 0L) ? 1 : 0) & (((this.glGetPathCommandsNV = GLContext.getFunctionAddress("glGetPathCommandsNV")) != 0L) ? 1 : 0) & (((this.glGetPathCoordsNV = GLContext.getFunctionAddress("glGetPathCoordsNV")) != 0L) ? 1 : 0) & (((this.glGetPathDashArrayNV = GLContext.getFunctionAddress("glGetPathDashArrayNV")) != 0L) ? 1 : 0) & (((this.glGetPathMetricsNV = GLContext.getFunctionAddress("glGetPathMetricsNV")) != 0L) ? 1 : 0) & (((this.glGetPathMetricRangeNV = GLContext.getFunctionAddress("glGetPathMetricRangeNV")) != 0L) ? 1 : 0) & (((this.glGetPathSpacingNV = GLContext.getFunctionAddress("glGetPathSpacingNV")) != 0L) ? 1 : 0) & (((this.glGetPathColorGenivNV = GLContext.getFunctionAddress("glGetPathColorGenivNV")) != 0L) ? 1 : 0) & (((this.glGetPathColorGenfvNV = GLContext.getFunctionAddress("glGetPathColorGenfvNV")) != 0L) ? 1 : 0) & (((this.glGetPathTexGenivNV = GLContext.getFunctionAddress("glGetPathTexGenivNV")) != 0L) ? 1 : 0) & (((this.glGetPathTexGenfvNV = GLContext.getFunctionAddress("glGetPathTexGenfvNV")) != 0L) ? 1 : 0) & (((this.glIsPointInFillPathNV = GLContext.getFunctionAddress("glIsPointInFillPathNV")) != 0L) ? 1 : 0) & (((this.glIsPointInStrokePathNV = GLContext.getFunctionAddress("glIsPointInStrokePathNV")) != 0L) ? 1 : 0) & (((this.glGetPathLengthNV = GLContext.getFunctionAddress("glGetPathLengthNV")) != 0L) ? 1 : 0) & (((this.glPointAlongPathNV = GLContext.getFunctionAddress("glPointAlongPathNV")) != 0L) ? 1 : 0);
  }
  
  private boolean NV_pixel_data_range_initNativeFunctionAddresses() {
    return (((this.glPixelDataRangeNV = GLContext.getFunctionAddress("glPixelDataRangeNV")) != 0L)) & (((this.glFlushPixelDataRangeNV = GLContext.getFunctionAddress("glFlushPixelDataRangeNV")) != 0L));
  }
  
  private boolean NV_point_sprite_initNativeFunctionAddresses() {
    return (((this.glPointParameteriNV = GLContext.getFunctionAddress("glPointParameteriNV")) != 0L)) & (((this.glPointParameterivNV = GLContext.getFunctionAddress("glPointParameterivNV")) != 0L));
  }
  
  private boolean NV_present_video_initNativeFunctionAddresses() {
    return (((this.glPresentFrameKeyedNV = GLContext.getFunctionAddress("glPresentFrameKeyedNV")) != 0L)) & (((this.glPresentFrameDualFillNV = GLContext.getFunctionAddress("glPresentFrameDualFillNV")) != 0L)) & (((this.glGetVideoivNV = GLContext.getFunctionAddress("glGetVideoivNV")) != 0L) ? 1 : 0) & (((this.glGetVideouivNV = GLContext.getFunctionAddress("glGetVideouivNV")) != 0L) ? 1 : 0) & (((this.glGetVideoi64vNV = GLContext.getFunctionAddress("glGetVideoi64vNV")) != 0L) ? 1 : 0) & (((this.glGetVideoui64vNV = GLContext.getFunctionAddress("glGetVideoui64vNV")) != 0L) ? 1 : 0);
  }
  
  private boolean NV_primitive_restart_initNativeFunctionAddresses() {
    return (((this.glPrimitiveRestartNV = GLContext.getFunctionAddress("glPrimitiveRestartNV")) != 0L)) & (((this.glPrimitiveRestartIndexNV = GLContext.getFunctionAddress("glPrimitiveRestartIndexNV")) != 0L));
  }
  
  private boolean NV_program_initNativeFunctionAddresses() {
    return (((this.glLoadProgramNV = GLContext.getFunctionAddress("glLoadProgramNV")) != 0L)) & (((this.glBindProgramNV = GLContext.getFunctionAddress("glBindProgramNV")) != 0L)) & (((this.glDeleteProgramsNV = GLContext.getFunctionAddress("glDeleteProgramsNV")) != 0L) ? 1 : 0) & (((this.glGenProgramsNV = GLContext.getFunctionAddress("glGenProgramsNV")) != 0L) ? 1 : 0) & (((this.glGetProgramivNV = GLContext.getFunctionAddress("glGetProgramivNV")) != 0L) ? 1 : 0) & (((this.glGetProgramStringNV = GLContext.getFunctionAddress("glGetProgramStringNV")) != 0L) ? 1 : 0) & (((this.glIsProgramNV = GLContext.getFunctionAddress("glIsProgramNV")) != 0L) ? 1 : 0) & (((this.glAreProgramsResidentNV = GLContext.getFunctionAddress("glAreProgramsResidentNV")) != 0L) ? 1 : 0) & (((this.glRequestResidentProgramsNV = GLContext.getFunctionAddress("glRequestResidentProgramsNV")) != 0L) ? 1 : 0);
  }
  
  private boolean NV_register_combiners_initNativeFunctionAddresses() {
    return (((this.glCombinerParameterfNV = GLContext.getFunctionAddress("glCombinerParameterfNV")) != 0L)) & (((this.glCombinerParameterfvNV = GLContext.getFunctionAddress("glCombinerParameterfvNV")) != 0L)) & (((this.glCombinerParameteriNV = GLContext.getFunctionAddress("glCombinerParameteriNV")) != 0L) ? 1 : 0) & (((this.glCombinerParameterivNV = GLContext.getFunctionAddress("glCombinerParameterivNV")) != 0L) ? 1 : 0) & (((this.glCombinerInputNV = GLContext.getFunctionAddress("glCombinerInputNV")) != 0L) ? 1 : 0) & (((this.glCombinerOutputNV = GLContext.getFunctionAddress("glCombinerOutputNV")) != 0L) ? 1 : 0) & (((this.glFinalCombinerInputNV = GLContext.getFunctionAddress("glFinalCombinerInputNV")) != 0L) ? 1 : 0) & (((this.glGetCombinerInputParameterfvNV = GLContext.getFunctionAddress("glGetCombinerInputParameterfvNV")) != 0L) ? 1 : 0) & (((this.glGetCombinerInputParameterivNV = GLContext.getFunctionAddress("glGetCombinerInputParameterivNV")) != 0L) ? 1 : 0) & (((this.glGetCombinerOutputParameterfvNV = GLContext.getFunctionAddress("glGetCombinerOutputParameterfvNV")) != 0L) ? 1 : 0) & (((this.glGetCombinerOutputParameterivNV = GLContext.getFunctionAddress("glGetCombinerOutputParameterivNV")) != 0L) ? 1 : 0) & (((this.glGetFinalCombinerInputParameterfvNV = GLContext.getFunctionAddress("glGetFinalCombinerInputParameterfvNV")) != 0L) ? 1 : 0) & (((this.glGetFinalCombinerInputParameterivNV = GLContext.getFunctionAddress("glGetFinalCombinerInputParameterivNV")) != 0L) ? 1 : 0);
  }
  
  private boolean NV_register_combiners2_initNativeFunctionAddresses() {
    return (((this.glCombinerStageParameterfvNV = GLContext.getFunctionAddress("glCombinerStageParameterfvNV")) != 0L)) & (((this.glGetCombinerStageParameterfvNV = GLContext.getFunctionAddress("glGetCombinerStageParameterfvNV")) != 0L));
  }
  
  private boolean NV_shader_buffer_load_initNativeFunctionAddresses() {
    return (((this.glMakeBufferResidentNV = GLContext.getFunctionAddress("glMakeBufferResidentNV")) != 0L)) & (((this.glMakeBufferNonResidentNV = GLContext.getFunctionAddress("glMakeBufferNonResidentNV")) != 0L)) & (((this.glIsBufferResidentNV = GLContext.getFunctionAddress("glIsBufferResidentNV")) != 0L) ? 1 : 0) & (((this.glMakeNamedBufferResidentNV = GLContext.getFunctionAddress("glMakeNamedBufferResidentNV")) != 0L) ? 1 : 0) & (((this.glMakeNamedBufferNonResidentNV = GLContext.getFunctionAddress("glMakeNamedBufferNonResidentNV")) != 0L) ? 1 : 0) & (((this.glIsNamedBufferResidentNV = GLContext.getFunctionAddress("glIsNamedBufferResidentNV")) != 0L) ? 1 : 0) & (((this.glGetBufferParameterui64vNV = GLContext.getFunctionAddress("glGetBufferParameterui64vNV")) != 0L) ? 1 : 0) & (((this.glGetNamedBufferParameterui64vNV = GLContext.getFunctionAddress("glGetNamedBufferParameterui64vNV")) != 0L) ? 1 : 0) & (((this.glGetIntegerui64vNV = GLContext.getFunctionAddress("glGetIntegerui64vNV")) != 0L) ? 1 : 0) & (((this.glUniformui64NV = GLContext.getFunctionAddress("glUniformui64NV")) != 0L) ? 1 : 0) & (((this.glUniformui64vNV = GLContext.getFunctionAddress("glUniformui64vNV")) != 0L) ? 1 : 0) & (((this.glGetUniformui64vNV = GLContext.getFunctionAddress("glGetUniformui64vNV")) != 0L) ? 1 : 0) & (((this.glProgramUniformui64NV = GLContext.getFunctionAddress("glProgramUniformui64NV")) != 0L) ? 1 : 0) & (((this.glProgramUniformui64vNV = GLContext.getFunctionAddress("glProgramUniformui64vNV")) != 0L) ? 1 : 0);
  }
  
  private boolean NV_texture_barrier_initNativeFunctionAddresses() {
    return ((this.glTextureBarrierNV = GLContext.getFunctionAddress("glTextureBarrierNV")) != 0L);
  }
  
  private boolean NV_texture_multisample_initNativeFunctionAddresses() {
    return (((this.glTexImage2DMultisampleCoverageNV = GLContext.getFunctionAddress("glTexImage2DMultisampleCoverageNV")) != 0L)) & (((this.glTexImage3DMultisampleCoverageNV = GLContext.getFunctionAddress("glTexImage3DMultisampleCoverageNV")) != 0L)) & (((this.glTextureImage2DMultisampleNV = GLContext.getFunctionAddress("glTextureImage2DMultisampleNV")) != 0L) ? 1 : 0) & (((this.glTextureImage3DMultisampleNV = GLContext.getFunctionAddress("glTextureImage3DMultisampleNV")) != 0L) ? 1 : 0) & (((this.glTextureImage2DMultisampleCoverageNV = GLContext.getFunctionAddress("glTextureImage2DMultisampleCoverageNV")) != 0L) ? 1 : 0) & (((this.glTextureImage3DMultisampleCoverageNV = GLContext.getFunctionAddress("glTextureImage3DMultisampleCoverageNV")) != 0L) ? 1 : 0);
  }
  
  private boolean NV_transform_feedback_initNativeFunctionAddresses() {
    return (((this.glBindBufferRangeNV = GLContext.getFunctionAddress("glBindBufferRangeNV")) != 0L)) & (((this.glBindBufferOffsetNV = GLContext.getFunctionAddress("glBindBufferOffsetNV")) != 0L)) & (((this.glBindBufferBaseNV = GLContext.getFunctionAddress("glBindBufferBaseNV")) != 0L) ? 1 : 0) & (((this.glTransformFeedbackAttribsNV = GLContext.getFunctionAddress("glTransformFeedbackAttribsNV")) != 0L) ? 1 : 0) & (((this.glTransformFeedbackVaryingsNV = GLContext.getFunctionAddress("glTransformFeedbackVaryingsNV")) != 0L) ? 1 : 0) & (((this.glBeginTransformFeedbackNV = GLContext.getFunctionAddress("glBeginTransformFeedbackNV")) != 0L) ? 1 : 0) & (((this.glEndTransformFeedbackNV = GLContext.getFunctionAddress("glEndTransformFeedbackNV")) != 0L) ? 1 : 0) & (((this.glGetVaryingLocationNV = GLContext.getFunctionAddress("glGetVaryingLocationNV")) != 0L) ? 1 : 0) & (((this.glGetActiveVaryingNV = GLContext.getFunctionAddress("glGetActiveVaryingNV")) != 0L) ? 1 : 0) & (((this.glActiveVaryingNV = GLContext.getFunctionAddress("glActiveVaryingNV")) != 0L) ? 1 : 0) & (((this.glGetTransformFeedbackVaryingNV = GLContext.getFunctionAddress("glGetTransformFeedbackVaryingNV")) != 0L) ? 1 : 0);
  }
  
  private boolean NV_transform_feedback2_initNativeFunctionAddresses() {
    return (((this.glBindTransformFeedbackNV = GLContext.getFunctionAddress("glBindTransformFeedbackNV")) != 0L)) & (((this.glDeleteTransformFeedbacksNV = GLContext.getFunctionAddress("glDeleteTransformFeedbacksNV")) != 0L)) & (((this.glGenTransformFeedbacksNV = GLContext.getFunctionAddress("glGenTransformFeedbacksNV")) != 0L) ? 1 : 0) & (((this.glIsTransformFeedbackNV = GLContext.getFunctionAddress("glIsTransformFeedbackNV")) != 0L) ? 1 : 0) & (((this.glPauseTransformFeedbackNV = GLContext.getFunctionAddress("glPauseTransformFeedbackNV")) != 0L) ? 1 : 0) & (((this.glResumeTransformFeedbackNV = GLContext.getFunctionAddress("glResumeTransformFeedbackNV")) != 0L) ? 1 : 0) & (((this.glDrawTransformFeedbackNV = GLContext.getFunctionAddress("glDrawTransformFeedbackNV")) != 0L) ? 1 : 0);
  }
  
  private boolean NV_vertex_array_range_initNativeFunctionAddresses() {
    return (((this.glVertexArrayRangeNV = GLContext.getFunctionAddress("glVertexArrayRangeNV")) != 0L)) & (((this.glFlushVertexArrayRangeNV = GLContext.getFunctionAddress("glFlushVertexArrayRangeNV")) != 0L)) & (((this.glAllocateMemoryNV = GLContext.getPlatformSpecificFunctionAddress("gl", new String[] { "Windows", "Linux" }, new String[] { "wgl", "glX" }, "glAllocateMemoryNV")) != 0L) ? 1 : 0) & (((this.glFreeMemoryNV = GLContext.getPlatformSpecificFunctionAddress("gl", new String[] { "Windows", "Linux" }, new String[] { "wgl", "glX" }, "glFreeMemoryNV")) != 0L) ? 1 : 0);
  }
  
  private boolean NV_vertex_attrib_integer_64bit_initNativeFunctionAddresses(Set<String> supported_extensions) {
    return (((this.glVertexAttribL1i64NV = GLContext.getFunctionAddress("glVertexAttribL1i64NV")) != 0L)) & (((this.glVertexAttribL2i64NV = GLContext.getFunctionAddress("glVertexAttribL2i64NV")) != 0L)) & (((this.glVertexAttribL3i64NV = GLContext.getFunctionAddress("glVertexAttribL3i64NV")) != 0L) ? 1 : 0) & (((this.glVertexAttribL4i64NV = GLContext.getFunctionAddress("glVertexAttribL4i64NV")) != 0L) ? 1 : 0) & (((this.glVertexAttribL1i64vNV = GLContext.getFunctionAddress("glVertexAttribL1i64vNV")) != 0L) ? 1 : 0) & (((this.glVertexAttribL2i64vNV = GLContext.getFunctionAddress("glVertexAttribL2i64vNV")) != 0L) ? 1 : 0) & (((this.glVertexAttribL3i64vNV = GLContext.getFunctionAddress("glVertexAttribL3i64vNV")) != 0L) ? 1 : 0) & (((this.glVertexAttribL4i64vNV = GLContext.getFunctionAddress("glVertexAttribL4i64vNV")) != 0L) ? 1 : 0) & (((this.glVertexAttribL1ui64NV = GLContext.getFunctionAddress("glVertexAttribL1ui64NV")) != 0L) ? 1 : 0) & (((this.glVertexAttribL2ui64NV = GLContext.getFunctionAddress("glVertexAttribL2ui64NV")) != 0L) ? 1 : 0) & (((this.glVertexAttribL3ui64NV = GLContext.getFunctionAddress("glVertexAttribL3ui64NV")) != 0L) ? 1 : 0) & (((this.glVertexAttribL4ui64NV = GLContext.getFunctionAddress("glVertexAttribL4ui64NV")) != 0L) ? 1 : 0) & (((this.glVertexAttribL1ui64vNV = GLContext.getFunctionAddress("glVertexAttribL1ui64vNV")) != 0L) ? 1 : 0) & (((this.glVertexAttribL2ui64vNV = GLContext.getFunctionAddress("glVertexAttribL2ui64vNV")) != 0L) ? 1 : 0) & (((this.glVertexAttribL3ui64vNV = GLContext.getFunctionAddress("glVertexAttribL3ui64vNV")) != 0L) ? 1 : 0) & (((this.glVertexAttribL4ui64vNV = GLContext.getFunctionAddress("glVertexAttribL4ui64vNV")) != 0L) ? 1 : 0) & (((this.glGetVertexAttribLi64vNV = GLContext.getFunctionAddress("glGetVertexAttribLi64vNV")) != 0L) ? 1 : 0) & (((this.glGetVertexAttribLui64vNV = GLContext.getFunctionAddress("glGetVertexAttribLui64vNV")) != 0L) ? 1 : 0) & ((!supported_extensions.contains("GL_NV_vertex_buffer_unified_memory") || (this.glVertexAttribLFormatNV = GLContext.getFunctionAddress("glVertexAttribLFormatNV")) != 0L) ? 1 : 0);
  }
  
  private boolean NV_vertex_buffer_unified_memory_initNativeFunctionAddresses() {
    return (((this.glBufferAddressRangeNV = GLContext.getFunctionAddress("glBufferAddressRangeNV")) != 0L)) & (((this.glVertexFormatNV = GLContext.getFunctionAddress("glVertexFormatNV")) != 0L)) & (((this.glNormalFormatNV = GLContext.getFunctionAddress("glNormalFormatNV")) != 0L) ? 1 : 0) & (((this.glColorFormatNV = GLContext.getFunctionAddress("glColorFormatNV")) != 0L) ? 1 : 0) & (((this.glIndexFormatNV = GLContext.getFunctionAddress("glIndexFormatNV")) != 0L) ? 1 : 0) & (((this.glTexCoordFormatNV = GLContext.getFunctionAddress("glTexCoordFormatNV")) != 0L) ? 1 : 0) & (((this.glEdgeFlagFormatNV = GLContext.getFunctionAddress("glEdgeFlagFormatNV")) != 0L) ? 1 : 0) & (((this.glSecondaryColorFormatNV = GLContext.getFunctionAddress("glSecondaryColorFormatNV")) != 0L) ? 1 : 0) & (((this.glFogCoordFormatNV = GLContext.getFunctionAddress("glFogCoordFormatNV")) != 0L) ? 1 : 0) & (((this.glVertexAttribFormatNV = GLContext.getFunctionAddress("glVertexAttribFormatNV")) != 0L) ? 1 : 0) & (((this.glVertexAttribIFormatNV = GLContext.getFunctionAddress("glVertexAttribIFormatNV")) != 0L) ? 1 : 0) & (((this.glGetIntegerui64i_vNV = GLContext.getFunctionAddress("glGetIntegerui64i_vNV")) != 0L) ? 1 : 0);
  }
  
  private boolean NV_vertex_program_initNativeFunctionAddresses() {
    return (((this.glExecuteProgramNV = GLContext.getFunctionAddress("glExecuteProgramNV")) != 0L)) & (((this.glGetProgramParameterfvNV = GLContext.getFunctionAddress("glGetProgramParameterfvNV")) != 0L)) & (((this.glGetProgramParameterdvNV = GLContext.getFunctionAddress("glGetProgramParameterdvNV")) != 0L) ? 1 : 0) & (((this.glGetTrackMatrixivNV = GLContext.getFunctionAddress("glGetTrackMatrixivNV")) != 0L) ? 1 : 0) & (((this.glGetVertexAttribfvNV = GLContext.getFunctionAddress("glGetVertexAttribfvNV")) != 0L) ? 1 : 0) & (((this.glGetVertexAttribdvNV = GLContext.getFunctionAddress("glGetVertexAttribdvNV")) != 0L) ? 1 : 0) & (((this.glGetVertexAttribivNV = GLContext.getFunctionAddress("glGetVertexAttribivNV")) != 0L) ? 1 : 0) & (((this.glGetVertexAttribPointervNV = GLContext.getFunctionAddress("glGetVertexAttribPointervNV")) != 0L) ? 1 : 0) & (((this.glProgramParameter4fNV = GLContext.getFunctionAddress("glProgramParameter4fNV")) != 0L) ? 1 : 0) & (((this.glProgramParameter4dNV = GLContext.getFunctionAddress("glProgramParameter4dNV")) != 0L) ? 1 : 0) & (((this.glProgramParameters4fvNV = GLContext.getFunctionAddress("glProgramParameters4fvNV")) != 0L) ? 1 : 0) & (((this.glProgramParameters4dvNV = GLContext.getFunctionAddress("glProgramParameters4dvNV")) != 0L) ? 1 : 0) & (((this.glTrackMatrixNV = GLContext.getFunctionAddress("glTrackMatrixNV")) != 0L) ? 1 : 0) & (((this.glVertexAttribPointerNV = GLContext.getFunctionAddress("glVertexAttribPointerNV")) != 0L) ? 1 : 0) & (((this.glVertexAttrib1sNV = GLContext.getFunctionAddress("glVertexAttrib1sNV")) != 0L) ? 1 : 0) & (((this.glVertexAttrib1fNV = GLContext.getFunctionAddress("glVertexAttrib1fNV")) != 0L) ? 1 : 0) & (((this.glVertexAttrib1dNV = GLContext.getFunctionAddress("glVertexAttrib1dNV")) != 0L) ? 1 : 0) & (((this.glVertexAttrib2sNV = GLContext.getFunctionAddress("glVertexAttrib2sNV")) != 0L) ? 1 : 0) & (((this.glVertexAttrib2fNV = GLContext.getFunctionAddress("glVertexAttrib2fNV")) != 0L) ? 1 : 0) & (((this.glVertexAttrib2dNV = GLContext.getFunctionAddress("glVertexAttrib2dNV")) != 0L) ? 1 : 0) & (((this.glVertexAttrib3sNV = GLContext.getFunctionAddress("glVertexAttrib3sNV")) != 0L) ? 1 : 0) & (((this.glVertexAttrib3fNV = GLContext.getFunctionAddress("glVertexAttrib3fNV")) != 0L) ? 1 : 0) & (((this.glVertexAttrib3dNV = GLContext.getFunctionAddress("glVertexAttrib3dNV")) != 0L) ? 1 : 0) & (((this.glVertexAttrib4sNV = GLContext.getFunctionAddress("glVertexAttrib4sNV")) != 0L) ? 1 : 0) & (((this.glVertexAttrib4fNV = GLContext.getFunctionAddress("glVertexAttrib4fNV")) != 0L) ? 1 : 0) & (((this.glVertexAttrib4dNV = GLContext.getFunctionAddress("glVertexAttrib4dNV")) != 0L) ? 1 : 0) & (((this.glVertexAttrib4ubNV = GLContext.getFunctionAddress("glVertexAttrib4ubNV")) != 0L) ? 1 : 0) & (((this.glVertexAttribs1svNV = GLContext.getFunctionAddress("glVertexAttribs1svNV")) != 0L) ? 1 : 0) & (((this.glVertexAttribs1fvNV = GLContext.getFunctionAddress("glVertexAttribs1fvNV")) != 0L) ? 1 : 0) & (((this.glVertexAttribs1dvNV = GLContext.getFunctionAddress("glVertexAttribs1dvNV")) != 0L) ? 1 : 0) & (((this.glVertexAttribs2svNV = GLContext.getFunctionAddress("glVertexAttribs2svNV")) != 0L) ? 1 : 0) & (((this.glVertexAttribs2fvNV = GLContext.getFunctionAddress("glVertexAttribs2fvNV")) != 0L) ? 1 : 0) & (((this.glVertexAttribs2dvNV = GLContext.getFunctionAddress("glVertexAttribs2dvNV")) != 0L) ? 1 : 0) & (((this.glVertexAttribs3svNV = GLContext.getFunctionAddress("glVertexAttribs3svNV")) != 0L) ? 1 : 0) & (((this.glVertexAttribs3fvNV = GLContext.getFunctionAddress("glVertexAttribs3fvNV")) != 0L) ? 1 : 0) & (((this.glVertexAttribs3dvNV = GLContext.getFunctionAddress("glVertexAttribs3dvNV")) != 0L) ? 1 : 0) & (((this.glVertexAttribs4svNV = GLContext.getFunctionAddress("glVertexAttribs4svNV")) != 0L) ? 1 : 0) & (((this.glVertexAttribs4fvNV = GLContext.getFunctionAddress("glVertexAttribs4fvNV")) != 0L) ? 1 : 0) & (((this.glVertexAttribs4dvNV = GLContext.getFunctionAddress("glVertexAttribs4dvNV")) != 0L) ? 1 : 0);
  }
  
  private boolean NV_video_capture_initNativeFunctionAddresses() {
    return (((this.glBeginVideoCaptureNV = GLContext.getFunctionAddress("glBeginVideoCaptureNV")) != 0L)) & (((this.glBindVideoCaptureStreamBufferNV = GLContext.getFunctionAddress("glBindVideoCaptureStreamBufferNV")) != 0L)) & (((this.glBindVideoCaptureStreamTextureNV = GLContext.getFunctionAddress("glBindVideoCaptureStreamTextureNV")) != 0L) ? 1 : 0) & (((this.glEndVideoCaptureNV = GLContext.getFunctionAddress("glEndVideoCaptureNV")) != 0L) ? 1 : 0) & (((this.glGetVideoCaptureivNV = GLContext.getFunctionAddress("glGetVideoCaptureivNV")) != 0L) ? 1 : 0) & (((this.glGetVideoCaptureStreamivNV = GLContext.getFunctionAddress("glGetVideoCaptureStreamivNV")) != 0L) ? 1 : 0) & (((this.glGetVideoCaptureStreamfvNV = GLContext.getFunctionAddress("glGetVideoCaptureStreamfvNV")) != 0L) ? 1 : 0) & (((this.glGetVideoCaptureStreamdvNV = GLContext.getFunctionAddress("glGetVideoCaptureStreamdvNV")) != 0L) ? 1 : 0) & (((this.glVideoCaptureNV = GLContext.getFunctionAddress("glVideoCaptureNV")) != 0L) ? 1 : 0) & (((this.glVideoCaptureStreamParameterivNV = GLContext.getFunctionAddress("glVideoCaptureStreamParameterivNV")) != 0L) ? 1 : 0) & (((this.glVideoCaptureStreamParameterfvNV = GLContext.getFunctionAddress("glVideoCaptureStreamParameterfvNV")) != 0L) ? 1 : 0) & (((this.glVideoCaptureStreamParameterdvNV = GLContext.getFunctionAddress("glVideoCaptureStreamParameterdvNV")) != 0L) ? 1 : 0);
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
    Set<String> supported_extensions = new HashSet<String>(256);
    int profileMask = GLContext.getSupportedExtensions(supported_extensions);
    if (supported_extensions.contains("OpenGL31") && !supported_extensions.contains("GL_ARB_compatibility") && (profileMask & 0x2) == 0)
      forwardCompatible = true; 
    if (!GL11_initNativeFunctionAddresses(forwardCompatible))
      throw new LWJGLException("GL11 not supported"); 
    if (supported_extensions.contains("GL_ARB_fragment_program"))
      supported_extensions.add("GL_ARB_program"); 
    if (supported_extensions.contains("GL_ARB_pixel_buffer_object"))
      supported_extensions.add("GL_ARB_buffer_object"); 
    if (supported_extensions.contains("GL_ARB_vertex_buffer_object"))
      supported_extensions.add("GL_ARB_buffer_object"); 
    if (supported_extensions.contains("GL_ARB_vertex_program"))
      supported_extensions.add("GL_ARB_program"); 
    if (supported_extensions.contains("GL_EXT_pixel_buffer_object"))
      supported_extensions.add("GL_ARB_buffer_object"); 
    if (supported_extensions.contains("GL_NV_fragment_program"))
      supported_extensions.add("GL_NV_program"); 
    if (supported_extensions.contains("GL_NV_vertex_program"))
      supported_extensions.add("GL_NV_program"); 
    if ((supported_extensions.contains("GL_AMD_debug_output") || supported_extensions.contains("GL_AMDX_debug_output")) && !AMD_debug_output_initNativeFunctionAddresses()) {
      remove(supported_extensions, "GL_AMDX_debug_output");
      remove(supported_extensions, "GL_AMD_debug_output");
    } 
    if (supported_extensions.contains("GL_AMD_draw_buffers_blend") && !AMD_draw_buffers_blend_initNativeFunctionAddresses())
      remove(supported_extensions, "GL_AMD_draw_buffers_blend"); 
    if (supported_extensions.contains("GL_AMD_interleaved_elements") && !AMD_interleaved_elements_initNativeFunctionAddresses())
      remove(supported_extensions, "GL_AMD_interleaved_elements"); 
    if (supported_extensions.contains("GL_AMD_multi_draw_indirect") && !AMD_multi_draw_indirect_initNativeFunctionAddresses())
      remove(supported_extensions, "GL_AMD_multi_draw_indirect"); 
    if (supported_extensions.contains("GL_AMD_name_gen_delete") && !AMD_name_gen_delete_initNativeFunctionAddresses())
      remove(supported_extensions, "GL_AMD_name_gen_delete"); 
    if (supported_extensions.contains("GL_AMD_performance_monitor") && !AMD_performance_monitor_initNativeFunctionAddresses())
      remove(supported_extensions, "GL_AMD_performance_monitor"); 
    if (supported_extensions.contains("GL_AMD_sample_positions") && !AMD_sample_positions_initNativeFunctionAddresses())
      remove(supported_extensions, "GL_AMD_sample_positions"); 
    if (supported_extensions.contains("GL_AMD_sparse_texture") && !AMD_sparse_texture_initNativeFunctionAddresses())
      remove(supported_extensions, "GL_AMD_sparse_texture"); 
    if (supported_extensions.contains("GL_AMD_stencil_operation_extended") && !AMD_stencil_operation_extended_initNativeFunctionAddresses())
      remove(supported_extensions, "GL_AMD_stencil_operation_extended"); 
    if (supported_extensions.contains("GL_AMD_vertex_shader_tessellator") && !AMD_vertex_shader_tessellator_initNativeFunctionAddresses())
      remove(supported_extensions, "GL_AMD_vertex_shader_tessellator"); 
    if (supported_extensions.contains("GL_APPLE_element_array") && !APPLE_element_array_initNativeFunctionAddresses())
      remove(supported_extensions, "GL_APPLE_element_array"); 
    if (supported_extensions.contains("GL_APPLE_fence") && !APPLE_fence_initNativeFunctionAddresses())
      remove(supported_extensions, "GL_APPLE_fence"); 
    if (supported_extensions.contains("GL_APPLE_flush_buffer_range") && !APPLE_flush_buffer_range_initNativeFunctionAddresses())
      remove(supported_extensions, "GL_APPLE_flush_buffer_range"); 
    if (supported_extensions.contains("GL_APPLE_object_purgeable") && !APPLE_object_purgeable_initNativeFunctionAddresses())
      remove(supported_extensions, "GL_APPLE_object_purgeable"); 
    if (supported_extensions.contains("GL_APPLE_texture_range") && !APPLE_texture_range_initNativeFunctionAddresses())
      remove(supported_extensions, "GL_APPLE_texture_range"); 
    if (supported_extensions.contains("GL_APPLE_vertex_array_object") && !APPLE_vertex_array_object_initNativeFunctionAddresses())
      remove(supported_extensions, "GL_APPLE_vertex_array_object"); 
    if (supported_extensions.contains("GL_APPLE_vertex_array_range") && !APPLE_vertex_array_range_initNativeFunctionAddresses())
      remove(supported_extensions, "GL_APPLE_vertex_array_range"); 
    if (supported_extensions.contains("GL_APPLE_vertex_program_evaluators") && !APPLE_vertex_program_evaluators_initNativeFunctionAddresses())
      remove(supported_extensions, "GL_APPLE_vertex_program_evaluators"); 
    if (supported_extensions.contains("GL_ARB_ES2_compatibility") && !ARB_ES2_compatibility_initNativeFunctionAddresses())
      remove(supported_extensions, "GL_ARB_ES2_compatibility"); 
    if (supported_extensions.contains("GL_ARB_ES3_1_compatibility") && !ARB_ES3_1_compatibility_initNativeFunctionAddresses())
      remove(supported_extensions, "GL_ARB_ES3_1_compatibility"); 
    if (supported_extensions.contains("GL_ARB_base_instance") && !ARB_base_instance_initNativeFunctionAddresses())
      remove(supported_extensions, "GL_ARB_base_instance"); 
    if (supported_extensions.contains("GL_ARB_bindless_texture") && !ARB_bindless_texture_initNativeFunctionAddresses())
      remove(supported_extensions, "GL_ARB_bindless_texture"); 
    if (supported_extensions.contains("GL_ARB_blend_func_extended") && !ARB_blend_func_extended_initNativeFunctionAddresses())
      remove(supported_extensions, "GL_ARB_blend_func_extended"); 
    if (supported_extensions.contains("GL_ARB_buffer_object") && !ARB_buffer_object_initNativeFunctionAddresses())
      remove(supported_extensions, "GL_ARB_buffer_object"); 
    if (supported_extensions.contains("GL_ARB_buffer_storage") && !ARB_buffer_storage_initNativeFunctionAddresses(supported_extensions))
      remove(supported_extensions, "GL_ARB_buffer_storage"); 
    if (supported_extensions.contains("GL_ARB_cl_event") && !ARB_cl_event_initNativeFunctionAddresses())
      remove(supported_extensions, "GL_ARB_cl_event"); 
    if (supported_extensions.contains("GL_ARB_clear_buffer_object") && !ARB_clear_buffer_object_initNativeFunctionAddresses(supported_extensions))
      remove(supported_extensions, "GL_ARB_clear_buffer_object"); 
    if (supported_extensions.contains("GL_ARB_clear_texture") && !ARB_clear_texture_initNativeFunctionAddresses())
      remove(supported_extensions, "GL_ARB_clear_texture"); 
    if (supported_extensions.contains("GL_ARB_clip_control") && !ARB_clip_control_initNativeFunctionAddresses())
      remove(supported_extensions, "GL_ARB_clip_control"); 
    if (supported_extensions.contains("GL_ARB_color_buffer_float") && !ARB_color_buffer_float_initNativeFunctionAddresses())
      remove(supported_extensions, "GL_ARB_color_buffer_float"); 
    if (supported_extensions.contains("GL_ARB_compute_shader") && !ARB_compute_shader_initNativeFunctionAddresses())
      remove(supported_extensions, "GL_ARB_compute_shader"); 
    if (supported_extensions.contains("GL_ARB_compute_variable_group_size") && !ARB_compute_variable_group_size_initNativeFunctionAddresses())
      remove(supported_extensions, "GL_ARB_compute_variable_group_size"); 
    if (supported_extensions.contains("GL_ARB_copy_buffer") && !ARB_copy_buffer_initNativeFunctionAddresses())
      remove(supported_extensions, "GL_ARB_copy_buffer"); 
    if (supported_extensions.contains("GL_ARB_copy_image") && !ARB_copy_image_initNativeFunctionAddresses())
      remove(supported_extensions, "GL_ARB_copy_image"); 
    if (supported_extensions.contains("GL_ARB_debug_output") && !ARB_debug_output_initNativeFunctionAddresses())
      remove(supported_extensions, "GL_ARB_debug_output"); 
    if (supported_extensions.contains("GL_ARB_direct_state_access") && !ARB_direct_state_access_initNativeFunctionAddresses())
      remove(supported_extensions, "GL_ARB_direct_state_access"); 
    if (supported_extensions.contains("GL_ARB_draw_buffers") && !ARB_draw_buffers_initNativeFunctionAddresses())
      remove(supported_extensions, "GL_ARB_draw_buffers"); 
    if (supported_extensions.contains("GL_ARB_draw_buffers_blend") && !ARB_draw_buffers_blend_initNativeFunctionAddresses())
      remove(supported_extensions, "GL_ARB_draw_buffers_blend"); 
    if (supported_extensions.contains("GL_ARB_draw_elements_base_vertex") && !ARB_draw_elements_base_vertex_initNativeFunctionAddresses())
      remove(supported_extensions, "GL_ARB_draw_elements_base_vertex"); 
    if (supported_extensions.contains("GL_ARB_draw_indirect") && !ARB_draw_indirect_initNativeFunctionAddresses())
      remove(supported_extensions, "GL_ARB_draw_indirect"); 
    if (supported_extensions.contains("GL_ARB_draw_instanced") && !ARB_draw_instanced_initNativeFunctionAddresses())
      remove(supported_extensions, "GL_ARB_draw_instanced"); 
    if (supported_extensions.contains("GL_ARB_framebuffer_no_attachments") && !ARB_framebuffer_no_attachments_initNativeFunctionAddresses(supported_extensions))
      remove(supported_extensions, "GL_ARB_framebuffer_no_attachments"); 
    if (supported_extensions.contains("GL_ARB_framebuffer_object") && !ARB_framebuffer_object_initNativeFunctionAddresses())
      remove(supported_extensions, "GL_ARB_framebuffer_object"); 
    if (supported_extensions.contains("GL_ARB_geometry_shader4") && !ARB_geometry_shader4_initNativeFunctionAddresses())
      remove(supported_extensions, "GL_ARB_geometry_shader4"); 
    if (supported_extensions.contains("GL_ARB_get_program_binary") && !ARB_get_program_binary_initNativeFunctionAddresses())
      remove(supported_extensions, "GL_ARB_get_program_binary"); 
    if (supported_extensions.contains("GL_ARB_get_texture_sub_image") && !ARB_get_texture_sub_image_initNativeFunctionAddresses())
      remove(supported_extensions, "GL_ARB_get_texture_sub_image"); 
    if (supported_extensions.contains("GL_ARB_gpu_shader_fp64") && !ARB_gpu_shader_fp64_initNativeFunctionAddresses(supported_extensions))
      remove(supported_extensions, "GL_ARB_gpu_shader_fp64"); 
    if (supported_extensions.contains("GL_ARB_imaging") && !ARB_imaging_initNativeFunctionAddresses(forwardCompatible))
      remove(supported_extensions, "GL_ARB_imaging"); 
    if (supported_extensions.contains("GL_ARB_indirect_parameters") && !ARB_indirect_parameters_initNativeFunctionAddresses())
      remove(supported_extensions, "GL_ARB_indirect_parameters"); 
    if (supported_extensions.contains("GL_ARB_instanced_arrays") && !ARB_instanced_arrays_initNativeFunctionAddresses())
      remove(supported_extensions, "GL_ARB_instanced_arrays"); 
    if (supported_extensions.contains("GL_ARB_internalformat_query") && !ARB_internalformat_query_initNativeFunctionAddresses())
      remove(supported_extensions, "GL_ARB_internalformat_query"); 
    if (supported_extensions.contains("GL_ARB_internalformat_query2") && !ARB_internalformat_query2_initNativeFunctionAddresses())
      remove(supported_extensions, "GL_ARB_internalformat_query2"); 
    if (supported_extensions.contains("GL_ARB_invalidate_subdata") && !ARB_invalidate_subdata_initNativeFunctionAddresses())
      remove(supported_extensions, "GL_ARB_invalidate_subdata"); 
    if (supported_extensions.contains("GL_ARB_map_buffer_range") && !ARB_map_buffer_range_initNativeFunctionAddresses())
      remove(supported_extensions, "GL_ARB_map_buffer_range"); 
    if (supported_extensions.contains("GL_ARB_matrix_palette") && !ARB_matrix_palette_initNativeFunctionAddresses())
      remove(supported_extensions, "GL_ARB_matrix_palette"); 
    if (supported_extensions.contains("GL_ARB_multi_bind") && !ARB_multi_bind_initNativeFunctionAddresses())
      remove(supported_extensions, "GL_ARB_multi_bind"); 
    if (supported_extensions.contains("GL_ARB_multi_draw_indirect") && !ARB_multi_draw_indirect_initNativeFunctionAddresses())
      remove(supported_extensions, "GL_ARB_multi_draw_indirect"); 
    if (supported_extensions.contains("GL_ARB_multisample") && !ARB_multisample_initNativeFunctionAddresses())
      remove(supported_extensions, "GL_ARB_multisample"); 
    if (supported_extensions.contains("GL_ARB_multitexture") && !ARB_multitexture_initNativeFunctionAddresses())
      remove(supported_extensions, "GL_ARB_multitexture"); 
    if (supported_extensions.contains("GL_ARB_occlusion_query") && !ARB_occlusion_query_initNativeFunctionAddresses())
      remove(supported_extensions, "GL_ARB_occlusion_query"); 
    if (supported_extensions.contains("GL_ARB_point_parameters") && !ARB_point_parameters_initNativeFunctionAddresses())
      remove(supported_extensions, "GL_ARB_point_parameters"); 
    if (supported_extensions.contains("GL_ARB_program") && !ARB_program_initNativeFunctionAddresses())
      remove(supported_extensions, "GL_ARB_program"); 
    if (supported_extensions.contains("GL_ARB_program_interface_query") && !ARB_program_interface_query_initNativeFunctionAddresses())
      remove(supported_extensions, "GL_ARB_program_interface_query"); 
    if (supported_extensions.contains("GL_ARB_provoking_vertex") && !ARB_provoking_vertex_initNativeFunctionAddresses())
      remove(supported_extensions, "GL_ARB_provoking_vertex"); 
    if (supported_extensions.contains("GL_ARB_robustness") && !ARB_robustness_initNativeFunctionAddresses(forwardCompatible, supported_extensions))
      remove(supported_extensions, "GL_ARB_robustness"); 
    if (supported_extensions.contains("GL_ARB_sample_shading") && !ARB_sample_shading_initNativeFunctionAddresses())
      remove(supported_extensions, "GL_ARB_sample_shading"); 
    if (supported_extensions.contains("GL_ARB_sampler_objects") && !ARB_sampler_objects_initNativeFunctionAddresses())
      remove(supported_extensions, "GL_ARB_sampler_objects"); 
    if (supported_extensions.contains("GL_ARB_separate_shader_objects") && !ARB_separate_shader_objects_initNativeFunctionAddresses())
      remove(supported_extensions, "GL_ARB_separate_shader_objects"); 
    if (supported_extensions.contains("GL_ARB_shader_atomic_counters") && !ARB_shader_atomic_counters_initNativeFunctionAddresses())
      remove(supported_extensions, "GL_ARB_shader_atomic_counters"); 
    if (supported_extensions.contains("GL_ARB_shader_image_load_store") && !ARB_shader_image_load_store_initNativeFunctionAddresses())
      remove(supported_extensions, "GL_ARB_shader_image_load_store"); 
    if (supported_extensions.contains("GL_ARB_shader_objects") && !ARB_shader_objects_initNativeFunctionAddresses())
      remove(supported_extensions, "GL_ARB_shader_objects"); 
    if (supported_extensions.contains("GL_ARB_shader_storage_buffer_object") && !ARB_shader_storage_buffer_object_initNativeFunctionAddresses())
      remove(supported_extensions, "GL_ARB_shader_storage_buffer_object"); 
    if (supported_extensions.contains("GL_ARB_shader_subroutine") && !ARB_shader_subroutine_initNativeFunctionAddresses())
      remove(supported_extensions, "GL_ARB_shader_subroutine"); 
    if (supported_extensions.contains("GL_ARB_shading_language_include") && !ARB_shading_language_include_initNativeFunctionAddresses())
      remove(supported_extensions, "GL_ARB_shading_language_include"); 
    if (supported_extensions.contains("GL_ARB_sparse_buffer") && !ARB_sparse_buffer_initNativeFunctionAddresses())
      remove(supported_extensions, "GL_ARB_sparse_buffer"); 
    if (supported_extensions.contains("GL_ARB_sparse_texture") && !ARB_sparse_texture_initNativeFunctionAddresses(supported_extensions))
      remove(supported_extensions, "GL_ARB_sparse_texture"); 
    if (supported_extensions.contains("GL_ARB_sync") && !ARB_sync_initNativeFunctionAddresses())
      remove(supported_extensions, "GL_ARB_sync"); 
    if (supported_extensions.contains("GL_ARB_tessellation_shader") && !ARB_tessellation_shader_initNativeFunctionAddresses())
      remove(supported_extensions, "GL_ARB_tessellation_shader"); 
    if (supported_extensions.contains("GL_ARB_texture_barrier") && !ARB_texture_barrier_initNativeFunctionAddresses())
      remove(supported_extensions, "GL_ARB_texture_barrier"); 
    if (supported_extensions.contains("GL_ARB_texture_buffer_object") && !ARB_texture_buffer_object_initNativeFunctionAddresses())
      remove(supported_extensions, "GL_ARB_texture_buffer_object"); 
    if (supported_extensions.contains("GL_ARB_texture_buffer_range") && !ARB_texture_buffer_range_initNativeFunctionAddresses(supported_extensions))
      remove(supported_extensions, "GL_ARB_texture_buffer_range"); 
    if (supported_extensions.contains("GL_ARB_texture_compression") && !ARB_texture_compression_initNativeFunctionAddresses())
      remove(supported_extensions, "GL_ARB_texture_compression"); 
    if (supported_extensions.contains("GL_ARB_texture_multisample") && !ARB_texture_multisample_initNativeFunctionAddresses())
      remove(supported_extensions, "GL_ARB_texture_multisample"); 
    if ((supported_extensions.contains("GL_ARB_texture_storage") || supported_extensions.contains("GL_EXT_texture_storage")) && !ARB_texture_storage_initNativeFunctionAddresses(supported_extensions)) {
      remove(supported_extensions, "GL_EXT_texture_storage");
      remove(supported_extensions, "GL_ARB_texture_storage");
    } 
    if (supported_extensions.contains("GL_ARB_texture_storage_multisample") && !ARB_texture_storage_multisample_initNativeFunctionAddresses(supported_extensions))
      remove(supported_extensions, "GL_ARB_texture_storage_multisample"); 
    if (supported_extensions.contains("GL_ARB_texture_view") && !ARB_texture_view_initNativeFunctionAddresses())
      remove(supported_extensions, "GL_ARB_texture_view"); 
    if (supported_extensions.contains("GL_ARB_timer_query") && !ARB_timer_query_initNativeFunctionAddresses())
      remove(supported_extensions, "GL_ARB_timer_query"); 
    if (supported_extensions.contains("GL_ARB_transform_feedback2") && !ARB_transform_feedback2_initNativeFunctionAddresses())
      remove(supported_extensions, "GL_ARB_transform_feedback2"); 
    if (supported_extensions.contains("GL_ARB_transform_feedback3") && !ARB_transform_feedback3_initNativeFunctionAddresses())
      remove(supported_extensions, "GL_ARB_transform_feedback3"); 
    if (supported_extensions.contains("GL_ARB_transform_feedback_instanced") && !ARB_transform_feedback_instanced_initNativeFunctionAddresses())
      remove(supported_extensions, "GL_ARB_transform_feedback_instanced"); 
    if (supported_extensions.contains("GL_ARB_transpose_matrix") && !ARB_transpose_matrix_initNativeFunctionAddresses())
      remove(supported_extensions, "GL_ARB_transpose_matrix"); 
    if (supported_extensions.contains("GL_ARB_uniform_buffer_object") && !ARB_uniform_buffer_object_initNativeFunctionAddresses())
      remove(supported_extensions, "GL_ARB_uniform_buffer_object"); 
    if (supported_extensions.contains("GL_ARB_vertex_array_object") && !ARB_vertex_array_object_initNativeFunctionAddresses())
      remove(supported_extensions, "GL_ARB_vertex_array_object"); 
    if (supported_extensions.contains("GL_ARB_vertex_attrib_64bit") && !ARB_vertex_attrib_64bit_initNativeFunctionAddresses(supported_extensions))
      remove(supported_extensions, "GL_ARB_vertex_attrib_64bit"); 
    if (supported_extensions.contains("GL_ARB_vertex_attrib_binding") && !ARB_vertex_attrib_binding_initNativeFunctionAddresses())
      remove(supported_extensions, "GL_ARB_vertex_attrib_binding"); 
    if (supported_extensions.contains("GL_ARB_vertex_blend") && !ARB_vertex_blend_initNativeFunctionAddresses())
      remove(supported_extensions, "GL_ARB_vertex_blend"); 
    if (supported_extensions.contains("GL_ARB_vertex_program") && !ARB_vertex_program_initNativeFunctionAddresses())
      remove(supported_extensions, "GL_ARB_vertex_program"); 
    if (supported_extensions.contains("GL_ARB_vertex_shader") && !ARB_vertex_shader_initNativeFunctionAddresses())
      remove(supported_extensions, "GL_ARB_vertex_shader"); 
    if (supported_extensions.contains("GL_ARB_vertex_type_2_10_10_10_rev") && !ARB_vertex_type_2_10_10_10_rev_initNativeFunctionAddresses())
      remove(supported_extensions, "GL_ARB_vertex_type_2_10_10_10_rev"); 
    if (supported_extensions.contains("GL_ARB_viewport_array") && !ARB_viewport_array_initNativeFunctionAddresses())
      remove(supported_extensions, "GL_ARB_viewport_array"); 
    if (supported_extensions.contains("GL_ARB_window_pos") && !ARB_window_pos_initNativeFunctionAddresses(forwardCompatible))
      remove(supported_extensions, "GL_ARB_window_pos"); 
    if (supported_extensions.contains("GL_ATI_draw_buffers") && !ATI_draw_buffers_initNativeFunctionAddresses())
      remove(supported_extensions, "GL_ATI_draw_buffers"); 
    if (supported_extensions.contains("GL_ATI_element_array") && !ATI_element_array_initNativeFunctionAddresses())
      remove(supported_extensions, "GL_ATI_element_array"); 
    if (supported_extensions.contains("GL_ATI_envmap_bumpmap") && !ATI_envmap_bumpmap_initNativeFunctionAddresses())
      remove(supported_extensions, "GL_ATI_envmap_bumpmap"); 
    if (supported_extensions.contains("GL_ATI_fragment_shader") && !ATI_fragment_shader_initNativeFunctionAddresses())
      remove(supported_extensions, "GL_ATI_fragment_shader"); 
    if (supported_extensions.contains("GL_ATI_map_object_buffer") && !ATI_map_object_buffer_initNativeFunctionAddresses())
      remove(supported_extensions, "GL_ATI_map_object_buffer"); 
    if (supported_extensions.contains("GL_ATI_pn_triangles") && !ATI_pn_triangles_initNativeFunctionAddresses())
      remove(supported_extensions, "GL_ATI_pn_triangles"); 
    if (supported_extensions.contains("GL_ATI_separate_stencil") && !ATI_separate_stencil_initNativeFunctionAddresses())
      remove(supported_extensions, "GL_ATI_separate_stencil"); 
    if (supported_extensions.contains("GL_ATI_vertex_array_object") && !ATI_vertex_array_object_initNativeFunctionAddresses())
      remove(supported_extensions, "GL_ATI_vertex_array_object"); 
    if (supported_extensions.contains("GL_ATI_vertex_attrib_array_object") && !ATI_vertex_attrib_array_object_initNativeFunctionAddresses())
      remove(supported_extensions, "GL_ATI_vertex_attrib_array_object"); 
    if (supported_extensions.contains("GL_ATI_vertex_streams") && !ATI_vertex_streams_initNativeFunctionAddresses())
      remove(supported_extensions, "GL_ATI_vertex_streams"); 
    if (supported_extensions.contains("GL_EXT_bindable_uniform") && !EXT_bindable_uniform_initNativeFunctionAddresses())
      remove(supported_extensions, "GL_EXT_bindable_uniform"); 
    if (supported_extensions.contains("GL_EXT_blend_color") && !EXT_blend_color_initNativeFunctionAddresses())
      remove(supported_extensions, "GL_EXT_blend_color"); 
    if (supported_extensions.contains("GL_EXT_blend_equation_separate") && !EXT_blend_equation_separate_initNativeFunctionAddresses())
      remove(supported_extensions, "GL_EXT_blend_equation_separate"); 
    if (supported_extensions.contains("GL_EXT_blend_func_separate") && !EXT_blend_func_separate_initNativeFunctionAddresses())
      remove(supported_extensions, "GL_EXT_blend_func_separate"); 
    if (supported_extensions.contains("GL_EXT_blend_minmax") && !EXT_blend_minmax_initNativeFunctionAddresses())
      remove(supported_extensions, "GL_EXT_blend_minmax"); 
    if (supported_extensions.contains("GL_EXT_compiled_vertex_array") && !EXT_compiled_vertex_array_initNativeFunctionAddresses())
      remove(supported_extensions, "GL_EXT_compiled_vertex_array"); 
    if (supported_extensions.contains("GL_EXT_depth_bounds_test") && !EXT_depth_bounds_test_initNativeFunctionAddresses())
      remove(supported_extensions, "GL_EXT_depth_bounds_test"); 
    supported_extensions.add("GL_EXT_direct_state_access");
    if (supported_extensions.contains("GL_EXT_direct_state_access") && !EXT_direct_state_access_initNativeFunctionAddresses(forwardCompatible, supported_extensions))
      remove(supported_extensions, "GL_EXT_direct_state_access"); 
    if (supported_extensions.contains("GL_EXT_draw_buffers2") && !EXT_draw_buffers2_initNativeFunctionAddresses())
      remove(supported_extensions, "GL_EXT_draw_buffers2"); 
    if (supported_extensions.contains("GL_EXT_draw_instanced") && !EXT_draw_instanced_initNativeFunctionAddresses())
      remove(supported_extensions, "GL_EXT_draw_instanced"); 
    if (supported_extensions.contains("GL_EXT_draw_range_elements") && !EXT_draw_range_elements_initNativeFunctionAddresses())
      remove(supported_extensions, "GL_EXT_draw_range_elements"); 
    if (supported_extensions.contains("GL_EXT_fog_coord") && !EXT_fog_coord_initNativeFunctionAddresses())
      remove(supported_extensions, "GL_EXT_fog_coord"); 
    if (supported_extensions.contains("GL_EXT_framebuffer_blit") && !EXT_framebuffer_blit_initNativeFunctionAddresses())
      remove(supported_extensions, "GL_EXT_framebuffer_blit"); 
    if (supported_extensions.contains("GL_EXT_framebuffer_multisample") && !EXT_framebuffer_multisample_initNativeFunctionAddresses())
      remove(supported_extensions, "GL_EXT_framebuffer_multisample"); 
    if (supported_extensions.contains("GL_EXT_framebuffer_object") && !EXT_framebuffer_object_initNativeFunctionAddresses())
      remove(supported_extensions, "GL_EXT_framebuffer_object"); 
    if (supported_extensions.contains("GL_EXT_geometry_shader4") && !EXT_geometry_shader4_initNativeFunctionAddresses())
      remove(supported_extensions, "GL_EXT_geometry_shader4"); 
    if (supported_extensions.contains("GL_EXT_gpu_program_parameters") && !EXT_gpu_program_parameters_initNativeFunctionAddresses())
      remove(supported_extensions, "GL_EXT_gpu_program_parameters"); 
    if (supported_extensions.contains("GL_EXT_gpu_shader4") && !EXT_gpu_shader4_initNativeFunctionAddresses())
      remove(supported_extensions, "GL_EXT_gpu_shader4"); 
    if (supported_extensions.contains("GL_EXT_multi_draw_arrays") && !EXT_multi_draw_arrays_initNativeFunctionAddresses())
      remove(supported_extensions, "GL_EXT_multi_draw_arrays"); 
    if (supported_extensions.contains("GL_EXT_paletted_texture") && !EXT_paletted_texture_initNativeFunctionAddresses())
      remove(supported_extensions, "GL_EXT_paletted_texture"); 
    if (supported_extensions.contains("GL_EXT_point_parameters") && !EXT_point_parameters_initNativeFunctionAddresses())
      remove(supported_extensions, "GL_EXT_point_parameters"); 
    if (supported_extensions.contains("GL_EXT_provoking_vertex") && !EXT_provoking_vertex_initNativeFunctionAddresses())
      remove(supported_extensions, "GL_EXT_provoking_vertex"); 
    if (supported_extensions.contains("GL_EXT_secondary_color") && !EXT_secondary_color_initNativeFunctionAddresses())
      remove(supported_extensions, "GL_EXT_secondary_color"); 
    if (supported_extensions.contains("GL_EXT_separate_shader_objects") && !EXT_separate_shader_objects_initNativeFunctionAddresses())
      remove(supported_extensions, "GL_EXT_separate_shader_objects"); 
    if (supported_extensions.contains("GL_EXT_shader_image_load_store") && !EXT_shader_image_load_store_initNativeFunctionAddresses())
      remove(supported_extensions, "GL_EXT_shader_image_load_store"); 
    if (supported_extensions.contains("GL_EXT_stencil_clear_tag") && !EXT_stencil_clear_tag_initNativeFunctionAddresses())
      remove(supported_extensions, "GL_EXT_stencil_clear_tag"); 
    if (supported_extensions.contains("GL_EXT_stencil_two_side") && !EXT_stencil_two_side_initNativeFunctionAddresses())
      remove(supported_extensions, "GL_EXT_stencil_two_side"); 
    if (supported_extensions.contains("GL_EXT_texture_array") && !EXT_texture_array_initNativeFunctionAddresses())
      remove(supported_extensions, "GL_EXT_texture_array"); 
    if (supported_extensions.contains("GL_EXT_texture_buffer_object") && !EXT_texture_buffer_object_initNativeFunctionAddresses())
      remove(supported_extensions, "GL_EXT_texture_buffer_object"); 
    if (supported_extensions.contains("GL_EXT_texture_integer") && !EXT_texture_integer_initNativeFunctionAddresses())
      remove(supported_extensions, "GL_EXT_texture_integer"); 
    if (supported_extensions.contains("GL_EXT_timer_query") && !EXT_timer_query_initNativeFunctionAddresses())
      remove(supported_extensions, "GL_EXT_timer_query"); 
    if (supported_extensions.contains("GL_EXT_transform_feedback") && !EXT_transform_feedback_initNativeFunctionAddresses())
      remove(supported_extensions, "GL_EXT_transform_feedback"); 
    if (supported_extensions.contains("GL_EXT_vertex_attrib_64bit") && !EXT_vertex_attrib_64bit_initNativeFunctionAddresses(supported_extensions))
      remove(supported_extensions, "GL_EXT_vertex_attrib_64bit"); 
    if (supported_extensions.contains("GL_EXT_vertex_shader") && !EXT_vertex_shader_initNativeFunctionAddresses())
      remove(supported_extensions, "GL_EXT_vertex_shader"); 
    if (supported_extensions.contains("GL_EXT_vertex_weighting") && !EXT_vertex_weighting_initNativeFunctionAddresses())
      remove(supported_extensions, "GL_EXT_vertex_weighting"); 
    if (supported_extensions.contains("OpenGL12") && !GL12_initNativeFunctionAddresses())
      remove(supported_extensions, "OpenGL12"); 
    if (supported_extensions.contains("OpenGL13") && !GL13_initNativeFunctionAddresses(forwardCompatible))
      remove(supported_extensions, "OpenGL13"); 
    if (supported_extensions.contains("OpenGL14") && !GL14_initNativeFunctionAddresses(forwardCompatible))
      remove(supported_extensions, "OpenGL14"); 
    if (supported_extensions.contains("OpenGL15") && !GL15_initNativeFunctionAddresses())
      remove(supported_extensions, "OpenGL15"); 
    if (supported_extensions.contains("OpenGL20") && !GL20_initNativeFunctionAddresses())
      remove(supported_extensions, "OpenGL20"); 
    if (supported_extensions.contains("OpenGL21") && !GL21_initNativeFunctionAddresses())
      remove(supported_extensions, "OpenGL21"); 
    if (supported_extensions.contains("OpenGL30") && !GL30_initNativeFunctionAddresses())
      remove(supported_extensions, "OpenGL30"); 
    if (supported_extensions.contains("OpenGL31") && !GL31_initNativeFunctionAddresses())
      remove(supported_extensions, "OpenGL31"); 
    if (supported_extensions.contains("OpenGL32") && !GL32_initNativeFunctionAddresses())
      remove(supported_extensions, "OpenGL32"); 
    if (supported_extensions.contains("OpenGL33") && !GL33_initNativeFunctionAddresses(forwardCompatible))
      remove(supported_extensions, "OpenGL33"); 
    if (supported_extensions.contains("OpenGL40") && !GL40_initNativeFunctionAddresses())
      remove(supported_extensions, "OpenGL40"); 
    if (supported_extensions.contains("OpenGL41") && !GL41_initNativeFunctionAddresses())
      remove(supported_extensions, "OpenGL41"); 
    if (supported_extensions.contains("OpenGL42") && !GL42_initNativeFunctionAddresses())
      remove(supported_extensions, "OpenGL42"); 
    if (supported_extensions.contains("OpenGL43") && !GL43_initNativeFunctionAddresses())
      remove(supported_extensions, "OpenGL43"); 
    if (supported_extensions.contains("OpenGL44") && !GL44_initNativeFunctionAddresses())
      remove(supported_extensions, "OpenGL44"); 
    if (supported_extensions.contains("OpenGL45") && !GL45_initNativeFunctionAddresses())
      remove(supported_extensions, "OpenGL45"); 
    if (supported_extensions.contains("GL_GREMEDY_frame_terminator") && !GREMEDY_frame_terminator_initNativeFunctionAddresses())
      remove(supported_extensions, "GL_GREMEDY_frame_terminator"); 
    if (supported_extensions.contains("GL_GREMEDY_string_marker") && !GREMEDY_string_marker_initNativeFunctionAddresses())
      remove(supported_extensions, "GL_GREMEDY_string_marker"); 
    if (supported_extensions.contains("GL_INTEL_map_texture") && !INTEL_map_texture_initNativeFunctionAddresses())
      remove(supported_extensions, "GL_INTEL_map_texture"); 
    if (supported_extensions.contains("GL_KHR_debug") && !KHR_debug_initNativeFunctionAddresses())
      remove(supported_extensions, "GL_KHR_debug"); 
    if (supported_extensions.contains("GL_KHR_robustness") && !KHR_robustness_initNativeFunctionAddresses())
      remove(supported_extensions, "GL_KHR_robustness"); 
    if (supported_extensions.contains("GL_NV_bindless_multi_draw_indirect") && !NV_bindless_multi_draw_indirect_initNativeFunctionAddresses())
      remove(supported_extensions, "GL_NV_bindless_multi_draw_indirect"); 
    if (supported_extensions.contains("GL_NV_bindless_texture") && !NV_bindless_texture_initNativeFunctionAddresses())
      remove(supported_extensions, "GL_NV_bindless_texture"); 
    if (supported_extensions.contains("GL_NV_blend_equation_advanced") && !NV_blend_equation_advanced_initNativeFunctionAddresses())
      remove(supported_extensions, "GL_NV_blend_equation_advanced"); 
    if (supported_extensions.contains("GL_NV_conditional_render") && !NV_conditional_render_initNativeFunctionAddresses())
      remove(supported_extensions, "GL_NV_conditional_render"); 
    if (supported_extensions.contains("GL_NV_copy_image") && !NV_copy_image_initNativeFunctionAddresses())
      remove(supported_extensions, "GL_NV_copy_image"); 
    if (supported_extensions.contains("GL_NV_depth_buffer_float") && !NV_depth_buffer_float_initNativeFunctionAddresses())
      remove(supported_extensions, "GL_NV_depth_buffer_float"); 
    if (supported_extensions.contains("GL_NV_draw_texture") && !NV_draw_texture_initNativeFunctionAddresses())
      remove(supported_extensions, "GL_NV_draw_texture"); 
    if (supported_extensions.contains("GL_NV_evaluators") && !NV_evaluators_initNativeFunctionAddresses())
      remove(supported_extensions, "GL_NV_evaluators"); 
    if (supported_extensions.contains("GL_NV_explicit_multisample") && !NV_explicit_multisample_initNativeFunctionAddresses())
      remove(supported_extensions, "GL_NV_explicit_multisample"); 
    if (supported_extensions.contains("GL_NV_fence") && !NV_fence_initNativeFunctionAddresses())
      remove(supported_extensions, "GL_NV_fence"); 
    if (supported_extensions.contains("GL_NV_fragment_program") && !NV_fragment_program_initNativeFunctionAddresses())
      remove(supported_extensions, "GL_NV_fragment_program"); 
    if (supported_extensions.contains("GL_NV_framebuffer_multisample_coverage") && !NV_framebuffer_multisample_coverage_initNativeFunctionAddresses())
      remove(supported_extensions, "GL_NV_framebuffer_multisample_coverage"); 
    if (supported_extensions.contains("GL_NV_geometry_program4") && !NV_geometry_program4_initNativeFunctionAddresses())
      remove(supported_extensions, "GL_NV_geometry_program4"); 
    if (supported_extensions.contains("GL_NV_gpu_program4") && !NV_gpu_program4_initNativeFunctionAddresses())
      remove(supported_extensions, "GL_NV_gpu_program4"); 
    if (supported_extensions.contains("GL_NV_gpu_shader5") && !NV_gpu_shader5_initNativeFunctionAddresses(supported_extensions))
      remove(supported_extensions, "GL_NV_gpu_shader5"); 
    if (supported_extensions.contains("GL_NV_half_float") && !NV_half_float_initNativeFunctionAddresses(supported_extensions))
      remove(supported_extensions, "GL_NV_half_float"); 
    if (supported_extensions.contains("GL_NV_occlusion_query") && !NV_occlusion_query_initNativeFunctionAddresses())
      remove(supported_extensions, "GL_NV_occlusion_query"); 
    if (supported_extensions.contains("GL_NV_parameter_buffer_object") && !NV_parameter_buffer_object_initNativeFunctionAddresses())
      remove(supported_extensions, "GL_NV_parameter_buffer_object"); 
    if (supported_extensions.contains("GL_NV_path_rendering") && !NV_path_rendering_initNativeFunctionAddresses())
      remove(supported_extensions, "GL_NV_path_rendering"); 
    if (supported_extensions.contains("GL_NV_pixel_data_range") && !NV_pixel_data_range_initNativeFunctionAddresses())
      remove(supported_extensions, "GL_NV_pixel_data_range"); 
    if (supported_extensions.contains("GL_NV_point_sprite") && !NV_point_sprite_initNativeFunctionAddresses())
      remove(supported_extensions, "GL_NV_point_sprite"); 
    if (supported_extensions.contains("GL_NV_present_video") && !NV_present_video_initNativeFunctionAddresses())
      remove(supported_extensions, "GL_NV_present_video"); 
    supported_extensions.add("GL_NV_primitive_restart");
    if (supported_extensions.contains("GL_NV_primitive_restart") && !NV_primitive_restart_initNativeFunctionAddresses())
      remove(supported_extensions, "GL_NV_primitive_restart"); 
    if (supported_extensions.contains("GL_NV_program") && !NV_program_initNativeFunctionAddresses())
      remove(supported_extensions, "GL_NV_program"); 
    if (supported_extensions.contains("GL_NV_register_combiners") && !NV_register_combiners_initNativeFunctionAddresses())
      remove(supported_extensions, "GL_NV_register_combiners"); 
    if (supported_extensions.contains("GL_NV_register_combiners2") && !NV_register_combiners2_initNativeFunctionAddresses())
      remove(supported_extensions, "GL_NV_register_combiners2"); 
    if (supported_extensions.contains("GL_NV_shader_buffer_load") && !NV_shader_buffer_load_initNativeFunctionAddresses())
      remove(supported_extensions, "GL_NV_shader_buffer_load"); 
    if (supported_extensions.contains("GL_NV_texture_barrier") && !NV_texture_barrier_initNativeFunctionAddresses())
      remove(supported_extensions, "GL_NV_texture_barrier"); 
    if (supported_extensions.contains("GL_NV_texture_multisample") && !NV_texture_multisample_initNativeFunctionAddresses())
      remove(supported_extensions, "GL_NV_texture_multisample"); 
    if (supported_extensions.contains("GL_NV_transform_feedback") && !NV_transform_feedback_initNativeFunctionAddresses())
      remove(supported_extensions, "GL_NV_transform_feedback"); 
    if (supported_extensions.contains("GL_NV_transform_feedback2") && !NV_transform_feedback2_initNativeFunctionAddresses())
      remove(supported_extensions, "GL_NV_transform_feedback2"); 
    if (supported_extensions.contains("GL_NV_vertex_array_range") && !NV_vertex_array_range_initNativeFunctionAddresses())
      remove(supported_extensions, "GL_NV_vertex_array_range"); 
    if (supported_extensions.contains("GL_NV_vertex_attrib_integer_64bit") && !NV_vertex_attrib_integer_64bit_initNativeFunctionAddresses(supported_extensions))
      remove(supported_extensions, "GL_NV_vertex_attrib_integer_64bit"); 
    if (supported_extensions.contains("GL_NV_vertex_buffer_unified_memory") && !NV_vertex_buffer_unified_memory_initNativeFunctionAddresses())
      remove(supported_extensions, "GL_NV_vertex_buffer_unified_memory"); 
    if (supported_extensions.contains("GL_NV_vertex_program") && !NV_vertex_program_initNativeFunctionAddresses())
      remove(supported_extensions, "GL_NV_vertex_program"); 
    if (supported_extensions.contains("GL_NV_video_capture") && !NV_video_capture_initNativeFunctionAddresses())
      remove(supported_extensions, "GL_NV_video_capture"); 
    return supported_extensions;
  }
  
  static void unloadAllStubs() {}
  
  ContextCapabilities(boolean forwardCompatible) throws LWJGLException {
    Set<String> supported_extensions = initAllStubs(forwardCompatible);
    this.GL_AMD_blend_minmax_factor = supported_extensions.contains("GL_AMD_blend_minmax_factor");
    this.GL_AMD_conservative_depth = supported_extensions.contains("GL_AMD_conservative_depth");
    this.GL_AMD_debug_output = (supported_extensions.contains("GL_AMD_debug_output") || supported_extensions.contains("GL_AMDX_debug_output"));
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
    this.GL_ARB_fragment_program = (supported_extensions.contains("GL_ARB_fragment_program") && supported_extensions.contains("GL_ARB_program"));
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
    this.GL_ARB_pixel_buffer_object = (supported_extensions.contains("GL_ARB_pixel_buffer_object") && supported_extensions.contains("GL_ARB_buffer_object"));
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
    this.GL_ARB_texture_buffer_object_rgb32 = (supported_extensions.contains("GL_ARB_texture_buffer_object_rgb32") || supported_extensions.contains("GL_EXT_texture_buffer_object_rgb32"));
    this.GL_ARB_texture_buffer_range = supported_extensions.contains("GL_ARB_texture_buffer_range");
    this.GL_ARB_texture_compression = supported_extensions.contains("GL_ARB_texture_compression");
    this.GL_ARB_texture_compression_bptc = (supported_extensions.contains("GL_ARB_texture_compression_bptc") || supported_extensions.contains("GL_EXT_texture_compression_bptc"));
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
    this.GL_ARB_texture_storage = (supported_extensions.contains("GL_ARB_texture_storage") || supported_extensions.contains("GL_EXT_texture_storage"));
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
    this.GL_ARB_vertex_buffer_object = (supported_extensions.contains("GL_ARB_vertex_buffer_object") && supported_extensions.contains("GL_ARB_buffer_object"));
    this.GL_ARB_vertex_program = (supported_extensions.contains("GL_ARB_vertex_program") && supported_extensions.contains("GL_ARB_program"));
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
    this.GL_EXT_pixel_buffer_object = (supported_extensions.contains("GL_EXT_pixel_buffer_object") && supported_extensions.contains("GL_ARB_buffer_object"));
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
    this.GL_NV_fragment_program = (supported_extensions.contains("GL_NV_fragment_program") && supported_extensions.contains("GL_NV_program"));
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
    this.GL_NV_vertex_program = (supported_extensions.contains("GL_NV_vertex_program") && supported_extensions.contains("GL_NV_program"));
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


/* Location:              C:\Users\Joona\Downloads\Cupid.jar!\org\lwjgl\opengl\ContextCapabilities.class
 * Java compiler version: 5 (49.0)
 * JD-Core Version:       1.1.3
 */