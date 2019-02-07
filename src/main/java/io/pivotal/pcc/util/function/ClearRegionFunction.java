package io.pivotal.pcc.util.function;

import lombok.extern.log4j.Log4j2;
import org.apache.geode.cache.Region;
import org.apache.geode.cache.execute.Function;
import org.apache.geode.cache.execute.FunctionContext;
import org.apache.geode.cache.execute.RegionFunctionContext;
import org.apache.geode.cache.partition.PartitionRegionHelper;

@Log4j2
public class ClearRegionFunction implements Function {

    public void execute(FunctionContext context) {
        log.info(Thread.currentThread().getName() + " executing " + getId());
        RegionFunctionContext rfc = (RegionFunctionContext) context;
        Region<?, ?> localRegion = PartitionRegionHelper.getLocalDataForContext(rfc);
        int numLocalEntries = localRegion.size();

        // Destroy each entry
        localRegion.keySet().removeIf(o -> true);

        log.info(localRegion.getName() + ": cleared " + numLocalEntries + " entries");
        context.getResultSender().lastResult(true);
    }

    public String getId() {
        return getClass().getSimpleName();
    }

    public boolean optimizeForWrite() {
        return true;
    }
}
