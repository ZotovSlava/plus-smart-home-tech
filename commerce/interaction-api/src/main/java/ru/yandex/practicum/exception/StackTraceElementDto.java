package ru.yandex.practicum.exception;

import lombok.Getter;

@Getter
public class StackTraceElementDto {
    private final String classLoaderName;
    private final String moduleName;
    private final String moduleVersion;
    private final String methodName;
    private final String fileName;
    private final int lineNumber;
    private final String className;
    private final boolean nativeMethod;

    public StackTraceElementDto(StackTraceElement element) {
        this.classLoaderName = element.getClassLoaderName();
        this.moduleName = element.getModuleName();
        this.moduleVersion = element.getModuleVersion();
        this.methodName = element.getMethodName();
        this.fileName = element.getFileName();
        this.lineNumber = element.getLineNumber();
        this.className = element.getClassName();
        this.nativeMethod = element.isNativeMethod();
    }
}
