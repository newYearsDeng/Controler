cmake_minimum_required(VERSION 3.4.1)



add_library( # Sets the name of the library.
             control-lib
             SHARED

             # Provides a relative path to your source file(s).
             src/main/cpp/meter/cmd/connect.c
             src/main/cpp/meter/cmd/dll.h
             src/main/cpp/meter/cmd/main.c
             src/main/cpp/common/bct645.c
             src/main/cpp/common/BCT645.h
             src/main/cpp/common/param.c
             src/main/cpp/common/param.h
             src/main/cpp/common/pub_func.h
             src/main/cpp/common/des.c
             src/main/cpp/common/des.h
             src/main/cpp/meter/switch/dll.h
             src/main/cpp/meter/switch/dll.c
             src/main/cpp/meter/switch/meter_switch_connect.c

             src/main/cpp/light/dll.h
             src/main/cpp/light/dll.c
             src/main/cpp/light/light_connect.c

             src/main/cpp/airconditioner/dll.c
             src/main/cpp/airconditioner/dll.h
             src/main/cpp/airconditioner/air_conditioner_connect.c

             src/main/cpp/socket/dll.h
             src/main/cpp/socket/dll.c
             src/main/cpp/socket/socket_connect.c

             src/main/cpp/airswitch/dll.h
             src/main/cpp/airswitch/ftkt_tz_and_hz.c
             src/main/cpp/airswitch/air_conditioner_switch_connect.c

             src/main/cpp/fourlight/getdll.h
             src/main/cpp/fourlight/set/setdll.h
             src/main/cpp/fourlight/four_light_connect.c
             src/main/cpp/fourlight/getSiLuDengKong.c
             src/main/cpp/fourlight/set/setSiLuDengKong.c
             src/main/cpp/fourlight/set/four_light_connect_set.c

             src/main/cpp/centerairconditioner/read/dll.h
             src/main/cpp/centerairconditioner/read/getZhongYangKongTiao.c
             src/main/cpp/centerairconditioner/read/read_connect.c

             src/main/cpp/centerairconditioner/set/dll.h
             src/main/cpp/centerairconditioner/set/setZhongYangKongTiao.c
             src/main/cpp/centerairconditioner/set/set_connect.c

           )

find_library(log-lib
              log
              )

target_link_libraries(control-lib
                       ${log-lib} )