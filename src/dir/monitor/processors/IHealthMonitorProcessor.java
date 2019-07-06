package dir.monitor.processors;

import dir.monitor.impl.MonitorProcessRequest;

public interface IHealthMonitorProcessor {

    public IHealthMonitorProcessResult process(MonitorProcessRequest request);
}