#!/usr/bin/env python3

import sys
import jpyutil

properties = {
    'jpy.pythonPrefix': sys.prefix,
    'jpy.pythonExecutable': sys.executable,
    'jpy.pythonLib': jpyutil._find_python_dll_file(fail=True),
    'jpy.jpyLib': jpyutil._get_module_path('jpy', fail=True),
    'jpy.jdlLib': jpyutil._get_module_path('jdl', fail=True)
}

start_options = ' '.join([ f'-D{key}={value}' for key, value in properties.items() ])
print(start_options)
