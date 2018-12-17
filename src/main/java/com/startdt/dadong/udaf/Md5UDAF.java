package com.startdt.dadong.udaf;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.hive.ql.exec.Description;
import org.apache.hadoop.hive.ql.exec.UDFArgumentTypeException;
import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.hive.ql.parse.SemanticException;
import org.apache.hadoop.hive.ql.udf.generic.AbstractGenericUDAFResolver;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDAFEvaluator;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspectorFactory;
import org.apache.hadoop.hive.serde2.typeinfo.PrimitiveTypeInfo;
import org.apache.hadoop.hive.serde2.typeinfo.TypeInfo;

import static org.apache.hadoop.hive.serde2.objectinspector.PrimitiveObjectInspector.PrimitiveCategory.STRING;

/**
 * @Auther: yanchuan
 * @Date: 2018-12-04 15:02
 * @Description: hive数据查询时返回字符串的md5值
 */

@Description(name = "md5", value = "_FUNC_(expr) - 返回输入字符串value的md5值")
public class Md5UDAF extends AbstractGenericUDAFResolver {

    static final Log log = LogFactory.getLog(Md5UDAF.class.getName());

    @Override
    public GenericUDAFEvaluator getEvaluator(TypeInfo[] parameters) throws SemanticException {
        if (parameters.length != 1) {
            throw new UDFArgumentTypeException((parameters.length - 1), "Exactly one argument is expected.");
        }
        if (((PrimitiveTypeInfo) parameters[0]).getPrimitiveCategory() != STRING) {
            throw new UDFArgumentTypeException((parameters.length - 1), "Only string type arguments are accepted but "
                    + parameters[0].getTypeName() + " is passed.");
        }
        return new Md5Evaluator();
    }

    public static class Md5Evaluator extends GenericUDAFEvaluator {

        ObjectInspector outputOI;

        @Override
        public ObjectInspector init(Mode m, ObjectInspector[] parameters) throws HiveException {
            assert (parameters.length == 1);
            super.init(m, parameters);
            outputOI = ObjectInspectorFactory.getReflectionObjectInspector(String.class, ObjectInspectorFactory.ObjectInspectorOptions.JAVA);
            return outputOI;
        }

        @Override
        public AbstractAggregationBuffer getNewAggregationBuffer() throws HiveException {
            return null;
        }

        @Override
        public void reset(AggregationBuffer agg) throws HiveException {

        }

        @Override
        public void iterate(AggregationBuffer agg, Object[] parameters) throws HiveException {

        }

        @Override
        public Object terminatePartial(AggregationBuffer agg) throws HiveException {
            return null;
        }

        @Override
        public void merge(AggregationBuffer agg, Object partial) throws HiveException {

        }

        @Override
        public Object terminate(AggregationBuffer agg) throws HiveException {
            return null;
        }
    }
}
